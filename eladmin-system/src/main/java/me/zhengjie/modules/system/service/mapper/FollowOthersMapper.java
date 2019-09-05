package me.zhengjie.modules.system.service.mapper;

import me.zhengjie.mapper.EntityMapper;
import me.zhengjie.modules.system.domain.FollowOthers;
import me.zhengjie.modules.system.service.dto.FollowOthersDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author handsome
 * @date 2019-08-28
 */
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FollowOthersMapper extends EntityMapper<FollowOthersDTO, FollowOthers> {

}