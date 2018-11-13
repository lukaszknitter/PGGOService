package app.dataloader;

import app.display.building.BuildingDisplayDto;
import app.display.building.BuildingDisplayService;
import app.display.poi.POIDto;
import app.display.poi.POIService;
import app.models.building.BuildingCreationDto;
import app.models.building.BuildingService;
import app.models.department.DepartmentDto;
import app.models.faculty.FacultyCreationDto;
import app.models.faculty.FacultyService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DataLoader {

	private final BuildingDisplayService buildingDisplayService;
	private final BuildingService buildingService;
	private final FacultyService facultyService;
	private final POIService poiService;

	public void loadBuildings() {

		String resources = "resources";

		ArrayList<File> files = new ArrayList<>();
		try {
			Files.walk(Paths.get(resources))
					.filter(Files::isRegularFile)
					.forEach(path -> {
						System.out.println(path.toUri());
						files.add(new File(path.toUri()));
					});
		} catch (IOException e) {
			e.printStackTrace();
		}
		readJsonDataFromFiles(files);

/*
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(this.getClass().getClassLoader());
		try {
			Resource[] resources = resolver.getResources("classpath:/*");
			for (Resource resource : resources) {
				Pattern pattern = Pattern.compile("JSON_.*");
				if (resource.getFilename() != null) {
					Matcher matcher = pattern.matcher(resource.getFilename());
					if (matcher.find()) {
						File[] files = resource.getFile().listFiles();
						if (files != null) {
							readJsonDataFromFiles(files);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	private void readJsonDataFromFiles(ArrayList<File> files) {
		Gson gson = new Gson();
		Type departmentListType = new TypeToken<ArrayList<DepartmentDto>>() {
		}.getType();
		Type facultyListType = new TypeToken<ArrayList<FacultyCreationDto>>() {
		}.getType();
		Type buildingDisplayListType = new TypeToken<ArrayList<BuildingDisplayDto>>() {
		}.getType();
		Type buildingListType = new TypeToken<ArrayList<BuildingCreationDto>>() {
		}.getType();
		Type POIListType = new TypeToken<ArrayList<POIDto>>() {
		}.getType();

		ArrayList<BuildingDisplayDto> buildingDisplays = new ArrayList<>();
		ArrayList<DepartmentDto> departments = new ArrayList<>();
		ArrayList<BuildingCreationDto> buildings = new ArrayList<>();
		ArrayList<FacultyCreationDto> faculties = new ArrayList<>();
		ArrayList<POIDto> pois = new ArrayList<>();

		for (File file : files) {
			switch (file.getName()) {
				case "building_displays":
					buildingDisplays.addAll(gson.fromJson(readDataFromFile(file), buildingDisplayListType));
					break;
				case "departments":
					departments.addAll(gson.fromJson(readDataFromFile(file), departmentListType));
					break;
				case "buildings":
					buildings.addAll(gson.fromJson(readDataFromFile(file), buildingListType));
					break;
				case "faculties":
					faculties.addAll(gson.fromJson(readDataFromFile(file), facultyListType));
					break;
				case "poi":
					pois.addAll(gson.fromJson(readDataFromFile(file), POIListType));
					break;
				default:
					System.out.println("Loaded picture: " + file.getName());
					break;
			}
		}

		saveDataToDatabase(buildingDisplays, buildings, faculties, departments, pois);
	}

	private void saveDataToDatabase(ArrayList<BuildingDisplayDto> buildingDisplays,
									ArrayList<BuildingCreationDto> buildings,
									ArrayList<FacultyCreationDto> faculties,
									ArrayList<DepartmentDto> departments,
									ArrayList<POIDto> pois) {
		buildings.stream()
				.filter(Objects::nonNull)
				.forEach(buildingDto -> {

					// copying building dto to save it without facultiesIds, but to keep data in array
					BuildingCreationDto buildingDtoWithoutFaculties = new BuildingCreationDto(buildingDto);
					buildingDtoWithoutFaculties.setFacultiesIds(null);
					long buildingId = buildingService.createBuilding(buildingDtoWithoutFaculties).getId();

					// saving relation between building and its display
					buildingDisplays.stream()
							.filter(buildingDisplayDto -> buildingDisplayDto.getId() == buildingDto.getId())
							.findFirst().get().setBuildingId(buildingId);

					faculties.stream()
							.filter(Objects::nonNull)
							.forEach(facultyDto -> {
								if (buildingDto.getFacultiesIds() != null
										&& buildingDto.getFacultiesIds().contains(facultyDto.getId())) {
									// copying faculty dto to save it without departmentsIds, but to keep data in array
									FacultyCreationDto updatedFacultyDto = new FacultyCreationDto(facultyDto);
									updatedFacultyDto.setDepartmentsIds(null);
									long facultyId = facultyService.createFaculty(updatedFacultyDto).getId();
									updatedFacultyDto.setId(facultyId);
									// saving relation between building and current faculty
									buildingService.addFaculty(buildingId, updatedFacultyDto);

									departments.stream()
											.filter(Objects::nonNull)
											.forEach(departmentDto -> {
												if (facultyDto.getDepartmentsIds().contains(departmentDto.getId())) {
													departmentDto.setFacultyId(facultyId);
													// saving relation between faculty and current department

													facultyService.addDepartment(facultyId, departmentDto);
												}
											});
								}

							});
				});
		pois.forEach(poiDto -> poiService.createPOI(poiDto));
		buildingDisplays.forEach((buildingDisplayService::createBuildingDisplay));
	}

	private String readDataFromFile(File file) {
		String json = null;
		try {
			InputStream inputStream = new FileInputStream(file);
			int size = inputStream.available();
			byte[] buffer = new byte[size];
			inputStream.read(buffer);
			inputStream.close();
			json = new String(buffer, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}
}
