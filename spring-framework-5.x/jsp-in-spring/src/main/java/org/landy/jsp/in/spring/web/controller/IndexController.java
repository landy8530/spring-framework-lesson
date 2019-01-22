package org.landy.jsp.in.spring.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    //http://localhost:8080/index?message=landy
    @RequestMapping("index")
    public String index(@RequestParam(value = "message", required = false) String message, Model model) {

        System.out.println("jsp in spring demo");

        model.addAttribute("message",message);

        return "index";
    }

}
