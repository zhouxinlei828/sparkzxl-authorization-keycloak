package com.github.sparkzxl.authentication.domain.service;

import com.github.sparkzxl.authentication.domain.repository.IRoleAuthorityRepository;
import com.github.sparkzxl.authentication.application.service.IRoleAuthorityService;
import com.github.sparkzxl.authentication.infrastructure.constant.CacheConstant;
import com.github.sparkzxl.authentication.infrastructure.entity.RoleAuthority;
import com.github.sparkzxl.authentication.infrastructure.mapper.RoleAuthorityMapper;
import com.github.sparkzxl.authentication.interfaces.dto.role.RoleAuthoritySaveDTO;
import com.github.sparkzxl.database.base.service.impl.AbstractSuperCacheServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * description: 角色的资源 服务实现类
 *
 * @author: zhouxinlei
 * @date: 2020-07-19 20:59:45
 */
@Service
public class RoleAuthorityServiceImpl extends AbstractSuperCacheServiceImpl<RoleAuthorityMapper, RoleAuthority> implements IRoleAuthorityService {

    @Autowired
    private IRoleAuthorityRepository authorityRepository;

    @Override
    protected String getRegion() {
        return CacheConstant.ROLE_RESOURCE;
    }

    @Override
    public boolean saveRoleAuthorityBatch(RoleAuthoritySaveDTO roleAuthoritySaveDTO) {
        return authorityRepository.saveRoleAuthorityBatch(roleAuthoritySaveDTO.getRoleId(),
                roleAuthoritySaveDTO.getResourceIds(),
                roleAuthoritySaveDTO.getMenuIds());
    }
}
