package com.judgemesh.api.client;

import com.judgemesh.api.dto.ProblemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/** problem-service Feign 客户端。由 C 实现服务端。 */
@FeignClient(name = "problem-service", path = "/api/problems")
public interface ProblemClient {

    @GetMapping("/{id}")
    ProblemDTO getById(@PathVariable("id") Long id);
}
