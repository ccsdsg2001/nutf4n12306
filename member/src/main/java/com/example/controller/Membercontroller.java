package com.example.controller;

import com.example.R.CommonResp;
import com.example.R.memberRegisterRequest;
import com.example.service.MemberService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cc
 * @date 2023年08月20日 18:28
 */
@RestController
@RequestMapping("/member")
public class Membercontroller {

    @Resource
    private MemberService memberService;

    @GetMapping("/count")
    public CommonResp<Integer> test(){
        CommonResp<Integer> objectCommonResp = new CommonResp<>();
        objectCommonResp.setContent(memberService.count());

        return objectCommonResp;
    }


    @PostMapping("/register")
    public CommonResp<Long> register(@Valid memberRegisterRequest mobile){
        long register = memberService.register(mobile);

        return new CommonResp<>(register);
    }
}
