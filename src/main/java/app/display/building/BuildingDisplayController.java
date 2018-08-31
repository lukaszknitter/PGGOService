package app.display.building;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

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
    public ArrayList<BuildingDisplayDto> getBuildings() {
        return service.getBuildingDisplays();
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
