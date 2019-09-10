package me.zhengjie.modules.system.service.mapper;

import me.zhengjie.mapper.EntityMapper;
import me.zhengjie.modules.system.domain.ZwDealSite;
import me.zhengjie.modules.system.service.dto.ZwDealSiteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author groot
* @date 2019-09-06
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ZwDealSiteMapper extends EntityMapper<ZwDealSiteDTO, ZwDealSite> {

}