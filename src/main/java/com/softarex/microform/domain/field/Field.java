package com.softarex.microform.domain.field;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(of = "id")
@ToString(of = { "id", "label" })
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
    private List<String> options = new ArrayList<>();

    public Field(@NonNull final Long id) {
        this();

        this.id = id;
    }

    public void setOptions(final Collection<String> options) {
        this.options = new ArrayList<>(options);
    }
}
