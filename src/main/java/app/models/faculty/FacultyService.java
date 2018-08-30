package app.models.faculty;

import app.exception.ConflictException;
import app.exception.ResourceNotFoundException;
import app.models.department.Department;
import app.models.department.DepartmentDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Supplier;


@Service
@RequiredArgsConstructor
public class FacultyService {

    private final ModelMapper mapper;
    private final FacultyRepository repository;

    public FacultyDto createFaculty(FacultyCreationDto dto) {
        Optional<Faculty> facultyWithSameName = repository.findFirstByName(dto.getName());
        if (facultyWithSameName.isPresent()) {
            throw new ConflictException(String.format("Faculty with name '%s' already exists", dto.getName()));
        }
        Faculty faculty = repository.saveAndFlush(mapper.map(dto, Faculty.class));
        return mapper.map(faculty, FacultyDto.class);
    }

    public FacultyDto getFacultyDto(long id) {
        Faculty faculty = repository.findById(id)
                .orElseThrow(facultyNotFoundException(id));
        return mapper.map(faculty, FacultyDto.class);
    }

    public Page<FacultyDto> getFaculties(Pageable pageable) {
        Page<Faculty> result = repository.findAll(pageable);
        return result.map(entity -> mapper.map(entity, FacultyDto.class));
    }

    public FacultyDto updateFaculty(long id, FacultyCreationDto dto) {
        Faculty faculty = repository.findById(id)
                .orElseThrow((facultyNotFoundException(id)));
        mapper.map(dto, faculty);

        Optional<Faculty> facultyWithSameName = repository.findFirstByName(dto.getName());
        if (facultyWithSameName.isPresent() && facultyWithSameName.get().getId() != id) {
            throw new ConflictException(String.format("Faculty with name '%s' already exists", dto.getName()));
        }

        Faculty saved = repository.saveAndFlush(faculty);
        return mapper.map(saved, FacultyDto.class);
    }

    public void deleteFaculty(long id) {
        Faculty faculty = repository.findById(id).orElseThrow((facultyNotFoundException(id)));
        repository.delete(faculty);
    }

    private Supplier<ResourceNotFoundException> facultyNotFoundException(long id) {
        return () -> new ResourceNotFoundException(String.format("Faculty with id %d could not be found", id));
    }

    public void addDepartment(long id, DepartmentDto departmentDto) {
        Faculty faculty = repository.findById(id).orElseThrow((facultyNotFoundException(id)));
        Department department = mapper.map(departmentDto, Department.class);
        faculty.getDepartments().add(department);
        repository.saveAndFlush(faculty);
    }
}
