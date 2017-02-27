

<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="weixin.pojo.Message,java.lang.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fun"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1,user-scalable=yes">
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Expires" content="0" />
<title>${title}</title>
<link href='css/basics.css' rel="stylesheet" />
<link href='css/Audit_center.css' rel="stylesheet" />
<script src='js/jquery-2.1.4.js'></script>
<script src='js/Audit_center.js'></script>
</head>
<body>
	<div class="audit_body">
		<!--内容盒子-->
		<div class="audit_content">

			<!--选项卡-->
			<ul class="audit_tab_control">
				<li><a href="${bu_url0}0${bu_url2}" class="audit_tabcon_click">待审</a>
				</li>
				<li><a href="${bu_url0}1${bu_url2}" class="">已审</a></li>
				<!--<li><a href="${bu_url0}2${bu_url2}" class="">驳回</a></li>-->
				<li><a href="${bu_url0}3${bu_url2}" class="">我发起的</a></li>
			</ul>
			<!--选项卡 END-->
			<!--列表盒子-->
			<div class="audit_list">
				<!--待审-->
				<div class="audit_list_container" style="display: block;">
					<!--列表-->
					<c:forEach var="list" items="${listM}">
						<div class="audit_list_box">
							<img src="img/point.png" />
							<div class="audit_dashed_box">
								<div class="audit_dsd_lbox">
									<div style="margin-top:20px">${list.title}</div>
									<div>
										${list.tjtimeStr}
									</div>
									<div style="margin-bottom:9px">提交人:${list.submit}</div>
									<!--<div>制单人编号:${list.smake}</div>-->
								</div>
								<div class="audit_dsd_rbox">
									<a href="${xqurl}${list.id}${url2}" class="audit_btn"> 详情 </a>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
				<!--待审 END-->
			</div>
			<!--列表盒子 END-->
		</div>
		<!--内容盒子 END-->
	</div>
</body>
</html>