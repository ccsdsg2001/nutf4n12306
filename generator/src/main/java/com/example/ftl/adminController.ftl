package com.example.controller.admin;

import com.example.R.*;
import com.example.context.LoginMemberContext;
import com.example.resp.${Domain}QueryResp;
import com.example.service.${Domain}Service;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cc
 * @date 2023年08月20日 18:28
 */
@RestController
@RequestMapping("/admin/${do_main}")
public class ${Domain}controller {

    @Resource
    private ${Domain}Service ${domain}Service;

    @PostMapping("/save")
    public CommonResp<Object> test(@Valid @RequestBody ${Domain}SaveReq req){

        ${domain}Service.save(req);
        return new CommonResp<>();
    }


    @GetMapping("/query-list")
public CommonResp<List<${Domain}QueryResp>> querylist(@Valid  ${Domain}QueryReq req){

        List<${Domain}QueryResp> query = (List<${Domain}QueryResp>) ${domain}Service.query(req);
        return new CommonResp<>(query);
    }


    @DeleteMapping("/delete/{id}")
    public  CommonResp<Object> delete(@Valid @PathVariable Long id){

        ${domain}Service.delete(id);
        return new CommonResp<>();


    }

}
