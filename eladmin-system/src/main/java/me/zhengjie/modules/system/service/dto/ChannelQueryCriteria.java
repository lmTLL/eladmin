package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author groot
* @date 2019-07-09
*/
@Data
public class ChannelQueryCriteria{

    // 精确
    @Query
    private Long id;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String channelName;

    // 精确
    @Query
    private String followType;

    // 精确
    @Query
    private String assuranceTime;

    // 精确
    @Query
    private String openId;

    // 精确
    @Query
    private String enabled;
}