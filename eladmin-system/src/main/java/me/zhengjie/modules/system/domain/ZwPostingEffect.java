package me.zhengjie.modules.system.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author groot
 * @date 2019-09-19
 */
@Entity
@Data
@Table(name = "zw_posting_effect")
public class ZwPostingEffect implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "zw_sale_id", nullable = false)
    private Long zwSaleId;

    @Column(name = "posting_effect")
    private String postingEffect;


    // 提交时间
    @Column(name = "submit_time")
    private Timestamp submitTime;
}