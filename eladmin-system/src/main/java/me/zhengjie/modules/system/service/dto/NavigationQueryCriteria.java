package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author groot
* @date 2019-08-14
*/
@Data
public class NavigationQueryCriteria{

    // 精确
    @Query
    private Integer id;

    @Query
    private Integer headDirectld;
}