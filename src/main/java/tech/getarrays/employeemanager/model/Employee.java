package tech.getarrays.employeemanager.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "employee")
public class Employee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String name;
    private String email;
    private String position;
    private String department;
    private String phone;
    private double salary;

    public Employee() {}

    public Employee(String name, String email, String position, String department, String phone, double salary) {
        this.name = name;
        this.email = email;
        this.position = position;
        this.department = department;
        this.phone = phone;
        this.salary = salary;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
}
