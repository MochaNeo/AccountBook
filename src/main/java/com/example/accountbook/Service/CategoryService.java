package com.example.accountbook.Service;

import java.util.ArrayList;
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

    //全てのカテゴリーの名前を配列で返す
    public List<String> getAllCategoriesName() {
        return repository.findAllCategoryName();
    }

    //全ての支出のカテゴリーの名前を返す
    public String[] getAllExpenseCategoriesName() {
        //配列を作成
        List<String> CategoryNameList = new ArrayList<String>();
        List<Category> categories = repository.findByBalanceFalse();
        //配列にカテゴリー名を全て代入する
        for(int i = 0; i < categories.size(); i++) {
            CategoryNameList.add(categories.get(i).getCategoryName());
        }
        return CategoryNameList.toArray(new String [CategoryNameList.size()]);
	}

    //全ての収入のカテゴリーの名前を返す
    public String[] getAllIncomeCategoriesName() {
        //配列を作成
        List<String> CategoryNameList = new ArrayList<String>();
        List<Category> categories = repository.findByBalanceTrue();
        //配列にカテゴリー名を全て代入する
        for(int i = 0; i < categories.size(); i++) {
            CategoryNameList.add(categories.get(i).getCategoryName());
        }
        return CategoryNameList.toArray(new String [CategoryNameList.size()]);
	}

    //カテゴリーを保存
    @SuppressWarnings("null")
    public void saveCategory(Category category) {
        repository.saveAndFlush(category);
    }

    //カテゴリーを削除
    public void deleteCategory(long id) {
        repository.deleteById(id);
    }

    //カテゴリーのデフォルトデータを作成（テスト用）
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