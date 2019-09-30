package me.zhengjie.modules.system.service.mapper;

import me.zhengjie.mapper.EntityMapper;
import me.zhengjie.modules.system.domain.Navigation;
import me.zhengjie.modules.system.service.dto.NavigationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author groot
 * @date 2019-08-14
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NavigationMapper extends EntityMapper<NavigationDTO, Navigation> {

}