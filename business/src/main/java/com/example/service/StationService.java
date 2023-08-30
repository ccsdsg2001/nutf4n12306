package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.example.R.PageResp;

import com.example.domain.Station;
import com.example.domain.StationExample;
import com.example.domain.Train;
import com.example.domain.TrainExample;
import com.example.exception.BusinessException;
import com.example.exception.BusinessExceptionEnum;
import com.example.mapper.StationMapper;
import com.example.req.StationQueryReq;
import com.example.req.StationSaveReq;
import com.example.resp.StationQueryResp;
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
public class StationService {


    @Resource
    private StationMapper stationMapper;

    public void save(StationSaveReq req){
        DateTime now = DateTime.now();
        Station station = BeanUtil.copyProperties(req, Station.class);

        if(ObjectUtil.isNull(station.getId())){

            //检查是否唯一
            Station station1 = selectByUnique(req.getName());
            if(ObjectUtil.isNotEmpty(station1)){
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_STATION_NAME_UNIQUE_ERROR);
            }

            station.setId(SnowUtil.getSnowflakeNextId());
            station.setCreateTime(now);
            station.setUpdateTime(now);
            stationMapper.insert(station);
        }else {

            station.setUpdateTime(now);
            stationMapper.updateByPrimaryKey(station);
        }


    }

    private Station selectByUnique(String  name) {
        StationExample stationExample = new StationExample();
        stationExample.createCriteria().andNameEqualTo(name);
        List<Station> list = stationMapper.selectByExample(stationExample);
        if(CollUtil.isNotEmpty(list)){
            return list.get(0);
        }else {
            return null;
        }
    }

    ;

    public List<StationQueryResp> queryAll(){
        StationExample trainExample = new StationExample();
        trainExample.setOrderByClause("name_pinyin desc");
        List<Station> trains = stationMapper.selectByExample(trainExample);
        return BeanUtil.copyToList(trains,StationQueryResp.class);
    }

    public PageResp<StationQueryResp> query(StationQueryReq req){

        StationExample stationExample = new StationExample();
        stationExample.setOrderByClause("id desc");
        StationExample.Criteria criteria = stationExample.createCriteria();



        PageHelper.startPage(req.getPage(),req.getSize());
        List<Station> stations = stationMapper.selectByExample(stationExample);
        List<StationQueryResp> list = BeanUtil.copyToList(stations, StationQueryResp.class);


        PageInfo<Station> pageInfo =new PageInfo<>();
        log.info("行数", pageInfo.getTotal());
        log.info("ye数", pageInfo.getPages());
        PageResp<StationQueryResp> stationQueryRespPageResp = new PageResp<>();
        stationQueryRespPageResp.setTotal(pageInfo.getTotal());
        stationQueryRespPageResp.setList(list);

        return stationQueryRespPageResp;


    }

    public void delete(Long id) {

    stationMapper.deleteByPrimaryKey(id);
    }
}
