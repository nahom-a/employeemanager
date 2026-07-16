package tech.getarrays.employeemanager.service;

import org.springframework.stereotype.Service;
import tech.getarrays.employeemanager.exception.UserNotFoundException;
import tech.getarrays.employeemanager.exception.ValidationException;
import tech.getarrays.employeemanager.model.Employee;
import tech.getarrays.employeemanager.repo.EmployeeRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepo employeeRepo;

    public EmployeeServiceImpl(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepo.findAll();
    }

    @Override
    public Employee findEmployeeById(Long id) {
        return employeeRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Employee with id " + id + " not found"));
    }

    @Override
    public Employee addEmployee(Employee employee) {
        validate(employee, null);
        return employeeRepo.save(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        if (employee.getId() == null || !employeeRepo.existsById(employee.getId())) {
            throw new UserNotFoundException("Employee with id " + employee.getId() + " not found");
        }
        validate(employee, employee.getId());
        return employeeRepo.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepo.existsById(id)) {
            throw new UserNotFoundException("Employee with id " + id + " not found");
        }
        employeeRepo.deleteById(id);
    }

    private void validate(Employee e, Long currentId) {
        List<String> errors = new ArrayList<>();
        String name = e.getName() == null ? "" : e.getName().trim();
        String email = e.getEmail() == null ? "" : e.getEmail().trim();
        String position = e.getPosition() == null ? "" : e.getPosition().trim();
        String department = e.getDepartment() == null ? "" : e.getDepartment().trim();
        double salary = e.getSalary();

        if (name.isEmpty()) errors.add("Name is required.");
        else if (name.length() < 2 || name.length() > 100) errors.add("Name must be between 2 and 100 characters.");

        if (position.isEmpty()) errors.add("Position is required.");
        if (department.isEmpty()) errors.add("Department is required.");

        if (email.isEmpty()) errors.add("Email is required.");
        else if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) errors.add("Email format is invalid.");

        if (!(salary >= 0)) errors.add("Salary must be a number greater than or equal to 0.");

        if (!name.isEmpty()) {
            boolean duplicate = employeeRepo.findAll().stream()
                    .anyMatch(x -> !Objects.equals(x.getId(), currentId)
                            && x.getName() != null && x.getName().trim().equalsIgnoreCase(name));
            if (duplicate) errors.add("An employee with this name already exists.");
        }
        if (!email.isEmpty()) {
            boolean duplicate = employeeRepo.findAll().stream()
                    .anyMatch(x -> !Objects.equals(x.getId(), currentId)
                            && x.getEmail() != null && x.getEmail().trim().equalsIgnoreCase(email));
            if (duplicate) errors.add("An employee with this email already exists.");
        }

        if (!errors.isEmpty()) throw new ValidationException(errors);
    }
}
