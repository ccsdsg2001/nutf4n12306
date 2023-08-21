package com.example.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.example.R.memberRegisterRequest;
import com.example.domain.Member;
import com.example.domain.MemberExample;
import com.example.exception.BusinessException;
import com.example.exception.BusinessExceptionEnum;
import com.example.mapper.MemberMapper;
import com.example.util.SnowUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cc
 * @date 2023年08月21日 16:18
 */
@Service
public class MemberService {

    @Resource
    private MemberMapper memberMapper;

    public int count(){
        return Math.toIntExact(memberMapper.countByExample(null));
    }


    public long register(memberRegisterRequest mobile){
        String mobile1 = mobile.getMobile();



        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile1);
        List<Member> members = memberMapper.selectByExample(memberExample);
        //members 为null yichang
        if(CollUtil.isEmpty(members)){
//            return members.get(0).getId();
            throw  new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }


        Member member = new Member();
        member.setId(SnowUtil.getSnowflakeNextId());
        member.setMobile(mobile1);
        memberMapper.insert(member);
        return member.getId();
    }

}
