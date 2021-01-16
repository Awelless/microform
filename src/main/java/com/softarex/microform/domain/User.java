package com.softarex.microform.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;

@Entity
@Table(name = "usr")
@Data
@EqualsAndHashCode(of = "id")
@ToString(of = { "id", "email" })
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(
            message = "Email shouldn't be blank",
            groups  = Validation.Create.class
    )
    @Email(
            message = "Email is invalid",
            groups  = Validation.Create.class
    )
    private String email;

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
            regexp  = "^\\w{6,255}$",
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
            regexp  = "^(\\d{12}|)$",
            message = "Phone number is invalid",
            groups  = Validation.Edit.class
    )
    private String phoneNumber;

    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
