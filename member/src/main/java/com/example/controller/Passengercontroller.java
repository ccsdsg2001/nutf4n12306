package com.example.controller;

import com.example.R.*;
import com.example.context.LoginMemberContext;

import com.example.resp.PassengerQueryResp;

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
public CommonResp<PageResp<PassengerQueryResp>> querylist(@Valid  PassengerQueryReq req){
        req.setMemberId(LoginMemberContext.getId());
        PageResp<PassengerQueryResp> query =  passengerService.query(req);
        return new CommonResp<>(query);
    }


    @DeleteMapping("/delete/{id}")
    public  CommonResp<Object> delete(@Valid @PathVariable Long id){

        passengerService.delete(id);
        return new CommonResp<>();


    }

    @GetMapping("/query-mine")
    public CommonResp<List<PassengerQueryResp>> queryMine() {
        List<PassengerQueryResp> list = passengerService.queryMine();
        return new CommonResp<>(list);
    }

}
