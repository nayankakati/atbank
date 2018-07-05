package com.awantunai.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HtmlController {

	@GetMapping("/")
	public String scan() {
		return "welcome";
	}

	@GetMapping("/details")
	public String detail(@RequestParam String accountId, Model model) {
		model.addAttribute("accountId", accountId);
		return "detail";
	}

	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}
}
