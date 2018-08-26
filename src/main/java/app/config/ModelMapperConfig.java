package app.config;

import app.models.building.Building;
import app.models.building.BuildingDto;
import app.models.faculty.Faculty;
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
        return mapper;
    }
    private void configureBuildingToBuildingDtoMapping(ModelMapper mapper) {
        Converter<List<Faculty>, List<String>> getPalettesIds = context -> context.getSource() == null ? null :
                context.getSource()
                        .stream()
                        .map(Faculty::getTag)
                        .collect(Collectors.toList());

        mapper.createTypeMap(Building.class, BuildingDto.class)
                .addMappings(expr -> expr.using(getPalettesIds).map(Building::getFaculties, BuildingDto::setFaculties));
    }
}
