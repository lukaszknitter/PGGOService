package app.models.faculty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService service;

    @Autowired
    public FacultyController(FacultyService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public FacultyDto getDetail(@PathVariable long id) {
        return this.service.getFaculty(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FacultyDto createDetail(@RequestBody @Valid FacultyDto dto) {
        return service.createFaculty(dto);
    }

    @GetMapping
    public Page<FacultyDto> getDetails(@PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return service.getFaculties(pageable);
    }

    @PutMapping("/{id}")
    public FacultyDto updateDetail(@PathVariable long id, @RequestBody @Valid FacultyDto body) {
        return service.updateFaculty(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deteteDetail(@PathVariable long id) {
        service.deleteFaculty(id);
    }
}
