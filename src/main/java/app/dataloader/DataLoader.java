package app.dataloader;

import app.display.building.BuildingDisplayDto;
import app.display.building.BuildingDisplayService;
import app.models.building.BuildingDto;
import app.models.building.BuildingService;
import app.models.department.DepartmentDto;
import app.models.department.DepartmentService;
import app.models.faculty.FacultyDto;
import app.models.faculty.FacultyService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeToken;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class DataLoader {

    private final BuildingDisplayService buildingDisplayService;
    private final BuildingService buildingService;
    private final FacultyService facultyService;
    private final DepartmentService departmentService;

    public void loadBuildings() {
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
        }
    }

    private void readJsonDataFromFiles(File[] files) {
        Gson gson = new Gson();
        Type departmentListType = new TypeToken<ArrayList<DepartmentDto>>() {
        }.getType();
        Type facultyListType = new TypeToken<ArrayList<FacultyDto>>() {
        }.getType();
        Type buildingDisplayListType = new TypeToken<ArrayList<BuildingDisplayDto>>() {
        }.getType();
        Type buildingListType = new TypeToken<ArrayList<BuildingDto>>() {
        }.getType();

        ArrayList<BuildingDisplayDto> buildingDisplays = null;
        ArrayList<DepartmentDto> departments = null;
        ArrayList<BuildingDto> buildings = null;
        ArrayList<FacultyDto> faculties = null;

        for (File file : files) {
            switch (file.getName()) {
                case "building_displays":
                    buildingDisplays = gson.fromJson(readDataFromFile(file), buildingDisplayListType);
                    break;
                case "departments":
                    departments = gson.fromJson(readDataFromFile(file), departmentListType);
                    break;
                case "buildings":
                    buildings = gson.fromJson(readDataFromFile(file), buildingListType);
                    break;
                case "faculties":
                    faculties = gson.fromJson(readDataFromFile(file), facultyListType);
                    break;
                default:
                    System.out.println("Unexpected file: " + file.getName());
                    break;
            }
        }

        ArrayList<FacultyDto> finalFaculties = faculties;
        departments.forEach(departmentDto -> {
            finalFaculties.forEach(facultyDto -> {
                if (departmentDto.getId() == facultyDto.getId()) {
                    facultyService.createFaculty(facultyDto);
                    departmentDto.setFacultyId(facultyDto.getId());
                    departmentService.createDepartment(departmentDto);
                }
            });
        });

        ArrayList<DepartmentDto> finalDepartments = departments;
        faculties.forEach(facultyDto -> {
            finalDepartments.forEach(departmentDto -> {
                if (departmentDto.getId() == facultyDto.getId()) {
                    facultyService.addDepartment(facultyDto.getId(), departmentDto);
                }
            });
        });

        ArrayList<BuildingDisplayDto> finalBuildingDisplays = buildingDisplays;
        buildings.forEach(buildingDto -> {
            finalBuildingDisplays.forEach(buildingDisplayDto -> {
                if (buildingDto.getId() == buildingDisplayDto.getId()) {
                    long buildingDisplayId = buildingService.createBuilding(buildingDto).getId();
                    buildingDisplayDto.setBuildingId(buildingDisplayId);
                }
            });
        });

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
