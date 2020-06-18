package com.dkm.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author qf
 * @date 2020/6/14
 * @vesion 1.0
 **/
@Controller
public class PageController {


   @GetMapping("/toPage/{page}")
   public String page (@PathVariable("page") String page) {
      return page;
   }
}
