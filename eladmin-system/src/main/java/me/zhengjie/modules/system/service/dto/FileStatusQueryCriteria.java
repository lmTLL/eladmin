package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author groot
 * @date 2019-07-24
 */
@Data
public class FileStatusQueryCriteria {

    // 精确
    @Query
    private Long id;

    // 精确
    @Query
    private String nickname;

    // 精确
    @Query
    private String filename;

    // 精确
    @Query
    private String newStatus;
}