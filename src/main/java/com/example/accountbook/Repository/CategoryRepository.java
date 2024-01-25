package com.example.accountbook.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.accountbook.Entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}
