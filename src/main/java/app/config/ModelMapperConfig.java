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
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {

	public static String encodeToString(BufferedImage image, String type) {
		String imageString = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ImageIO.write(image, type, bos);
			byte[] imageBytes = bos.toByteArray();

			BASE64Encoder encoder = new BASE64Encoder();
			imageString = encoder.encode(imageBytes);

			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageString;
	}

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

		Converter<List<Faculty>, List<String>> getFacultiesNames = context -> context.getSource() == null ? null :
				context.getSource()
						.stream()
						.map(Faculty::getName)
						.collect(Collectors.toList());

		Converter<String, String> getPicture = context -> {
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(this.getClass().getClassLoader());
			try {
				Resource[] resources = resolver.getResources("classpath:/*");
				for (Resource resource : resources) {
					File[] files = resource.getFile().listFiles();
					if (files != null) {
						for (File file : files) {
							if (file.getName().equals(context.getSource())) {
								final InputStream inputStream = new DataInputStream(new FileInputStream(file));
								return encodeToString(ImageIO.read(inputStream), "png");
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		};

		mapper.createTypeMap(Building.class, BuildingDto.class)
				.addMappings(expr -> expr.using(getFacultiesIds).map(Building::getFaculties, BuildingDto::setFacultiesIds))
				.addMappings(expr -> expr.using(getFacultiesNames).map(Building::getFaculties, BuildingDto::setFacultiesNames))
				.addMappings(expr -> expr.using(getPicture).map(Building::getPicture, BuildingDto::setPicture));
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
