package io.czen.errorhandling.controller;

import io.czen.errorhandling.model.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Controller
@RequestMapping(value = "/v1")
@Validated
public class StudentController {

    @GetMapping(value = "/student")
    public ResponseEntity<Student> getStudent(@RequestParam("studentId") @Min(1) @Max(12) Long studentId) {
        return new ResponseEntity<>(new Student(studentId, "Wayne", "Bruce"), HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/student", consumes = "application/json")
    public ResponseEntity<Student> postStudent(@RequestBody @Valid Student student) {
        return new ResponseEntity<>(new Student(student.getId(), student.getFirstName(), student.getLastName()), HttpStatus.CREATED);
    }
}
