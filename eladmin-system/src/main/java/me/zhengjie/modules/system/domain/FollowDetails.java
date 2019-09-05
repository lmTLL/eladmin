package me.zhengjie.modules.system.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
* @author groot
* @date 2019-07-23
*/
@Entity
@Data
@Table(name="follow_details")
public class FollowDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "shop_name")
    private String shopName;

    @Column(name = "seller")
    private String seller;

    @Column(name = "follow_type")
    private String followType;

    @Column(name = "follow_price")
    private String followPrice;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "asin",nullable = false)
    private String asin;

    @Column(name = "site")
    private String site;

}