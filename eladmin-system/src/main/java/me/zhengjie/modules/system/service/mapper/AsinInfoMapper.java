package me.zhengjie.modules.system.service.mapper;

import me.zhengjie.mapper.EntityMapper;
import me.zhengjie.modules.system.domain.AsinInfo;
import me.zhengjie.modules.system.service.dto.AsinInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author groot
 * @date 2019-07-22
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AsinInfoMapper extends EntityMapper<AsinInfoDTO, AsinInfo> {

}