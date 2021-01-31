package com.github.sparkzxl.authentication.domain.service;

import cn.hutool.core.net.URLEncoder;
import cn.hutool.extra.pinyin.PinyinUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.javafaker.Faker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.sparkzxl.authentication.domain.model.aggregates.AuthUserBasicInfo;
import com.github.sparkzxl.authentication.domain.model.aggregates.MenuBasicInfo;
import com.github.sparkzxl.authentication.domain.model.aggregates.UserExcel;
import com.github.sparkzxl.authentication.domain.model.vo.AuthUserBasicVO;
import com.github.sparkzxl.authentication.domain.repository.IAuthUserRepository;
import com.github.sparkzxl.authentication.application.event.ImportUserDataListener;
import com.github.sparkzxl.authentication.application.service.*;
import com.github.sparkzxl.authentication.infrastructure.constant.CacheConstant;
import com.github.sparkzxl.authentication.infrastructure.convert.AuthUserConvert;
import com.github.sparkzxl.authentication.infrastructure.entity.AuthUser;
import com.github.sparkzxl.authentication.infrastructure.entity.CoreOrg;
import com.github.sparkzxl.authentication.infrastructure.entity.CoreStation;
import com.github.sparkzxl.authentication.infrastructure.enums.SexEnum;
import com.github.sparkzxl.authentication.infrastructure.mapper.AuthUserMapper;
import com.github.sparkzxl.authentication.interfaces.dto.user.AuthUserDTO;
import com.github.sparkzxl.authentication.interfaces.dto.user.AuthUserPageDTO;
import com.github.sparkzxl.authentication.interfaces.dto.user.AuthUserSaveDTO;
import com.github.sparkzxl.authentication.interfaces.dto.user.AuthUserUpdateDTO;
import com.github.sparkzxl.core.context.BaseContextHandler;
import com.github.sparkzxl.core.entity.AuthUserInfo;
import com.github.sparkzxl.database.base.service.impl.AbstractSuperCacheServiceImpl;
import com.github.sparkzxl.database.entity.RemoteData;
import com.github.sparkzxl.database.utils.PageInfoUtils;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * description: 用户查询 服务实现类
 *
 * @author zhouxinlei
 * @date 2020-05-24 12:22:57
 */
@Service
@Slf4j
public class AuthUserServiceImpl extends AbstractSuperCacheServiceImpl<AuthUserMapper, AuthUser> implements IAuthUserService {

    @Autowired
    private IAuthUserRepository authUserRepository;
    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IAuthMenuService authMenuService;
    @Autowired
    private ICoreStationService coreStationService;
    @Autowired
    private ICoreOrgService coreOrgService;
    @Autowired
    private ICommonDictionaryItemService dictionaryItemService;

    @Override
    public AuthUserDTO getAuthUser(Long id) {
        return AuthUserConvert.INSTANCE.convertAuthUserDTO(getByIdCache(id));
    }


    @Override
    protected String getRegion() {
        return CacheConstant.USER;
    }


    @Override
    public void deleteOrgId(Long id) {
        UpdateWrapper<AuthUser> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.set("org_id", null);
        userUpdateWrapper.eq("org_id", id);
        update(userUpdateWrapper);
    }

    @Override
    public void deleteUserRelation(List<Long> ids) {
        authUserRepository.deleteUserRelation(ids);
    }

    @Override
    public AuthUserInfo<Long> getAuthUserInfo(String username) {
        AuthUser authUser = authUserRepository.selectByAccount(username);
        if (ObjectUtils.isNotEmpty(authUser)) {
            AuthUserInfo<Long> authUserInfo = AuthUserConvert.INSTANCE.convertAuthUserInfo(authUser);
            List<String> userRoles = authUserRepository.getAuthUserRoles(authUser.getId());
            authUserInfo.setAuthorityList(userRoles);
            Map<String, Object> extraInfo = Maps.newHashMap();
            extraInfo.put("org", authUser.getOrg().getData());
            extraInfo.put("station", authUser.getStation());
            extraInfo.put("mobile", authUser.getMobile());
            extraInfo.put("email", authUser.getEmail());
            extraInfo.put("education", authUser.getEducation());
            extraInfo.put("positionStatus", authUser.getPositionStatus());
            authUserInfo.setExtraInfo(extraInfo);
            return authUserInfo;
        }
        return null;
    }

    @Override
    public AuthUser getByAccount(String username) {
        return authUserRepository.selectByAccount(username);
    }

    @Override
    public PageInfo<AuthUser> getAuthUserPage(AuthUserPageDTO authUserPageDTO) {
        AuthUser authUser = AuthUserConvert.INSTANCE.convertAuthUser(authUserPageDTO);
        PageHelper.startPage(authUserPageDTO.getPageNum(), authUserPageDTO.getPageSize());
        List<AuthUser> authUserList = authUserRepository.getAuthUserList(authUser);
        return PageInfoUtils.pageInfo(authUserList);
    }

    @Override
    public boolean saveAuthUser(AuthUserSaveDTO authUserSaveDTO) {
        AuthUser authUser = AuthUserConvert.INSTANCE.convertAuthUser(authUserSaveDTO);
        String password = passwordEncoder.encode(authUser.getPassword());
        authUser.setPassword(password);
        return save(authUser);
    }

    @Override
    public boolean updateAuthUser(AuthUserUpdateDTO authUserUpdateDTO) {
        AuthUser authUser = AuthUserConvert.INSTANCE.convertAuthUser(authUserUpdateDTO);
        return updateById(authUser);
    }

    @Override
    public void deleteOrgIds(List<Long> orgIds) {
        UpdateWrapper<AuthUser> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.set("org_id", null);
        userUpdateWrapper.in("org_id", orgIds);
        update(userUpdateWrapper);
    }

    @Override
    public boolean mockUserData() {
        Faker fakerWithCN = new Faker(Locale.CHINA);
        for (int i = 0; i < 10; i++) {
            String password = passwordEncoder.encode("123456");
            AuthUser userInfo = new AuthUser();
            userInfo.setPassword(password);
            String name = fakerWithCN.name().fullName();
            userInfo.setName(name);
            userInfo.setMobile(fakerWithCN.phoneNumber().cellPhone());
            RemoteData<Long, CoreOrg> orgRemoteData = new RemoteData<>();
            orgRemoteData.setKey(643776594376135105L);
            userInfo.setOrg(orgRemoteData);
            RemoteData<Long, CoreStation> stationRemoteData = new RemoteData<>();
            stationRemoteData.setKey(643776594376135105L);
            userInfo.setStation(stationRemoteData);
            String pinyin = StringUtils.deleteWhitespace(PinyinUtil.getPinyin(name));
            userInfo.setAccount(pinyin);
            String email = pinyin.concat("@163.com");
            userInfo.setEmail(email);
            if (i % 2 == 0) {
                userInfo.setSex(SexEnum.MAN);
            } else {
                userInfo.setSex(SexEnum.WOMAN);
            }
            userInfo.setStatus(true);
            RemoteData<String, String> nationRemoteData = new RemoteData<>();
            nationRemoteData.setKey("mz_hanz");
            userInfo.setNation(nationRemoteData);

            RemoteData<String, String> educationRemoteData = new RemoteData<>();
            educationRemoteData.setKey("BOSHIHOU");
            userInfo.setEducation(educationRemoteData);

            RemoteData<String, String> positionRemoteData = new RemoteData<>();
            positionRemoteData.setKey("WORKING");
            userInfo.setPositionStatus(positionRemoteData);
            log.info(" 仿真数据：{}", JSONUtil.toJsonPrettyStr(userInfo));
            save(userInfo);
        }
        return true;
    }

    @Override
    public AuthUserBasicVO getAuthUserBasicInfo() {
        Long userId = BaseContextHandler.getUserId(Long.TYPE);
        AuthUserBasicInfo authUserBasicInfo = authUserRepository.getAuthUserBasicInfo(userId);
        return AuthUserConvert.INSTANCE.convertAuthUserBasicVO(authUserBasicInfo);
    }

    @Override
    public List<MenuBasicInfo> routers() {
        return authMenuService.routers();
    }

    @Override
    public Integer importUserData(MultipartFile multipartFile) {
        ImportUserDataListener importUserDataListener = new ImportUserDataListener();
        importUserDataListener.setAuthUserService(this);
        importUserDataListener.setCoreOrgService(coreOrgService);
        importUserDataListener.setCoreStationService(coreStationService);
        importUserDataListener.setDictionaryItemService(dictionaryItemService);
        importUserDataListener.setPasswordEncoder(passwordEncoder);
        try {
            EasyExcel.read(multipartFile.getInputStream(), UserExcel.class, importUserDataListener)
                    .sheet(0).doRead();
            return importUserDataListener.getCount();
        } catch (IOException e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("读取Excel发生异常：{}", e.getMessage());
        }
        return 0;
    }

    @Override
    public void exportUserData(AuthUserPageDTO authUserPageDTO, HttpServletResponse response) throws IOException {
        AuthUser authUser = AuthUserConvert.INSTANCE.convertAuthUser(authUserPageDTO);
        List<AuthUser> authUserList = authUserRepository.getAuthUserList(authUser);
        List<UserExcel> userExcels = AuthUserConvert.INSTANCE.convertUserExcels(authUserList);
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easy excel没有关系
        String fileName = URLEncoder.ALL.encode("user_data_export_".concat(String.valueOf(System.currentTimeMillis())),
                StandardCharsets.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), UserExcel.class).sheet("模板").doWrite(userExcels);
    }
}
