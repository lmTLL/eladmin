package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author groot
* @date 2019-09-06
*/
@Data
public class ZwChannelQueryCriteria{

    // 精确
    @Query
    private String zwChannelName;

    // 精确
    @Query
    private String openId;
}