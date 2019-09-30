package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author groot
 * @date 2019-09-19
 */
@Data
public class ZwPostingEffectQueryCriteria {

    // 精确
    @Query
    private Long id;

    // 精确
    @Query
    private Long zwSaleId;

}