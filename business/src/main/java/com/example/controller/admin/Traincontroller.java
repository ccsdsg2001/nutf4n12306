package com.example.controller.admin;

import com.example.R.*;
import com.example.context.LoginMemberContext;
import com.example.req.TrainQueryReq;
import com.example.req.TrainSaveReq;
import com.example.resp.TrainQueryResp;
import com.example.service.TrainSeatService;
import com.example.service.TrainService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cc
 * @date 2023年08月20日 18:28
 */
@RestController
@RequestMapping("/admin/train")
public class Traincontroller {

    @Resource
    private TrainService trainService;

    @Resource
    private TrainSeatService trainSeatService;

    @PostMapping("/save")
    public CommonResp<Object> test(@Valid @RequestBody TrainSaveReq req){

        trainService.save(req);
        return new CommonResp<>();
    }


    @GetMapping("/query-list")
public CommonResp<PageResp<TrainQueryResp>> querylist(@Valid TrainQueryReq req){

        PageResp<TrainQueryResp> query = trainService.query(req);
        return new CommonResp<>(query);
    }


    @DeleteMapping("/delete/{id}")
    public  CommonResp<Object> delete(@Valid @PathVariable Long id){

        trainService.delete(id);
        return new CommonResp<>();


    }

    @GetMapping("/query-all")
    public CommonResp<List<TrainQueryResp>> querylist(){

        List<TrainQueryResp> query =  trainService.queryAll();
        return new CommonResp<>(query);
    }

    @GetMapping("/gen-seat/{trainCode}")
    public  CommonResp<Object> genSeat( @PathVariable String trainCode){
        trainSeatService.genTrainSeat(trainCode);

        return new CommonResp<>();


    }

}
