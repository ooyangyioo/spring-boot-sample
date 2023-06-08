package org.yangyi.project.mapstruct;

import org.mapstruct.*;
import org.yangyi.project.system.dto.DataFormatDTO;
import org.yangyi.project.system.vo.DataFormatVO;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface DataFormatConverter {

    DataFormatVO convert(DataFormatDTO dataFormatDTO);

}
