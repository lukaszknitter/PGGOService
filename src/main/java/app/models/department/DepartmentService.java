package app.models.department;

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
public class DepartmentService {

    private final ModelMapper mapper;
    private final DepartmentRepository repository;


    public DepartmentDto createDepartment(DepartmentDto dto) {
        Optional<Department> departmentWithSameName = repository.findFirstByName(dto.getName());
        if (departmentWithSameName.isPresent()) {
            throw new ConflictException(String.format("Department with name '%s' already exists", dto.getName()));
        }
        Department department = mapper.map(dto, Department.class);

        repository.save(department);
        return mapper.map(department, DepartmentDto.class);
    }

    public DepartmentDto getDepartment(long id) {
        Department department = repository.findById(id)
                .orElseThrow(departmentNotFoundException(id));
        return mapper.map(department, DepartmentDto.class);
    }

    public Optional<Department> getDepartment(String name) {
        return repository.findFirstByName(name);
    }

    public Page<DepartmentDto> getDepartments(Pageable pageable) {
        Page<Department> result = repository.findAll(pageable);
        return result.map(entity -> mapper.map(entity, DepartmentDto.class));
    }

    public DepartmentDto updateDepartment(long id, DepartmentDto dto) {
        Department department = repository.findById(id)
                .orElseThrow((departmentNotFoundException(id)));
        mapper.map(dto, department);

        Optional<Department> departmentWithSameName = repository.findFirstByName(dto.getName());
        if (departmentWithSameName.isPresent() && departmentWithSameName.get().getId() != id) {
            throw new ConflictException(String.format("Department with name '%s' already exists", dto.getName()));
        }

        Department saved = repository.save(department);
        return mapper.map(saved, DepartmentDto.class);
    }

    public void deleteDepartment(long id) {
        Department department = repository.findById(id).orElseThrow((departmentNotFoundException(id)));
        DepartmentDto dto = mapper.map(department, DepartmentDto.class);
        repository.delete(department);
    }

    private Supplier<ResourceNotFoundException> departmentNotFoundException(long id) {
        return () -> new ResourceNotFoundException(String.format("Department with id %d could not be found", id));
    }

    private Supplier<ResourceNotFoundException> departmentNotFoundException(String name) {
        return () -> new ResourceNotFoundException(String.format("Department with id %s could not be found", name));
    }
}
