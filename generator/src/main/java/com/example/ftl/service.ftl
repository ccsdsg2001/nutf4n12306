package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.example.R.PageResp;
import com.example.R.${Domain}QueryReq;
import com.example.R.${Domain}Req;

import com.example.domain.${Domain};
import com.example.domain.${Domain}Example;
import com.example.mapper.${Domain}Mapper;
import com.example.resp.${Domain}QueryResp;
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
public class ${Domain}Service {


    @Resource
    private ${Domain}Mapper ${domain}Mapper;

    public void save(${Domain}Req req){
        DateTime now = DateTime.now();
        ${Domain} ${domain} = BeanUtil.copyProperties(req, ${Domain}.class);

        if(ObjectUtil.isNull(${domain}.getId())){
            ${domain}.setId(SnowUtil.getSnowflakeNextId());
            ${domain}.setCreateTime(now);
            ${domain}.setUpdateTime(now);
            ${domain}Mapper.insert(${domain});
        }else {

            ${domain}.setUpdateTime(now);
            ${domain}Mapper.updateByPrimaryKey(${domain});
        }


    };


    public PageResp<${Domain}QueryResp> query(${Domain}QueryReq req){

        ${Domain}Example ${domain}Example = new ${Domain}Example();
        ${domain}Example.setOrderByClause("id desc");
        ${Domain}Example.Criteria criteria = ${domain}Example.createCriteria();



        PageHelper.startPage(req.getPage(),req.getSize());
        List<${Domain}> ${domain}s = ${domain}Mapper.selectByExample(${domain}Example);
        List<${Domain}QueryResp> list = BeanUtil.copyToList(${domain}s, ${Domain}QueryResp.class);


        PageInfo<${Domain}> pageInfo =new PageInfo<>();
        log.info("行数", pageInfo.getTotal());
        log.info("ye数", pageInfo.getPages());
        PageResp<${Domain}QueryResp> ${domain}QueryRespPageResp = new PageResp<>();
        ${domain}QueryRespPageResp.setTotal(pageInfo.getTotal());
        ${domain}QueryRespPageResp.setList(list);

        return ${domain}QueryRespPageResp;


    }

    public void delete(Long id) {

    ${domain}Mapper.deleteByPrimaryKey(id);
    }
}
