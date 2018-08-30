package app.config;

import app.models.building.Building;
import app.models.building.BuildingCreationDto;
import app.models.building.BuildingDto;
import app.models.department.Department;
import app.models.faculty.Faculty;
import app.models.faculty.FacultyDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {

    @Bean
    ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        configureBuildingDtoToBuildingMapping(mapper);
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

        Converter<List<Faculty>, List<String>> getFacultiesNames = context -> context.getSource() == null ? null :
                context.getSource()
                        .stream()
                        .map(Faculty::getName)
                        .collect(Collectors.toList());

        mapper.createTypeMap(Building.class, BuildingDto.class)
                .addMappings(expr -> expr.using(getFacultiesIds).map(Building::getFaculties, BuildingDto::setFacultiesIds))
                .addMappings(expr -> expr.using(getFacultiesNames).map(Building::getFaculties, BuildingDto::setFacultiesNames));
    }

    // todo parse name of image to string with data
    private void configureBuildingDtoToBuildingMapping(ModelMapper mapper) {
        Converter<String, String> getPicture = context -> {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(this.getClass().getClassLoader());
            try {
                Resource[] resources = resolver.getResources("classpath:/*");
                for (Resource resource : resources) {
                    Pattern pattern = Pattern.compile("JSON_.*");
                    if (resource.getFilename() != null) {
                        Matcher matcher = pattern.matcher(resource.getFilename());
                        if (matcher.find()) {
                            File[] files = resource.getFile().listFiles();
                            for (File file : files) {
                                if (file.getName().equals(context.getSource())) {
                                    final InputStream inputStream = new DataInputStream(new FileInputStream(file));
                                    StringBuilder textBuilder = new StringBuilder();
                                    try (Reader reader = new BufferedReader(new InputStreamReader
                                            (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
                                        int c = 0;
                                        while ((c = reader.read()) != -1) {
                                            textBuilder.append((char) c);
                                        }
                                    }
                                    return textBuilder.toString();
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        };

        mapper.createTypeMap(BuildingCreationDto.class, Building.class)
                .addMappings(expr -> expr.using(getPicture).map(BuildingCreationDto::getPicture, Building::setPicture));
    }


    private void configureFacultyToFacultyDtoMapping(ModelMapper mapper) {
        Converter<List<Department>, List<Long>> getDepartmentsIds = context -> context.getSource() == null ? null :
                context.getSource()
                        .stream()
                        .map(Department::getId)
                        .collect(Collectors.toList());

        Converter<List<Department>, List<String>> getDepartmentsNames = context -> context.getSource() == null ? null :
                context.getSource()
                        .stream()
                        .map(Department::getName)
                        .collect(Collectors.toList());

        mapper.createTypeMap(Faculty.class, FacultyDto.class)
                .addMappings(expr -> expr.using(getDepartmentsIds).map(Faculty::getDepartments, FacultyDto::setDepartmentsIds))
                .addMappings(expr -> expr.using(getDepartmentsNames).map(Faculty::getDepartments, FacultyDto::setDepartmentsNames));
    }
}
