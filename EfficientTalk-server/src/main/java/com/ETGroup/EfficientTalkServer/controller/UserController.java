package com.ETGroup.EfficientTalkServer.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户相关接口", description = "用户相关接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("api/user")
public class UserController {

}
