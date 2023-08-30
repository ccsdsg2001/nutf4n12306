package com.example.controller.admin;

import com.example.R.*;

import com.example.req.StationQueryReq;
import com.example.req.StationSaveReq;
import com.example.resp.StationQueryResp;
import com.example.resp.TrainQueryResp;
import com.example.service.StationService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cc
 * @date 2023年08月20日 18:28
 */
@RestController
@RequestMapping("/admin/station")
public class adminStationController {

    @Resource
    private StationService stationService;

    @PostMapping("/save")
    public CommonResp<Object> test(@Valid @RequestBody StationSaveReq req){

        stationService.save(req);
        return new CommonResp<>();
    }


    @GetMapping("/query-list")
public CommonResp<List<StationQueryResp>> querylist(@Valid StationQueryReq req){

        List<StationQueryResp> query = (List<StationQueryResp>) stationService.query(req);
        return new CommonResp<>(query);
    }

    @GetMapping("/query-all")
    public CommonResp<List<StationQueryResp>> querylist(){

        List<StationQueryResp> query =  stationService.queryAll();
        return new CommonResp<>(query);
    }
    @DeleteMapping("/delete/{id}")
    public  CommonResp<Object> delete(@Valid @PathVariable Long id){

        stationService.delete(id);
        return new CommonResp<>();


    }

}
