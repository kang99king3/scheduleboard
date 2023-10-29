package com.hk.calboard.command;

import java.util.Date;

import lombok.Data;

@Data
public class InsertCalCommand {
	private int seq;
	private String id;
	private String year;
	private String month;
	private String date;
	private String hour;
	private String min;
	private String title;
	private String content;
	private String mdate;
	private Date regdate;
}
