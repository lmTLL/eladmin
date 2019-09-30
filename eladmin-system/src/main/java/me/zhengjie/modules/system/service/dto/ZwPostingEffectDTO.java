package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * @author groot
 * @date 2019-09-19
 */
@Data
public class ZwPostingEffectDTO implements Serializable {

    private Long id;

    private Long zwSaleId;

    private String postingEffect;

    private Timestamp submitTime;
}