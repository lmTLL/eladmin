package me.zhengjie.modules.system.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author groot
 * @date 2019-09-06
 */
@Entity
@Data
@Table(name = "zw_deal_site")
public class ZwDealSite implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 站点
    @Column(name = "site")
    private String site;

    // Deal 站
    @Column(name = "deal_site")
    private String dealSite;

    // 价格
    @Column(name = "price")
    private String price;
}