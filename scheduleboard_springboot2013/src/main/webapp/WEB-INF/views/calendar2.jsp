<%@page import="java.util.Calendar"%>
<%@page import="com.hk.calboard.utils.Util"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("utf-8"); %>    
<%response.setContentType("text/html; charset=utf-8"); %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<jsp:include page="header.jsp"/>
<style type="text/css">
	#calendar td > p {
		margin-bottom: 5px ;
		background-color: 	#28A8A8;
		font-size: 7px;
		color:white;
		font-weight: bold;
		padding-left: 5px;
	}
	#calendar td{
		height: 115px;
		position: relative;
 		width:200px; 
		
	}
	.pen{width: 20px; height: 20px;}
	.d{font-size: 15px; font-family: bold;}
	
	.cp{
		position: absolute;
		left:20px;
		top:-30px;
		width: 40px;
		height: 40px;
		border-radius: 20px 20px 20px 1px;
		background-color: pink;
		line-height: 40px;
		text-align: center;
		font-weight: bold;
	}
</style>
<script type="text/javascript">
	function isTwo(str){
		return str.length<2?"0"+str:str;
	}

	$(function(){    //--> jquery onload 실행
		$(".d").hover(function(){
			//controller쪽으로 전달할 파라미터 yyyyMMdd를 구하자
			var aDate=$(this);//현재 마우스가 올라간, 일을 나타내는 a태그
			var year=$(".y").text().trim();//년   <a>2023 </a>
			var month=$(".m").text().trim();//월
			var date=aDate.text().trim();//일
			var yyyyMMdd=year+isTwo(month)+isTwo(date);
// 			alert(yyyyMMdd);
			if(aDate.nextAll("p").length>0){
				$.ajax({
					method:"get", //전송방식
					url:"calCountAjax.do", //요청 url
					data:{"yyyyMMdd":yyyyMMdd}, //서버로 보낼 값
					dataType:"json",  //서버에서 받을 데이터의 타입
					async:false,   //ajax()메서드가 비동기로 실행하는것을 막음:false설정
					success:function(data){ //서버와 통신 성공했다면 기능을 실행하자
	// 					alert(data.count);// data["count"]이렇게도 쓰고...
						aDate.after("<div class='cp'>"+data.count+"</div>");						
					}
				});
			}
		},function(){
			$(".cp").remove();
		});
	
	})
</script>
</head>
<body>
<div id="container">
<h1>일정관리2[달력보기]</h1>
<table class="table" id="calendar">
	<caption style="text-align: center; font-size: 30px;">
		<a href="calendar.do?year=${calMap.year-1}&month=${calMap.month}">◁</a>
		<a href="calendar.do?year=${calMap.year}&month=${calMap.month-1}">◀</a>
		<span class="y">${calMap.year}</span>
		<span>년</span>
		<span class="m">${calMap.month}</span>
		<span>월</span>
		<a href="calendar.do?year=${calMap.year}&month=${calMap.month+1}">▶</a>
		<a href="calendar.do?year=${calMap.year+1}&month=${calMap.month}">▷</a>
	</caption>
	<tr>
		<th>일</th>
		<th>월</th>
		<th>화</th>
		<th>수</th>
		<th>목</th>
		<th>금</th>
		<th>토</th>
	</tr>
	<tr>
		<c:forEach begin="1" end="${calMap.dayOfWeek-1}"  step="1" >
			<td>&nbsp;</td>
		</c:forEach>
		<c:forEach begin="1" end="${calMap.lastDay}" var="i" step="1">
			<td>
				<a href="calBoardList.do?year=${calMap.year}&month=${calMap.month}&date=${i}" class="d">${i}</a>
			    <a href="addCalBoardForm.do?year=${calMap.year}&month=${calMap.month}&date=${i}">
			    	<img class="pen" alt="일정추가" src="img/pen.png"/>
			    </a>
			    ${Util.getCalViewList(i,clist)}
			</td>
			<c:if test="${(calMap.dayOfWeek-1+i)%7 == 0}">
				${"</tr><tr>"}
			</c:if>
		</c:forEach>
		<c:forEach begin="1" end="${(7-(calMap.dayOfWeek-1+calMap.lastDay)%7)%7}" step="1">
			<td>&nbsp;</td>
		</c:forEach>
	</tr>		
	</table>								
</div>                                 
<jsp:include page="footer.jsp"/>

</body>
</html>









