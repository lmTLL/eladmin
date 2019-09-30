package me.zhengjie.modules.system.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;


/**
 * @author groot
 * @date 2019-07-24
 */
@Data
public class FileStatusDTO implements Serializable {

    // 处理精度丢失问题
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String nickname;

    private String filename;

    private String newStatus;
}