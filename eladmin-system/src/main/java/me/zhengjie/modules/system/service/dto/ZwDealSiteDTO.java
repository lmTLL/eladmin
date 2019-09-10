package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;


/**
* @author groot
* @date 2019-09-06
*/
@Data
public class ZwDealSiteDTO implements Serializable {

    private Long id;

    // 站点
    private String site;

    // Deal 站
    private String dealSite;

    // 价格
    private String price;
}