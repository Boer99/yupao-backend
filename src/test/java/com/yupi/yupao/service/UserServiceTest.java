package com.yupi.yupao.service;

// [编程学习交流圈](https://www.code-nav.cn/) 连接万名编程爱好者，一起优秀！20000+ 小伙伴交流分享、40+ 大厂嘉宾一对一答疑、100+ 各方向编程交流群、4000+ 编程问答参考

import com.yupi.yupao.model.domain.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 用户服务测试
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 测试添加用户
     */
    @Test
    public void testAddUser() {
        User user = new User();
        user.setUsername("dogYupi4");
        user.setUserAccount("123");
        user.setAvatarUrl("https://636f-codenav-8grj8px727565176-1256524210.tcb.qcloud.la/img/logo.png");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("123");
        user.setEmail("456");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    // https://www.code-nav.cn/

    /**
     * 测试更新用户
     */
    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("dogYupi");
        user.setUserAccount("123");
        user.setAvatarUrl("https://636f-codenav-8grj8px727565176-1256524210.tcb.qcloud.la/img/logo.png");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("123");
        user.setEmail("456");
        boolean result = userService.updateById(user);
        Assertions.assertTrue(result);
    }

    /**
     * 测试删除用户
     */
    @Test
    public void testDeleteUser() {
        boolean result = userService.removeById(1L);
        Assertions.assertTrue(result);
    }

    /**
     * 测试获取用户
     */
    @Test
    public void testGetUser() {
        User user = userService.getById(1L);
        Assertions.assertNotNull(user);
    }

    /**
     * 测试用户注册
     */
    @Test
    void userRegister() {
        String userAccount = "yupi";
        String userPassword = "";
        String checkPassword = "123456";
        String planetCode = "1";
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "yu";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "yupi";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "yu pi";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "dogYupi";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "yupi";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
    }

    /**
     * 测试标签搜索用户
     */
    @Test
    void searchUsersByTags() {
        List<String> tagNameList = Arrays.asList("java", "python");
        List<User> users = userService.searchUsersByTags(tagNameList);
        Assert.assertNotNull(users);
    }


    /**
     * 测试自定义RedisTemplate
     */
    @Test
    void testRedisTemplate() {
        User user = new User();
        user.setUsername("user" + UUID.randomUUID());
        user.setUserPassword("6666666666");
        user.setUserAccount("account" + UUID.randomUUID());
        user.setAvatarUrl("https://img2.woyaogexing.com/2020/06/30/c13b1ee4d51a4b4fa9ac7f788cad2a0a!400x400.jpeg");
        user.setGender(0);
        user.setPhone("13445632498");
        user.setEmail("ceshi@lanPao");
        user.setPlanetCode("111111");
        user.setUserRole(0);
        user.setUserStatus(0);
        user.setTags("[\"java\",\"python\",\"c++\",\"男\"]");
        user.setProfile("大家好，我是ikun");
        redisTemplate.opsForValue().set("yupao:test:userServiceTest:testRedisTemplate", user);
    }
}