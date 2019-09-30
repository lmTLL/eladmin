package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * @author groot
 * @date 2019-07-26
 */
@Data
public class KeyMsgDTO implements Serializable {

    private Long id;

    private String key;

    private String msg;
}