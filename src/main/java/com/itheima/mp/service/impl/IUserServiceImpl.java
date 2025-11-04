package com.itheima.mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public void deductMoneyById(Long id, Integer money) {
        // 查询用户PO
        User user = getById(id);
        //校验用户状态
        if (user == null||user.getStatus() ==2) {
            throw new RuntimeException("用户状态异常");
        }

        //校验余额充足

        if (user.getBalance() < money) {
            throw new RuntimeException("余额不足");
        }
        //更新用户余额
        int remainBalance = user.getBalance() - money;
       lambdaUpdate()
               .set(User::getBalance, user.getBalance() - money)
               .set(remainBalance==0, User::getStatus,2)
               .eq(User::getId, id)
               .eq(User::getBalance, user.getBalance())
               .update();
    }

    @Override
    public List<User> queryUsers(String name, Integer status, Integer minBalance, Integer maxBalance) {
       lambdaQuery()
               .like(name != null, User::getUsername, name)
               .eq(status != null, User::getStatus, status)
               .gt( minBalance != null, User::getBalance, minBalance)
               .lt( maxBalance != null, User::getBalance, maxBalance)
               .list();
       return list();
    }
}
