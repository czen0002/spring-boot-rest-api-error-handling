package io.czen.errorhandling.controller;

import io.czen.errorhandling.model.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/v1")
public class StudentController {

    @GetMapping(value = "/student")
    public ResponseEntity<Student> getStudent(@RequestParam("studentId") Long studentId) {
        return ResponseEntity.ok(new Student(studentId, "Wayne", "Bruce"));
    }
}
