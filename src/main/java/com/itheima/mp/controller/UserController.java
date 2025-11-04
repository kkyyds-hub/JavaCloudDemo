package com.itheima.mp.controller;

import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

private final IUserService userService;

    @PostMapping("/save")
    @ApiOperation("新增用户接口")
    public void saveUser(@RequestBody UserFormDTO userDTO) {
        User user = new User();  // 先创建User对象
        BeanUtils.copyProperties(userDTO, user);  // 修正copyProperties用法
        userService.save(user);
    }
    @DeleteMapping("{id}")
    @ApiOperation("删除用户接口")
    public void delUser(@ApiParam ("用户id") @PathVariable("id") Long id) {
      userService.removeById(id);
    }
    @GetMapping("{id}")
    @ApiOperation("根据id查询用户接口")
    public UserVO queryUserById(@ApiParam ("用户id") @PathVariable("id") Long id) {
        // 查询用户PO
        User user = userService.getById(id);
        return BeanUtil.copyProperties(user, UserVO.class);
    }

    @GetMapping("{id}")
    @ApiOperation("根据id批量查询用户接口")
    public UserVO queryUserByIds(@ApiParam ("用户id集合") @RequestParam List<Long> ids) {

        // 查询用户PO
        List<User> users = userService.listByIds(ids);
        return BeanUtil.copyProperties(users, UserVO.class);
    }
}
