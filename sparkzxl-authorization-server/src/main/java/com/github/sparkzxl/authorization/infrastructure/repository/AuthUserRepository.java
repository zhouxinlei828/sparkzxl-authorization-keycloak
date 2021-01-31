package com.github.sparkzxl.authorization.infrastructure.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.sparkzxl.authorization.infrastructure.convert.AuthRoleConvert;
import com.github.sparkzxl.authorization.infrastructure.convert.AuthUserConvert;
import com.github.sparkzxl.authorization.infrastructure.entity.*;
import com.github.sparkzxl.authorization.infrastructure.mapper.*;
import com.github.sparkzxl.authorization.domain.model.aggregates.AuthUserBasicInfo;
import com.github.sparkzxl.authorization.domain.model.aggregates.OrgBasicInfo;
import com.github.sparkzxl.authorization.domain.model.aggregates.ResourceBasicInfo;
import com.github.sparkzxl.authorization.domain.model.aggregates.RoleBasicInfo;
import com.github.sparkzxl.authorization.domain.repository.IAuthUserRepository;
import com.github.sparkzxl.core.tree.TreeUtils;
import com.github.sparkzxl.database.annonation.InjectionResult;
import com.github.sparkzxl.database.entity.RemoteData;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * description：用户仓储层实现类
 *
 * @author zhouxinlei
 * @date 2020/6/5 8:45 下午
 */
@AllArgsConstructor
@Repository
@Slf4j
public class AuthUserRepository implements IAuthUserRepository {

    public final AuthUserMapper authUserMapper;
    private final UserRoleMapper userRoleMapper;
    private final AuthRoleMapper authRoleMapper;
    private final RoleAuthorityMapper roleAuthorityMapper;
    private final AuthResourceMapper authResourceMapper;
    private final AuthMenuMapper authMenuMapper;
    private final CoreOrgMapper coreOrgMapper;

    @Override
    public AuthUser selectById(Long id) {
        return authUserMapper.selectById(id);
    }

    @Override
    @InjectionResult
    public AuthUser selectByAccount(String account) {
        String pattern = "^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
        boolean isMatch = ReUtil.isMatch(pattern, account);
        QueryWrapper<AuthUser> queryWrapper = new QueryWrapper<>();
        if (isMatch) {
            queryWrapper.lambda().eq(AuthUser::getMobile, account);
        } else {
            queryWrapper.lambda().eq(AuthUser::getAccount, account);
        }
        queryWrapper.lambda().eq(AuthUser::getStatus, true);
        return authUserMapper.selectOne(queryWrapper);
    }

    @Override
    public List<String> getAuthUserPermissions(Long id) {
        return authUserMapper.getAuthUserPermissions(id);
    }

    @Override
    public List<String> getAuthUserRoles(Long id) {
        return authUserMapper.getAuthUserRoles(id);
    }

    @Override
    public List<RoleResource> getRoleResourceList() {
        return authUserMapper.getRoleResourceList();
    }

    @Override
    public void deleteUserRelation(List<Long> ids) {
        userRoleMapper.delete(new LambdaUpdateWrapper<UserRole>().in(UserRole::getUserId, ids));
    }

    @Override
    @InjectionResult
    public List<AuthUser> getAuthUserList(AuthUser authUser) {
        LambdaQueryWrapper<AuthUser> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(authUser.getAccount())) {
            queryWrapper.like(AuthUser::getAccount, authUser.getAccount());
        }
        if (StringUtils.isNotEmpty(authUser.getName())) {
            queryWrapper.like(AuthUser::getName, authUser.getName());
        }
        if (ObjectUtils.isNotEmpty(authUser.getStatus())) {
            queryWrapper.eq(AuthUser::getStatus, authUser.getStatus());
        }
        if (ObjectUtils.isNotEmpty(authUser.getSex()) && ObjectUtils.isNotEmpty(authUser.getSex().getCode())) {
            queryWrapper.eq(AuthUser::getSex, authUser.getSex());
        }
        if (ObjectUtils.isNotEmpty(authUser.getNation()) && StringUtils.isNotEmpty(authUser.getNation().getKey())) {
            queryWrapper.eq(AuthUser::getNation, authUser.getNation());
        }
        if (ObjectUtils.isNotEmpty(authUser.getOrg()) && ObjectUtils.isNotEmpty(authUser.getOrg().getKey())) {
            queryWrapper.eq(AuthUser::getOrg, authUser.getOrg().getKey());
        }
        return authUserMapper.selectList(queryWrapper);
    }

    @Override
    public AuthUserBasicInfo getAuthUserBasicInfo(Long userId) {
        AuthUser authUser = authUserMapper.getById(userId);
        AuthUserBasicInfo authUserBasicInfo = AuthUserConvert.INSTANCE.convertAuthUserBasicInfo(authUser);
        RemoteData<Long, CoreOrg> org = authUser.getOrg();
        List<OrgBasicInfo> orgTreeList = CollUtil.newArrayList();
        if (ObjectUtils.isNotEmpty(org)) {
            CoreOrg data = org.getData();
            if (ObjectUtils.isNotEmpty(data)) {
                OrgBasicInfo orgBasicInfo = new OrgBasicInfo();
                orgBasicInfo.setId(data.getId());
                orgBasicInfo.setLabel(data.getLabel());
                orgBasicInfo.setParentId(data.getParentId());
                orgBasicInfo.setSortValue(data.getSortValue());
                orgTreeList.add(orgBasicInfo);
                if (data.getParentId() != 0) {
                    CoreOrg coreOrg = coreOrgMapper.selectById(data.getParentId());
                    OrgBasicInfo parentOrgBasicInfo = new OrgBasicInfo();
                    parentOrgBasicInfo.setId(coreOrg.getId());
                    parentOrgBasicInfo.setLabel(coreOrg.getLabel());
                    parentOrgBasicInfo.setParentId(coreOrg.getParentId());
                    parentOrgBasicInfo.setSortValue(coreOrg.getSortValue());
                    orgTreeList.add(parentOrgBasicInfo);
                    authUserBasicInfo.setOrgName(coreOrg.getLabel().concat("-").concat(data.getLabel()));
                } else {
                    authUserBasicInfo.setOrgName(data.getLabel());
                }
                authUserBasicInfo.setOrg(TreeUtils.buildTree(orgTreeList));
            }
        }

        List<Long> roleIds =
                userRoleMapper.selectList(new LambdaUpdateWrapper<UserRole>().eq(UserRole::getUserId, userId)).stream().map(UserRole::getRoleId)
                        .collect(Collectors.toList());
        List<AuthRole> roleList = authRoleMapper.selectBatchIds(roleIds);
        List<RoleBasicInfo> roleBasicInfos = AuthRoleConvert.INSTANCE.convertRoleBasicInfo(roleList);
        authUserBasicInfo.setRoleBasicInfos(roleBasicInfos);
        List<RoleAuthority> roleAuthorities =
                roleAuthorityMapper.selectList(new LambdaQueryWrapper<RoleAuthority>().in(RoleAuthority::getRoleId, roleIds)
                        .eq(RoleAuthority::getAuthorityType, "RESOURCE")
                        .groupBy(RoleAuthority::getAuthorityId, RoleAuthority::getRoleId));
        List<Long> authorityIds = roleAuthorities.stream().map(RoleAuthority::getAuthorityId).collect(Collectors.toList());
        Map<Long, Long> roleAuthorityIdMap =
                roleAuthorities.stream().collect(Collectors.toMap(RoleAuthority::getAuthorityId, RoleAuthority::getRoleId));
        // 获取用户资源列表
        List<AuthResource> resourceList = authResourceMapper.selectBatchIds(authorityIds);
        List<ResourceBasicInfo> resourceBasicInfos = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(resourceList)) {
            resourceList.forEach(resource -> {
                ResourceBasicInfo resourceBasicInfo = new ResourceBasicInfo();
                resourceBasicInfo.setCode(resource.getCode());
                resourceBasicInfo.setName(resource.getName());
                resourceBasicInfo.setRoleId(roleAuthorityIdMap.get(resource.getId()));
                resourceBasicInfos.add(resourceBasicInfo);
            });
        }
        authUserBasicInfo.setResourceBasicInfos(resourceBasicInfos);
        return authUserBasicInfo;
    }
}
