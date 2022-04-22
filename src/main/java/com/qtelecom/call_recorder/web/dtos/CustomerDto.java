package com.qtelecom.call_recorder.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto implements Serializable {
    static final long serialVersionUID = -5815566940065181210L;

    @Schema(description =  "Unique ID that belongs to customer")
    private Integer id;

    @Schema(description =  "Customer version")
    private Integer version;

    @Email
    @NotBlank
    private String email;

    @Length(max = 127)
    @NotBlank
    @Schema(description =  "Customer's first name, its length must be less than 128 characters")
    private String firstName;

    @Length(max = 127)
    @NotBlank
    @Schema(description =  "Customer's last name, its length must be less than 128 characters")
    private String lastName;

    @Length(max = 255)
    @Schema(description =  "File used for searching customers")
    private String searchName;

    @Positive
    @Schema(description =  "Shows customer's age")
    private Integer age;
}
