package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.R.PageResp;


import com.example.domain.TrainCarriage;
import com.example.domain.TrainSeat;
import com.example.domain.TrainSeatExample;
import com.example.enums.SeatColEnum;
import com.example.mapper.TrainSeatMapper;
import com.example.req.TrainSeatQueryReq;
import com.example.req.TrainSeatSaveReq;
import com.example.resp.TrainSeatQueryResp;
import com.example.util.SnowUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author cc
 * @date 2023年08月25日 19:42
 */
@Slf4j
@Service
public class TrainSeatService {


    @Resource
    private TrainSeatMapper trainSeatMapper;

    @Resource
    private TrainCarriageService trainCarriageService;


    public void save(TrainSeatSaveReq req){
        DateTime now = DateTime.now();
        TrainSeat trainSeat = BeanUtil.copyProperties(req, TrainSeat.class);

        if(ObjectUtil.isNull(trainSeat.getId())){
            trainSeat.setId(SnowUtil.getSnowflakeNextId());
            trainSeat.setCreateTime(now);
            trainSeat.setUpdateTime(now);
            trainSeatMapper.insert(trainSeat);
        }else {

            trainSeat.setUpdateTime(now);
            trainSeatMapper.updateByPrimaryKey(trainSeat);
        }


    };


    public PageResp<TrainSeatQueryResp> query(TrainSeatQueryReq req){

        TrainSeatExample trainSeatExample = new TrainSeatExample();
        trainSeatExample.setOrderByClause("id desc");
        TrainSeatExample.Criteria criteria = trainSeatExample.createCriteria();



        PageHelper.startPage(req.getPage(),req.getSize());
        List<TrainSeat> trainSeats = trainSeatMapper.selectByExample(trainSeatExample);
        List<TrainSeatQueryResp> list = BeanUtil.copyToList(trainSeats, TrainSeatQueryResp.class);


        PageInfo<TrainSeat> pageInfo =new PageInfo<>();
        log.info("行数", pageInfo.getTotal());
        log.info("ye数", pageInfo.getPages());
        PageResp<TrainSeatQueryResp> trainSeatQueryRespPageResp = new PageResp<>();
        trainSeatQueryRespPageResp.setTotal(pageInfo.getTotal());
        trainSeatQueryRespPageResp.setList(list);

        return trainSeatQueryRespPageResp;


    }

    public void delete(Long id) {

    trainSeatMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    public void genTrainSeat(String trainCode){
        TrainSeatExample trainSeatExample = new TrainSeatExample();
        DateTime now = DateTime.now();

        TrainSeatExample.Criteria criteria = trainSeatExample.createCriteria();
        criteria.andTrainCodeEqualTo(trainCode);
        //清空当前车次下的所有座位记录
        trainSeatMapper.deleteByExample(trainSeatExample);


//        查找当前车次下的所有车厢
        List<TrainCarriage> carriageList = trainCarriageService.selecttraincode(trainCode);
log.info("车厢：{}", carriageList.size());
//        循环生成每个车厢的座位

        for(TrainCarriage trainCarriage:carriageList){
            //        拿到车厢数据：行数，列数，座位类型
            Integer rowCount = trainCarriage.getRowCount();
            String seatType = trainCarriage.getSeatType();
            int seatIndex =1;

//        根据车厢的座位类型，筛选出所有的列，比如车厢类型是一等座
            List<SeatColEnum> colEnumList = SeatColEnum.getColsByType(seatType);

//        循环行数
            log.info("列：{}", colEnumList);
            for (int row = 1; row <= rowCount ; row++) {

//循环列数
                for(SeatColEnum seatColEnum:colEnumList){
                    //        构造座位数据并保存数据库
                    TrainSeat trainSeat = new TrainSeat();


                    trainSeat.setId(SnowUtil.getSnowflakeNextId());
                    trainSeat.setTrainCode(trainCode);
                    trainSeat.setCarriageIndex(trainCarriage.getIndex());
                    trainSeat.setRow(StrUtil.fillBefore(String.valueOf(row), '0', 2));
                    trainSeat.setCol(seatColEnum.getCode());
                    trainSeat.setSeatType(seatType);
                    trainSeat.setCarriageSeatIndex(seatIndex++);
                    trainSeat.setCreateTime(now);
                    trainSeat.setUpdateTime(now);
                    trainSeatMapper.insert(trainSeat);
                }
            }



        }





    }

}
