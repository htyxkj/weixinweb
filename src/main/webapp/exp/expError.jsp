<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>错误</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=yes">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="">
	<meta http-equiv="description" content="">
 
	<script type="text/javascript">
	 
	</script>
	<style type="text/css">
	</style>
  </head>
  <body>
  	<div style="text-align: center; padding-top: 200px;">
  		系统错误(未找到用户)，请联系管理员！
  	</div>
  </body>
</html>