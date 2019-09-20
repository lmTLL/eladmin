package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;

/**
 * @author groot
 * @date 2019-07-09
 */
@Data
public class SaleOrderQueryCriteria{

    // 精确
    @Query
    private String site;

    @Query
    private Long customerId;
    // 精确
    @Query
    private String asin;
    @Query
    private String customerNickname;
    @Query
    private String channelId;

    @Query
    private String channelName;
    @Query
    private String saleNumber;

    // 精确
    @Query(type = Query.Type.INNER_LIKE)
    private String followShopUrl;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String followShopName;

    // 精确
    @Query
    private String assuranceTime;
    // 精确
    @Query
    private String invitation;
    //精确
    @Query(type = Query.Type.GREATER_THAN)
    private String status;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private Timestamp startTime;

    @Query
    private Long channelUserId;

    @Query
    private String financePayment;
}