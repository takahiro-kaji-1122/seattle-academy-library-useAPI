package jp.co.seattle.library.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.service.BooksService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class HomeController {
    final static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private BooksService booksService;

    /**
     * Homeボタンからホーム画面に戻るページ
     * @param model
     * @return ホーム画面
     */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String transitionHome(Model model,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpServletResponse response) {
        List<BookInfo> bookList;
        //セッションからユーザー名を取得する
        HttpSession session = request.getSession(false);

        //セッションがない場合
        if (session == null) {

            //ログイン画面に遷移する
            return "redirect:/";
        }

        //セッションからユーザ名を取得する
        String username = (String) session.getAttribute("username");
        //書籍情報が0件の場合
        try {
            bookList = booksService.getBookList();
        } catch (Exception e) {
            model.addAttribute("unknownError", true);
            return "home";
        }
        if (CollectionUtils.isEmpty(bookList)) {
            model.addAttribute("username", username);
            model.addAttribute("notExistBookData", true);
            return "home";
        }
        model.addAttribute("username", username);
        model.addAttribute("bookList", bookList);
        return "home";
    }

    /**
     * 書籍検索
     *  @param model
     * @return ホーム画面
     */
    @RequestMapping(value = "/searchBook", method = RequestMethod.GET)
    public String transitionHome(@RequestParam("searchedBookName") String searchedBookName,
            Model model) {
        List<BookInfo> searchedBookList;

        try {
            //検索対象の書籍情報をbooksテーブルから取得
            searchedBookList = booksService.getSearchedBookList(searchedBookName);
        } catch (Exception e) {
            model.addAttribute("unknownError", true);
            return "home";
        }
        //書籍情報が0件の場合
        if (CollectionUtils.isEmpty(searchedBookList)) {
            model.addAttribute("notExistBookData", true);
            return "home";
        }
        model.addAttribute("bookList", searchedBookList);
        return "home";
    }

}
