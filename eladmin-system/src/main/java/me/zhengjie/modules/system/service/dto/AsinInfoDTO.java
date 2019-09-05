package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


/**
* @author groot
* @date 2019-07-22
*/
@Data
public class AsinInfoDTO implements Serializable {

    private Integer id;

    private String asin;

    private String title;

    private String site;

    private String excludeShop;

    private Timestamp startDate;

    private Timestamp updateDate;

    private String openId;

    private Integer count;

    private Integer startCount;

    private String followListen;

    private String titleListen;

    private String priceListen;

    private String fivepointListen;

    private String nickName;

    private String headImgurl;

}