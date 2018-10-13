package app.config;

import app.models.building.Building;
import app.models.building.BuildingDto;
import app.models.department.Department;
import app.models.department.DepartmentDto;
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
		configureDepartmentToDepartmentDtoMapping(mapper);
		return mapper;
	}

	private void configureDepartmentToDepartmentDtoMapping(ModelMapper mapper) {
		Converter<Faculty, Long> getFacultyId = context -> context.getSource() == null ? null :
				context.getSource().getId();

		mapper.createTypeMap(Department.class, DepartmentDto.class)
				.addMappings(expr -> expr.using(getFacultyId).map(Department::getFaculty, DepartmentDto::setFacultyId));
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
