package app.display.building;

import app.exception.ConflictException;
import app.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;


@Service
@RequiredArgsConstructor
public class BuildingDisplayService {

    private final ModelMapper mapper;
    private final BuildingDisplayRepository buildingDisplayRepository;

    public BuildingDisplayDto createBuildingDisplay(BuildingDisplayDto dto) {
        Optional<BuildingDisplay> buildingWithSameName = buildingDisplayRepository.findFirstByName(dto.getName());
        if (buildingWithSameName.isPresent()) {
            throw new ConflictException(String.format("Building display with name '%s' already exists", dto.getName()));
        }

        BuildingDisplay buildingDisplay = mapper.map(dto, BuildingDisplay.class);
        buildingDisplayRepository.saveAndFlush(buildingDisplay);
        return mapper.map(buildingDisplay, BuildingDisplayDto.class);
    }

    public BuildingDisplayDto getBuildingDisplay(long id) {
        BuildingDisplay buildingDisplay = buildingDisplayRepository.findById(id).orElseThrow(buildingDisplayNotFoundException(id));
        return mapper.map(buildingDisplay, BuildingDisplayDto.class);
    }

    public ArrayList<BuildingDisplayDto> getBuildingDisplays() {
        ArrayList<BuildingDisplay> result = new ArrayList<>(buildingDisplayRepository.findAll());
        Type listType = new TypeToken<List<BuildingDisplayDto>>() {
        }.getType();
        return mapper.map(result, listType);
    }

    public BuildingDisplayDto updateBuildingDisplay(long id, BuildingDisplayDto dto) {
        BuildingDisplay buildingDisplay = buildingDisplayRepository.findById(id).orElseThrow((buildingDisplayNotFoundException(id)));
        mapper.map(dto, buildingDisplay);

        Optional<BuildingDisplay> buildingWithSameName = buildingDisplayRepository.findFirstByName(dto.getName());
        if (buildingWithSameName.isPresent() && buildingWithSameName.get().getId() != id) {
            throw new ConflictException(String.format("Building display with name '%s' already exists", dto.getName()));
        }

        BuildingDisplay saved = buildingDisplayRepository.saveAndFlush(buildingDisplay);
        return mapper.map(saved, BuildingDisplayDto.class);
    }

    public void deleteBuildingDisplay(long id) {
        BuildingDisplay buildingDisplay = buildingDisplayRepository.findById(id).orElseThrow((buildingDisplayNotFoundException(id)));
        buildingDisplayRepository.delete(buildingDisplay);
    }

    private Supplier<ResourceNotFoundException> buildingDisplayNotFoundException(long id) {
        return () -> new ResourceNotFoundException(String.format("Building display with id %d could not be found", id));
    }
}
