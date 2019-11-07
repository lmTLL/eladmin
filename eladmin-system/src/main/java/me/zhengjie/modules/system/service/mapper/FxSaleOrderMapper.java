package me.zhengjie.modules.system.service.mapper;

import me.zhengjie.mapper.EntityMapper;
import me.zhengjie.modules.system.domain.FxSaleOrder;
import me.zhengjie.modules.system.service.dto.FxSaleOrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author groot
 * @date 2019-10-31
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FxSaleOrderMapper extends EntityMapper
        <FxSaleOrderDTO, FxSaleOrder> {

}