package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author groot
 * @date 2019-07-22
 */
@Data
public class AsinInfoQueryCriteria {

    // 精确
    @Query
    private String asin;

    // 精确
    @Query
    private String openId;

    // 精确
    @Query
    private String followListen;
    // 精确
    @Query
    private String titleListen;
    // 精确
    @Query
    private String priceListen;
    // 精确
    @Query
    private String fivepointListen;
}