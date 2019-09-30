package me.zhengjie.modules.system.service.mapper;

import me.zhengjie.mapper.EntityMapper;
import me.zhengjie.modules.system.domain.ZwChannel;
import me.zhengjie.modules.system.service.dto.ZwChannelDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author groot
 * @date 2019-09-06
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ZwChannelMapper extends EntityMapper<ZwChannelDTO, ZwChannel> {

}