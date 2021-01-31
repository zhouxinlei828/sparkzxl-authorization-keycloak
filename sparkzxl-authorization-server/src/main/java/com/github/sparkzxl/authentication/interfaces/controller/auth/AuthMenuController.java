package com.github.sparkzxl.authentication.interfaces.controller.auth;


import cn.hutool.core.convert.Convert;
import com.github.sparkzxl.authentication.interfaces.dto.menu.AuthMenuPageDTO;
import com.github.sparkzxl.authentication.interfaces.dto.menu.AuthMenuSaveDTO;
import com.github.sparkzxl.authentication.interfaces.dto.menu.AuthMenuUpdateDTO;
import com.github.sparkzxl.database.dto.DeleteDTO;
import com.github.sparkzxl.authentication.application.service.IAuthMenuService;
import com.github.sparkzxl.authentication.infrastructure.entity.AuthMenu;
import com.github.sparkzxl.database.base.controller.SuperCacheController;
import com.github.sparkzxl.database.mybatis.conditions.Wraps;
import com.github.sparkzxl.database.utils.TreeUtil;
import com.github.sparkzxl.log.annotation.WebLog;
import com.github.sparkzxl.web.annotation.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * description: 资源管理
 *
 * @author zhouxinlei
 * @date 2020-06-07 13:39:30
 */
@RestController
@RequestMapping("/menu")
@ResponseResult
@WebLog
@Api(tags = "菜单管理")
public class AuthMenuController extends SuperCacheController<IAuthMenuService, Long,
        AuthMenu, AuthMenuPageDTO, AuthMenuSaveDTO, AuthMenuUpdateDTO> {


    @Override
    public boolean handlerSave(AuthMenuSaveDTO model) {
        model.setIsEnable(Convert.toBool(model.getIsEnable(), true));
        model.setIsPublic(Convert.toBool(model.getIsPublic(), false));
        model.setParentId(Convert.toLong(model.getParentId(), 0L));
        return true;
    }

    @ApiOperation(value = "查询系统所有的菜单", notes = "查询系统所有的菜单")
    @GetMapping("/tree")
    public List<AuthMenu> allTree() {
        List<AuthMenu> list = baseService.list(Wraps.<AuthMenu>lbQ().orderByAsc(AuthMenu::getSortValue));
        return TreeUtil.buildTree(list);
    }

    @ApiOperation(value = "删除菜单", notes = "删除菜单")
    @DeleteMapping("/deleteMenu")
    public boolean deleteMenu(@RequestBody DeleteDTO deleteDTO) {
        return baseService.deleteMenu(deleteDTO.getIds());
    }
}
