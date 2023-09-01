package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.example.R.PageResp;

import com.example.domain.TrainCarriage;
import com.example.domain.TrainCarriageExample;
import com.example.domain.Trainstation;
import com.example.domain.TrainstationExample;
import com.example.exception.BusinessException;
import com.example.exception.BusinessExceptionEnum;
import com.example.mapper.TrainstationMapper;
import com.example.req.TrainstationQueryReq;
import com.example.req.TrainstationSaveReq;
import com.example.resp.TrainstationQueryResp;
import com.example.util.SnowUtil;

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
public class TrainstationService {


    @Resource
    private TrainstationMapper trainstationMapper;

    public void save(TrainstationSaveReq req){
        DateTime now = DateTime.now();
        Trainstation trainstation = BeanUtil.copyProperties(req, Trainstation.class);

        if(ObjectUtil.isNull(trainstation.getId())){

            Trainstation station1 = selectByUnique(req.getTrainCode(),req.getIndex());
            if(ObjectUtil.isNotEmpty(station1)){
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_STATION_INDEX_UNIQUE_ERROR);
            }

            Trainstation trainstation1 = selectByUnique(req.getTrainCode(),req.getName());
            if(ObjectUtil.isNotEmpty(trainstation1)){
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_STATION_NAME_UNIQUE_ERROR);
            }
            trainstation.setId(SnowUtil.getSnowflakeNextId());
            trainstation.setCreateTime(now);
            trainstation.setUpdateTime(now);
            trainstationMapper.insert(trainstation);
        }else {

            trainstation.setUpdateTime(now);
            trainstationMapper.updateByPrimaryKey(trainstation);
        }


    };


    public PageResp<TrainstationQueryResp> query(TrainstationQueryReq req){

        TrainstationExample trainstationExample = new TrainstationExample();
        trainstationExample.setOrderByClause("id desc");
        TrainstationExample.Criteria criteria = trainstationExample.createCriteria();



        PageHelper.startPage(req.getPage(),req.getSize());
        List<Trainstation> trainstations = trainstationMapper.selectByExample(trainstationExample);
        List<TrainstationQueryResp> list = BeanUtil.copyToList(trainstations, TrainstationQueryResp.class);


        PageInfo<Trainstation> pageInfo =new PageInfo<>();
        log.info("行数", pageInfo.getTotal());
        log.info("ye数", pageInfo.getPages());
        PageResp<TrainstationQueryResp> trainstationQueryRespPageResp = new PageResp<>();
        trainstationQueryRespPageResp.setTotal(pageInfo.getTotal());
        trainstationQueryRespPageResp.setList(list);

        return trainstationQueryRespPageResp;


    }

    private Trainstation selectByUnique(String  traincode, Integer index) {
        TrainstationExample stationExample = new TrainstationExample();
        stationExample.createCriteria().andTrainCodeEqualTo(traincode).andIndexEqualTo(index);
        List<Trainstation> list = trainstationMapper.selectByExample(stationExample);
        if(CollUtil.isNotEmpty(list)){
            return list.get(0);
        }else {
            return null;
        }
    }
    private Trainstation selectByUnique(String  traincode, String name) {
        TrainstationExample stationExample = new TrainstationExample();
        stationExample.createCriteria().andTrainCodeEqualTo(traincode).andNameEqualTo(name);
        List<Trainstation> list = trainstationMapper.selectByExample(stationExample);
        if(CollUtil.isNotEmpty(list)){
            return list.get(0);
        }else {
            return null;
        }
    }
    public void delete(Long id) {

    trainstationMapper.deleteByPrimaryKey(id);
    }

    public List<Trainstation> selectByTrainCode(String trainCode) {
        TrainstationExample trainStationExample = new TrainstationExample();
        trainStationExample.setOrderByClause("`index` asc");
        trainStationExample.createCriteria().andTrainCodeEqualTo(trainCode);
        return trainstationMapper.selectByExample(trainStationExample);
    }
}
