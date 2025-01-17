package com.example.accountbook.Repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.accountbook.Entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    //カテゴリーが使われているか確認する
    boolean existsByCategoryId(long categoryId);

    //支出のレコードを全て検索する
    List<Account> findByBalanceFalse();

    //収入のレコードを全て検索する
    List<Account> findByBalanceTrue();

    //名前が含まれるレコードを検索する
    List<Account> findByNameContaining(String name);

    //カテゴリーと月でpriceの合計を返す
    @Query("SELECT SUM(a.price) FROM Account a WHERE a.category.categoryName = :name AND a.date BETWEEN :startDate AND :endDate")
    Integer findTotalPriceByCategoryAndDateRange(
            @Param("name") String name,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
}