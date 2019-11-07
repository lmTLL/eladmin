package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;


/**
* @author groot
* @date 2019-10-23
*/
@Data
public class PrizesNumDTO implements Serializable {

        private Long id;

        private String prizesNum;

        private Long num;
}