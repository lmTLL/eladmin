package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author groot
* @date 2019-07-26
*/
@Data
public class KeyMsgQueryCriteria{

    // 精确
    @Query
    private String key;
}