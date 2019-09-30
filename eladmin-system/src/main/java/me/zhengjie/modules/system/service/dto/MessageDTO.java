package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * @author groot
 * @date 2019-09-11
 */
@Data
public class MessageDTO implements Serializable {

    // id
    private Long id;

    // 消息 key值
    private String msgKey;

    // 消息发送者
    private String msgName;

    // 消息内容
    private String msgValue;

    // 消息时间
    private Timestamp msgTime;
}