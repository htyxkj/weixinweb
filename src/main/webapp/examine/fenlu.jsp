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
	String usercode = request.getParameter("usercode");
	String documentstype = request.getParameter("documentstype");
	String serverurl = request.getParameter("serverurl");
	String documentsid = request.getParameter("documentsid");
%>
<head>
	<meta charset="UTF-8">
	<title>单据分录</title>
	<script src='<%=basePath %>js/jquery-2.1.4.js'></script>
	<link rel="stylesheet" href="<%=basePath %>css/basics_fl.css">
    <link rel="stylesheet" href="<%=basePath %>css/Block-list.css">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <meta content="telephone=no" name="format-detection" />
	<script type="text/javascript">
    $(function(){
	  var html = "";
  	  $.ajax({
			type : "POST",
			contentType : "application/x-www-form-urlencoded",
			url : "<%=serverurl%>lxvar",
			data :  {"VARID":"{&<%=documentstype%>FL}","DBID":"01","USRCODE":"<%=usercode%>","CONT":"<%=documentsid%>"},
			dataType : "JSON",
			success : function(data) {
				console.log(data);
				for(var value = 0; value < data.values.length;value++){
					var va = data.values[value];
					html += "<div class='list-a'>";
					for(var oneValue = 0; oneValue < va.length; oneValue++){
						if(data.labers[oneValue]=="附件名称"){
							//html += "<div class='list-aa'><span class='list-bt'>"+data.labers[oneValue]+"：</span><span class='list-hk'></span></div>";
							html += "<div class='list-aa'><span class='list-bt'>"+data.labers[oneValue]+"：</span><span class='list-hk'>"+getUrl(va[oneValue],va[oneValue+1])+"</span></div>";
						}else if(data.labers[oneValue]=="附件路径"){
						
						}else{
						    var baseValue = va[oneValue];
						    var checkValue = '';
						    if(baseValue){
                                checkValue = baseValue;
							}
                            <% if(documentstype.equals("3060")){%>
                            	if(data.labers[oneValue]=="交通工具"){
                                    if(checkValue == '0'){
                                        checkValue = "飞机";
									}
                                    if(checkValue == '1'){
                                        checkValue = "轮船";
                                    }
                                    if(checkValue == '2'){
                                        checkValue = "汽车";
                                    }
                                    if(checkValue == '3'){
                                        checkValue = "高铁";
                                    }
                                    if(checkValue == '4'){
                                        checkValue = "其他";
                                    }
                                    if(checkValue == '5'){
                                        checkValue = "火车";
                                    }
                            	}
							<%}%>
                            <% if(documentstype.equals("3075")){%>
                            if(data.labers[oneValue]=="印章类别"){
                                if(checkValue == '0'){
                                    checkValue = "公章";
                                }
                                if(checkValue == '1'){
                                    checkValue = "法人章";
                                }
                                if(checkValue == '2'){
                                    checkValue = "合同章";
                                }
                                if(checkValue == '3'){
                                    checkValue = "财务专用章";
                                }
                                if(checkValue == '4'){
                                    checkValue = "其他";
                                }
                            }
                            <%}%>
							html += "<div class='list-aa'><span class='list-bt'>"+data.labers[oneValue]+"：</span><span class='list-hk'>"+checkValue+"</span></div>";
						}
					}
					html += "</div>";
				}
				$("#list").append(html);
			},
			error : function(err) {
				alert("系统错误，请联系系统管理员！");
			}
		});
    });
	
	function getUrl(names,url){
		var Str = "";
		if(names=="null" || names==null)return "无";
		var namearr = names.split(";");
		for(var name=0;name<namearr.length;name++){
			Str += "<a href='<%=serverurl%>mydoc/db_01/"+url+namearr[name]+"'>"+namearr[name]+"</a><br/>";	
		}
		return Str;
	}
    </script>
</head>
<body>
	<div class="list-body">
		<h1 class="list-title">
			<c:if test="${documentstype eq 'C10901'}">
				借款单分录
			</c:if>
			<c:if test="${documentstype eq 'C10309'}">
				费用报销单分录
			</c:if>
			<c:if test="${documentstype eq 'C10912'}">
				付款单分录
			</c:if>
			<c:if test="${documentstype eq '3050'}">
				差旅费报销单
			</c:if>
			<c:if test="${documentstype eq '3060'}">
				出差申请单
			</c:if>
			<c:if test="${documentstype eq '3075'}">
				印章申请
			</c:if>
		</h1>
		<div id="list" class="list-con">
            
        </div>
	</div>
</body>
</html>