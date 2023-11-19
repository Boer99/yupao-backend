package com.yupi.yupao.once;

import com.yupi.yupao.mapper.UserMapper;
import com.yupi.yupao.model.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;

@Deprecated
@Component
public class InsertUsers {
    @Resource
    private UserMapper userMapper;

    public void doInsertUsers(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM=10000000;
        for (int i = 9; i <INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("user"+i);
            user.setUserAccount("account"+i);
            user.setAvatarUrl("https://img2.woyaogexing.com/2020/06/30/c13b1ee4d51a4b4fa9ac7f788cad2a0a!400x400.jpeg");
            user.setGender(0);
            user.setPhone("13445632498");
            user.setEmail("ceshi@lanPao");
            user.setPlanetCode("111111");
            user.setUserRole(0);
            user.setUserStatus(0);
            user.setTags("[\"java\",\"python\",\"c++\",\"男\"]");
            user.setProfile("大家好，我是ikun");
            userMapper.insert(user);
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }
}
