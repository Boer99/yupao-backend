package com.yupi.yupao.job;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.yupao.model.domain.User;
import com.yupi.yupao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PreCacheJob {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    // 重点用户
    private List<Long> mainUserList = Arrays.asList(1L);

    //    @Scheduled(cron = "0 0/5 * * * *") // 每5分钟执行一次
    //    @Scheduled(cron = "0/13 * * * * *") // 每13s执行一次
    @Scheduled(cron = "0 8 22 * * *") // 每天22:08分执行
    public void doCacheRecommendUser() {
        RLock rLock = redissonClient.getLock("yupao:precache:doCacheRecommendUser:lock");
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        try {
            // 等待时间为0，不需要可重入，一天只要一个服务执行就够了，抢不到就第二天再抢
            if (rLock.tryLock(0, 30000L, TimeUnit.MILLISECONDS)) {
                // 获取锁成功
                log.info("getLock:" + Thread.currentThread().getId());
                for (Long userId : mainUserList) {
                    String key = String.format("yupao:user:recommend:%s", userId);
                    Page<User> page = new Page<>(1, 10);
                    Page<User> userPage = userService.page(page);
                    userPage.setRecords(userPage.getRecords().stream()
                            .map(user -> userService.getSafetyUser(user))
                            .collect(Collectors.toList()));
                    // 写缓存
                    // 这里要直接捕获异常，即便写入失败，数据库也查出来，可以响应数据
                    try {
                        ops.set(key, userPage, 5, TimeUnit.MINUTES);
                    } catch (Exception e) {
                        log.error("redis set error");
                    }
                    log.info("定时任务 doCacheRecommendUser---" + LocalDateTime.now());
                }
            }else {
                log.info("fail to getLock");
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                rLock.unlock();
                log.info("unLock:" + Thread.currentThread().getId());
            }
        }
    }


}
