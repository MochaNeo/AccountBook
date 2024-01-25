package com.example.accountbook.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.accountbook.Entity.Account;

import io.micrometer.common.lang.Nullable;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    //合計金額の計算用JPA
    @Query("SELECT COALESCE(SUM(CASE WHEN a.balance = true THEN a.price ELSE -a.price END), 0) FROM Account a")
    @Nullable
    Integer getTotalPrice();

    //収入の合計金額を返す(nullの場合は0を返す)
    @Query("SELECT COALESCE(SUM(a.price), 0) FROM Account a WHERE a.balance = true")
    @Nullable
    Integer getIncomeTotal();

    //支出の合計金額を返す(nullの場合は0を返す)
    @Query("SELECT COALESCE(SUM(a.price), 0) FROM Account a WHERE a.balance = false")
    @Nullable
    Integer getExpenseTotal();

    //カテゴリーが使われているか確認する
    boolean existsByCategoryId(long categoryId);
}
