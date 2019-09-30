package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author handsome
 * @date 2019-08-28
 */
@Data
public class FollowOthersQueryCriteria {

    // 精确
    @Query
    private String asin;
}