package io.czen.errorhandling.model.error;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Errors {

    private List<Error> errorList = new ArrayList<>();

    public Errors errorList(List<Error> errors) {
        this.errorList = errors;
        return this;
    }
}
