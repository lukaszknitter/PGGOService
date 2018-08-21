package app.models.department;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService service;

    @Autowired
    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public DepartmentDto getDetail(@PathVariable long id) {
        return this.service.getDepartment(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentDto createDetail(@RequestBody @Valid DepartmentDto dto) {
        return service.createDepartment(dto);
    }

    @GetMapping
    public Page<DepartmentDto> getDetails(@PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return service.getDepartments(pageable);
    }

    @PutMapping("/{id}")
    public DepartmentDto updateDetail(@PathVariable long id, @RequestBody @Valid DepartmentDto body) {
        return service.updateDepartment(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deteteDetail(@PathVariable long id) {
        service.deleteDepartment(id);
    }
}
