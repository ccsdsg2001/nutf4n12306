package com.example.controller.admin;

import com.example.R.*;
import com.example.context.LoginMemberContext;
import com.example.req.DailyTrainSeatQueryReq;
import com.example.req.DailyTrainSeatSaveReq;
import com.example.resp.DailyTrainSeatQueryResp;
import com.example.service.DailyTrainSeatService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cc
 * @date 2023年08月20日 18:28
 */
@RestController
@RequestMapping("/admin/daily-train-seat")
public class DailyTrainSeatAdminController {

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @PostMapping("/save")
    public CommonResp<Object> test(@Valid @RequestBody DailyTrainSeatSaveReq req){

        dailyTrainSeatService.save(req);
        return new CommonResp<>();
    }


    @GetMapping("/query-list")
public CommonResp<PageResp<DailyTrainSeatQueryResp>> querylist(@Valid DailyTrainSeatQueryReq req){

        PageResp<DailyTrainSeatQueryResp> query =  dailyTrainSeatService.query(req);
        return new CommonResp<>(query);
    }


    @DeleteMapping("/delete/{id}")
    public  CommonResp<Object> delete(@Valid @PathVariable Long id){

        dailyTrainSeatService.delete(id);
        return new CommonResp<>();


    }

}
