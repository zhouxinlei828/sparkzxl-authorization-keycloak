package com.github.sparkzxl.authorization.domain.service;

import com.github.sparkzxl.authorization.domain.model.aggregates.MenuBasicInfo;
import com.github.sparkzxl.authorization.application.service.IAuthMenuService;
import com.github.sparkzxl.authorization.infrastructure.constant.CacheConstant;
import com.github.sparkzxl.authorization.infrastructure.entity.AuthMenu;
import com.github.sparkzxl.authorization.infrastructure.mapper.AuthMenuMapper;
import com.github.sparkzxl.authorization.infrastructure.repository.AuthMenuRepository;
import com.github.sparkzxl.core.context.BaseContextHandler;
import com.github.sparkzxl.core.tree.TreeUtils;
import com.github.sparkzxl.database.base.service.impl.AbstractSuperCacheServiceImpl;
import com.github.sparkzxl.database.utils.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description: 菜单 服务实现类
 *
 * @author zhouxinlei
 * @date 2020-06-07 13:35:18
 */
@Service
public class AuthMenuServiceImpl extends AbstractSuperCacheServiceImpl<AuthMenuMapper, AuthMenu> implements IAuthMenuService {

    @Autowired
    private AuthMenuRepository authMenuRepository;
    @Override
    public List<AuthMenu> findMenuTree() {
        List<AuthMenu> authMenuList = list();
        return TreeUtil.buildTree(authMenuList);
    }

    @Override
    public boolean deleteMenu(List<Long> ids) {
        removeByIds(ids);
        return true;
    }

    @Override
    public List<MenuBasicInfo> routers() {
        Long userId = BaseContextHandler.getUserId(Long.TYPE);
        List<MenuBasicInfo> authMenuList = authMenuRepository.getAuthMenuList(userId);
        return TreeUtils.buildTree(authMenuList);
    }

    @Override
    protected String getRegion() {
        return CacheConstant.MENU;
    }
}
