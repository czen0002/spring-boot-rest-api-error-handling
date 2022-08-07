package io.czen.errorhandling.model.error;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonPropertyOrder({"title", "code", "detail"})
public class Error implements Serializable {

    private String code;

    private String title;

    private String detail;

    public Error code(String code) {
        this.code = code;
        return this;
    }

    public Error title(String title) {
        this.title = title;
        return this;
    }

    public Error detail(String detail) {
        this.detail = detail;
        return this;
    }
}
