package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author groot
 * @date 2019-09-11
 */
@Data
public class MessageQueryCriteria {

    // 精确
    @Query
    private String msgKey;
}