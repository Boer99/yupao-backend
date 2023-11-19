package com.yupi.yupao.service;

import com.yupi.yupao.model.domain.Team;
import com.yupi.yupao.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class InsertUsersTest {
    @Resource
    private UserService userService;
    @Resource
    private TeamService teamService;

//    @Test
//    public void doInsertUsers() {
//        StopWatch stopWatch = new StopWatch();
//        final int INSERT_NUM = 500000;
//        List<User> userList = new ArrayList<>();
//        for (int i = 0; i < INSERT_NUM; i++) {
//            User user = new User();
//            user.setUsername("user" + UUID.randomUUID());
//            user.setUserPassword("6666666666");
//            user.setUserAccount("account" + UUID.randomUUID());
//            user.setAvatarUrl("https://img2.woyaogexing.com/2020/06/30/c13b1ee4d51a4b4fa9ac7f788cad2a0a!400x400.jpeg");
//            user.setGender(0);
//            user.setPhone("13445632498");
//            user.setEmail("ceshi@lanPao");
//            user.setPlanetCode("111111");
//            user.setUserRole(0);
//            user.setUserStatus(0);
//            user.setTags("[\"java\",\"python\",\"c++\",\"男\"]");
//            user.setProfile("大家好，我是ikun");
//            userList.add(user);
//        }
//        stopWatch.start();
//        userService.saveBatch(userList, 10000);
//        stopWatch.stop();
//        System.out.println(stopWatch.getTotalTimeMillis());
//    }

    @Test
    public void testLong() {
        Team team = teamService.getById(1);
        User user = userService.getById(1);
        // 校验你是不是队伍的队长
        // !team.getUserId().equals(loginUser.getId())
        System.out.println(team.getUserId() != user.getId());
    }
}
