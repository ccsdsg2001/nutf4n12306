package com.example.controller;

import com.example.R.*;
import com.example.req.ConfirmOrderQueryReq;
import com.example.req.ConfirmOrderDoReq;
import com.example.resp.ConfirmOrderQueryResp;
import com.example.service.ConfirmOrderService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cc
 * @date 2023年08月20日 18:28
 */
@RestController
@RequestMapping("/confirm-order")
public class ConfirmOrderController{

    @Resource
    private ConfirmOrderService confirmOrderService;

    @PostMapping("/do")
    public CommonResp<Object> test(@Valid @RequestBody ConfirmOrderDoReq req){

        confirmOrderService.doconfirm(req);
        return new CommonResp<>();
    }



}
