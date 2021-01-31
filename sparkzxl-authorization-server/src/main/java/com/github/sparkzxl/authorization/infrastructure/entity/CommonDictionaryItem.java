package com.github.sparkzxl.authorization.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.sparkzxl.database.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE_RIGHT;

/**
 * description: 字典项
 *
 * @author: zhouxinlei
 * @date: 2020-07-28 19:38:46
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("u_common_dictionary_item")
@ApiModel(value = "CCommonDictionaryItem对象", description = "字典项")
public class CommonDictionaryItem extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类型ID")
    @TableField("dictionary_id")
    private Long dictionaryId;

    @ApiModelProperty(value = "类型")
    @TableField(value = "dictionary_type",condition = LIKE_RIGHT)
    private String dictionaryType;

    @ApiModelProperty(value = "编码")
    @TableField(value = "code",condition = LIKE_RIGHT)
    private String code;

    @ApiModelProperty(value = "名称")
    @TableField(value = "name",condition = LIKE_RIGHT)
    private String name;

    @ApiModelProperty(value = "状态")
    @TableField("status_")
    private Boolean status;

    @ApiModelProperty(value = "描述")
    @TableField("describe_")
    private String describe;

    @ApiModelProperty(value = "排序")
    @TableField("sort_value")
    private Integer sortValue;

}
