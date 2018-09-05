package app.models.building;

import app.exception.ConflictException;
import app.exception.ResourceNotFoundException;
import app.models.faculty.Faculty;
import app.models.faculty.FacultyCreationDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BuildingService {

	private final ModelMapper mapper;
	private final BuildingRepository buildingRepository;

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}

	public BuildingDto createBuilding(BuildingCreationDto dto) {
		Optional<Building> buildingWithSameName = buildingRepository.findFirstByName(dto.getName());
		if (buildingWithSameName.isPresent()) {
			throw new ConflictException(String.format("Building with name '%s' already exists", dto.getName()));
		}

		Building building = buildingRepository.saveAndFlush(mapper.map(dto, Building.class));
		return mapper.map(building, BuildingDto.class);
	}

	public BuildingDto getBuilding(long id) {
		Building building = buildingRepository.findById(id).orElseThrow(buildingNotFoundException(id));
		ArrayList<Faculty> faculties = (ArrayList<Faculty>) building.getFaculties()
				.stream().filter(distinctByKey(Faculty::getId))
				.collect(Collectors.toList());
		building.setFaculties(faculties);
		return mapper.map(building, BuildingDto.class);
	}

	public ArrayList<BuildingDto> getBuildings() {
		ArrayList<Building> result = new ArrayList<>(buildingRepository.findAll());
		Type listType = new TypeToken<List<BuildingDto>>() {
		}.getType();
		return mapper.map(result, listType);
	}

	public BuildingDto updateBuilding(long id, BuildingCreationDto dto) {
		Building building = buildingRepository.findById(id).orElseThrow((buildingNotFoundException(id)));
		mapper.map(dto, building);

		Optional<Building> buildingWithSameName = buildingRepository.findFirstByName(dto.getName());
		if (buildingWithSameName.isPresent() && buildingWithSameName.get().getId() != id) {
			throw new ConflictException(String.format("Building with name '%s' already exists", dto.getName()));
		}

		Building saved = buildingRepository.saveAndFlush(building);
		return mapper.map(saved, BuildingDto.class);
	}

	public void deleteBuilding(long id) {
		Building building = buildingRepository.findById(id).orElseThrow((buildingNotFoundException(id)));
		buildingRepository.delete(building);
	}

	private Supplier<ResourceNotFoundException> buildingNotFoundException(long id) {
		return () -> new ResourceNotFoundException(String.format("Building with id %d could not be found", id));
	}

	public void addFaculty(long id, FacultyCreationDto facultyDto) {
		Building building = buildingRepository.findById(id).orElseThrow((buildingNotFoundException(id)));
		Faculty faculty = mapper.map(facultyDto, Faculty.class);

		ArrayList<Faculty> faculties = (ArrayList<Faculty>) building.getFaculties()
				.stream().filter(distinctByKey(Faculty::getId))
				.collect(Collectors.toList());

		faculties.add(faculty);
		building.setFaculties(faculties);
		buildingRepository.saveAndFlush(building);
	}
}
