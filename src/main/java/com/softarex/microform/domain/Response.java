package com.softarex.microform.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Map;

@Entity
@Data
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "response_field",
            joinColumns = @JoinColumn(name = "response_id")
    )
    @MapKeyColumn(name = "field_id")
    @Column(name = "value")
    private Map<Long, String> body;
}
