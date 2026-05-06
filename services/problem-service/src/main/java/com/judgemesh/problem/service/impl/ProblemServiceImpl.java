package com.judgemesh.problem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.judgemesh.api.dto.ProblemDTO;
import com.judgemesh.problem.converter.ProblemConverter;
import com.judgemesh.problem.entity.Problem;
import com.judgemesh.problem.entity.ProblemTag;
import com.judgemesh.problem.mapper.ProblemMapper;
import com.judgemesh.problem.mapper.ProblemTagMapper;
import com.judgemesh.problem.service.ProblemService;
import com.judgemesh.problem.vo.ProblemCreateReq;
import com.judgemesh.problem.vo.ProblemUpdateReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {

    private final ProblemMapper problemMapper;
    private final ProblemTagMapper problemTagMapper;
    private final ProblemConverter problemConverter;

    @Override
    @Transactional(rollbackFor = Exception.class) // 保证主表和标签表同时成功或失败
    public Long createProblem(ProblemCreateReq req, Long setterId) {
        Problem problem = new Problem();
        problem.setTitle(req.getTitle());
        problem.setDescription(req.getDescription());
        problem.setTimeLimitMs(req.getTimeLimitMs());
        problem.setMemoryLimitMb(req.getMemoryLimitMb());
        problem.setDifficulty(req.getDifficulty());
        problem.setSetterId(setterId);
        problem.setPublished(false); // 默认草稿
        problem.setTotalSubmit(0);
        problem.setTotalAc(0);
        problem.setCreatedAt(LocalDateTime.now());
        problem.setUpdatedAt(LocalDateTime.now());

        problemMapper.insert(problem);

        // 插入标签
        if (req.getTags() != null && !req.getTags().isEmpty()) {
            for (String tag : req.getTags()) {
                ProblemTag pt = new ProblemTag();
                pt.setProblemId(problem.getId());
                pt.setTag(tag);
                problemTagMapper.insert(pt);
            }
        }
        return problem.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProblem(Long id, ProblemUpdateReq req) {
        Problem problem = problemMapper.selectById(id);
        if (problem == null) return;

        if (req.getTitle() != null) problem.setTitle(req.getTitle());
        if (req.getDescription() != null) problem.setDescription(req.getDescription());
        if (req.getTimeLimitMs() != null) problem.setTimeLimitMs(req.getTimeLimitMs());
        if (req.getMemoryLimitMb() != null) problem.setMemoryLimitMb(req.getMemoryLimitMb());
        if (req.getDifficulty() != null) problem.setDifficulty(req.getDifficulty());
        if (req.getPublished() != null) problem.setPublished(req.getPublished());
        problem.setUpdatedAt(LocalDateTime.now());

        problemMapper.updateById(problem);

        // 如果传了新的标签列表，则全量替换旧标签
        if (req.getTags() != null) {
            problemTagMapper.delete(new LambdaQueryWrapper<ProblemTag>().eq(ProblemTag::getProblemId, id));
            for (String t : req.getTags()) {
                ProblemTag pt = new ProblemTag();
                pt.setProblemId(id);
                pt.setTag(t);
                problemTagMapper.insert(pt);
            }
        }
    }

    @Override
    public ProblemDTO getProblemDetail(Long id) {
        Problem problem = problemMapper.selectById(id);
        if (problem == null) return null;

        ProblemDTO dto = problemConverter.toDto(problem);
        dto.setStatus(problem.getPublished() ? "PUBLISHED" : "DRAFT");

        // 查询对应的标签
        List<ProblemTag> tags = problemTagMapper.selectList(
            new LambdaQueryWrapper<ProblemTag>().eq(ProblemTag::getProblemId, id)
        );
        dto.setTags(tags.stream().map(ProblemTag::getTag).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public Page<ProblemDTO> listProblems(int current, int size, String keyword, String tag, String difficulty) {
        LambdaQueryWrapper<Problem> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Problem::getTitle, keyword);
        }
        if (StringUtils.hasText(difficulty)) {
            wrapper.eq(Problem::getDifficulty, difficulty);
        }
        if (StringUtils.hasText(tag)) {
            // SQL注入防护：简单的转义或使用 MyBatis Plus 的 inSql
            String safeTag = tag.replace("'", "''");
            wrapper.inSql(Problem::getId, "SELECT problem_id FROM problem_tag WHERE tag = '" + safeTag + "'");
        }
        wrapper.orderByDesc(Problem::getId);

        Page<Problem> page = problemMapper.selectPage(new Page<>(current, size), wrapper);

        // 转换为 DTO
        List<ProblemDTO> dtoList = page.getRecords().stream().map(p -> {
            ProblemDTO dto = problemConverter.toDto(p);
            dto.setStatus(p.getPublished() ? "PUBLISHED" : "DRAFT");
            return dto;
        }).collect(Collectors.toList());

        // 批量查询标签，避免 N+1 性能问题（关键的分布式性能优化）
        if (!dtoList.isEmpty()) {
            List<Long> problemIds = dtoList.stream().map(ProblemDTO::getId).collect(Collectors.toList());
            List<ProblemTag> allTags = problemTagMapper.selectList(
                new LambdaQueryWrapper<ProblemTag>().in(ProblemTag::getProblemId, problemIds)
            );
            Map<Long, List<String>> tagMap = allTags.stream()
                .collect(Collectors.groupingBy(ProblemTag::getProblemId,
                    Collectors.mapping(ProblemTag::getTag, Collectors.toList())));

            dtoList.forEach(dto -> dto.setTags(tagMap.getOrDefault(dto.getId(), Collections.emptyList())));
        }

        Page<ProblemDTO> dtoPage = new Page<>(current, size, page.getTotal());
        dtoPage.setRecords(dtoList);
        return dtoPage;
    }
}
