package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author groot
* @date 2019-09-05
*/
@Data
public class ZwSaleOrderQueryCriteria{

    // 精确
    @Query
    private String zwSaleNumber;

    // 精确
    @Query
    private String zwChannelName;

    // 精确
    @Query
    private String invitation;

    @Query(type = Query.Type.GREATER_THAN)
    private String status;

    // 精确
    @Query
    private String customerNickname;

    // 精确
    @Query
    private String site;

    @Query
    private Long customerId;

    // 精确
    @Query
    private String link;

    // 站外渠道用户id
    @Query
    private Long zwChannelUserId;

    // 精确
    @Query
    private String dealSite;
}