package com.example.accountbook.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.accountbook.Entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c.categoryName FROM Category c")
    List<String> findAllCategoryName();

    //支出のレコードを全て検索する
    List<Category> findByBalanceFalse();

    //収入のレコードを全て検索する
    List<Category> findByBalanceTrue();

}
