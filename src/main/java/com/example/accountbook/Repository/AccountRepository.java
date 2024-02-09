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

    //カテゴリーごとのpriceの合計を返す
    @Query("SELECT SUM(a.price) FROM Account a WHERE a.category.categoryName = :name")
    Integer findTotalPriceByCategoryContaining(String name);
    
    //カテゴリーごとの月のpriceの総額を返す
    @Query("SELECT a.category as category, SUM(a.price) as total FROM Account a " +
            "WHERE a.date BETWEEN :startDate AND :endDate " +
            "AND a.balance = :balance " +
            "GROUP BY a.category")
    List<Object[]> findTotalsByCategoryAndDateRangeAndBalance(
            @Param("startDate") Date startDate, 
            @Param("endDate") Date endDate,
            @Param("balance") boolean balance);

}