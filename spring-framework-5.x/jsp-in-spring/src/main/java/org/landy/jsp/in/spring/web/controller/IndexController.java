package org.landy.jsp.in.spring.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Index Controller(Application Controller)
 *
 * @author Landy
 * @copyright Landy
 * @since 2018/1/10
 */
@Controller
public class IndexController {

    //在早点版本中，是返回ModelAndView对象
    @RequestMapping("index")
    public String index(Model model) {

        System.out.println("jsp in spring demo");

        return "index";
    }

}
