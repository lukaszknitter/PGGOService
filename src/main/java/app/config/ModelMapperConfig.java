package app.config;

import app.models.building.Building;
import app.models.building.BuildingDto;
import app.models.department.Department;
import app.models.faculty.Faculty;
import app.models.faculty.FacultyDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {

    @Bean
    ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        configureBuildingToBuildingDtoMapping(mapper);
        configureFacultyToFacultyDtoMapping(mapper);
        return mapper;
    }

    private void configureBuildingToBuildingDtoMapping(ModelMapper mapper) {
        Converter<List<Faculty>, List<Long>> getFacultiesIds = context -> context.getSource() == null ? null :
                context.getSource()
                        .stream()
                        .map(Faculty::getId)
                        .collect(Collectors.toList());

        mapper.createTypeMap(Building.class, BuildingDto.class)
                .addMappings(expr -> expr.using(getFacultiesIds).map(Building::getFaculties, BuildingDto::setFaculties));
    }

    private void configureFacultyToFacultyDtoMapping(ModelMapper mapper) {
        Converter<List<Department>, List<Long>> getDepartmentsIds = context -> context.getSource() == null ? null :
                context.getSource()
                        .stream()
                        .map(Department::getId)
                        .collect(Collectors.toList());

        mapper.createTypeMap(Faculty.class, FacultyDto.class)
                .addMappings(expr -> expr.using(getDepartmentsIds).map(Faculty::getDepartments, FacultyDto::setDepartments));
    }
}
