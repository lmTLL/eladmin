package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;
import java.util.Set;

/**
 * @author groot
 * @date 2019-07-09
 */
@Data
public class UserQueryCriteria implements Serializable {

    @Query
    private Long id;

    @Query(propName = "id", type = Query.Type.IN, joinName = "dept")
    private Set<Long> deptIds;

    @Query(type = Query.Type.INNER_LIKE)
    private String username;

    @Query(type = Query.Type.INNER_LIKE)
    private String email;

    @Query
    private Boolean enabled;

    private Long deptId;
}
