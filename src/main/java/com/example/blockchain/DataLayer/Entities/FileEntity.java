package com.example.blockchain.DataLayer.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.id.factory.spi.GenerationTypeStrategy;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileEntity {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private UUID fileIdentifier;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] fileData;
}

