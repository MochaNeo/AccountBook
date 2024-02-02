package com.example.accountbook.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.accountbook.Entity.Account;
import com.example.accountbook.Repository.AccountRepository;

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
}
