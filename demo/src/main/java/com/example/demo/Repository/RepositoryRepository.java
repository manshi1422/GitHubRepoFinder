package com.example.demo.Repository;

import com.example.demo.entities.RepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryRepository extends JpaRepository<RepositoryEntity, Long>, JpaSpecificationExecutor<RepositoryEntity> {
}
