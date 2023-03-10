package jp.co.seattle.library.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.BooksStatusService;

/**
 * 詳細表示コントローラー
 */
@Controller
public class DetailsController {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);

    @Autowired
    private BooksService booksService;
    @Autowired
    private BooksStatusService booksStatusService;

    /**
     * 詳細画面に遷移する
     * @param locale
     * @param bookId
     * @param model
     * @return
     */
    @Transactional
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public String detailsBook(Locale locale,
            @RequestParam("bookId") Integer bookId,
            @RequestParam(name = "isInsertSuccess", required = false) boolean isInsertSuccess,
            @RequestParam(name = "isEditSuccess", required = false) boolean isEditSuccess,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {
        // デバッグ用ログ
        logger.info("Welcome detailsControler.java! The client locale is {}.", locale);

        model.addAttribute("isInsertSuccess", isInsertSuccess);
        model.addAttribute("isEditSuccess", isEditSuccess);
        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
        model.addAttribute("latestBookStatusInfo", booksStatusService.getLatestBookStatusInfo(bookId));

        return "details";
    }
}
