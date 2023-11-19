package com.yupi.yupao.service;

import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class RedissonTest {
    @Resource
    private RedissonClient redissonClient;

    @Test
    void addTest() {
        List<Object> list = new ArrayList<>();
        list.add("test");
        System.out.println("list[0] =" + list.get(0));

        RList<String> rList = redissonClient.getList("test-list");
        rList.add("test");
        System.out.println("rlist[0] =" + rList.get(0));
    }

    @Test
    void removeTest() {
        RList<String> rList = redissonClient.getList("test-list");
        rList.remove(0);
    }
}
