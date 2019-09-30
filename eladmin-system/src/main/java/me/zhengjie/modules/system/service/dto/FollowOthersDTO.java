package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * @author handsome
 * @date 2019-08-28
 */
@Data
public class FollowOthersDTO implements Serializable {

    private Integer id;

    private Timestamp startDate;

    private String asin;

    private String title;

    private String price;

    private String fivePoints;


}