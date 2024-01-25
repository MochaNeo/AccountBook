package com.example.accountbook.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.accountbook.Entity.Category;
import com.example.accountbook.Repository.CategoryRepository;

import jakarta.annotation.PostConstruct;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    //全てのカテゴリーを返す
    public List<Category> getAllCategories() {
        return repository.findAll();
    }

    //カテゴリーを保存する(データベースに保存)
    public void saveCategory(Category category) {
        repository.saveAndFlush(category);
    }

    //idに該当するカテゴリーを削除する
    public void deleteCategory(long id) {
        repository.deleteById(id);
    }

    //カテゴリーのデフォルトデータを作成する(データベースに保存)
    @PostConstruct
    public void init() {
        Category initial = new Category();
        initial.setCategoryName("-");
        repository.saveAndFlush(initial);
    }
}
