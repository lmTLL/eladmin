package me.zhengjie.modules.system.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author groot
 * @date 2019-07-26
 */
@Entity
@Data
@Table(name = "key_msg")
public class KeyMsg implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "key_s")
    private String key;

    @Column(name = "msg")
    private String msg;
}