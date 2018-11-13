package app.display.poi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("/poi")
public class POIController {

	private final POIService service;

	@Autowired
	public POIController(POIService service) {
		this.service = service;
	}

	@GetMapping("/{id}")
	public POIDto getPOI(@PathVariable long id) {
		return this.service.getPOI(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public POIDto createPOI(@RequestBody @Valid POIDto dto) {
		return service.createPOI(dto);
	}

	@GetMapping
	public ArrayList<POIDto> getPOIs() {
		return service.getPOI();
	}

	@PutMapping("/{id}")
	public POIDto updatePOI(@PathVariable long id, @RequestBody @Valid POIDto body) {
		return service.updatePOI(id, body);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deletePOI(@PathVariable long id) {
		service.deletePOI(id);
	}
}
