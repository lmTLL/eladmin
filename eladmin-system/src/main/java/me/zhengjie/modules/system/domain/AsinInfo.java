package me.zhengjie.modules.system.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author groot
 * @date 2019-07-22
 */
@Entity
@Data
@Table(name = "asin_info")
public class AsinInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "asin")
    private String asin;

    @Column(name = "title")
    private String title;

    @Column(name = "site")
    private String site;

    @Column(name = "exclude_shop")
    private String excludeShop;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @Column(name = "open_Id")
    private String openId;

    @Column(name = "count")
    private Integer count;

    @Column(name = "start_count")
    private Integer startCount;

    @Column(name = "follow_listen")
    private String followListen;

    @Column(name = "title_listen")
    private String titleListen;

    @Column(name = "price_listen")
    private String priceListen;

    @Column(name = "fivepoint_listen")
    private String fivepointListen;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "head_imgurl")
    private String headImgurl;

}