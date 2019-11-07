package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author groot
 * @date 2019-11-01
 */
@Data
public class DzSaleOrderQueryCriteria {

    // 精确
    @Query
    private Long id;

    // 精确
    @Query
    private String dzSaleNumber;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String nickname;

    // 精确
    @Query
    private String site;

    // 精确
    @Query
    private String asin;

    // 精确
    @Query
    private String reviewUrl;

    // 精确
    @Query
    private Long customerId;

    // 精确
    @Query(type = Query.Type.GREATER_THAN)
    private String status;
    // 精确
    @Query
    private String invitation;
}