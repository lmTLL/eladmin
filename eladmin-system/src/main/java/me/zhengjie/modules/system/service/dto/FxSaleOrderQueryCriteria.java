package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;

/**
 * @author groot
 * @date 2019-10-31
 */
@Data
public class FxSaleOrderQueryCriteria {

    // 精确
    @Query
    private String fxSaleNumber;

    // 精确
    @Query
    private String nickName;

    // 精确
    @Query
    private String site;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String oldSku;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String oldFnsku;

    // 精确
    @Query
    private String oldAsin;

    // 精确
    @Query
    private String newAsin;

    // 精确
    @Query
    private Timestamp startDate;

    // 精确
    @Query
    private Timestamp updateDate;

    // 精确
    @Query
    private String status;

    // 精确
    @Query
    private String invitation;

    // 精确
    @Query
    private String erpSaleNumber;

    // 精确
    @Query
    private String isReviews;
    @Query
    private Long customerId;
}