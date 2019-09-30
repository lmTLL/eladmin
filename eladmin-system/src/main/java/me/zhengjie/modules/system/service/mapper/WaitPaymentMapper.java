package me.zhengjie.modules.system.service.mapper;

import me.zhengjie.mapper.EntityMapper;
import me.zhengjie.modules.system.domain.WaitPayment;
import me.zhengjie.modules.system.service.dto.WaitPaymentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author groot
 * @date 2019-08-01
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WaitPaymentMapper extends EntityMapper<WaitPaymentDTO, WaitPayment> {

}