package com.softarex.internship.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "usr")
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(
            message = "Email shouldn't be blank",
            groups  = Validation.Edit.class
    )
    @Email(
            message = "Email is invalid",
            groups  = Validation.Edit.class
    )
    private String email;

    @NotBlank(
            message = "Username shouldn't be blank",
            groups  = Validation.Create.class
    )
    @Size(
            min     = 2,
            max     = 63,
            message = "Username length should be from 2 to 63",
            groups  = Validation.Create.class
    )
    @Pattern(
            message = "Username should contain only letters of latin alphabet, numbers and '_' symbol",
            regexp  = "\\w+",
            groups  = Validation.Create.class
    )
    private String username;

    @JsonProperty(access = Access.WRITE_ONLY)
    @NotBlank(
            message = "Password shouldn't be blank",
            groups  = Validation.Create.class
    )
    @Size(
            min     = 6,
            max     = 255,
            message = "Password length should be from 6 to 255",
            groups  = Validation.Create.class
    )
    @Pattern(
            regexp  = "\\w+",
            message = "Password should contain only letters of latin alphabet, numbers and '_' symbol",
            groups  = Validation.Create.class
    )
    private String password;

    @Size(
            max     = 255,
            message = "First name length shouldn't be longer than 255",
            groups  = Validation.Edit.class
    )
    private String firstName;

    @Size(
            max     = 255,
            message = "Last name length shouldn't be longer than 255",
            groups  = Validation.Edit.class
    )
    private String lastName;

    @Pattern(
            regexp  = "\\d*",
            message = "Phone number is invalid",
            groups  = Validation.Edit.class
    )
    private String phoneNumber;
}
