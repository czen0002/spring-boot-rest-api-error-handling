package io.czen.errorhandling.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
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

    public Student id(Long id) {
        this.id = id;
        return this;
    }

    public Student firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Student lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
}
