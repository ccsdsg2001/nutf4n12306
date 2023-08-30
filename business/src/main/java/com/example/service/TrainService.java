package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.example.R.PageResp;

import com.example.domain.*;
import com.example.exception.BusinessException;
import com.example.exception.BusinessExceptionEnum;
import com.example.mapper.TrainMapper;
import com.example.req.TrainQueryReq;
import com.example.req.TrainSaveReq;
import com.example.resp.TrainQueryResp;
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
public class TrainService {


    @Resource
    private TrainMapper trainMapper;

    public void save(TrainSaveReq req){
        DateTime now = DateTime.now();
        Train train = BeanUtil.copyProperties(req, Train.class);

        if(ObjectUtil.isNull(train.getId())){

            Train station1 = selectByUnique(req.getCode());
            if(ObjectUtil.isNotEmpty(station1)){
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_CODE_UNIQUE_ERROR);
            }
            train.setId(SnowUtil.getSnowflakeNextId());
            train.setCreateTime(now);
            train.setUpdateTime(now);
            trainMapper.insert(train);
        }else {

            train.setUpdateTime(now);
            trainMapper.updateByPrimaryKey(train);
        }


    };


    public PageResp<TrainQueryResp> query(TrainQueryReq req){

        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("id desc");
        TrainExample.Criteria criteria = trainExample.createCriteria();



        PageHelper.startPage(req.getPage(),req.getSize());
        List<Train> trains = trainMapper.selectByExample(trainExample);
        List<TrainQueryResp> list = BeanUtil.copyToList(trains, TrainQueryResp.class);


        PageInfo<Train> pageInfo =new PageInfo<>();
        log.info("行数", pageInfo.getTotal());
        log.info("ye数", pageInfo.getPages());
        PageResp<TrainQueryResp> trainQueryRespPageResp = new PageResp<>();
        trainQueryRespPageResp.setTotal(pageInfo.getTotal());
        trainQueryRespPageResp.setList(list);

        return trainQueryRespPageResp;


    }

    public List<TrainQueryResp> queryAll(){
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("code desc");
        List<Train> trains = trainMapper.selectByExample(trainExample);
        return BeanUtil.copyToList(trains,TrainQueryResp.class);
    }

    private Train selectByUnique(String  code) {
        TrainExample stationExample = new TrainExample();
        stationExample.createCriteria().andCodeEqualTo(code);
        List<Train> list = trainMapper.selectByExample(stationExample);
        if(CollUtil.isNotEmpty(list)){
            return list.get(0);
        }else {
            return null;
        }
    }


    public void delete(Long id) {

    trainMapper.deleteByPrimaryKey(id);
    }
}
