package com.judgemesh.problem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.judgemesh.api.dto.ProblemDTO;
import com.judgemesh.api.error.ApiResponse;
import com.judgemesh.api.error.ErrorCode;
import com.judgemesh.problem.service.ProblemService;
import com.judgemesh.problem.vo.ProblemCreateReq;
import com.judgemesh.problem.vo.ProblemUpdateReq;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping
    public ApiResponse<Page<ProblemDTO>> listProblems(
        @RequestParam(defaultValue = "1") int current,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String tag,
        @RequestParam(required = false) String difficulty) {

        return ApiResponse.ok(problemService.listProblems(current, size, keyword, tag, difficulty));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProblemDTO> getProblem(@PathVariable Long id) {
        ProblemDTO dto = problemService.getProblemDetail(id);
        if (dto == null) {
            return ApiResponse.fail(ErrorCode.PROBLEM_NOT_FOUND);
        }
        return ApiResponse.ok(dto);
    }

    @PostMapping
    public ApiResponse<Long> createProblem(
        @Validated @RequestBody ProblemCreateReq req,
        // X-User-Id 这个 Header 是由组长在校验 JWT 后自动塞进来的，体现微服务协作
        @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") Long setterId) {

        Long id = problemService.createProblem(req, setterId);
        return ApiResponse.ok(id);
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> updateProblem(
        @PathVariable Long id,
        @RequestBody ProblemUpdateReq req) {

        problemService.updateProblem(id, req);
        return ApiResponse.ok(null);
    }
}
