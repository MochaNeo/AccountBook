package com.example.accountbook.Service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.accountbook.Entity.Account;
import com.example.accountbook.Entity.Category;
import com.example.accountbook.Repository.AccountRepository;
import org.springframework.http.ResponseEntity;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository repository;

    //全てのレコードを返す
    public List<Account> getAllAccounts() {
        return repository.findAll();
    }
        
    //全ての支出のレコードを返す(balanceがfalseのもの)
    public List<Account> getAllExpense() {
        return repository.findByBalanceFalse();
    }

    //全ての収入のレコードを返す(balanceがtrueのもの)
    public List<Account> getAllIncome() {
        return repository.findByBalanceTrue();
    }

    //アカウントを保存する
    @SuppressWarnings("null")
    public void saveAccount(Account account) {
        repository.saveAndFlush(account);
    }

    //idに該当するアカウントを返す
    public Optional<Account> getAccountById(long id) {
        return repository.findById(id);
    }

    //idに該当するアカウントを削除する(データベースから削除)
    public void deleteAccount(long id) {
        repository.deleteById(id);
    }

    //カテゴリーが使われているかどうか確認する(true:使われている false:使われていない)
    public boolean isCategoryInUse(long categoryId) {
        return repository.existsByCategoryId(categoryId);
    }

    //特定の年と月における特定のカテゴリーのpriceの合計を取得
    public Integer findTotalPriceForCategoryInMonth(String categoryName, int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1); //月の初日
        Date startDate = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1); //月の最終日
        Date endDate = calendar.getTime();

        //カテゴリーごとの月のpriceの総額を返す
        return repository.findTotalPriceByCategoryAndDateRange(categoryName, startDate, endDate);
    }

    //dateから年を取り出す
    public int parsedDateYear(String date) {
        YearMonth parsedDate;
        //年月をパースするためのフォーマッターを定義
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        //受け取ったdateパラメータからYearMonthを生成
        parsedDate = YearMonth.parse(date, formatter);
        //年を返す
        return parsedDate.getYear();
    }

    //dateから月を取り出す
    public int parsedDateMonth(String date) {
        YearMonth parsedDate;
        // 年月をパースするためのフォーマッターを定義
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        // 受け取ったdateパラメータからYearMonthを生成
        parsedDate = YearMonth.parse(date, formatter);
        //年を返す
        return parsedDate.getMonthValue();
    }

    //該当月の合計の支出・収入額を調べる
    public ResponseEntity<Integer[]> findTotalPriceForMonth(List<Category> categories, int year, int month) {
        //配列を作成
        List<String> CategoryList = new ArrayList<String>();
        //配列にカテゴリー名をすべて代入する
        for(int i = 0; i < categories.size(); i++) {
            CategoryList.add(categories.get(i).getCategoryName());
        }
        //カテゴリごとの合計のpriceを入れる配列を作成
        List<Integer> CategoryTotal = new ArrayList<Integer>();
        //配列にカテゴリごとのpriceの値を代入する
        for(int i = 0; i < CategoryList.size(); i++) {
            CategoryTotal.add(findTotalPriceForCategoryInMonth(CategoryList.get(i), year, month));
        }
        Integer CategoryTotalLabel[] = CategoryTotal.toArray(new Integer [CategoryTotal.size()]);
        return ResponseEntity.ok(CategoryTotalLabel);
    }
    
}