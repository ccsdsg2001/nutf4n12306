package com.example.controller.admin;

import com.example.R.*;
import com.example.context.LoginMemberContext;
import com.example.req.SkTokenQueryReq;
import com.example.req.SkTokenSaveReq;
import com.example.resp.SkTokenQueryResp;
import com.example.service.SkTokenService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cc
 * @date 2023年08月20日 18:28
 */
@RestController
@RequestMapping("/admin/sk-token")
public class SkTokenAdminController {

    @Resource
    private SkTokenService skTokenService;

    @PostMapping("/save")
    public CommonResp<Object> test(@Valid @RequestBody SkTokenSaveReq req){

        skTokenService.save(req);
        return new CommonResp<>();
    }


    @GetMapping("/query-list")
public CommonResp<PageResp<SkTokenQueryResp>> querylist(@Valid SkTokenQueryReq req){

        PageResp<SkTokenQueryResp> query =  skTokenService.queryList(req);
        return new CommonResp<>(query);
    }


    @DeleteMapping("/delete/{id}")
    public  CommonResp<Object> delete(@Valid @PathVariable Long id){

        skTokenService.delete(id);
        return new CommonResp<>();


    }

}
