package me.zhengjie.modules.system.service.mapper;

import me.zhengjie.mapper.EntityMapper;
import me.zhengjie.modules.system.domain.ScpSaleOrder;
import me.zhengjie.modules.system.service.dto.ScpSaleOrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author groot
 * @date 2019-10-25
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScpSaleOrderMapper extends EntityMapper
        <ScpSaleOrderDTO, ScpSaleOrder> {

}