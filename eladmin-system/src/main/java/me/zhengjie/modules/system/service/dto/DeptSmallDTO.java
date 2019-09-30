package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author groot
 * @date 2019-07-09
 */
@Data
public class DeptSmallDTO implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;
}