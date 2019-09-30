package me.zhengjie.modules.system.service.mapper;

import me.zhengjie.mapper.EntityMapper;
import me.zhengjie.modules.system.domain.ZwSaleOrderUpdate;
import me.zhengjie.modules.system.service.dto.ZwSaleOrderUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author groot
 * @date 2019-09-27
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ZwSaleOrderUpdateMapper extends EntityMapper<ZwSaleOrderUpdateDTO, ZwSaleOrderUpdate> {

}