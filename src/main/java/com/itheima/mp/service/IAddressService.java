package com.itheima.mp.service;

import com.itheima.mp.domain.po.Address;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 虎哥
 * @since 2025-11-05
 */
public interface IAddressService extends IService<Address> {

    List<Address> getAddressByUserId(Long userId);
}
