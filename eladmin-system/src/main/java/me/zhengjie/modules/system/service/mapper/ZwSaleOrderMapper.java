package me.zhengjie.modules.system.service.mapper;

import me.zhengjie.mapper.EntityMapper;
import me.zhengjie.modules.system.domain.ZwSaleOrder;
import me.zhengjie.modules.system.service.dto.ZwSaleOrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author groot
* @date 2019-09-05
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ZwSaleOrderMapper extends EntityMapper<ZwSaleOrderDTO, ZwSaleOrder> {

}