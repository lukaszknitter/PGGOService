package app.models.building;

import app.exception.ConflictException;
import app.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Supplier;


@Service
@RequiredArgsConstructor
public class BuildingService {

    private final ModelMapper mapper;
    private final BuildingRepository repository;

    public BuildingDto createBuilding(BuildingDto dto) {
        Optional<Building> detailWithSameName = repository.findFirstByName(dto.getName());
        if (detailWithSameName.isPresent()) {
            throw new ConflictException(String.format("Building with name '%s' already exists", dto.getName()));
        }

        Building building = mapper.map(dto, Building.class);
        repository.save(building);
        return mapper.map(building, BuildingDto.class);
    }

    public BuildingDto getBuilding(long id) {
        Building building = repository.findById(id).orElseThrow(detailNotFoundException(id));
        return mapper.map(building, BuildingDto.class);
    }

    public Optional<Building> getBuilding(String name) {
        return repository.findFirstByName(name);
    }

    public Page<BuildingDto> getBuildings(Pageable pageable) {
        Page<Building> result = repository.findAll(pageable);
        return result.map(entity -> mapper.map(entity, BuildingDto.class));
    }

    public BuildingDto updateBuilding(long id, BuildingDto dto) {
        Building building = repository.findById(id).orElseThrow((detailNotFoundException(id)));
        mapper.map(dto, building);

        Optional<Building> buildingWithSameName = repository.findFirstByName(dto.getName());
        if (buildingWithSameName.isPresent() && buildingWithSameName.get().getId() != id) {
            throw new ConflictException(String.format("Building with name '%s' already exists", dto.getName()));
        }

        Building saved = repository.save(building);
        return mapper.map(saved, BuildingDto.class);
    }

    public void deleteBuilding(long id) {
        Building building = repository.findById(id).orElseThrow((detailNotFoundException(id)));
        repository.delete(building);
    }

    private Supplier<ResourceNotFoundException> detailNotFoundException(long id) {
        return () -> new ResourceNotFoundException(String.format("Building with id %d could not be found", id));
    }

    private Supplier<ResourceNotFoundException> detailNotFoundException(String name) {
        return () -> new ResourceNotFoundException(String.format("Building with name %s could not be found", name));
    }
}
