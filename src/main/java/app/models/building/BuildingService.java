package app.models.building;

import app.exception.ConflictException;
import app.exception.ResourceNotFoundException;
import app.models.faculty.Faculty;
import app.models.faculty.FacultyDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;


@Service
@RequiredArgsConstructor
public class BuildingService {

    private final ModelMapper mapper;
    private final BuildingRepository buildingRepository;

    public BuildingDto createBuilding(BuildingDto dto) {
        Optional<Building> buildingWithSameName = buildingRepository.findFirstByName(dto.getName());
        if (buildingWithSameName.isPresent()) {
            dto.setId(-1);
            return dto;
            //throw new ConflictException(String.format("Building with name '%s' already exists", dto.getName()));
        }

        Building building = buildingRepository.saveAndFlush(mapper.map(dto, Building.class));
        return mapper.map(building, BuildingDto.class);
    }

    public BuildingDto getBuilding(long id) {
        Building building = buildingRepository.findById(id).orElseThrow(buildingNotFoundException(id));
        return mapper.map(building, BuildingDto.class);
    }

    public Optional<Building> getBuilding(String name) {
        return buildingRepository.findFirstByName(name);
    }

    public Page<BuildingDto> getBuildings(Pageable pageable) {
        Page<Building> result = buildingRepository.findAll(pageable);
        return result.map(entity -> mapper.map(entity, BuildingDto.class));
    }

    public BuildingDto updateBuilding(long id, BuildingDto dto) {
        Building building = buildingRepository.findById(id).orElseThrow((buildingNotFoundException(id)));
        mapper.map(dto, building);

        Optional<Building> buildingWithSameName = buildingRepository.findFirstByName(dto.getName());
        if (buildingWithSameName.isPresent() && buildingWithSameName.get().getId() != id) {
            throw new ConflictException(String.format("Building with name '%s' already exists", dto.getName()));
        }

        Building saved = buildingRepository.save(building);
        return mapper.map(saved, BuildingDto.class);
    }

    public void deleteBuilding(long id) {
        Building building = buildingRepository.findById(id).orElseThrow((buildingNotFoundException(id)));
        buildingRepository.delete(building);
    }

    private Supplier<ResourceNotFoundException> buildingNotFoundException(long id) {
        return () -> new ResourceNotFoundException(String.format("Building with id %d could not be found", id));
    }

    private Supplier<ResourceNotFoundException> buildingNotFoundException(String name) {
        return () -> new ResourceNotFoundException(String.format("Building with name %s could not be found", name));
    }

    public void addFaculty(long id, FacultyDto facultyDto) {
        Building building = buildingRepository.findById(id).orElseThrow((buildingNotFoundException(id)));
        Faculty faculty = mapper.map(facultyDto, Faculty.class);

        List<Faculty> faculties = building.getFaculties();
        faculties.add(faculty);
        building.setFaculties(faculties);
        buildingRepository.save(building);
    }
}
