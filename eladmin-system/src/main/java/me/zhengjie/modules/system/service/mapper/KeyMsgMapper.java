package me.zhengjie.modules.system.service.mapper;

import me.zhengjie.mapper.EntityMapper;

import me.zhengjie.modules.system.domain.KeyMsg;
import me.zhengjie.modules.system.service.dto.KeyMsgDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author groot
* @date 2019-07-26
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KeyMsgMapper extends EntityMapper<KeyMsgDTO, KeyMsg> {

}