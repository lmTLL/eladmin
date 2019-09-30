package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * @author groot
 * @date 2019-09-27
 */
@Data
public class ZwSaleOrderUpdateDTO implements Serializable {

    private Long id;

    private String zwSaleNumber;

    private String befores;

    private String nows;
    private String updateUser;
}