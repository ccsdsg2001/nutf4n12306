package com.example.controller;

import com.example.R.CommonResp;
import com.example.R.MemberLoginReq;
import com.example.R.MemberSendCodeReq;
import com.example.R.memberRegisterRequest;
import com.example.resp.MemberLoginResp;
import com.example.service.MemberService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/send-code")
    public CommonResp<Long> sedncode(@Valid @RequestBody MemberSendCodeReq mobile){


        memberService.sendcode(mobile);

        return new CommonResp<>();
    }

    @PostMapping("/login")
    public CommonResp<MemberLoginResp> login(@Valid MemberLoginReq mobile){
        MemberLoginResp login = memberService.login(mobile);

        return new CommonResp<>(login);
    }
}
