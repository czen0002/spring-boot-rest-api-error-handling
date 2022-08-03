package io.czen.errorhandling.controller;

import io.czen.errorhandling.model.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/v1")
public class StudentController {

    @GetMapping(value = "/student")
    public ResponseEntity<Student> getStudent(@RequestParam("studentId") Long studentId) {
        return new ResponseEntity<>(new Student(studentId, "Wayne", "Bruce"), HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/student", consumes = "application/json")
    public ResponseEntity<Student> postStudent(@RequestBody @Valid Student student) {
        return new ResponseEntity<>(new Student(student.getId(), student.getFirstName(), student.getLastName()), HttpStatus.CREATED);
    }
}
