package me.zhengjie.modules.system.service.mapper;

import me.zhengjie.mapper.EntityMapper;
import me.zhengjie.modules.system.domain.PrizesNum;
import me.zhengjie.modules.system.service.dto.PrizesNumDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author groot
* @date 2019-10-23
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PrizesNumMapper extends EntityMapper
<PrizesNumDTO, PrizesNum> {

}