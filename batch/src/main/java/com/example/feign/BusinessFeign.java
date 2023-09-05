package com.example.feign;

import com.example.R.CommonResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

/**
 * @author cc
 * @date 2023年09月01日 16:29
 */

@FeignClient("business")
//@FeignClient(name ="business",url = "http://127.0.0.1:8002/business")
public interface BusinessFeign {


    @GetMapping("/business/admin/daily-train/gen-daily/{date}")
    public CommonResp<Object> genDaily(@PathVariable
                                    @DateTimeFormat(pattern = "yyyy-MM-dd")
                                    Date date);


}
