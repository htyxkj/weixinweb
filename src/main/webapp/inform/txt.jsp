<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">
    <title>消息通知</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=yes">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="查看消息通知">
	<link rel="stylesheet" href="css/weui.css" />
	<link rel="stylesheet" href="css/example.css" />
	<style type="text/css">
		.p0{margin-left:5%;margin-right:5%;text-indent:2em;}
		.p1{text-align:center;}
	</style>
  </head>
  <body>
    <c:if test="${state==1}">
    	<div class="page msg_warn js_show">
		<div class="weui-msg">
			<div class="weui-msg__icon-area">
					<h2 class="weui-msg__title">
						<i class="weui-icon-warn weui-icon_msg"></i>
					</h2>
			</div>
			<div class="weui-msg__text-area">
					<h2 class="weui-msg__title">对不起，您无权访问此条信息！</h2>
			</div>
			<div class="weui-msg__opr-area">
				<p class="weui-btn-area">
						<a href="javascript:closeie();" class="weui-btn weui-btn_primary">返回</a>
				</p>
			</div>
		</div>
	</div>
    </c:if>
	<c:if test="${state==0}">
	 
	 <div>
	 	<p>&nbsp;</p>
	 	<p class="p1">${txt.title}</p>
	 	<p class="p0">
 		${txt.content}
 		</p>
 		<p style="text-align:right;margin-right:7%;">${txt.time}</p>
	 </div>
	</c:if>
  </body>
</html>