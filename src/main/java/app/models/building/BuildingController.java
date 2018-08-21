package app.models.building;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/building")
public class BuildingController {

    private final BuildingService service;

    @Autowired
    public BuildingController(BuildingService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public BuildingDto getBuilding(@PathVariable long id) {
        return this.service.getBuilding(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BuildingDto createBuilding(@RequestBody @Valid BuildingDto dto) {
        return service.createBuilding(dto);
    }

    @GetMapping
    public Page<BuildingDto> getBuildings(@PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return service.getBuildings(pageable);
    }

    @PutMapping("/{id}")
    public BuildingDto updateBuilding(@PathVariable long id, @RequestBody @Valid BuildingDto body) {
        return service.updateBuilding(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBuilding(@PathVariable long id) {
        service.deleteBuilding(id);
    }
}
