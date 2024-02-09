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

    //全ての支出のカテゴリーを返す(balanceがfalseのもの)
    public List<Category> getAllExpenseCategories() {
        return repository.findByBalanceFalse();
    }

    //全ての収入のカテゴリーを返す(balanceがtrueのもの)
    public List<Category> getAllIncomeCategories() {
        return repository.findByBalanceTrue();
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
        eat.setBalance(false);
        repository.saveAndFlush(eat);
        //交通費
        Category traffic = new Category();
        traffic.setCategoryName("交通費");
        traffic.setBalance(false);
        repository.saveAndFlush(traffic);
        //日用品
        Category daily = new Category();
        daily.setCategoryName("日用品");
        daily.setBalance(false);
        repository.saveAndFlush(daily);
        //ファッション・美容
        Category fashion = new Category();
        fashion.setCategoryName("ファッション・美容");
        fashion.setBalance(false);
        repository.saveAndFlush(fashion);
        //住居・通信
        Category residence = new Category();
        residence.setCategoryName("住居・通信");
        residence.setBalance(false);
        repository.saveAndFlush(residence);
        //趣味・娯楽
        Category hobby = new Category();
        hobby.setCategoryName("趣味・娯楽");
        hobby.setBalance(false);
        repository.saveAndFlush(hobby);
        //交際費・会費
        Category Entertainment = new Category();
        Entertainment.setCategoryName("交際費・会費");
        Entertainment.setBalance(false);
        repository.saveAndFlush(Entertainment);
        //給料
        Category salary = new Category();
        salary.setCategoryName("給料");
        salary.setBalance(true);
        repository.saveAndFlush(salary);
        //副業
        Category sub = new Category();
        sub.setCategoryName("副業");
        sub.setBalance(true);
        repository.saveAndFlush(sub);
    }
}
