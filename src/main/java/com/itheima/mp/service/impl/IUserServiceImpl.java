package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.enums.UserStatus;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public void deductMoneyById(Long id, Integer money) {
        // 查询用户PO
        User user = getById(id);
        //校验用户状态
        if (user == null||user.getStatus() == UserStatus.FROZEN) {
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
               .set(remainBalance==0, User::getStatus,UserStatus.FROZEN)
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

    @Override
    public UserVO queryUsersAddressById(Long userId) {
        // 1.查询用户
        User user = getById(userId);
        if (user == null||user.getStatus() == UserStatus.FROZEN) {
            throw new RuntimeException("用户状态异常");
        }
        // 2.查询收货地址
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .eq(Address::getUserId, userId)
                .list();
        // 3.处理vo
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        userVO.setAddresses(BeanUtil.copyToList(addresses, AddressVO.class));
        return userVO;
    }

    @Override
    public List<UserVO> queryUsersAddressByIds(List<Long> ids) {
         // 1.查询用户
        List<User> users = listByIds(ids);
        if (CollUtil.isEmpty( users)) {
            return Collections.emptyList();
        }
        // 2.查询用户id地址
        List<Long> userIds =users.stream()
                .map(User::getId)
                .collect(Collectors.toList());
        // 3.查询用户id地址
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .in(Address::getUserId, userIds)
                .list();
        // 4.处理vo
        List<AddressVO> addressVOS = BeanUtil.copyToList(addresses, AddressVO.class);
        //梳理地址集合，分类整理，相同用户的放入一个集合中
        Map<Long, List<AddressVO>> addressMap = new HashMap<>();
        if (CollUtil.isNotEmpty(addressVOS)){
            addressMap = addressVOS.stream().collect(Collectors.groupingBy(AddressVO::getUserId));
        }

        // 5.处理vo返回
       List<UserVO> list = new ArrayList<>(users.size());
       for (User user : users) {

           UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
           list.add(userVO);
           //转换地址vo
           userVO.setAddresses(addressMap.get(user.getId()));

       }
        return  list;

    }
}
