package com.github.sparkzxl.authentication.application.service;


import com.github.sparkzxl.authentication.infrastructure.entity.AuthResource;
import com.github.sparkzxl.authentication.interfaces.dto.resource.ResourceQueryDTO;
import com.github.sparkzxl.database.base.service.SuperCacheService;

import java.util.List;

/**
 * description: 资源 服务类
 *
 * @author zhouxinlei
 * @date 2020-06-07 13:31:28
 */
public interface IAuthResourceService extends SuperCacheService<AuthResource> {

    /**
     * 查询用户可用的所有资源
     *
     * @param userId   用户id
     * @param resource 资源查询对象
     * @return List<AuthResource>
     */
    List<AuthResource> findVisibleResource(Long userId, ResourceQueryDTO resource);

}
