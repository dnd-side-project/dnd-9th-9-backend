package com.dnd.Exercise.global.common.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
public class HealthCheckController {
    @GetMapping("/health-check")
    public String healthCheck() {
        return "health-check";
    }
}
