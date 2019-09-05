package me.zhengjie.modules.system.domain;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Value;

import javax.persistence.Entity;


@Data
public class ImageMessage {
    private String fromuser;
    private String touser;
    private String createtime;
    private Image image;
    private String msgtype;
}
