package com.example.controller.admin;

import com.example.R.*;
import com.example.context.LoginMemberContext;
import com.example.req.TrainstationQueryReq;
import com.example.req.TrainstationSaveReq;
import com.example.resp.TrainstationQueryResp;
import com.example.service.TrainstationService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cc
 * @date 2023年08月20日 18:28
 */
@RestController
@RequestMapping("/admin/train-station")
public class TrainstationAdminController {

    @Resource
    private TrainstationService trainstationService;

    @PostMapping("/save")
    public CommonResp<Object> test(@Valid @RequestBody TrainstationSaveReq req){

        trainstationService.save(req);
        return new CommonResp<>();
    }


    @GetMapping("/query-list")
public CommonResp<PageResp<TrainstationQueryResp>> querylist(@Valid TrainstationQueryReq req){

        PageResp<TrainstationQueryResp> query =  trainstationService.query(req);
        return new CommonResp<>(query);
    }


    @DeleteMapping("/delete/{id}")
    public  CommonResp<Object> delete(@Valid @PathVariable Long id){

        trainstationService.delete(id);
        return new CommonResp<>();


    }

}
