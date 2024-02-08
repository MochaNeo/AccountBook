package com.example.accountbook.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.accountbook.Entity.Account;
import com.example.accountbook.Repository.AccountRepository;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class SearchService {

    @Autowired
    AccountRepository repository;
    
	private String title;

    //検索処理の実行
	public List<Account> search(String name) {
        return repository.findByNameContaining(name);
	}
}