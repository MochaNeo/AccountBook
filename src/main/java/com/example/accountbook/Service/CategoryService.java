package com.example.accountbook.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.accountbook.Entity.Account;
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

    //すべてのカテゴリーの名前を配列で返す
    public List<String> getAllCategoriesName() {
        return repository.findAllCategoryName();
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
        //食費
        Category eat = new Category();
        eat.setCategoryName("食費");
        repository.saveAndFlush(eat);
        //交通費
        Category traffic = new Category();
        traffic.setCategoryName("交通費");
        repository.saveAndFlush(traffic);
        //日用品
        Category daily = new Category();
        daily.setCategoryName("日用品");
        repository.saveAndFlush(daily);
        //ファッション・美容
        Category fashion = new Category();
        fashion.setCategoryName("ファッション・美容");
        repository.saveAndFlush(fashion);
        //住居・通信
        Category residence = new Category();
        residence.setCategoryName("住居・通信");
        repository.saveAndFlush(residence);
        //趣味・娯楽
        Category hobby = new Category();
        hobby.setCategoryName("趣味・娯楽");
        repository.saveAndFlush(hobby);
        //交際費・会費
        Category money = new Category();
        money.setCategoryName("交際費・会費");
        repository.saveAndFlush(money);
        //その他
        Category other = new Category();
        other.setCategoryName("その他");
        repository.saveAndFlush(other);
    }
}
