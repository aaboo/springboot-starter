package com.nicepay.starter.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value="/")
public class HomeController {

	@Autowired HomeService homeService;
	
	//thyemeleaf 템플릿 테스트
	@GetMapping("/")
	public String HomeControllerEngine1(final HttpServletRequest req, final Model model) {
		model.addAttribute("message", "World");
		return "index"; // /src/main/resoureces/template/index.html
		//return "login/login"; // /src/main/resoureces/template/login/login.html
	}
	
	/*
	//리다이렉트 포워딩 테스트
	@RequestMapping(value="/", method={RequestMethod.GET})
	public String HomeControllerEngine1(){
		return "forward:index.html";//sveltekit redirection
	}
	*/
	@GetMapping("/{reqPath}")
	@ResponseBody
	public String HomeControllerEngine2(@PathVariable("reqPath") String reqPath, HttpServletRequest req, HttpServletResponse res) throws Exception {
		switch(reqPath){
			case "rollbackTest1": return homeService.rollbackTest1();//rollback 테스트 1 - Oracle
			default: return "Page not found";
		}
	}
	@PostMapping("/{reqPath}")
	@ResponseBody
	public String HomeControllerEngine3(@PathVariable("reqPath") String reqPath, HttpServletRequest req, HttpServletResponse res) throws Exception {
		switch(reqPath){	
			case "home": return "Welcome Home!";//ResponseBody 테스트
			case "test": return homeService.test();//DB접속 테스트(Oracle)
			case "heartBeat": return homeService.heartBeat();//DB접속 테스트(Oracle)	
			case "testMysql": return homeService.testMysql();//DB접속 테스트(MySql)
			case "testEnv": return homeService.testEnv();//application.properties 파일 정보 읽기 테스트
			case "rollbackTest2": return homeService.rollbackTest2();//rollback 테스트 2 - Oracle
			case "rollbackTest3": return homeService.rollbackTest3();//rollback 테스트 3 - Oracle
			case "rollbackTest4": return homeService.rollbackTest4();//rollback 테스트 4 - MySql
			case "rollbackTest5": return homeService.rollbackTest5();//rollback 테스트 5 - Oracle, MySql 		
			default: return "Page not found";
		}
	}
	
}
