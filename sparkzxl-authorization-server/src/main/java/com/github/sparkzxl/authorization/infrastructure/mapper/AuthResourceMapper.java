package com.github.sparkzxl.authorization.infrastructure.mapper;

import com.github.sparkzxl.authorization.infrastructure.entity.AuthResource;
import com.github.sparkzxl.database.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * description: 资源 Mapper 接口
 *
 * @author zhouxinlei
 * @date 2020-06-07 13:29:12
 */
@Repository
public interface AuthResourceMapper extends SuperMapper<AuthResource> {

    /**
     * 查询用户可用的所有资源
     *
     * @param userId
     * @param menuId
     * @return
     */
    @Select("SELECT id, create_user, create_time, update_user, update_time, code, name, menu_id, describe"
            + " from u_auth_resource where "
            + " id in (SELECT authority_id FROM u_auth_role_authority ra INNER JOIN u_auth_user_role ur on ra.role_id = ur.role_id "
            + " INNER JOIN u_auth_role r on r.id = ra.role_id "
            + " where ur.user_id = #{userId, jdbcType=BIGINT} and r.`status` = true "
            + " and ra.authority_type = 'RESOURCE')")
    List<AuthResource> findVisibleResource(Long userId, Long menuId);
}
