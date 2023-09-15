package com.example.controller.admin;

import com.example.R.*;
import com.example.context.LoginMemberContext;
import com.example.req.TicketQueryReq;
import com.example.req.TicketSaveReq;
import com.example.resp.TicketQueryResp;
import com.example.service.TicketService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cc
 * @date 2023年08月20日 18:28
 */
@RestController
@RequestMapping("/admin/ticket")
public class TicketAdminController {

    @Resource
    private TicketService ticketService;




    @GetMapping("/query-list")
public CommonResp<PageResp<TicketQueryResp>> querylist(@Valid TicketQueryReq req){

        PageResp<TicketQueryResp> query =ticketService.query(req);
        return new CommonResp<>(query);
    }



}
