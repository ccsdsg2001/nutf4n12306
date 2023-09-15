package com.example.controller.admin;

import com.example.R.*;
import com.example.context.LoginMemberContext;
import com.example.req.TrainCarriageQueryReq;
import com.example.req.TrainCarriageSaveReq;
import com.example.resp.TrainCarriageQueryResp;
import com.example.service.TrainCarriageService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cc
 * @date 2023年08月20日 18:28
 */
@RestController
@RequestMapping("/admin/train-carriage")
public class TrainCarriageAdminController {

    @Resource
    private TrainCarriageService trainCarriageService;

    @PostMapping("/save")
    public CommonResp<Object> test(@Valid @RequestBody TrainCarriageSaveReq req){

        trainCarriageService.save(req);
        return new CommonResp<>();
    }


    @GetMapping("/query-list")
public CommonResp<PageResp<TrainCarriageQueryResp>> querylist(@Valid TrainCarriageQueryReq req){

        PageResp<TrainCarriageQueryResp> query =  trainCarriageService.query(req);
        return new CommonResp<>(query);
    }


    @DeleteMapping("/delete/{id}")
    public  CommonResp<Object> delete(@Valid @PathVariable Long id){

        trainCarriageService.delete(id);
        return new CommonResp<>();


    }

}
