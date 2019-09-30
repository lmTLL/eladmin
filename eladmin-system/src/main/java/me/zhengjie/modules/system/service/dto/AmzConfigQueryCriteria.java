package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author groot
 * @date 2019-08-27
 */
@Data
public class AmzConfigQueryCriteria {

    // 精确
    @Query
    private Long id;

    // 精确
    @Query
    private String amzAccount;

    // 精确
    @Query
    private Long num;

    // 精确
    @Query
    private String ip;
}