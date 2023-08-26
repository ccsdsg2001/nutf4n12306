package com.example.controller;

import com.example.R.*;
import com.example.context.LoginMemberContext;
import com.example.resp.MemberLoginResp;
import com.example.resp.PassengerQueryResp;
import com.example.service.MemberService;
import com.example.service.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cc
 * @date 2023年08月20日 18:28
 */
@RestController
@RequestMapping("/passenger")
public class Passengercontroller {

    @Resource
    private PassengerService passengerService;

    @PostMapping("/save")
    public CommonResp<Object> test(@Valid @RequestBody PassengerReq req){

        passengerService.save(req);
        return new CommonResp<>();
    }


    @GetMapping("/query-list")
    public CommonResp<List<PassengerQueryResp>> querylist(@Valid  PassengerQueryReq req){
        req.setMemberId(LoginMemberContext.getId());
        List<PassengerQueryResp> query = passengerService.query(req);
        return new CommonResp<>(query);
    }


}
