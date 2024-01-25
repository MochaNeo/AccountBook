package com.example.accountbook.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.accountbook.Entity.Category;
import com.example.accountbook.Service.AccountService;
import com.example.accountbook.Service.CategoryService;

import jakarta.transaction.Transactional;

@Controller
public class CategoryController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private CategoryService categoryService;

    //カテゴリー(get)
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public ModelAndView categoryGet(@ModelAttribute Category category, ModelAndView mav) {
        mav.setViewName("category");
        addCategoryAttributes(mav);
        return mav;
    }
    
    //カテゴリー追加(post)
    @PostMapping("/category")
    @Transactional
    public ModelAndView categoryForm(@ModelAttribute @Validated Category category, BindingResult result, ModelAndView mav) {
        //空のmavを作成
        ModelAndView res;
        //エラーがなければ追加し、indexに戻る
        if (!result.hasErrors()) {
            categoryService.saveCategory(category);
            res = new ModelAndView("redirect:/category");
        //エラーがあればエラーメッセージを表示しcategoryに戻る
        } else {
            mav.setViewName("category");
            addCategoryAttributes(mav);
            res = mav;
        }
        return res;
    }

    //カテゴリーに共通するメソッド
    private void addCategoryAttributes(ModelAndView mav) {
        // 全てのカテゴリーをviewにわたす
        List<Category> list = categoryService.getAllCategories();
        mav.addObject("data", list);
    }

    //カテゴリーの削除(post)
    @PostMapping("/categoryDelete/{id}")
    @Transactional
    public ModelAndView deleteCategory(@ModelAttribute Category category, @RequestParam long id, ModelAndView mav) {
        //空のmavを作成
        ModelAndView res;
        //カテゴリーが使用されているかチェックし、isCategoryInUseに入れる
        boolean isCategoryInUse = accountService.isCategoryInUse(id);
        //カテゴリーが使用されていればエラーメッセージを表示し、categoryに戻る
        if (isCategoryInUse) {
            mav.addObject("errorMessage", "カテゴリーは使用中です。");
            mav.setViewName("category");
            addCategoryAttributes(mav);
            res = mav;
        //カテゴリーが使用されていなければ削除し、categoryに戻る
        } else {
            categoryService.deleteCategory(id);
            res = new ModelAndView("redirect:/category");
        }
        return res;  
    }
}
