package com.github.sparkzxl.authentication.interfaces.controller.auth;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.sparkzxl.authentication.interfaces.dto.role.RoleAuthorityPageDTO;
import com.github.sparkzxl.authentication.interfaces.dto.role.RoleAuthoritySaveDTO;
import com.github.sparkzxl.authentication.interfaces.dto.role.RoleAuthorityUpdateDTO;
import com.github.sparkzxl.authentication.application.service.IRoleAuthorityService;
import com.github.sparkzxl.authentication.infrastructure.entity.RoleAuthority;
import com.github.sparkzxl.database.base.controller.SuperCacheController;
import com.github.sparkzxl.log.annotation.WebLog;
import com.github.sparkzxl.web.annotation.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * description: 角色资源管理
 *
 * @author: zhouxinlei
 * @date: 2020-07-19 21:05:10
 */
@RestController
@RequestMapping("/role/authority")
@ResponseResult
@WebLog
@Api(tags = "角色资源管理")
public class RoleAuthorityController extends SuperCacheController<IRoleAuthorityService, Long,
        RoleAuthority, RoleAuthorityPageDTO, RoleAuthoritySaveDTO, RoleAuthorityUpdateDTO> {

    @ApiOperation("批量保存角色资源")
    @PostMapping("/batch")
    public boolean saveRoleAuthorityBatch(@Validated @RequestBody RoleAuthoritySaveDTO roleAuthoritySaveDTO) {
        return baseService.saveRoleAuthorityBatch(roleAuthoritySaveDTO);
    }

    @ApiOperation("批量删除角色资源")
    @DeleteMapping("/batch")
    public boolean deleteRoleAuthorityBatch(@RequestParam(value = "roleIds") List<Long> roleIds) {
        return baseService.remove(new LambdaUpdateWrapper<RoleAuthority>().in(RoleAuthority::getRoleId, roleIds));
    }
}
