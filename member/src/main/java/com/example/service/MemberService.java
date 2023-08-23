package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.R.MemberLoginReq;
import com.example.R.MemberSendCodeReq;
import com.example.R.memberRegisterRequest;
import com.example.domain.Member;
import com.example.domain.MemberExample;
import com.example.exception.BusinessException;
import com.example.exception.BusinessExceptionEnum;
import com.example.mapper.MemberMapper;
import com.example.resp.MemberLoginResp;
import com.example.util.SnowUtil;
import jakarta.annotation.Resource;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cc
 * @date 2023年08月21日 16:18
 */
@Service
public class MemberService {


    private static final Logger LOG = LoggerFactory.getLogger(MemberService.class);
    @Resource
    private MemberMapper memberMapper;

    public int count() {
        return Math.toIntExact(memberMapper.countByExample(null));
    }


    public long register(memberRegisterRequest req) {
        // TODO:手机号注册登录接口（阿里云），第一次登录失败显示验证码。 检验短信频繁，保存短信登录表
        String mobile1 = req.getMobile();


        Member member1 = selectByMobile(mobile1);
        //members 为null yichang
        if (ObjectUtil.isNotNull(member1)) {
//            return members.get(0).getId();
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }


        Member member = new Member();
        member.setId(SnowUtil.getSnowflakeNextId());
        member.setMobile(mobile1);
        memberMapper.insert(member);
        return member.getId();
    }

    private Member selectByMobile(String mobile) {
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);
        if (CollUtil.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }


    public void sendcode(MemberSendCodeReq req) {
        String mobile = req.getMobile();
        Member member1 = selectByMobile(mobile);
        if (ObjectUtil.isNull(member1)) {
            LOG.info("手机号不存在");
            Member member = new Member();
            member.setId(SnowUtil.getSnowflakeNextId());
            member.setMobile(mobile);
            memberMapper.insert(member);
        } else {
            LOG.info("手机号存在");
        }

        //code
//        String code = RandomUtil.randomString(4);
        String code = "8888";
        LOG.info("生成短信验证码", code);
        //TODO:保存短信记录表：手机号，短信验证码，有效期，是否已经使用，业务类型，发送时间，使用时间
        LOG.info("保存短信记录表");
        //TODO:对接短信通道，发送通道，阿里云发送短信
        LOG.info("对接短信通道，发送短信");


    }

    public MemberLoginResp login(MemberLoginReq req) {
        String mobile = req.getMobile();
        String code = req.getCode();
        Member member1 = selectByMobile(mobile);
        if (ObjectUtil.isNull(member1)) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_NOT_EXIST);
        }

        //检验短信验证码 TODO:这里先写死,倒是或在修改
        if(!"8888".equals(code)){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_CODE_ERROR);
        }


        return BeanUtil.copyProperties(member1, MemberLoginResp.class);


    }
}
