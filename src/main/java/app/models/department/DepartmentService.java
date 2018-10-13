package app.models.department;

import app.exception.ConflictException;
import app.exception.ResourceNotFoundException;
import app.models.SearchSpecifications;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;


@Service
@RequiredArgsConstructor
public class DepartmentService {

	private final ModelMapper mapper;
	private final DepartmentRepository repository;
	private final SearchSpecifications searchSpecifications;

	public DepartmentDto createDepartment(DepartmentDto dto) {
		Optional<Department> departmentWithSameName = repository.findFirstByName(dto.getName());
		if (departmentWithSameName.isPresent()) {
			throw new ConflictException(String.format("Department with name '%s' already exists", dto.getName()));
		}
		Department department = repository.saveAndFlush(mapper.map(dto, Department.class));
		return mapper.map(department, DepartmentDto.class);
	}

	public DepartmentDto getDepartment(long id) {
		Department department = repository.findById(id)
				.orElseThrow(departmentNotFoundException(id));
		return mapper.map(department, DepartmentDto.class);
	}

	public ArrayList<DepartmentDto> getDepartments(String name) {
		List result = repository.findAll(searchSpecifications.nameContains(name));
		Type listType = new TypeToken<List<DepartmentDto>>() {
		}.getType();
		return mapper.map(result, listType);
	}

	public DepartmentDto updateDepartment(long id, DepartmentDto dto) {
		Department department = repository.findById(id)
				.orElseThrow((departmentNotFoundException(id)));
		mapper.map(dto, department);

		Optional<Department> departmentWithSameName = repository.findFirstByName(dto.getName());
		if (departmentWithSameName.isPresent() && departmentWithSameName.get().getId() != id) {
			throw new ConflictException(String.format("Department with name '%s' already exists", dto.getName()));
		}

		Department saved = repository.saveAndFlush(department);
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
}
