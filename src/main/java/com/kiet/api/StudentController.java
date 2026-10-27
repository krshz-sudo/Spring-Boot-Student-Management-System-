package com.kiet.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
public class StudentController {

    // In-memory storage (no database)
    private final List<Student> students = new CopyOnWriteArrayList<>();

    // Q3: in-memory list with 5 students
    public StudentController() {
        students.add(new Student(101, "Rahul Sharma", "rahul@kiet.edu", "CSE(AI & ML)", 2, 8.65));
        students.add(new Student(102, "Priya Singh", "priya@kiet.edu", "CSE", 2, 9.10));
        students.add(new Student(103, "Ankit Verma", "ankit@kiet.edu", "ECE", 3, 7.85));
        students.add(new Student(104, "Sneha Gupta", "sneha@kiet.edu", "IT", 4, 9.30));
        students.add(new Student(105, "Rohan Mehta", "rohan@kiet.edu", "CSE(AI)", 1, 8.20));
    }

    private Map<String, String> message(String text) {
        return Collections.singletonMap("message", text);
    }

    // Q3-A: GET /students
    @GetMapping
    public List<Student> getAllStudents() {
        return students;
    }

    // Q6-A: GET /students/search?branch=CSE&year=2
    @GetMapping("/search")
    public List<Student> searchByBranchAndYear(@RequestParam String branch,
                                               @RequestParam int year) {
        return students.stream()
                .filter(s -> s.getBranch().toLowerCase().contains(branch.toLowerCase())
                        && s.getYear() == year)
                .collect(Collectors.toList());
    }

    // Q6-B: GET /students/topper?cgpa=9.0
    @GetMapping("/topper")
    public List<Student> getStudentsByCgpa(@RequestParam double cgpa) {
        return students.stream()
                .filter(s -> s.getCgpa() >= cgpa)
                .collect(Collectors.toList());
    }

    // Q3-B: GET /students/{studentId}
    @GetMapping("/{studentId}")
    public ResponseEntity<?> getStudentById(@PathVariable int studentId) {
        for (Student s : students) {
            if (s.getStudentId() == studentId) {
                return ResponseEntity.ok(s);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message("Student not found"));
    }

    // Q4: POST /students
    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody Student student) {
        for (Student s : students) {
            if (s.getStudentId() == student.getStudentId()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(message("Student with this ID already exists"));
            }
        }
        students.add(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    // Q5-A: PUT /students/{studentId}
    @PutMapping("/{studentId}")
    public ResponseEntity<?> updateStudent(@PathVariable int studentId,
                                           @RequestBody Student updated) {
        for (Student s : students) {
            if (s.getStudentId() == studentId) {
                s.setName(updated.getName());
                s.setEmail(updated.getEmail());
                s.setBranch(updated.getBranch());
                s.setYear(updated.getYear());
                s.setCgpa(updated.getCgpa());
                return ResponseEntity.ok(s);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message("Student not found"));
    }

    // Q5-B: DELETE /students/{studentId}
    @DeleteMapping("/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable int studentId) {
        boolean removed = students.removeIf(s -> s.getStudentId() == studentId);
        if (removed) {
            return ResponseEntity.ok(message("Student deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message("Student not found"));
    }
}