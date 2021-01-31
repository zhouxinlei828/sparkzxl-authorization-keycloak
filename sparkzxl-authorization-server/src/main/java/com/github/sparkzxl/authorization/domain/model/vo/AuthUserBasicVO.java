package com.github.sparkzxl.authorization.domain.model.vo;

import com.github.sparkzxl.authorization.domain.model.aggregates.OrgBasicInfo;
import com.github.sparkzxl.authorization.domain.model.aggregates.ResourceBasicInfo;
import com.github.sparkzxl.authorization.domain.model.aggregates.RoleBasicInfo;
import com.github.sparkzxl.authorization.domain.model.aggregates.StationBasicInfo;
import com.github.sparkzxl.authorization.infrastructure.enums.SexEnum;
import com.github.sparkzxl.database.entity.RemoteData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * description: 用户基本信息显示层对象
 *
 * @author: zhouxinlei
 * @date: 2020-12-27 11:08:49
 */
@Data
public class AuthUserBasicVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "组织")
    private List<OrgBasicInfo> org;

    @ApiModelProperty(value = "组织全称")
    private String orgName;

    @ApiModelProperty(value = "岗位")
    private StationBasicInfo station;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "性别")
    private SexEnum sex;

    @ApiModelProperty(value = "状态 1启用 0禁用")
    private Boolean status;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "民族")
    private RemoteData<String, String> nation;

    @ApiModelProperty(value = "学历")
    private RemoteData<String, String> education;

    @ApiModelProperty(value = "职位状态")
    private RemoteData<String, String> positionStatus;

    @ApiModelProperty(value = "工作描述比如：市长、管理员、局长等等   用于登陆展示")
    private String workDescribe;

    @ApiModelProperty(value = "最后登录时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty(value = "角色列表")
    private List<RoleBasicInfo> roleBasicInfos;

    @ApiModelProperty(value = "资源树")
    private List<ResourceBasicInfo> resourceBasicInfos;

}
