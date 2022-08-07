package io.czen.errorhandling.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class Student implements Serializable {

    @NotNull(message = "Student Id must be provided")
    private Long id;

    private String firstName;

    private String lastName;

    public Student(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
