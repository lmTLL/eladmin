package me.zhengjie.modules.system.domain;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;


@Data
public class LongTimeImage {
    private String title;
    @JSONField(name = "thumb_media_id")
    private String thumbMediaId;
    @JSONField(name = "show_cover_pic")
    private String showCoverPic;
    private String author;
    private String content;
    private String digest;
    @JSONField(name = "content_source_url")
    private String contentSourceUrl;
}
