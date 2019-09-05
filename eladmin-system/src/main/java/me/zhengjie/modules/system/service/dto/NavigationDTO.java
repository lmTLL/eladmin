package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;


/**
* @author groot
* @date 2019-08-14
*/
@Data
public class NavigationDTO implements Serializable {

    // ID
    private Integer id;

    // 文章标题
    private String title;

    // 文章链接
    private String url;
    private String imgUrl;
    private String body;

    // 文章所属位置
    private Integer headDirectld;
}