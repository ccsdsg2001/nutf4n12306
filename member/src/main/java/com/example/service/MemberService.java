package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.jwt.JWTUtil;
import com.example.R.CommonResp;
import com.example.R.MemberLoginReq;
import com.example.R.MemberSendCodeReq;
import com.example.R.memberRegisterRequest;
import com.example.domain.Member;
import com.example.domain.MemberExample;
import com.example.exception.BusinessException;
import com.example.exception.BusinessExceptionEnum;
import com.example.mapper.MemberMapper;
import com.example.resp.MemberLoginResp;
import com.example.util.JwtUtil;
import com.example.util.SnowUtil;
import com.example.utils.ValidateCodeUtils;
import jakarta.annotation.Resource;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author cc
 * @date 2023年08月21日 16:18
 */
@Service
public class MemberService {


    private static final Logger LOG = LoggerFactory.getLogger(MemberService.class);
    @Resource
    private MemberMapper memberMapper;

    @Autowired
    private SendMsgService sendMsgService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public int count() {
        return Math.toIntExact(memberMapper.countByExample(null));
    }


    public long register(memberRegisterRequest req) {
        // TODO:手机号注册登录接口（阿里云），第一次登录失败显示验证码。 检验短信频繁，保存短信登录表
//
        String mobile = req.getMobile();
        Member memberDB = selectByMobile(mobile);

        //members 为null yichang
        if (ObjectUtil.isNotNull(memberDB)) {
            // return list.get(0).getId();
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }


        Member member = new Member();
        member.setId(SnowUtil.getSnowflakeNextId());
        member.setMobile(mobile);
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


    public void sendcode(MemberSendCodeReq req) throws ExecutionException, InterruptedException {
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
        String code1 = String.valueOf(ValidateCodeUtils.generateValidateCode(4));
//        String code = "8888";
        LOG.info("生成短信验证码", code1);
        //TODO:保存短信记录表：手机号，短信验证码，有效期，是否已经使用，业务类型，发送时间，使用时间
        LOG.info("保存短信记录表");


        stringRedisTemplate.opsForValue().set(mobile,code1);

//        对接短信通道，发送通道，阿里云发送短信
        LOG.info("对接短信通道，发送短信");
        sendMsgService.SendMsg(mobile,code1);

        LOG.info("短信验证码为:{}", code1);




    }

    public MemberLoginResp login(MemberLoginReq req) {
        String mobile = req.getMobile();
        String code = req.getCode();
//        查询的号码
        Member member1 = selectByMobile(mobile);
        String rediscode = stringRedisTemplate.opsForValue().get(mobile);
//
////

        if (ObjectUtil.isNull(member1)) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_NOT_EXIST);
        }

        if(ObjectUtil.isNull(rediscode)){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_CODE_ERROR);
        }




        //检验短信验证码
        if(!rediscode.equals(code)){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_CODE_ERROR);
        }





        MemberLoginResp memberLoginResp = BeanUtil.copyProperties(member1, MemberLoginResp.class);
        String token = JwtUtil.createToken(memberLoginResp.getId(), memberLoginResp.getMobile());
        memberLoginResp.setToken(token);
        stringRedisTemplate.delete(mobile);

        return memberLoginResp;


    }
}
