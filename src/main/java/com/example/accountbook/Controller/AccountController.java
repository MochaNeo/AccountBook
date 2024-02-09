package com.example.accountbook.Controller;

import java.util.ArrayList;
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
    
    //ホーム画面(get)
    @RequestMapping("/")
    public ModelAndView index(@ModelAttribute Account account, ModelAndView mav) {
        mav.setViewName("index");
        addAccountAttributes(mav);
        return mav;
    }

    //account追加画面(get)
    @GetMapping("/add")
    public ModelAndView add(@ModelAttribute Account account, ModelAndView mav) {
        //add.htmlに遷移
        mav.setViewName("add");
        //全ての支出のカテゴリーのレコードをviewにわたす
        List<Category> expenseCategories = categoryService.getAllExpenseCategories();
        mav.addObject("expenseCategories", expenseCategories);
        //全ての収入のカテゴリーのレコードをviewにわたす
        List<Category> incomeCategories = categoryService.getAllIncomeCategories();
        mav.addObject("incomeCategories", incomeCategories);
        return mav;
	}
    
    // 支出カテゴリーのリストを返すエンドポイント
    @GetMapping("/api/categories/expense")
    public ResponseEntity<List<Category>> getAllExpenseCategories() {
        List<Category> expenseCategories = categoryService.getAllExpenseCategories();
        return ResponseEntity.ok(expenseCategories);
    }

    // 収入カテゴリーのリストを返すエンドポイント
    @GetMapping("/api/categories/income")
    public ResponseEntity<List<Category>> getAllIncomeCategories() {
        List<Category> incomeCategories = categoryService.getAllIncomeCategories();
        return ResponseEntity.ok(incomeCategories);
    }

    //account追加画面(post)
    @PostMapping("/add")
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
            mav.setViewName("add");
            //カテゴリのリストをモデルに追加
            List<Category> categories = categoryService.getAllCategories();
            mav.addObject("categories", categories);
            res = mav;
        }
        return res;
    }

    //アカウントの共通の属性を追加するメソッド
    private void addAccountAttributes(ModelAndView mav) {
        //全てのレコードをviewにわたす
        List<Account> list = accountService.getAllAccounts();
        mav.addObject("total", list);
        //全ての支出のレコードをviewにわたす
        List<Account> expenseList = accountService.getAllExpense();
        mav.addObject("expense", expenseList);
        //全ての収入のレコードをviewにわたす
        List<Account> incomeList = accountService.getAllIncome();
        mav.addObject("income", incomeList);
        
        //全てのカテゴリのレコードをviewにわたす
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
        //全ての支出のカテゴリーのレコードをviewにわたす
        List<Category> expenseCategories = categoryService.getAllExpenseCategories();
        mav.addObject("expenseCategories", expenseCategories);
        //全ての収入のカテゴリーのレコードをviewにわたす
        List<Category> incomeCategories = categoryService.getAllIncomeCategories();
        mav.addObject("incomeCategories", incomeCategories);
        // 現在選択されているカテゴリーIDをModelAndViewに追加
        mav.addObject("selectedCategoryId", data.get().getCategory().getId());
        mav.addObject("selectedBalanceType", data.get().isBalance());
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

    //データの削除(post)
    @PostMapping("/delete")
    @Transactional
    public ModelAndView remove(@RequestParam long id, ModelAndView mav) {
        //accountを上書き保存し、index.htmlに遷移する
        accountService.deleteAccount(id);
        return new ModelAndView("redirect:/");
    }
    
    //検索する(post)
    @PostMapping("/search")
    public ModelAndView search(@RequestParam("name") String name, ModelAndView mav) {
        mav.setViewName("search");
        List<Account> searchResult = searchService.search(name);
        mav.addObject("search", searchResult);
        // searchResultのレコードの件数を取得してModelAndViewオブジェクトに追加
        int count = searchResult.size();
        mav.addObject("count", count);
        // 文字列をそのまま返す
        mav.addObject("name", name);
        return mav;
    }

    //　統計用クラス(get)
    @GetMapping("/stat")
    public ModelAndView getstat(ModelAndView mav) {
        //支出用のカテゴリー配列を作成
        List<String> ExpenseCategoryList = new ArrayList<String>();
        //配列にカテゴリー名を全て代入する
        for(int i = 0; i < categoryService.getAllExpenseCategories().size(); i++) {
            ExpenseCategoryList.add(categoryService.getAllExpenseCategories().get(i).getCategoryName());
        }

        //支出のカテゴリごとの合計のpriceを入れる配列を作成
        List<Integer> ExpenseCategoryTotal =  new ArrayList<Integer>();
        //配列にカテゴリーごとのpriceの値を代入する
        for(int i = 0; i < categoryService.getAllExpenseCategories().size(); i++) {
            ExpenseCategoryTotal.add(0 + accountService.CategoryTotalPrice(ExpenseCategoryList.get(i)));
        }


        //収入用のカテゴリー配列を作成
        List<String> IncomeCategoryList = new ArrayList<String>();
        //配列にカテゴリー名を全て代入する
        for(int i = 0; i < categoryService.getAllIncomeCategories().size(); i++) {
            IncomeCategoryList.add(categoryService.getAllIncomeCategories().get(i).getCategoryName());
        }

        //収入のカテゴリごとの合計のpriceを入れる配列を作成
        List<Integer> IncomeCategoryTotal = new ArrayList<Integer>();
        //配列にカテゴリーごとのpriceの値を代入する
        for(int i = 0; i < categoryService.getAllIncomeCategories().size(); i++) {
            IncomeCategoryTotal.add(0 + accountService.CategoryTotalPrice(IncomeCategoryList.get(i)));
        }

        //ExoenseListを配列に変換
        String ExpenseCategoryListLabel[] = ExpenseCategoryList.toArray(new String [ExpenseCategoryList.size()]);
        //Expensetotalを配列に変換
        Integer ExpenseCategoryTotalLabel[] = ExpenseCategoryTotal.toArray(new Integer [ExpenseCategoryTotal.size()]);
        //IncomeListを配列に変換
        String IncomeCategoryListLabel[] = IncomeCategoryList.toArray(new String [IncomeCategoryList.size()]);
        //Incometotalを配列に変換
        Integer IncomeCategoryTotalLabel[] = IncomeCategoryTotal.toArray(new Integer [IncomeCategoryTotal.size()]);
        //モデルに追加
        mav.addObject("ExpenseCategoryList", ExpenseCategoryListLabel);
        mav.addObject("ExpenseCategoryTotal", ExpenseCategoryTotalLabel);
        mav.addObject("IncomeCategoryList", IncomeCategoryListLabel);
        mav.addObject("IncomeCategoryTotal", IncomeCategoryTotalLabel);
        mav.setViewName("stat");
        return mav;
    }
    
    
}