package com.example.accountbook.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.accountbook.Entity.Account;
import com.example.accountbook.Entity.Category;
import com.example.accountbook.Service.AccountService;
import com.example.accountbook.Service.CategoryService;

import jakarta.transaction.Transactional;

@Controller
public class AccountController {
  
    @Autowired
    private AccountService accountService;
    @Autowired
    private CategoryService categoryService;
    
    //ホーム画面(get)
    @RequestMapping("/")
    public ModelAndView index(@ModelAttribute Account account, ModelAndView mav) {
        mav.setViewName("index");
        addAccountAttributes(mav);
        return mav;
    }

    //ホーム画面（account追加）(post)
    @PostMapping
    @Transactional
    public ModelAndView form(@ModelAttribute @Validated Account account, BindingResult result, ModelAndView mav) {
        //空のmavを作成
        ModelAndView res;
        //エラーがなければ追加し、indexに戻る
        if (!result.hasErrors()) {
            accountService.saveAccount(account);
            res = new ModelAndView("redirect:/");
        //エラーがあればエラーメッセージを表示しindexに戻る
        } else {
            mav.setViewName("index");
            addAccountAttributes(mav);
            res = mav;
        }
        return res;
    }

    //アカウントの共通の属性を追加するメソッド
    private void addAccountAttributes(ModelAndView mav) {
        //全てのレコードをviewにわたす
        List<Account> list = accountService.getAllAccounts();
        mav.addObject("data", list);
        //全てのpriceの総額をviewに渡す
        int totalPrice = accountService.calculateTotalPrice();
        mav.addObject("totalPrice", totalPrice);
        //全てのexpenseの総額をviewに渡す
        int totalExpense = accountService.calculateTotalExpense();
        mav.addObject("totalExpense", totalExpense);
        //全てのincomeの総額をviewに渡す
        int totalIncome = accountService.calculateTotalIncome();
        mav.addObject("totalIncome", totalIncome);
        //カテゴリのリストをモデルに追加
        List<Category> categories = categoryService.getAllCategories();
        mav.addObject("categories", categories);
    }
    

    //データの編集(get)
    @GetMapping("/edit/{id}")
    public ModelAndView edit(@ModelAttribute Account Account, @PathVariable long id, ModelAndView mav) {
        //edit.htmlに遷移
        mav.setViewName("edit");
        //idに対応するAccountを取得し、formModelに渡す。（edit.htmlに渡す）
        Optional<Account> data = accountService.getAccountById(id);
        mav.addObject("account", data.get());
        //プルダウン用にカテゴリを渡す
        List<Category> categories = categoryService.getAllCategories();
        mav.addObject("categories", categories);
        return mav;
    }

    //データの編集(post)
    @PostMapping("/edit")
    @Transactional
    public ModelAndView update(@ModelAttribute Account Account, ModelAndView mav) {
        //accountを上書き保存し、index.htmlに遷移する
        accountService.saveAccount(Account);
        return new ModelAndView("redirect:/");
    }

    //データの削除(get)
    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable long id, ModelAndView mav) {
        //delete.htmlに遷移
        mav.setViewName("delete");
        //idに対応するAccountを取得し、formModelに渡す。(delete.htmlに渡す)
        Optional<Account> data = accountService.getAccountById(id);
        mav.addObject("account", data.get());
        return mav;
    }

    //データの削除(post)
    @PostMapping("/delete")
    @Transactional
    public ModelAndView remove(@RequestParam long id, ModelAndView mav) {
        //accountを上書き保存し、index.htmlに遷移する
        accountService.deleteAccount(id);
        return new ModelAndView("redirect:/");
    }
}

