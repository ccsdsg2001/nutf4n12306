package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.R.PageResp;


import com.example.domain.DailyTrain;
import com.example.domain.DailyTrainExample;
import com.example.domain.Train;
import com.example.mapper.DailyTrainMapper;
import com.example.req.DailyTrainQueryReq;
import com.example.req.DailyTrainSaveReq;
import com.example.resp.DailyTrainQueryResp;
import com.example.util.SnowUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author cc
 * @date 2023年08月25日 19:42
 */
@Slf4j
@Service
public class DailyTrainService {


    @Resource
    private  TrainService trainService;

    @Resource
    private DailyTrainMapper dailyTrainMapper;

    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

//    @Resource
//    private DailyTrainTicketService dailyTrainTicketService;


    public void save(DailyTrainSaveReq req){
        DateTime now = DateTime.now();
        DailyTrain dailyTrain = BeanUtil.copyProperties(req, DailyTrain.class);

        if(ObjectUtil.isNull(dailyTrain.getId())){
            dailyTrain.setId(SnowUtil.getSnowflakeNextId());
            dailyTrain.setCreateTime(now);
            dailyTrain.setUpdateTime(now);
            dailyTrainMapper.insert(dailyTrain);
        }else {

            dailyTrain.setUpdateTime(now);
            dailyTrainMapper.updateByPrimaryKey(dailyTrain);
        }


    };


    public PageResp<DailyTrainQueryResp> query(DailyTrainQueryReq req){

        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        dailyTrainExample.setOrderByClause("date desc, code asc");
        DailyTrainExample.Criteria criteria = dailyTrainExample.createCriteria();
        if (ObjectUtil.isNotNull(req.getDate())) {
            criteria.andDateEqualTo(req.getDate());
        }
        if (ObjectUtil.isNotEmpty(req.getCode())) {
            criteria.andCodeEqualTo(req.getCode());
        }

        log.info("查询页码：{}", req.getPage());
        log.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<DailyTrain> dailyTrainList = dailyTrainMapper.selectByExample(dailyTrainExample);

        PageInfo<DailyTrain> pageInfo = new PageInfo<>(dailyTrainList);
        log.info("总行数：{}", pageInfo.getTotal());
        log.info("总页数：{}", pageInfo.getPages());

        List<DailyTrainQueryResp> list = BeanUtil.copyToList(dailyTrainList, DailyTrainQueryResp.class);

        PageResp<DailyTrainQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;


    }

    public void delete(Long id) {

    dailyTrainMapper.deleteByPrimaryKey(id);
    }

    public void genDaily(Date date){
//        生成某日所有车次信息，包括车次，车站，车厢，座位
        List<Train> trains = trainService.selectAll();
        if(CollUtil.isEmpty(trains)){
            log.info("没有车次，任务结束");
            return;
        }
        for (Train train : trains) {
            genDailyTrain(date, train);
        }


    };

    @Transactional
    public void genDailyTrain(Date date, Train train) {
        log.info("生成日期【{}】车次【{}】的信息开始", DateUtil.formatDate(date), train.getCode());
        // 删除该车次已有的数据
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        dailyTrainExample.createCriteria()
                .andDateEqualTo(date)
                .andCodeEqualTo(train.getCode());
        dailyTrainMapper.deleteByExample(dailyTrainExample);

        // 生成该车次的数据
        DateTime now = DateTime.now();
        DailyTrain dailyTrain = BeanUtil.copyProperties(train, DailyTrain.class);
        dailyTrain.setId(SnowUtil.getSnowflakeNextId());
        dailyTrain.setCreateTime(now);
        dailyTrain.setUpdateTime(now);
        dailyTrain.setDate(date);
        dailyTrainMapper.insert(dailyTrain);

        // 生成该车次的车站数据
        dailyTrainStationService.genDaily(date, train.getCode());
//
//        // 生成该车次的车厢数据
        dailyTrainCarriageService.genDaily(date, train.getCode());
//
//        // 生成该车次的座位数据
        dailyTrainSeatService.genDaily(date, train.getCode());
//
//        // 生成该车次的余票数据
//        dailyTrainTicketService.genDaily(dailyTrain, date, train.getCode());
//
//        // 生成令牌余量数据
//        skTokenService.genDaily(date, train.getCode());

        log.info("生成日期【{}】车次【{}】的信息结束", DateUtil.formatDate(date), train.getCode());
    }
}
