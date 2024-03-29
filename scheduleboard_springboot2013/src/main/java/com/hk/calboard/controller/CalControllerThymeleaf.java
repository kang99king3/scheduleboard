package com.hk.calboard.controller;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hk.calboard.command.DeleteCalCommand;
import com.hk.calboard.command.InsertCalCommand;
import com.hk.calboard.command.UpdateCalCommand;
import com.hk.calboard.dtos.CalDto;
import com.hk.calboard.service.ICalService;
import com.hk.calboard.utils.Util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/schedule")
public class CalControllerThymeleaf {
	//log를 원하는 위치에 설정하여 디버깅하기 위함
	private static final Logger logger=LoggerFactory.getLogger(CalController.class);
//			@Autowired
//			private TestController testController;
	@Autowired
	private ICalService calService;
	
	@GetMapping(value="/calendar")
	public String calendar(String year,String month, Locale locale, Model model,HttpServletRequest request) {
		logger.info("달력보기{}",locale);
//				Map<String, Integer>map=testController.makeCalendar(request);
//				model.addAttribute("calMap", map);
		
		if(year==null||month==null) {
			Calendar cal=Calendar.getInstance();
			year=cal.get(Calendar.YEAR)+"";
			month=(cal.get(Calendar.MONTH)+1)+"";
		}
		String id="kbj";//나중에는 세션에서 로그인한 아이디로 쓰자
		String yyyyMM=year+Util.isTwo(month);//"202304" 6자리
		List<CalDto> clist = calService.CalViewList(id, yyyyMM);
		model.addAttribute("clist", clist);
	
		Map<String,Integer>calMap=calService.makeCalendar(request);
		model.addAttribute("calMap", calMap);
		return "thymeleaf/calboard/calendar";//redirect가 아니죠?? forward 방식으로 응답
	}
	
	@GetMapping(value="/addCalBoardForm")
	public String addCalForm(Locale locale, Model model,InsertCalCommand insertCalCommand) {
		logger.info("일정 추가폼으로 이동{}",locale);
		System.out.println("year:"+insertCalCommand.getYear());
		model.addAttribute("insertCalCommand", insertCalCommand);
		return "thymeleaf/calboard/addCalBoardForm";
	}
	
	@PostMapping(value="/addCalBoard")
	public String addCalBoard(@Validated InsertCalCommand insertCalCommand
			                  ,BindingResult result, Locale locale
			                 ) throws Exception {
		logger.info("일정작성하기{}",locale);
		if(result.hasErrors()) {
			System.out.println("글을 모두 입력하세요");
			return "thymeleaf/calboard/addCalBoardForm";
		}
		System.out.println("글입력 직전");
		calService.insertCalBoard(insertCalCommand);

		return "redirect:/schedule/calendar";			
	}
	
	@GetMapping(value="/calBoardList")
	public String calBoardList(Locale locale,
							   HttpServletRequest request,
							   Model model,
							   @RequestParam Map<String, String> map) {
		logger.info("일정 목록보기 이동{}",locale);
		//일정관리에서 id는 로그인 한 아이디를 사용해야 하기 때문에
//				HttpSession session=request.getSession();//session객체 구하기
//				String id=(String)session.getAttribute("id");//id는 session에서 가져오기
		System.out.println("year:"+map.get("year"));
		String id="kbj";
		
		model.addAttribute("deleteCalCommand", new DeleteCalCommand()); 
		
		//일정목록 조회할 때마다 필요로 하는 year, month, date를 세션에 담아서 관리
		HttpSession session=request.getSession();
		
		if(map.get("year")==null) {
			map=(Map<String,String>)session.getAttribute("ymdMap");
		}else {			
			session.setAttribute("ymdMap", map);
		}
		
		String yyyyMMdd=map.get("year")
					   +Util.isTwo(map.get("month")) 
					   +Util.isTwo(map.get("date"));//8자리 만들기
		
		List<CalDto>list=calService.calBoardList(id, yyyyMMdd);
		model.addAttribute("list", list);
		return "thymeleaf/calboard/calBoardList";
	}
	
	@GetMapping(value="/calBoardDetail")
	public String calBoardDetail(UpdateCalCommand updateCalCommand,int seq, Model model) {
		logger.info("일정상세보기");
		CalDto dto=calService.calBoardDetail(seq);
		//dto --> command
		updateCalCommand.setSeq(dto.getSeq());
		updateCalCommand.setId(dto.getId());
		updateCalCommand.setTitle(dto.getTitle());
		updateCalCommand.setContent(dto.getContent());
		updateCalCommand.setYear(Integer.parseInt(dto.getMdate().substring(0, 4)));
		updateCalCommand.setMonth(Integer.parseInt(dto.getMdate().substring(4, 6)));
		updateCalCommand.setDate(Integer.parseInt(dto.getMdate().substring(6, 8)));
		updateCalCommand.setHour(Integer.parseInt(dto.getMdate().substring(8, 10)));
		updateCalCommand.setMin(Integer.parseInt(dto.getMdate().substring(10)));
		
		model.addAttribute("updateCalCommand",updateCalCommand);
//				model.addAttribute("dto", dto);
		return "thymeleaf/calboard/calBoardDetail";
	}

//			@GetMapping(value="calBoardUpdateform.do")
//			public String calBoardUpdateform(int seq, Model model) {
//				logger.info("일정 수정폼이동");
//				CalDto dto=calService.calBoardDetail(seq);
//				model.addAttribute("dto", dto);
//				return "calBoardUpdate";
//			}
//			
	@PostMapping(value="/calBoardUpdate")
	public String calBoardUpdate(@Validated UpdateCalCommand updateCalCommand,
								 BindingResult result,
			                     Model model) {
		logger.info("일정 수정하기");
		if(result.hasErrors()) {
			System.out.println("수정할 목록을 모두 입력하세요");
			return "thymeleaf/calboard/calBoardDetail";
		}
		
	
		boolean isS=calService.calBoardUpdate(updateCalCommand);
		
		
		return "redirect:/schedule/calBoardDetail?seq="+updateCalCommand.getSeq();
	}
	
	@RequestMapping(value = "/calMulDel",method = {RequestMethod.POST})
	public String calMulDel(@Validated DeleteCalCommand deleteCalCommand
			  			   , BindingResult result
			  			   ,HttpServletRequest request
			  			   ,Model model) {
//				System.out.println(deleteCalCommand.getSeq());
		if(result.hasErrors()) {
			System.out.println("최소 하나 이상 체크하세요");
			
			//일정목록으로 돌아갈때 목록도 함께 보내야 되기 때문에 
			//목록 list를 구해서 가야된다.--> redirect로 가면 command 연동이 안됨
			HttpSession session=request.getSession();
			String id="kbj";
			Map<String,String>map=(Map<String,String>)session.getAttribute("ymdMap");
		
			String yyyyMMdd=map.get("year")
						   +Util.isTwo(map.get("month")) 
						   +Util.isTwo(map.get("date"));//8자리 만들기
			
			List<CalDto>list=calService.calBoardList(id, yyyyMMdd);
			model.addAttribute("list", list);
			return "thymeleaf/calboard/calBoardList";
		}
		
		try {
			boolean isS=calService.calMulDel(deleteCalCommand.getSeq());
		} catch (Exception e) {
			e.printStackTrace();
		}
//				return "redirect:calBoardList.do?year="+map.get("year")
//												+"&month="+map.get("month")
//												+"&date="+map.get("date");
		return "redirect:/schedule/calBoardList";
	}
	
	@GetMapping(value =  "/calDel")
	public String calDel(String [] seq) {
		calService.calMulDel(seq);
		return "redirect:/schedule/calBoardList";
	}
//			
////			public String ---> 값 하나 (text)로 반환 작성
////			public Map<String,Integer..String...> -> json로 반환 작성
	@ResponseBody  // --> printWriter pw=response.getWrite()
	@GetMapping(value="calCountAjax.do")
	public Map<String,Integer> calCountAjax(String yyyyMMdd){
		logger.info("일정개수보여주기");
		Map<String,Integer> map=new HashMap<>();
		String id="kbj";
		int count=calService.calBoardCount(id, yyyyMMdd);
		map.put("count", count);
		return map;
	}
			
}
