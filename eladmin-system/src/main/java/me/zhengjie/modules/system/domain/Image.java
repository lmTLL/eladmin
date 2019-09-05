package me.zhengjie.modules.system.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Entity;


@Data
public class Image {
    @JSONField(name="media_id")
    private String mediaid;
}
