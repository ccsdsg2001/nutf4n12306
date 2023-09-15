package com.example.controller.admin;

import com.example.R.*;
import com.example.context.LoginMemberContext;
import com.example.req.DailyTrainQueryReq;
import com.example.req.DailyTrainSaveReq;
import com.example.resp.DailyTrainQueryResp;
import com.example.service.DailyTrainService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author cc
 * @date 2023年08月20日 18:28
 */
@RestController
@RequestMapping("/admin/daily-train")
public class DailyTrainAdminController {

    @Resource
    private DailyTrainService dailyTrainService;

    @PostMapping("/save")
    public CommonResp<Object> test(@Valid @RequestBody DailyTrainSaveReq req){

        dailyTrainService.save(req);
        return new CommonResp<>();
    }


    @GetMapping("/query-list")
public CommonResp<PageResp<DailyTrainQueryResp>> querylist(@Valid DailyTrainQueryReq req){

        PageResp<DailyTrainQueryResp> query =  dailyTrainService.query(req);
        return new CommonResp<>(query);
    }


    @DeleteMapping("/delete/{id}")
    public  CommonResp<Object> delete(@Valid @PathVariable Long id){

        dailyTrainService.delete(id);
        return new CommonResp<>();


    }


    @GetMapping("/gen-daily/{date}")
    public CommonResp<Object>  daily(@PathVariable
             @DateTimeFormat(pattern = "yyyy-MM-dd")
                                     Date date){

        dailyTrainService.genDaily(date);
        return new CommonResp<>();
    }

}
