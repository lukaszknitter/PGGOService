package app.models.building;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

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
	public BuildingDto createBuilding(@RequestBody @Valid BuildingCreationDto dto) {
		return service.createBuilding(dto);
	}

	@GetMapping
	public ArrayList<BuildingDto> getBuildings() {
		return service.getBuildings();
	}

	@PutMapping("/{id}")
	public BuildingDto updateBuilding(@PathVariable long id, @RequestBody @Valid BuildingCreationDto body) {
		return service.updateBuilding(id, body);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteBuilding(@PathVariable long id) {
		service.deleteBuilding(id);
	}
}
