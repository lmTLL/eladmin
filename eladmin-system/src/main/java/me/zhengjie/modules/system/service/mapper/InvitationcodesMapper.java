package me.zhengjie.modules.system.service.mapper;

import me.zhengjie.mapper.EntityMapper;
import me.zhengjie.modules.system.domain.Invitationcodes;
import me.zhengjie.modules.system.service.dto.InvitationcodesDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author groot
 * @date 2019-07-05
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvitationcodesMapper extends EntityMapper<InvitationcodesDTO, Invitationcodes> {

}