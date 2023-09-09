package com.example.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.R.*;
import com.example.exception.BusinessException;
import com.example.exception.BusinessExceptionEnum;
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
public class ConfirmOrderController {

    @Resource
    private ConfirmOrderService confirmOrderService;

    @SentinelResource(value = "ConfirmOrderDo", blockHandler = "doConfirmBlock")
    @PostMapping("/do")
    public CommonResp<Object> test(@Valid @RequestBody ConfirmOrderDoReq req) {

        confirmOrderService.doconfirm(req);
        return new CommonResp<>();
    }

    public CommonResp<Object> doConfirmBlock(ConfirmOrderDoReq req, BlockException e) {
        CommonResp<Object> objectCommonResp = new CommonResp<>();
        objectCommonResp.setSuccess(false);
        objectCommonResp.setMessage(BusinessExceptionEnum.CONFIRM_ORDER_FLOW_EXCEPTION.getDesc());
        return objectCommonResp;
    }




}
