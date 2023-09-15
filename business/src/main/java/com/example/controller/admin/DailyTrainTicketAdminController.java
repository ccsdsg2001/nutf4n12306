package com.example.controller.admin;

import com.example.R.*;
import com.example.context.LoginMemberContext;
import com.example.req.DailyTrainTicketQueryReq;
import com.example.req.DailyTrainTicketSaveReq;
import com.example.resp.DailyTrainTicketQueryResp;

import com.example.service.DailyTrainTicketService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cc
 * @date 2023年08月20日 18:28
 */
@RestController
@RequestMapping("/admin/daily-train-ticket")
public class DailyTrainTicketAdminController {

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @PostMapping("/save")
    public CommonResp<Object> test(@Valid @RequestBody DailyTrainTicketSaveReq req){

        dailyTrainTicketService.save(req);
        return new CommonResp<>();
    }


    @GetMapping("/query-list")
public CommonResp<PageResp<DailyTrainTicketQueryResp>> querylist(@Valid DailyTrainTicketQueryReq req){

        PageResp<DailyTrainTicketQueryResp> query =  dailyTrainTicketService.query(req);
        return new CommonResp<>(query);
    }


    @DeleteMapping("/delete/{id}")
    public  CommonResp<Object> delete(@Valid @PathVariable Long id){

        dailyTrainTicketService.delete(id);
        return new CommonResp<>();


    }

}
