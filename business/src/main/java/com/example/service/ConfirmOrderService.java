package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.example.R.PageResp;


import com.example.context.LoginMemberContext;
import com.example.domain.*;
import com.example.dto.ConfirmOrderMQDto;
import com.example.enums.ConfirmOrderStatusEnum;
import com.example.enums.RedisKeyPreEnum;
import com.example.enums.SeatColEnum;
import com.example.enums.SeatTypeEnum;
import com.example.exception.BusinessException;
import com.example.exception.BusinessExceptionEnum;
import com.example.mapper.ConfirmOrderMapper;
import com.example.req.ConfirmOrderQueryReq;
import com.example.req.ConfirmOrderDoReq;
import com.example.req.ConfirmOrderTicketReq;
import com.example.resp.ConfirmOrderQueryResp;
import com.example.util.SnowUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;


import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.enums.SeatTypeEnum.YDZ;

/**
 * @author cc
 * @date 2023年08月25日 19:42
 */
@Slf4j
@Service
public class ConfirmOrderService {

    @Resource
    AfterConfirmOrderService afterConfirmOrderService;

    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;

//    @Autowired
//    private RedissonClient redissonClient;

    @Autowired
    private  SkTokenService skTokenService;

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @Resource
    private ConfirmOrderMapper confirmOrderMapper;
    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    public void save(ConfirmOrderDoReq req) {


        DateTime now = DateTime.now();
        ConfirmOrder confirmOrder = BeanUtil.copyProperties(req, ConfirmOrder.class);

        if (ObjectUtil.isNull(confirmOrder.getId())) {
            confirmOrder.setId(SnowUtil.getSnowflakeNextId());
            confirmOrder.setCreateTime(now);
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.insert(confirmOrder);
        } else {

            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.updateByPrimaryKey(confirmOrder);
        }


    }

    ;


    public PageResp<ConfirmOrderQueryResp> query(ConfirmOrderQueryReq req) {

        ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
        confirmOrderExample.setOrderByClause("id desc");
        ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();


        PageHelper.startPage(req.getPage(), req.getSize());
        List<ConfirmOrder> confirmOrders = confirmOrderMapper.selectByExample(confirmOrderExample);
        List<ConfirmOrderQueryResp> list = BeanUtil.copyToList(confirmOrders, ConfirmOrderQueryResp.class);


        PageInfo<ConfirmOrder> pageInfo = new PageInfo<>();
        log.info("行数", pageInfo.getTotal());
        log.info("ye数", pageInfo.getPages());
        PageResp<ConfirmOrderQueryResp> confirmOrderQueryRespPageResp = new PageResp<>();
        confirmOrderQueryRespPageResp.setTotal(pageInfo.getTotal());
        confirmOrderQueryRespPageResp.setList(list);

        return confirmOrderQueryRespPageResp;


    }

    public void delete(Long id) {

        confirmOrderMapper.deleteByPrimaryKey(id);
    }

    @SentinelResource(value = "doConfirm",blockHandler = "doConfirmBlock")
    @Async
    public void doconfirm(ConfirmOrderMQDto dto) {
//        String key = RedisKeyPreEnum.CONFIRM_ORDER+DateUtil.formatDate(req.getDate())+"-"+req.getTrainCode();
//         // 校验令牌余量
//         boolean validSkToken = skTokenService.validSkToken(dto.getDate(), dto.getTrainCode(), LoginMemberContext.getId());
//         if (validSkToken) {
//             log.info("令牌校验通过");
//         } else {
//             log.info("令牌校验不通过");
//             throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_SK_TOKEN_FAIL);
//         }
//
////         获取分布式锁
//
//
//        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(key, key, 5, TimeUnit.SECONDS);
//        if(aBoolean){
//            log.info("锁到");
//        }else {
//            log.info("无锁，请稍后重试");
//            throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_LOCK_FAIL);
//        }
// // 使用redisson，自带看门狗
        // lock = redissonClient.getLock(lockKey);
        //
        // // 红锁的写法
        // // RedissonRedLock redissonRedLock = new RedissonRedLock(lock, lock, lock);
        // // boolean tryLock1 = redissonRedLock.tryLock(0, TimeUnit.SECONDS);
        //
        // /**
        //   waitTime – the maximum time to acquire the lock 等待获取锁时间(最大尝试获得锁的时间)，超时返回false
        //   leaseTime – lease time 锁时长，即n秒后自动释放锁
        //   time unit – time unit 时间单位
        //  */
        // // boolean tryLock = lock.tryLock(30, 10, TimeUnit.SECONDS); // 不带看门狗
        // boolean tryLock = lock.tryLock(0, TimeUnit.SECONDS); // 带看门狗
        // if (tryLock) {
        //     log.info("恭喜，抢到锁了！");
        //     // 可以把下面这段放开，只用一个线程来测试，看看redisson的看门狗效果
        //     // for (int i = 0; i < 30; i++) {
        //     //     Long expire = redisTemplate.opsForValue().getOperations().getExpire(lockKey);
        //     //     log.info("锁过期时间还有：{}", expire);
        //     //     Thread.sleep(1000);
        //     // }
        // } else {
        //     // 只是没抢到锁，并不知道票抢完了没，所以提示稍候再试
        //     log.info("很遗憾，没抢到锁");
        //     throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_LOCK_FAIL);
        // }
//  TODO: 省略了：数据检验是否正确 如:车次 ，余票 ，车次有效 ，ticket ，同乘客同车次是否买过

//保存确认订单表，状态初始化
//try{
//            ConfirmOrder confirmOrder = new ConfirmOrder();
//            DateTime now = DateTime.now();
//            Date date = req.getDate();
//            String trainCode = req.getTrainCode();
//            String start = req.getStart();
//            String end = req.getEnd();
//            List<ConfirmOrderTicketReq> tickets = req.getTickets();
//            confirmOrder.setId(SnowUtil.getSnowflakeNextId());
//            confirmOrder.setCreateTime(now);
//            confirmOrder.setUpdateTime(now);
//            confirmOrder.setMemberId(LoginMemberContext.getId());
//            confirmOrder.setDate(req.getDate());
//            confirmOrder.setTrainCode(req.getTrainCode());
//            confirmOrder.setStart(req.getStart());
//            confirmOrder.setEnd(req.getEnd());
//            confirmOrder.setDailyTrainTicketId(req.getDailyTrainTicketId());
//            confirmOrder.setStatus(ConfirmOrderStatusEnum.INIT.getCode());
//            confirmOrder.setTickets(JSON.toJSONString(tickets));
//            confirmOrderMapper.insert(confirmOrder);
//
//
////        查出余票记录 得到库存
//            DailyTrainTicket dailyTrainTicket = dailyTrainTicketService.selectByUnique(date, trainCode, start, end);
//            log.info("查出余票记录:{}", dailyTrainTicket);
//
////        扣除余票数量，判断余票数量足够
//            reduceTickets(req, dailyTrainTicket);
//
////        最终选座结果
//            List<DailyTrainSeat> finalSeatList = new ArrayList<>();
////    计算相对第一个座位的偏移值 如C1,D2 则是【0,5】 如A1,B1,C1 则是【0,1,2】
//            ConfirmOrderTicketReq ticketReq = tickets.get(0);
//            if (StrUtil.isNotBlank(ticketReq.getSeat())) {
//                log.info("有选座");
////            查出本次选座座位类型哪些列,用于计算座位和第一个座位的偏移值
//                List<SeatColEnum> seatColEnums = SeatColEnum.getColsByType(ticketReq.getSeatTypeCode());
//                log.info("本次选座包含列：{}", seatColEnums);
//
//                // 组成和前端两排选座一样的列表，用于作参照的座位列表，例：referSeatList = {A1, C1, D1, F1, A2, C2, D2, F2}
//                List<String> referSeatList = new ArrayList<>();
//                for (int i = 1; i <= 2; i++) {
//                    for (SeatColEnum seatColEnum : seatColEnums) {
//                        referSeatList.add(seatColEnum.getCode() + i);
//                    }
//                }
//                log.info("用于作参照的两排座位：{}", referSeatList);
//
//                List<Integer> offsetList = new ArrayList<>();
//                // 绝对偏移值，即：在参照座位列表中的位置
//                List<Integer> aboluteOffsetList = new ArrayList<>();
//                for (ConfirmOrderTicketReq ticketReq1 : tickets) {
//                    int index = referSeatList.indexOf(ticketReq1.getSeat());
//                    aboluteOffsetList.add(index);
//                }
//
//                log.info("计算得到所有座位的绝对偏移值：{}", aboluteOffsetList);
//                for (Integer index : aboluteOffsetList) {
//                    int offset = index - aboluteOffsetList.get(0);
//                    offsetList.add(offset);
//                }
//                log.info("计算得到所有座位的相对第一个座位的偏移值：{}", offsetList);
//
//
//                getseat(finalSeatList,
//                        date,
//                        trainCode,
//                        ticketReq.getSeatTypeCode(),
//                        ticketReq.getSeat().split("")[0], // 从A1得到A
//                        offsetList,
//                        dailyTrainTicket.getStartIndex(),
//                        dailyTrainTicket.getEndIndex()
//                );
////        挑选符合条件的座位，入不满足，进入下个车厢
//
//            } else {
//                log.info("本次没有选座");
//                for(ConfirmOrderTicketReq ticketReq1 :tickets){
//                    getseat(finalSeatList,
//                            date,
//                            trainCode,
//                            ticketReq.getSeatTypeCode(),
//                            null,
//                            null,
//                            dailyTrainTicket.getStartIndex(),
//                            dailyTrainTicket.getEndIndex()
//                    );
//                }
//            }
//            log.info("最终选座：", finalSeatList);
//
//
////        选中座位后事务处理
//
////        座位表修改售卖情况
////        修改余票数量
////为会员增加购票记录
////        更新订单为成功
//            try {
//                afterConfirmOrderService.afterDoConfirm(dailyTrainTicket, finalSeatList, tickets, confirmOrder);
//            } catch (Exception e) {
//                log.error("保存购票信息失败", e);
//                throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_EXCEPTION);
//            }}finally {
//
//    //        删除分布式锁
//
//    redisTemplate.delete(key);
//            }
        MDC.put("log_ID", dto.getLogId());
        log.info("异步出票开始：{}", dto);
        // // 校验令牌余量
        // boolean validSkToken = skTokenService.validSkToken(dto.getDate(), dto.getTrainCode(), LoginMemberContext.getId());
        // if (validSkToken) {
        //     log.info("令牌校验通过");
        // } else {
        //     log.info("令牌校验不通过");
        //     throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_SK_TOKEN_FAIL);
        // }
        //
        // 获取分布式锁
        String lockKey = RedisKeyPreEnum.CONFIRM_ORDER + "-" + DateUtil.formatDate(dto.getDate()) + "-" + dto.getTrainCode();
        // setIfAbsent就是对应redis的setnx
        Boolean setIfAbsent = redisTemplate.opsForValue().setIfAbsent(lockKey, lockKey, 10, TimeUnit.SECONDS);
        if (Boolean.TRUE.equals(setIfAbsent)) {
            log.info("恭喜，抢到锁了！lockKey：{}", lockKey);
        } else {
            // 只是没抢到锁，并不知道票抢完了没，所以提示稍候再试
            // log.info("很遗憾，没抢到锁！lockKey：{}", lockKey);
            // throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_LOCK_FAIL);

            log.info("没抢到锁，有其它消费线程正在出票，不做任何处理");
            return;
        }
        try {
            while (true) {
                // 取确认订单表的记录，同日期车次，状态是I，分页处理，每次取N条
                ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
                confirmOrderExample.setOrderByClause("id asc");
                ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();
                criteria.andDateEqualTo(dto.getDate())
                        .andTrainCodeEqualTo(dto.getTrainCode())
                        .andStatusEqualTo(ConfirmOrderStatusEnum.INIT.getCode());
                PageHelper.startPage(1, 5);
                List<ConfirmOrder> list = confirmOrderMapper.selectByExampleWithBLOBs(confirmOrderExample);

                if (CollUtil.isEmpty(list)) {
                    log.info("没有需要处理的订单，结束循环");
                    break;
                } else {
                    log.info("本次处理{}条订单", list.size());
                }

                // 一条一条的卖
                list.forEach(confirmOrder -> {
                    try {
                        sell(confirmOrder);
                    } catch (BusinessException e) {
                        if (e.getE().equals(BusinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR)) {
                            log.info("本订单余票不足，继续售卖下一个订单");
                            confirmOrder.setStatus(ConfirmOrderStatusEnum.EMPTY.getCode());
                            updateStatus(confirmOrder);
                        } else {
                            throw e;
                        }
                    }
                });
            }

            // log.info("购票流程结束，释放锁！lockKey：{}", lockKey);
            // redisTemplate.delete(lockKey);
            // } catch (InterruptedException e) {
            //     log.error("购票异常", e);
        } finally {
            // try finally不能包含加锁的那段代码，否则加锁失败会走到finally里，从而释放别的线程的锁
            log.info("购票流程结束，释放锁！lockKey：{}", lockKey);
            redisTemplate.delete(lockKey);
            // log.info("购票流程结束，释放锁！");
            // if (null != lock && lock.isHeldByCurrentThread()) {
            //     lock.unlock();
            // }
        }


}

    /**
     * 售票
     * @param confirmOrder
     */
    private void sell(ConfirmOrder confirmOrder) {
        // 为了演示排队效果，每次出票增加200毫秒延时
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 构造ConfirmOrderDoReq
        ConfirmOrderDoReq req = new ConfirmOrderDoReq();
        req.setMemberId(confirmOrder.getMemberId());
        req.setDate(confirmOrder.getDate());
        req.setTrainCode(confirmOrder.getTrainCode());
        req.setStart(confirmOrder.getStart());
        req.setEnd(confirmOrder.getEnd());
        req.setDailyTrainTicketId(confirmOrder.getDailyTrainTicketId());
        req.setTickets(JSON.parseArray(confirmOrder.getTickets(), ConfirmOrderTicketReq.class));
        req.setImageCode("");
        req.setImageCodeToken("");
        req.setLogId("");

        // 省略业务数据校验，如：车次是否存在，余票是否存在，车次是否在有效期内，tickets条数>0，同乘客同车次是否已买过

        // 将订单设置成处理中，避免重复处理
        log.info("将确认订单更新成处理中，避免重复处理，confirm_order.id: {}", confirmOrder.getId());
        confirmOrder.setStatus(ConfirmOrderStatusEnum.PENDING.getCode());
        updateStatus(confirmOrder);

        Date date = req.getDate();
        String trainCode = req.getTrainCode();
        String start = req.getStart();
        String end = req.getEnd();
        List<ConfirmOrderTicketReq> tickets = req.getTickets();
        //
        // // 保存确认订单表，状态初始
        // DateTime now = DateTime.now();
        // ConfirmOrder confirmOrder = new ConfirmOrder();
        // confirmOrder.setId(SnowUtil.getSnowflakeNextId());
        // confirmOrder.setCreateTime(now);
        // confirmOrder.setUpdateTime(now);
        // confirmOrder.setMemberId(req.getMemberId());
        // confirmOrder.setDate(date);
        // confirmOrder.setTrainCode(trainCode);
        // confirmOrder.setStart(start);
        // confirmOrder.setEnd(end);
        // confirmOrder.setDailyTrainTicketId(req.getDailyTrainTicketId());
        // confirmOrder.setStatus(ConfirmOrderStatusEnum.INIT.getCode());
        // confirmOrder.setTickets(JSON.toJSONString(tickets));
        // confirmOrderMapper.insert(confirmOrder);

        // // 从数据库里查出订单
        // ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
        // confirmOrderExample.setOrderByClause("id asc");
        // ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();
        // criteria.andDateEqualTo(req.getDate())
        //         .andTrainCodeEqualTo(req.getTrainCode())
        //         .andStatusEqualTo(ConfirmOrderStatusEnum.INIT.getCode());
        // List<ConfirmOrder> list = confirmOrderMapper.selectByExampleWithBLOBs(confirmOrderExample);
        // ConfirmOrder confirmOrder;
        // if (CollUtil.isEmpty(list)) {
        //     log.info("找不到原始订单，结束");
        //     return;
        // } else {
        //     log.info("本次处理{}条确认订单", list.size());
        //     confirmOrder = list.get(0);
        // }

        // 查出余票记录，需要得到真实的库存
        DailyTrainTicket dailyTrainTicket = dailyTrainTicketService.selectByUnique(date, trainCode, start, end);
        log.info("查出余票记录：{}", dailyTrainTicket);

        // 预扣减余票数量，并判断余票是否足够
        reduceTickets(req, dailyTrainTicket);

        // 最终的选座结果
        List<DailyTrainSeat> finalSeatList = new ArrayList<>();
        // 计算相对第一个座位的偏移值
        // 比如选择的是C1,D2，则偏移值是：[0,5]
        // 比如选择的是A1,B1,C1，则偏移值是：[0,1,2]
        ConfirmOrderTicketReq ticketReq0 = tickets.get(0);
        if (StrUtil.isNotBlank(ticketReq0.getSeat())) {
            log.info("本次购票有选座");
            // 查出本次选座的座位类型都有哪些列，用于计算所选座位与第一个座位的偏离值
            List<SeatColEnum> colEnumList = SeatColEnum.getColsByType(ticketReq0.getSeatTypeCode());
            log.info("本次选座的座位类型包含的列：{}", colEnumList);

            // 组成和前端两排选座一样的列表，用于作参照的座位列表，例：referSeatList = {A1, C1, D1, F1, A2, C2, D2, F2}
            List<String> referSeatList = new ArrayList<>();
            for (int i = 1; i <= 2; i++) {
                for (SeatColEnum seatColEnum : colEnumList) {
                    referSeatList.add(seatColEnum.getCode() + i);
                }
            }
            log.info("用于作参照的两排座位：{}", referSeatList);

            List<Integer> offsetList = new ArrayList<>();
            // 绝对偏移值，即：在参照座位列表中的位置
            List<Integer> aboluteOffsetList = new ArrayList<>();
            for (ConfirmOrderTicketReq ticketReq : tickets) {
                int index = referSeatList.indexOf(ticketReq.getSeat());
                aboluteOffsetList.add(index);
            }
            log.info("计算得到所有座位的绝对偏移值：{}", aboluteOffsetList);
            for (Integer index : aboluteOffsetList) {
                int offset = index - aboluteOffsetList.get(0);
                offsetList.add(offset);
            }
            log.info("计算得到所有座位的相对第一个座位的偏移值：{}", offsetList);

            getseat(finalSeatList,
                    date,
                    trainCode,
                    ticketReq0.getSeatTypeCode(),
                    ticketReq0.getSeat().split("")[0], // 从A1得到A
                    offsetList,
                    dailyTrainTicket.getStartIndex(),
                    dailyTrainTicket.getEndIndex()
            );

        } else {
            log.info("本次购票没有选座");
            for (ConfirmOrderTicketReq ticketReq : tickets) {
                getseat(finalSeatList,
                        date,
                        trainCode,
                        ticketReq.getSeatTypeCode(),
                        null,
                        null,
                        dailyTrainTicket.getStartIndex(),
                        dailyTrainTicket.getEndIndex()
                );
            }
        }

        log.info("最终选座：{}", finalSeatList);

        // 选中座位后事务处理：
        // 座位表修改售卖情况sell；
        // 余票详情表修改余票；
        // 为会员增加购票记录
        // 更新确认订单为成功
        try {
            afterConfirmOrderService.afterDoConfirm(dailyTrainTicket, finalSeatList, tickets, confirmOrder);
        } catch (Exception e) {
            log.error("保存购票信息失败", e);
            throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_EXCEPTION);
        }
    }

//选座位
    private void getseat(List<DailyTrainSeat> finalSeatList, Date date, String trainCode, String seatType,
                         String column, List<Integer> offsetList, Integer startIndex, Integer endIndex) {
        //        选座：一个车厢一个车厢获取座位
        List<DailyTrainSeat> getSeatList = new ArrayList<>();
        List<DailyTrainCarriage> carriageList = dailyTrainCarriageService.selectBySeatType(date, trainCode, seatType);
        log.info("符合{}个条件车厢", getSeatList);

        // 一个车箱一个车箱的获取座位数据
        for (DailyTrainCarriage dailyTrainCarriage : carriageList) {
            log.info("开始从车厢{}选座", dailyTrainCarriage.getIndex());
            getSeatList = new ArrayList<>();
            List<DailyTrainSeat> seatList = dailyTrainSeatService.selectByCarriage(date, trainCode, dailyTrainCarriage.getIndex());
            log.info("车厢{}的座位数：{}", dailyTrainCarriage.getIndex(), seatList.size());
            for (int i = 0; i < seatList.size(); i++) {
                DailyTrainSeat dailyTrainSeat = seatList.get(i);
                Integer seatIndex = dailyTrainSeat.getCarriageSeatIndex();
                String col = dailyTrainSeat.getCol();

                // 判断当前座位不能被选中过
                boolean alreadyChooseFlag = false;
                for (DailyTrainSeat finalSeat : finalSeatList) {
                    if (finalSeat.getId().equals(dailyTrainSeat.getId())) {
                        alreadyChooseFlag = true;
                        break;
                    }
                }
                if (alreadyChooseFlag) {
                    log.info("座位{}被选中过，不能重复选中，继续判断下一个座位", seatIndex);
                    continue;
                }

                // 判断column，有值的话要比对列号
                if (StrUtil.isBlank(column)) {
                    log.info("无选座");
                } else {
                    if (!column.equals(col)) {
                        log.info("座位{}列值不对，继续判断下一个座位，当前列值：{}，目标列值：{}", seatIndex, col, column);
                        continue;
                    }
                }

                boolean isChoose = calSell(dailyTrainSeat, startIndex, endIndex);
                if (isChoose) {
                    log.info("选中座位");
                    getSeatList.add(dailyTrainSeat);
                } else {
                    continue;
                }

                // 根据offset选剩下的座位
                boolean isGetAllOffsetSeat = true;
                if (CollUtil.isNotEmpty(offsetList)) {
                    log.info("有偏移值：{}，校验偏移的座位是否可选", offsetList);
                    // 从索引1开始，索引0就是当前已选中的票
                    for (int j = 1; j < offsetList.size(); j++) {
                        Integer offset = offsetList.get(j);
                        // 座位在库的索引是从1开始
                        // int nextIndex = seatIndex + offset - 1;
                        int nextIndex = i + offset;

                        // 有选座时，一定是在同一个车箱
                        if (nextIndex >= seatList.size()) {
                            log.info("座位{}不可选，偏移后的索引超出了这个车箱的座位数", nextIndex);
                            isGetAllOffsetSeat = false;
                            break;
                        }

                        DailyTrainSeat nextDailyTrainSeat = seatList.get(nextIndex);
                        boolean isChooseNext = calSell(nextDailyTrainSeat, startIndex, endIndex);
                        if (isChooseNext) {
                            log.info("座位{}被选中", nextDailyTrainSeat.getCarriageSeatIndex());
                            getSeatList.add(nextDailyTrainSeat);
                        } else {
                            log.info("座位{}不可选", nextDailyTrainSeat.getCarriageSeatIndex());
                            isGetAllOffsetSeat = false;
                            break;
                        }
                    }
                }

                if (!isGetAllOffsetSeat) {
                    getSeatList = new ArrayList<>();
                    continue;
                }

                // 保存选好的座位
                finalSeatList.addAll(getSeatList);
                return;
            }

            }
        }

    /**
     * 计算某座位在区间内是否可卖
     * 例：sell=10001，本次购买区间站1~4，则区间已售000
     * 全部是0，表示这个区间可买；只要有1，就表示区间内已售过票
     *
     * 选中后，要计算购票后的sell，比如原来是10001，本次购买区间站1~4
     * 方案：构造本次购票造成的售卖信息01110，和原sell 10001按位或，最终得到11111
     */
    private boolean calSell(DailyTrainSeat dailyTrainSeat, Integer startIndex, Integer endIndex) {
        // 00001, 00000
        String sell = dailyTrainSeat.getSell();
        //  000, 000
        String sellPart = sell.substring(startIndex, endIndex);
        if (Integer.parseInt(sellPart) > 0) {
            log.info("座位{}在本次车站区间{}~{}已售过票，不可选中该座位", dailyTrainSeat.getCarriageSeatIndex(), startIndex, endIndex);
            return false;
        } else {
            log.info("座位{}在本次车站区间{}~{}未售过票，可选中该座位", dailyTrainSeat.getCarriageSeatIndex(), startIndex, endIndex);
            //  111,   111
            String curSell = sellPart.replace('0', '1');
            // 0111,  0111
            curSell = StrUtil.fillBefore(curSell, '0', endIndex);
            // 01110, 01110
            curSell = StrUtil.fillAfter(curSell, '0', sell.length());

            // 当前区间售票信息curSell 01110与库里的已售信息sell 00001按位与，即可得到该座位卖出此票后的售票详情
            // 15(01111), 14(01110 = 01110|00000)
            int newSellInt = NumberUtil.binaryToInt(curSell) | NumberUtil.binaryToInt(sell);
            //  1111,  1110
            String newSell = NumberUtil.getBinaryStr(newSellInt);
            // 01111, 01110
            newSell = StrUtil.fillBefore(newSell, '0', sell.length());
            log.info("座位{}被选中，原售票信息：{}，车站区间：{}~{}，即：{}，最终售票信息：{}"
                    , dailyTrainSeat.getCarriageSeatIndex(), sell, startIndex, endIndex, curSell, newSell);
            dailyTrainSeat.setSell(newSell);
            return true;

        }
    }
    private static void reduceTickets(ConfirmOrderDoReq req, DailyTrainTicket dailyTrainTicket) {
        for (ConfirmOrderTicketReq ticketReq : req.getTickets()) {
            String seatTypeCode = ticketReq.getSeatTypeCode();
            SeatTypeEnum seatTypeEnum = EnumUtil.getBy(SeatTypeEnum::getCode, seatTypeCode);

            switch (seatTypeEnum) {
                case YDZ -> {
                    int countLeft = dailyTrainTicket.getYdz() - 1;
                    if (countLeft < 0) {
                        throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setYdz(countLeft);
                }
                case EDZ -> {
                    int countLeft = dailyTrainTicket.getEdz() - 1;
                    if (countLeft < 0) {
                        throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setEdz(countLeft);
                }
                case RW -> {
                    int countLeft = dailyTrainTicket.getRw() - 1;
                    if (countLeft < 0) {
                        throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setRw(countLeft);
                }
                case YW -> {
                    int countLeft = dailyTrainTicket.getYw() - 1;
                    if (countLeft < 0) {
                        throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setYw(countLeft);
                }
            }
        }
    }

//    限流方法
    public void doConfirmBlock(ConfirmOrderDoReq req, BlockException e){
        throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_FLOW_EXCEPTION
        );
    } 
    
    /**
     * 更新状态
     * @param confirmOrder
     */
    public void updateStatus(ConfirmOrder confirmOrder) {
        ConfirmOrder confirmOrderForUpdate = new ConfirmOrder();
        confirmOrderForUpdate.setId(confirmOrder.getId());
        confirmOrderForUpdate.setUpdateTime(new Date());
        confirmOrderForUpdate.setStatus(confirmOrder.getStatus());
        confirmOrderMapper.updateByPrimaryKeySelective(confirmOrderForUpdate);
    }

    /**
     * 查询前面有几个人在排队
     * @param id
     */
    public Integer queryLineCount(Long id) {
        ConfirmOrder confirmOrder = confirmOrderMapper.selectByPrimaryKey(id);
        ConfirmOrderStatusEnum statusEnum = EnumUtil.getBy(ConfirmOrderStatusEnum::getCode, confirmOrder.getStatus());
        int result = switch (statusEnum) {
            case PENDING -> 0; // 排队0
            case SUCCESS -> -1; // 成功
            case FAILURE -> -2; // 失败
            case EMPTY -> -3; // 无票
            case CANCEL -> -4; // 取消
            case INIT -> 999; // 需要查表得到实际排队数量
        };

        if (result == 999) {
            // 排在第几位，下面的写法：where a=1 and (b=1 or c=1) 等价于 where (a=1 and b=1) or (a=1 and c=1)
            ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
            confirmOrderExample.or().andDateEqualTo(confirmOrder.getDate())
                    .andTrainCodeEqualTo(confirmOrder.getTrainCode())
                    .andCreateTimeLessThan(confirmOrder.getCreateTime())
                    .andStatusEqualTo(ConfirmOrderStatusEnum.INIT.getCode());
            confirmOrderExample.or().andDateEqualTo(confirmOrder.getDate())
                    .andTrainCodeEqualTo(confirmOrder.getTrainCode())
                    .andCreateTimeLessThan(confirmOrder.getCreateTime())
                    .andStatusEqualTo(ConfirmOrderStatusEnum.PENDING.getCode());
            return Math.toIntExact(confirmOrderMapper.countByExample(confirmOrderExample));
        } else {
            return result;
        }
    }


    /**
     * 取消排队，只有I状态才能取消排队，所以按状态更新
     * @param id
     */
    public Integer cancel(Long id) {
        ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
        ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();
        criteria.andIdEqualTo(id).andStatusEqualTo(ConfirmOrderStatusEnum.INIT.getCode());
        ConfirmOrder confirmOrder = new ConfirmOrder();
        confirmOrder.setStatus(ConfirmOrderStatusEnum.CANCEL.getCode());
        return confirmOrderMapper.updateByExampleSelective(confirmOrder, confirmOrderExample);
    }
}
