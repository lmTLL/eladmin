package me.zhengjie.modules.system.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author groot
 * @date 2019-09-11
 */
@Entity
@Data
@Table(name = "message")
public class Message implements Serializable {

    // id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 消息 key值
    @Column(name = "msg_key")
    private String msgKey;

    // 消息发送者
    @Column(name = "msg_name")
    private String msgName;

    // 消息内容
    @Column(name = "msg_value")
    private String msgValue;

    // 消息时间
    @Column(name = "msg_time")
    private Timestamp msgTime;
}