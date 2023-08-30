package com.example.controller.admin;

import com.example.R.*;
import com.example.context.LoginMemberContext;
import com.example.req.TrainSeatQueryReq;
import com.example.req.TrainSeatSaveReq;
import com.example.resp.TrainSeatQueryResp;
import com.example.service.TrainSeatService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cc
 * @date 2023年08月20日 18:28
 */
@RestController
@RequestMapping("/admin/train-seat")
public class TrainSeatAdminController{

    @Resource
    private TrainSeatService trainSeatService;

    @PostMapping("/save")
    public CommonResp<Object> test(@Valid @RequestBody TrainSeatSaveReq req){

        trainSeatService.save(req);
        return new CommonResp<>();
    }


    @GetMapping("/query-list")
public CommonResp<List<TrainSeatQueryResp>> querylist(@Valid TrainSeatQueryReq req){

        List<TrainSeatQueryResp> query = (List<TrainSeatQueryResp>) trainSeatService.query(req);
        return new CommonResp<>(query);
    }


    @DeleteMapping("/delete/{id}")
    public  CommonResp<Object> delete(@Valid @PathVariable Long id){

        trainSeatService.delete(id);
        return new CommonResp<>();


    }

}
