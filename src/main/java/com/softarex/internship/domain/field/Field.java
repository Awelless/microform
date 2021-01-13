package com.softarex.internship.domain.field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(
            message = "Label shouldn't be empty"
    )
    @Size(
            max     = 255,
            message = "Label length shouldn't be longer than 255"
    )
    private String label;

    @Enumerated(EnumType.STRING)
    private FieldType type;

    @Column(name = "is_required")
    private boolean required;

    @Column(name = "is_active")
    private boolean active;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "field_option", joinColumns = @JoinColumn(name = "field_id"))
    @Column(name = "option")
    private Set<String> options = new HashSet<>();

    public void setOptions(final Set<String> options) {
        this.options = new HashSet<>(options);
    }
}
