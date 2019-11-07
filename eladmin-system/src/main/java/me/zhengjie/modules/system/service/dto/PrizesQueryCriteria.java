package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author groot
* @date 2019-10-22
*/
@Data
public class PrizesQueryCriteria{

            // 精确
            @Query
        private String openId;

            // 模糊
            @Query(type = Query.Type.INNER_LIKE)
        private String nickname;

            // 精确
            @Query
        private String vxId;
}