package com.itheima.mp.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.enums.UserStatus;
import com.itheima.mp.service.IAddressService;
import com.itheima.mp.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2025-11-05
 */
@RestController
@RequestMapping("/address")
@Api(tags = "收货地址接口")
@RequiredArgsConstructor
public class AddressController {

    private final IUserService userService;
    private final IAddressService addressService;

    @GetMapping("/user/{userId}")
    @ApiOperation("根据查询收货地址接口")
    public List<AddressVO> list(@ApiParam ("用户id") @PathVariable Long userId) {
        // 实现根据用户id查询收货地址功能，需要验证用户状态，冻结用户抛出异常
        User user = Db.getById(userId, User.class);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (user.getStatus() == UserStatus.FROZEN) {
            throw new RuntimeException("用户已被冻结，无法查询地址");
        }
        List<Address> addresses = addressService.getAddressByUserId(userId);
        return BeanUtil.copyToList(addresses, AddressVO.class);


    }
}
