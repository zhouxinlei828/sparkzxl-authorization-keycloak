package com.github.sparkzxl.authentication.infrastructure.repository;


import com.github.sparkzxl.authentication.infrastructure.entity.AuthResource;
import com.github.sparkzxl.authentication.infrastructure.mapper.AuthResourceMapper;
import com.github.sparkzxl.authentication.domain.repository.IAuthResourceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * description: 资源 仓储层实现类
 *
 * @author zhouxinlei
 * @date 2020-06-07 13:31:28
 */
@AllArgsConstructor
@Repository
public class AuthResourceRepository implements IAuthResourceRepository {

    private final AuthResourceMapper authResourceMapper;

    @Override
    public List<AuthResource> authResourceList() {
        return authResourceMapper.selectList(null);
    }

    @Override
    public List<AuthResource> findVisibleResource(Long userId, Long menuId) {
        return authResourceMapper.findVisibleResource(userId, menuId);
    }
}
