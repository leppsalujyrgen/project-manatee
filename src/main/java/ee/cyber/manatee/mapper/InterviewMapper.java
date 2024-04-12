package ee.cyber.manatee.mapper;


import java.util.List;

import org.mapstruct.Mapper;

import ee.cyber.manatee.dto.InterviewDto;
import ee.cyber.manatee.model.Interview;


@Mapper(componentModel = "spring")
public interface InterviewMapper {

    InterviewDto entityToDto(Interview entity);

    Interview dtoToEntity(InterviewDto dto);

    List<InterviewDto> entitiesToDtoList(List<Interview> entity);
}
