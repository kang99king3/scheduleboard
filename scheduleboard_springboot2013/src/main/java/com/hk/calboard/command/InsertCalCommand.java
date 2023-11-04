package com.hk.calboard.command;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InsertCalCommand {
	private int seq;
	
	@NotBlank(message = "아이디를 입력하세요") //NotBlank: 문자열만 가능
	private String id;
	
	@NotNull(message = "년도를 입력하세요")  //NotNull: 문자열, 정수형 검증
	private int year;
	
	@NotNull(message = "달을 입력하세요")
	private int month;
	
	@NotNull(message = "일을 입력하세요")
	private int date;
	
	@NotNull(message = "시간을 입력하세요")
	private int hour;
	
	@NotNull(message = "분을 입력하세요")
	private int min;
	
	@NotBlank(message = "제목을 입력하세요")
	private String title;
	
	@NotBlank(message = "일정내용을 입력하세요")
	private String content;
	
	private String mdate;
	
	private Date regdate;
}
