package com.example.controller;

import cn.hutool.core.util.StrUtil;
import com.example.R.CommonResp;
import com.example.R.MemberLoginReq;
import com.example.R.MemberSendCodeReq;
import com.example.R.memberRegisterRequest;
import com.example.resp.MemberLoginResp;
import com.example.service.MemberService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
import org.springframework.boot.web.embedded.netty.NettyWebServer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.ExecutionException;

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
    public CommonResp<Long> register(@Valid memberRegisterRequest req) {
        long register = memberService.register(req);
        // CommonResp<Long> commonResp = new CommonResp<>();
        // commonResp.setContent(register);
        // return commonResp;
        return new CommonResp<>(register);
    }

    @PostMapping("/send-code")
    public CommonResp<Long> sedncode(@Valid @RequestBody MemberSendCodeReq mobile) throws ExecutionException, InterruptedException {

        memberService.sendcode(mobile);



        return new CommonResp<>();
    }

    @PostMapping("/login")
    public CommonResp<MemberLoginResp> login(@Valid @RequestBody MemberLoginReq req){


        MemberLoginResp login = memberService.login(req);





        return new CommonResp<>(login);
    }
}
