package me.zhengjie.modules.system.service.mapper;

import me.zhengjie.mapper.EntityMapper;
import me.zhengjie.modules.system.domain.ZwPostingEffect;
import me.zhengjie.modules.system.service.dto.ZwPostingEffectDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author groot
 * @date 2019-09-19
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ZwPostingEffectMapper extends EntityMapper<ZwPostingEffectDTO, ZwPostingEffect> {

}