package io.czen.errorhandling.controller;

import io.czen.errorhandling.model.Student;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(value = "/v1")
@Validated
public class StudentController {

    @GetMapping(value = "/student")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasRole('USER')")
    public Student getStudent(@RequestParam("studentId") @Min(1) @Max(100) Long studentId) {
        return new Student(studentId, "Wayne", "Bruce");
    }

    @PostMapping(value = "/student", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Student postStudent(@RequestBody @Valid Student student) {
        return new Student(student.getId(), student.getFirstName(), student.getLastName());
    }
}
