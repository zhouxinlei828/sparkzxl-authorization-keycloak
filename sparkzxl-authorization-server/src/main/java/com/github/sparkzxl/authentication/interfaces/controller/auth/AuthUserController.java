package com.github.sparkzxl.authentication.interfaces.controller.auth;

import com.github.pagehelper.PageInfo;
import com.github.sparkzxl.authentication.interfaces.dto.user.AuthUserDTO;
import com.github.sparkzxl.authentication.interfaces.dto.user.AuthUserPageDTO;
import com.github.sparkzxl.authentication.interfaces.dto.user.AuthUserSaveDTO;
import com.github.sparkzxl.authentication.interfaces.dto.user.AuthUserUpdateDTO;
import com.github.sparkzxl.authentication.application.service.IAuthUserService;
import com.github.sparkzxl.authentication.domain.model.aggregates.MenuBasicInfo;
import com.github.sparkzxl.authentication.domain.model.vo.AuthUserBasicVO;
import com.github.sparkzxl.authentication.infrastructure.entity.AuthUser;
import com.github.sparkzxl.database.base.controller.SuperCacheController;
import com.github.sparkzxl.log.annotation.WebLog;
import com.github.sparkzxl.web.annotation.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * description: 用户管理
 *
 * @author zhouxinlei
 * @date 2020-05-24 12:25:32
 */

@RestController
@RequestMapping("/user")
@ResponseResult
@WebLog
@Api(tags = "用户管理")
public class AuthUserController extends SuperCacheController<IAuthUserService, Long,
        AuthUser, AuthUserPageDTO, AuthUserSaveDTO, AuthUserUpdateDTO> {

    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    @ApiOperation("获取用户分页")
    @PostMapping("/listPage")
    public PageInfo<AuthUser> getAuthUserPage(@RequestBody AuthUserPageDTO authUserPageDTO) {
        return baseService.getAuthUserPage(authUserPageDTO);
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/authUser/{id}")
    public AuthUserDTO getAuthUser(@PathVariable("id") Long id) {
        return baseService.getAuthUser(id);
    }

    @ApiOperation("保存用户信息")
    @PostMapping("/saveAuthUser")
    public boolean saveAuthUser(@Valid @RequestBody AuthUserSaveDTO authUserSaveDTO) {
        return baseService.saveAuthUser(authUserSaveDTO);
    }

    @ApiOperation("修改用户信息")
    @PostMapping("/updateAuthUser")
    public boolean updateAuthUser(@Valid @RequestBody AuthUserUpdateDTO authUserUpdateDTO) {
        return baseService.updateAuthUser(authUserUpdateDTO);
    }

    @ApiOperation("删除用户信息")
    @DeleteMapping("/deleteAuthUser")
    public boolean deleteAuthUser(@RequestParam("id") Long id) {
        return baseService.removeById(id);
    }

    @Override
    public boolean handlerSave(AuthUserSaveDTO model) {
        model.setPassword(passwordEncoder.encode(model.getPassword()));
        return true;
    }

    @Override
    public boolean handlerDelete(List<Long> ids) {
        baseService.deleteUserRelation(ids);
        return true;
    }

    @ApiOperation(value = "用户路由菜单", notes = "用户路由菜单")
    @GetMapping("/routers")
    public List<MenuBasicInfo> routers() {
        return baseService.routers();
    }

    @ApiOperation("获取登录用户全量信息")
    @GetMapping("/currentUser")
    public AuthUserBasicVO getAuthUserBasicInfo() {
        return baseService.getAuthUserBasicInfo();
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/passwordEncoder")
    public String passwordEncoder(@RequestParam("password") String password) {
        return passwordEncoder.encode(password);
    }

    @ApiOperation("生成仿真数据")
    @GetMapping("/mockData")
    public boolean mockUserData() {
        return baseService.mockUserData();
    }

    @ApiOperation("Excel导入用户数据")
    @PostMapping("/importUserData")
    public Integer importUserData(@RequestParam("file") MultipartFile multipartFile) {
        return baseService.importUserData(multipartFile);
    }

    @ApiOperation("Excel导出用户数据")
    @GetMapping("/exportUserData")
    public void exportUserData(AuthUserPageDTO authUserPageDTO, HttpServletResponse response) throws IOException {
        baseService.exportUserData(authUserPageDTO, response);
    }

}
