<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page import="java.lang.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=yes">
    <title>报销</title>
    <link rel="stylesheet" href="css/weui/weui.min.css">
	<link rel="stylesheet" href="css/weui/jquery-weui.css">
	<link rel="stylesheet" href="css/weui/demos.css">
    <script src='js/jquery-2.1.4.js'></script> 
    <style type="text/css">
		body {background-color:#EBEBEB;}
		ul{margin:0px;padding:0px;width:100%;margin-top:15px;}
		li{list-style-type: none;}
		.li1{padding-top:20px;}
		.div1{background-color:#FFFFFF;color:#000000;width:88%;margin-left:5%;padding-left:10px;padding-top:14px;padding-bottom:16px;margin-bottom:25px}
	</style>
	<script type="text/javascript">
	var dbid = "01";
	var userid ="0050004"; 
	var currentPage=1;//当前页数
	var totalPage;//总页数
	var dbid1 ='<%=request.getAttribute("dbid")%>';  //数据库连接编号
	var userid1 ='<%=request.getAttribute("userid")%>'; //用户id
	var url ='<%=request.getAttribute("url")%>';	//平台地址
	var sid ='<%=request.getAttribute("sid")%>';	//单据id
	var zucheng = '<%=request.getAttribute("zucheng")%>'; //对象组成
	var cels={};	//
    var loading = false;  //状态标记
    $(function(){
    	mingxi();
    });
   
</script>
</head>

<body>
    <div id="zi">
	  	 <ul id="ziul">
	  	 	
	  	 </ul>
 	 </div>
<!-- 滚动加载 -->
<div id="jiazhai" class="weui-infinite-scroll">
	<div class="infinite-preloader"></div>
	正在加载
</div>
<script src="js/jquery-weui.js"></script> 
<script>
	var loading = false;
	$(document.body).infinite().on("infinite", function() {
		if(loading){ 
			return;
		}
		loading = true;
		if(currentPage==totalPage){
			$("#jiazhai").hide();
			return;
		}
		currentPage=currentPage+1;
		
	});
</script>
</body>
</html>