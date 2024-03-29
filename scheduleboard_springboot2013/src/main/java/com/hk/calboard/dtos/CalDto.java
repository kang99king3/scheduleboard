package com.hk.calboard.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class CalDto {

	private int seq;
	@NonNull
	private String id;
	@NonNull
	private String title;
	private String content;
	private String mdate;
	private Date regdate;
}
