package com.softarex.internship.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;

@Entity
@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ElementCollection
    @CollectionTable(
            name = "response_field",
            joinColumns = @JoinColumn(name = "response_id")
    )
    @MapKeyColumn(name = "field_id")
    @Column(name = "value")
    public Map<Long, String> body;
}
