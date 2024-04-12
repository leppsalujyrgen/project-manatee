package ee.cyber.manatee.mapper;


import java.util.List;

import org.mapstruct.Mapper;

import ee.cyber.manatee.dto.ApplicationDto;
import ee.cyber.manatee.model.Application;


@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    ApplicationDto entityToDto(Application entity);

    Application dtoToEntity(ApplicationDto dto);

    List<ApplicationDto> entitiesToDtoList(List<Application> entity);
}
