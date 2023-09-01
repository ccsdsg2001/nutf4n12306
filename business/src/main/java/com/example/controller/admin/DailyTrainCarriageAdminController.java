package com.example.controller.admin;

import com.example.R.*;
import com.example.context.LoginMemberContext;
import com.example.req.DailyTrainCarriageQueryReq;
import com.example.req.DailyTrainCarriageSaveReq;
import com.example.resp.DailyTrainCarriageQueryResp;
import com.example.service.DailyTrainCarriageService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cc
 * @date 2023年08月20日 18:28
 */
@RestController
@RequestMapping("/admin/daily-train-carriage")
public class DailyTrainCarriageAdminController {

    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;

    @PostMapping("/save")
    public CommonResp<Object> test(@Valid @RequestBody DailyTrainCarriageSaveReq req){

        dailyTrainCarriageService.save(req);
        return new CommonResp<>();
    }


    @GetMapping("/query-list")
public CommonResp<List<DailyTrainCarriageQueryResp>> querylist(@Valid DailyTrainCarriageQueryReq req){

        List<DailyTrainCarriageQueryResp> query = (List<DailyTrainCarriageQueryResp>) dailyTrainCarriageService.query(req);
        return new CommonResp<>(query);
    }


    @DeleteMapping("/delete/{id}")
    public  CommonResp<Object> delete(@Valid @PathVariable Long id){

        dailyTrainCarriageService.delete(id);
        return new CommonResp<>();


    }

}
