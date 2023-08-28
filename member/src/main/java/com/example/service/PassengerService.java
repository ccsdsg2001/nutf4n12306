package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.example.R.PageResp;
import com.example.R.PassengerQueryReq;
import com.example.R.PassengerReq;
import com.example.context.LoginMemberContext;
import com.example.domain.Passenger;
import com.example.domain.PassengerExample;
import com.example.mapper.PassengerMapper;
import com.example.resp.PassengerQueryResp;
import com.example.util.SnowUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cc
 * @date 2023年08月25日 19:42
 */
@Slf4j
@Service
public class PassengerService {


    @Resource
    private PassengerMapper passengerMapper;

    public void save(PassengerReq req){
        DateTime now = DateTime.now();
        Passenger passenger = BeanUtil.copyProperties(req, Passenger.class);

        if(ObjectUtil.isNull(passenger.getId())){
            passenger.setMemberId(LoginMemberContext.getId());
            passenger.setId(SnowUtil.getSnowflakeNextId());
            passenger.setCreateTime(now);
            passenger.setUpdateTime(now);
            passengerMapper.insert(passenger);
        }else {

            passenger.setUpdateTime(now);
            passengerMapper.updateByPrimaryKey(passenger);
        }


    };


    public PageResp<PassengerQueryResp> query(PassengerQueryReq req){

        PassengerExample passengerExample = new PassengerExample();
        passengerExample.setOrderByClause("id desc");
        PassengerExample.Criteria criteria = passengerExample.createCriteria();
        if(ObjectUtil.isNotNull(req.getMemberId())){
            criteria.andMemberIdEqualTo(req.getMemberId());
        }

        PageHelper.startPage(req.getPage(),req.getSize());
        List<Passenger> passengers = passengerMapper.selectByExample(passengerExample);
        List<PassengerQueryResp> list = BeanUtil.copyToList(passengers, PassengerQueryResp.class);


        PageInfo<Passenger> pageInfo =new PageInfo<>();
        log.info("行数", pageInfo.getTotal());
        log.info("ye数", pageInfo.getPages());
        PageResp<PassengerQueryResp> passengerQueryRespPageResp = new PageResp<>();
        passengerQueryRespPageResp.setTotal(pageInfo.getTotal());
        passengerQueryRespPageResp.setList(list);

        return passengerQueryRespPageResp;


    }

    public void delete(Long id) {

    passengerMapper.deleteByPrimaryKey(id);
    }
}
