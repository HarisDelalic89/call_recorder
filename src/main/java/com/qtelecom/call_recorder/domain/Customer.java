package com.qtelecom.call_recorder.domain;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "CUSTOMER" )
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Email
    @NotBlank
    @Column(unique = true, updatable = false)
    private String email;

    @Version
    private Integer version;

    @Length(max = 127)
    @NotBlank
    private String firstName;

    @Length(max = 127)
    @NotBlank
    private String lastName;

    @Length(max = 255)
    @Column(updatable = false)
    private String searchName;

    @Positive
    @NotNull
    private Integer age;

    public void calculateSearchName() {
        this.searchName = this.firstName + this.lastName;
    }
}


