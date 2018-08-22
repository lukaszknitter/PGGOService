package app.models.faculty;

import app.exception.ConflictException;
import app.exception.ResourceNotFoundException;
import app.models.department.DepartmentService;
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

    public FacultyDto createFaculty(FacultyDto dto) {
        Optional<Faculty> facultyWithSameName = repository.findFirstByName(dto.getName());
        if (facultyWithSameName.isPresent()) {
            throw new ConflictException(String.format("Faculty with name '%s' already exists", dto.getName()));
        }
        Faculty faculty = mapper.map(dto, Faculty.class);

        repository.save(faculty);
        return mapper.map(faculty, FacultyDto.class);
    }

    public FacultyDto getFacultyDto(long id) {
        Faculty faculty = repository.findById(id)
                .orElseThrow(facultyNotFoundException(id));
        return mapper.map(faculty, FacultyDto.class);
    }

    public Faculty getFaculty(long id) {
        Faculty faculty = repository.findById(id).orElseThrow(facultyNotFoundException(id));
        return faculty;
    }

    public Optional<Faculty> getFaculty(String name) {
        return repository.findFirstByName(name);
    }

    public Page<FacultyDto> getFaculties(Pageable pageable) {
        Page<Faculty> result = repository.findAll(pageable);
        return result.map(entity -> mapper.map(entity, FacultyDto.class));
    }

    public FacultyDto updateFaculty(long id, FacultyDto dto) {
        Faculty faculty = repository.findById(id)
                .orElseThrow((facultyNotFoundException(id)));
        mapper.map(dto, faculty);

        Optional<Faculty> facultyWithSameName = repository.findFirstByName(dto.getName());
        if (facultyWithSameName.isPresent() && facultyWithSameName.get().getId() != id) {
            throw new ConflictException(String.format("Faculty with name '%s' already exists", dto.getName()));
        }

        Faculty saved = repository.save(faculty);
        return mapper.map(saved, FacultyDto.class);
    }

    public void deleteFaculty(long id) {
        Faculty faculty = repository.findById(id).orElseThrow((facultyNotFoundException(id)));
        repository.delete(faculty);
    }

    private Supplier<ResourceNotFoundException> facultyNotFoundException(long id) {
        return () -> new ResourceNotFoundException(String.format("Faculty with id %d could not be found", id));
    }

    private Supplier<ResourceNotFoundException> facultyNotFoundException(String name) {
        return () -> new ResourceNotFoundException(String.format("Faculty with name %s could not be found", name));
    }
}