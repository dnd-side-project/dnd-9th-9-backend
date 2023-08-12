package com.dnd.Exercise.domain.user.controller;

import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "사용자 👤")
@RestController
@RequestMapping("/users")
public class UserController {

    @ApiOperation(value = "test")
    @PostMapping("/{id}")
    public String testApi(@PathVariable int id, @RequestBody String name){
        try {
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
