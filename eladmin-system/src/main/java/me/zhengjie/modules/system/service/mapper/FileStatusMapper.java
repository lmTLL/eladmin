package me.zhengjie.modules.system.service.mapper;

import me.zhengjie.mapper.EntityMapper;

import me.zhengjie.modules.system.domain.FileStatus;
import me.zhengjie.modules.system.service.dto.FileStatusDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author groot
* @date 2019-07-24
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileStatusMapper extends EntityMapper<FileStatusDTO, FileStatus> {

}