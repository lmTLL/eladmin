package me.zhengjie.modules.system.service.mapper;

import me.zhengjie.mapper.EntityMapper;
import me.zhengjie.modules.system.domain.DzSaleOrder;
import me.zhengjie.modules.system.service.dto.DzSaleOrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author groot
 * @date 2019-11-01
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DzSaleOrderMapper extends EntityMapper
        <DzSaleOrderDTO, DzSaleOrder> {

}