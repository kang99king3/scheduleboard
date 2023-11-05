package com.hk.calboard.command;

import jakarta.validation.constraints.NotEmpty;
//import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteCalCommand {
	//Null이거나 길이가 0인 경우
	@NotEmpty(message = "삭제하시려면 최소 하나이상 체크해야 합니다.")
	private String[] seq;
}
