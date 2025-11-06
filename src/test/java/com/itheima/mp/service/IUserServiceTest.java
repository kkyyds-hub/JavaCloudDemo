package com.itheima.mp.service;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.po.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class IUserServiceTest {
    @Autowired
    private IUserService userService;

    @Test
    void testSaveUser() {
        User user = new User();
        user.setId(5L);
        user.setUsername("Lucy");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        user.setInfo(UserInfo.of(24,"英文老师","female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
       userService.save(user);
    }

    @Test
    void tsetPageQuery() {
        int pageNo = 1, pageSize = 2;
        Page<User> page = Page.of(pageNo, pageSize);
        page.addOrder(new OrderItem("balance",true));
        page.addOrder(new OrderItem("id",false));
        Page<User> result = userService.page(page);
        long total = result.getTotal();
        System.out.println("total = " + total);
        long pages = result.getPages();
        System.out.println("pages = " + pages);
        List<User> users = result.getRecords();
        users.forEach(System.out::println);
    }
}
