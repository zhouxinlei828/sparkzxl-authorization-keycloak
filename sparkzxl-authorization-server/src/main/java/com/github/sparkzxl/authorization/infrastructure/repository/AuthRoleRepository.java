package com.github.sparkzxl.authorization.infrastructure.repository;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.sparkzxl.authorization.infrastructure.entity.RoleAuthority;
import com.github.sparkzxl.authorization.infrastructure.entity.RoleOrg;
import com.github.sparkzxl.authorization.infrastructure.entity.UserRole;
import com.github.sparkzxl.authorization.infrastructure.mapper.RoleAuthorityMapper;
import com.github.sparkzxl.authorization.infrastructure.mapper.RoleOrgMapper;
import com.github.sparkzxl.authorization.infrastructure.mapper.UserRoleMapper;
import com.github.sparkzxl.authorization.domain.repository.IAuthRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * description: 角色 仓储层实现类
 *
 * @author zhouxinlei
 * @date 2020-06-07 13:31:48
 */
@AllArgsConstructor
@Repository
public class AuthRoleRepository implements IAuthRoleRepository {

    private final UserRoleMapper userRoleMapper;
    private final RoleOrgMapper roleOrgMapper;
    private final RoleAuthorityMapper roleAuthorityMapper;

    @Override
    public void deleteAuthRoleRelation(List<Long> ids) {
        userRoleMapper.delete(new LambdaUpdateWrapper<UserRole>().in(UserRole::getRoleId, ids));
        roleOrgMapper.delete(new LambdaUpdateWrapper<RoleOrg>().in(RoleOrg::getRoleId, ids));
        roleAuthorityMapper.delete(new LambdaUpdateWrapper<RoleAuthority>().in(RoleAuthority::getRoleId, ids));
    }
}
