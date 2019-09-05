package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


/**
* @author groot
* @date 2019-07-23
*/
@Data
public class FollowDetailsDTO implements Serializable {

    private Integer id;

    private String shopName;

    private String seller;

    private String followType;

    private String followPrice;

    private Timestamp startDate;

    private String asin;

    private String site;
}