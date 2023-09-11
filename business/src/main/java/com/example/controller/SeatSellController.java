package com.example.controller;


import com.example.R.CommonResp;
import com.example.req.SeatSellReq;
import com.example.resp.SeatSellResp;
import com.example.service.DailyTrainSeatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

;

@RestController
@RequestMapping("/seat-sell")
public class SeatSellController {

    @Autowired
    private DailyTrainSeatService dailyTrainSeatService;

    @GetMapping("/query")
    public CommonResp<List<SeatSellResp>> query(@Valid SeatSellReq req) {
        List<SeatSellResp> seatList = dailyTrainSeatService.querySeatSell(req);
        return new CommonResp<>(seatList);
    }

}
