package com.example.feign;




import com.example.R.CommonResp;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author cc
 * @date 2023年09月09日 8:22
 */
@Slf4j
public class BusinessFeignFallBack implements BusinessFeign {
    @Override
    public CommonResp<Object> genDaily(Date date) {
        log.info("服务降级");
        return null;
    }
}
