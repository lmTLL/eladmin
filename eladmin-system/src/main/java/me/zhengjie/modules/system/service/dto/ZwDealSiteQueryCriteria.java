package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author groot
 * @date 2019-09-06
 */
@Data
public class ZwDealSiteQueryCriteria {

    // 精确
    @Query
    private String site;

    // 精确
    @Query
    private String dealSite;
}