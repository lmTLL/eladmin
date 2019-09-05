package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author groot
 * @date 2019-07-09
 */
@Data
public class InvitationcodesQueryCriteria{

    // 精确
    @Query
    private Long id;

    // 精确
    @Query
    private String username;

    // 精确
    @Query
    private String invitationCode;
}