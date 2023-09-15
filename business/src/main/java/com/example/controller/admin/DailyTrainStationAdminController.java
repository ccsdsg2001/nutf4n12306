package com.example.controller.admin;

import com.example.R.*;
import com.example.context.LoginMemberContext;
import com.example.req.DailyTrainStationQueryReq;
import com.example.req.DailyTrainStationSaveReq;
import com.example.resp.DailyTrainStationQueryResp;
import com.example.service.DailyTrainStationService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cc
 * @date 2023年08月20日 18:28
 */
@RestController
@RequestMapping("/admin/daily-train-station")
public class DailyTrainStationAdminController {

    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @PostMapping("/save")
    public CommonResp<Object> test(@Valid @RequestBody DailyTrainStationSaveReq req){

        dailyTrainStationService.save(req);
        return new CommonResp<>();
    }


    @GetMapping("/query-list")
public CommonResp<PageResp<DailyTrainStationQueryResp>> querylist(@Valid DailyTrainStationQueryReq req){

        PageResp<DailyTrainStationQueryResp> query =dailyTrainStationService.queryList(req);
        return new CommonResp<>(query);
    }


    @DeleteMapping("/delete/{id}")
    public  CommonResp<Object> delete(@Valid @PathVariable Long id){

        dailyTrainStationService.delete(id);
        return new CommonResp<>();


    }

}
