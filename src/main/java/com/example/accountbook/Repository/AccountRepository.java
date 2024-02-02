package com.example.accountbook.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
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
}
