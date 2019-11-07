package me.zhengjie.modules.system.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
* @author groot
* @date 2019-10-23
*/
@Entity
@Data
@Table(name="prizes_num")
public class PrizesNum implements Serializable {

            @Id
                @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "prizes_num",nullable = false)
        private String prizesNum;

        @Column(name = "num",nullable = false)
        private Long num;
}