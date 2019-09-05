package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author groot
* @date 2019-08-01
*/
@Data
public class WaitPaymentQueryCriteria{

    // 精确
    @Query
    private String paymentRemarks;
}