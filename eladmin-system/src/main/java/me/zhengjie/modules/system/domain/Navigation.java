package me.zhengjie.modules.system.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author groot
 * @date 2019-08-14
 */
@Entity
@Data
@Table(name = "navigation")
public class Navigation implements Serializable {

    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 文章标题
    @Column(name = "title")
    private String title;

    // 文章链接
    @Column(name = "url")
    private String url;
    //图片链接
    @Column(name = "img_url")
    private String imgUrl;
    //文章正文
    @Column(name = "body")
    private String body;

    // 文章所属位置
    @Column(name = "head_directld")
    private Integer headDirectld;
}