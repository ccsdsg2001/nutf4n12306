package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.example.R.PageResp;


import com.example.domain.*;
import com.example.enums.SeatColEnum;
import com.example.exception.BusinessException;
import com.example.exception.BusinessExceptionEnum;
import com.example.mapper.TrainCarriageMapper;
import com.example.req.TrainCarriageQueryReq;
import com.example.req.TrainCarriageSaveReq;
import com.example.resp.TrainCarriageQueryResp;
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
public class TrainCarriageService {


    @Resource
    private TrainCarriageMapper trainCarriageMapper;

    public void save(TrainCarriageSaveReq req){
        DateTime now = DateTime.now();
//        自动计算出列数和总座位数
        List<SeatColEnum> seatColEnums = SeatColEnum.getColsByType(req.getSeatType());
        req.setColCount(seatColEnums.size());
        req.setSeatCount(req.getColCount()*req.getRowCount());
        TrainCarriage trainCarriage = BeanUtil.copyProperties(req, TrainCarriage.class);

        if(ObjectUtil.isNull(trainCarriage.getId())){
            TrainCarriage station1 = selectByUnique(req.getTrainCode(),req.getIndex());
            if(ObjectUtil.isNotEmpty(station1)){
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_CARRIAGE_INDEX_UNIQUE_ERROR);
            }

            trainCarriage.setId(SnowUtil.getSnowflakeNextId());
            trainCarriage.setCreateTime(now);
            trainCarriage.setUpdateTime(now);
            trainCarriageMapper.insert(trainCarriage);
        }else {

            trainCarriage.setUpdateTime(now);
            trainCarriageMapper.updateByPrimaryKey(trainCarriage);
        }


    };


    public PageResp<TrainCarriageQueryResp> query(TrainCarriageQueryReq req){

        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        trainCarriageExample.setOrderByClause("id desc");
        TrainCarriageExample.Criteria criteria = trainCarriageExample.createCriteria();



        PageHelper.startPage(req.getPage(),req.getSize());
        List<TrainCarriage> trainCarriages = trainCarriageMapper.selectByExample(trainCarriageExample);
        List<TrainCarriageQueryResp> list = BeanUtil.copyToList(trainCarriages, TrainCarriageQueryResp.class);


        PageInfo<TrainCarriage> pageInfo =new PageInfo<>();
        log.info("行数", pageInfo.getTotal());
        log.info("ye数", pageInfo.getPages());
        PageResp<TrainCarriageQueryResp> trainCarriageQueryRespPageResp = new PageResp<>();
        trainCarriageQueryRespPageResp.setTotal(pageInfo.getTotal());
        trainCarriageQueryRespPageResp.setList(list);

        return trainCarriageQueryRespPageResp;


    }

public void delete(Long id) {

    trainCarriageMapper.deleteByPrimaryKey(id);
    }

    public List<TrainCarriage>  selecttraincode(String trainCode ){
        TrainCarriageExample trainSeatExample = new TrainCarriageExample()
        ;

        trainSeatExample.setOrderByClause("`index` asc");


        TrainCarriageExample.Criteria criteria = trainSeatExample.createCriteria();
        criteria.andTrainCodeEqualTo(trainCode);
        //清空当前车次下的所有座位记录
      return trainCarriageMapper.selectByExample(trainSeatExample);

    }

    private TrainCarriage selectByUnique(String  traincode,Integer index) {
        TrainCarriageExample stationExample = new TrainCarriageExample();
        stationExample.createCriteria().andTrainCodeEqualTo(traincode).andIndexEqualTo(index);
        List<TrainCarriage> list = trainCarriageMapper.selectByExample(stationExample);
        if(CollUtil.isNotEmpty(list)){
            return list.get(0);
        }else {
            return null;
        }
    }

    public List<TrainCarriage> selectByTrainCode(String trainCode) {
        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        trainCarriageExample.setOrderByClause("`index` asc");
        TrainCarriageExample.Criteria criteria = trainCarriageExample.createCriteria();
        criteria.andTrainCodeEqualTo(trainCode);
        return trainCarriageMapper.selectByExample(trainCarriageExample);
    }
}
