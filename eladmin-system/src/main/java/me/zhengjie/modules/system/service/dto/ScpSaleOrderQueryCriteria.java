package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;
import org.hibernate.annotations.OrderBy;

import java.sql.Timestamp;

/**
* @author groot
* @date 2019-10-25
*/
@Data
public class ScpSaleOrderQueryCriteria{

            // 精确
            @Query
        private Long id;

            // 精确
            @Query
        private String scpSaleNumber;

            // 精确
            @Query
        private String fileName;

            // 模糊
            @Query(type = Query.Type.INNER_LIKE)
        private String nickName;

            // 精确
            @Query
        private String site;

            // 精确
            @Query
        private String asin;

            // 精确
            @Query
        private String cpUrl;

            // 精确
            @Query
        private Timestamp startDate;

            // 精确
            @Query
        private Timestamp canKillOrderDate;

            // 精确
            @Query
        private String status;

            // 精确
            @Query
        private String invitation;
            @Query
            private String erpSaleNumber;
    // 精确
    @Query
    private Long customerId;
}