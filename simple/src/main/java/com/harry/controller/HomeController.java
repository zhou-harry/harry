package com.harry.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("index")
public class HomeController {
	private static Logger logger = Logger.getLogger(HomeController.class);

	@RequestMapping
	public String index(HttpServletRequest request) {
		return "main";
	}

}
