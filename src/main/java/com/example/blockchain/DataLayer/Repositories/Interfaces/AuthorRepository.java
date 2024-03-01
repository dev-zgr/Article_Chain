package com.example.blockchain.DataLayer.Repositories.Interfaces;

import com.example.blockchain.DataLayer.Entities.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository  extends JpaRepository<AuthorEntity, Long> {
}
