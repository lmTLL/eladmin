package me.zhengjie.modules.system.service.mapper;

import me.zhengjie.mapper.EntityMapper;
import me.zhengjie.modules.system.domain.FollowDetails;
import me.zhengjie.modules.system.service.dto.FollowDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author groot
 * @date 2019-07-23
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FollowDetailsMapper extends EntityMapper<FollowDetailsDTO, FollowDetails> {

}