package com.example.accountbook.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.example.accountbook.Service.SearchService;

import jakarta.transaction.Transactional;

@Controller
public class AccountController {
  
    @Autowired
    private AccountService accountService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SearchService searchService;
    
    //index(get)
    @RequestMapping("/")
    public ModelAndView index(@ModelAttribute Account account, ModelAndView mav) {
        mav.setViewName("index");
        addAccountAttributes(mav);
        return mav;
    }

    //add(get)
    @GetMapping("/add")
    public ModelAndView add(@ModelAttribute Account account, ModelAndView mav) {
        mav.setViewName("add");
        return mav;
	}

    //add(post)
    @PostMapping("/add")
    @Transactional
    public ModelAndView form(@ModelAttribute @Validated Account account, BindingResult result, ModelAndView mav) {
        //空のmavを作成
        ModelAndView res;
        //エラーがなければ追加し、indexに戻る
        if (!result.hasErrors()) {
            accountService.saveAccount(account);
            res = new ModelAndView("redirect:/");
        //エラーがあればaddに戻る
        } else {
            mav.setViewName("add");
            res = mav;
        }
        return res;
    }
    
    //edit(get)
    @GetMapping("/edit/{id}")
    public ModelAndView edit(@ModelAttribute Account Account, @PathVariable long id, ModelAndView mav) {
        //edit.htmlに遷移
        mav.setViewName("edit");
        //idに対応するAccountを取得、editに渡す）
        Optional<Account> data = accountService.getAccountById(id);
        mav.addObject("account", data.get());
        // 現在選択されているカテゴリーIDをModelAndViewに追加
        mav.addObject("selectedCategoryId", data.get().getCategory().getId());
        mav.addObject("selectedBalanceType", data.get().isBalance());
        return mav;
    }

    //edit(post)
    @PostMapping("/edit")
    @Transactional
    public ModelAndView update(@ModelAttribute Account Account, ModelAndView mav) {
        //accountを編集、indexに遷移する
        accountService.saveAccount(Account);
        return new ModelAndView("redirect:/");
    }

    //delete(post)
    @PostMapping("/delete")
    @Transactional
    public ModelAndView remove(@RequestParam long id, ModelAndView mav) {
        //accountを削除、indexに遷移する
        accountService.deleteAccount(id);
        return new ModelAndView("redirect:/");
    }
    
    //search(post)
    @PostMapping("/search")
    public ModelAndView search(@RequestParam("name") String name, ModelAndView mav) {
        mav.setViewName("search");
        List<Account> searchResult = searchService.search(name);
        mav.addObject("search", searchResult);
        // searchResultのレコードの件数を取得してmavに追加
        mav.addObject("count", searchResult.size());
        // 文字列をそのまま返す
        mav.addObject("name", name);
        return mav;
    }

    //stat(get)
    @GetMapping("/stat")
    public ModelAndView getStat(ModelAndView mav) {
        //カテゴリー名の配列をmavに追加
        mav.addObject("ExpenseCategoryList", categoryService.getAllExpenseCategoriesName());
        mav.addObject("IncomeCategoryList", categoryService.getAllIncomeCategoriesName());
        mav.setViewName("stat");
        return mav;
    }



    //アカウントの共通の属性を追加するメソッド
    private void addAccountAttributes(ModelAndView mav) {
        //全てのレコードをviewにわたす
        mav.addObject("total", accountService.getAllAccounts());
        //全ての支出のレコードをviewにわたす
        mav.addObject("expense", accountService.getAllExpense());
        //全ての収入のレコードをviewにわたす
        mav.addObject("income", accountService.getAllIncome());
    }
    
    
    
    //addメソッド用
    // 支出カテゴリーリストを返すエンドポイント
    @GetMapping("/api/categories/expense")
    public ResponseEntity<List<Category>> getAllExpenseCategories() {
        return ResponseEntity.ok(categoryService.getAllExpenseCategories());
    }

    // 収入カテゴリーリストを返すエンドポイント
    @GetMapping("/api/categories/income")
    public ResponseEntity<List<Category>> getAllIncomeCategories() {
        return ResponseEntity.ok(categoryService.getAllIncomeCategories());
    }

    
    
    //statメソッド用
    // 日付を指定して支出データを取得するエンドポイント
    @GetMapping("/api/categories/price/expense")
    public ResponseEntity<Integer[]> getExpenseDataByDate(@RequestParam("date") String date) {
        int year = accountService.parsedDateYear(date);
        int month = accountService.parsedDateMonth(date);
        return accountService.findTotalPriceForMonth(categoryService.getAllExpenseCategories(), year, month);
    }

    // 日付を指定して収入データを取得するエンドポイント
    @GetMapping("/api/categories/price/income")
    public ResponseEntity<Integer[]> getIncomeDataByDate(@RequestParam("date") String date) {
        int year = accountService.parsedDateYear(date);
        int month = accountService.parsedDateYear(date);
        return accountService.findTotalPriceForMonth(categoryService.getAllIncomeCategories(), year, month);
    }

}