package app.display.building;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/building/display")
public class BuildingDisplayController {

    private final BuildingDisplayService service;

    @Autowired
    public BuildingDisplayController(BuildingDisplayService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public BuildingDisplayDto getBuilding(@PathVariable long id) {
        return this.service.getBuildingDisplay(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BuildingDisplayDto createBuilding(@RequestBody @Valid BuildingDisplayDto dto) {
        return service.createBuildingDisplay(dto);
    }

    @GetMapping
    public Page<BuildingDisplayDto> getBuildings(@PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return service.getBuildingDisplays(pageable);
    }

    @PutMapping("/{id}")
    public BuildingDisplayDto updateBuilding(@PathVariable long id, @RequestBody @Valid BuildingDisplayDto body) {
        return service.updateBuildingDisplay(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBuilding(@PathVariable long id) {
        service.deleteBuildingDisplay(id);
    }
}
