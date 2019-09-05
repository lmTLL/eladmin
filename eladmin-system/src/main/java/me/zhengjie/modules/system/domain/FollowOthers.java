package me.zhengjie.modules.system.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author handsome
 * @date 2019-08-28
 */
@Entity
@Data
@Table(name="follow_others")
public class FollowOthers implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private String price;

    @Column(name = "five_points")
    private String fivePoints;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "asin",nullable = false)
    private String asin;

}
