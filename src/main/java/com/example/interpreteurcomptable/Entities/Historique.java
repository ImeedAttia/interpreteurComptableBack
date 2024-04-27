package com.example.interpreteurcomptable.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Historique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String titre;
    Date createdAt;
    Date updatedAt;
    @ManyToOne
    FileEntity inputFile;
    @ManyToOne
    FileEntity outputFile;
    @ManyToOne(cascade = CascadeType.ALL)
    TVA tva;
    @ManyToOne(cascade = CascadeType.ALL)
    CVAE cvae;
    @ManyToOne(cascade = CascadeType.ALL)
    CFE cfe;

}
