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
	var zhuzi=true;  //true 主表，false子表

	var currentPage=1;//当前页数
	var totalPage;//总页数
	var cels={};
	var dbid = "01";
	var userid ="0050004"; 
	var dbid1='<%=request.getAttribute("dbid")%>';
	var userid1='<%=request.getAttribute("userid")%>';
	var url ='<%=request.getAttribute("url")%>';

    var loading = false;  //状态标记
    $(function(){
    	var regdate= $("#regdate").val();
		if(regdate!=''){
			alert(regdate);
		}
        $("#zi").hide();
		//登录所需信息
		var outlogin = {"dbid": dbid,"usercode": userid,"apiId": "outlogin"};
		//进行登录
		$.ajax({
			type: 'post',
			url: url+"api/",
			data: outlogin,
			contentType: 'application/x-www-form-urlencoded',
			dataType: 'json',
			async: true,
			success: function (data) { 
				if(data.id!=0){
					alert("系统错误请联系管理员,谢谢。");
					return;
				}
				selectzhu(true);
			}
        });
    });
    function selectzhu(qk) {
    	
    	var j=0;
		var zucheng=$('select[name="zucheng"]').val();
		if(zucheng==""||zucheng==null)
			return;
		var num1=zucheng.indexOf("(");
		var num2=zucheng.indexOf(")");
		var zi=zucheng.substring(num1+1,num2);
		if(zi.indexOf(';')!=-1){
			j=1;
		}
		var dataSelect = {
	        'dbid': dbid,
	        'apiId': "cellparam",
	        'usercode': userid,
	        'pcell': zucheng,
	        'pdata': "",
	        'bebill': 1,
	        'currentPage': currentPage,
	        'pageSize': 10,
	        'cellid': ''
		};
		$.ajax({
			type: 'post',
			url: url+"api/",
			data: dataSelect,
			contentType: 'application/x-www-form-urlencoded',
			dataType: 'json',
			async: false,
			success: function (data) { 
				 console.log(data);
				//子表标签名
				cels=data.data.layCels.subLayCells[j].cels;
				//总页数
				totalPage=data.data.pages.totalPage; 
				if(totalPage==1){
					$("#jiazhai").hide();
				}
				var obj =data; //JSON.parse(data);
				var datatitle=obj.data.layCels.cels;
				if(obj.id==0){
					if(qk)
					$('#zhuul').html("");
					var dataval=obj.data.pages.celData;
					var html = ''; 
					for (var i=0;i<dataval.length;i++){
						//初始化
						html  += ['<li><div class="div1">'+
       						'<a onClick="javascript:mingxi('+"'"+dataval[i][0]+"'"+')">'+
       	                    '<div>'+
							'<div style="color:#000000;font-size:20px">' + datatitle[0].labelString+':'+dataval[i][0] + '</div>'+
       	                    '<div style="color:#000;padding-top:4px;">'+datatitle[29].labelString+':'+dataval[i][29]+'</div>'+
       	                    '<div style="color:#000000;padding-top:4px;">' +datatitle[22].labelString+':'+ dataval[i][22]+ '</div>'+
       	                    '</div>'+
       	                    '<div style="font-size:14px;color:#000000;border-top: 1px dashed  #888888;width:98%;"><br/>&nbsp;查看详情<span style="margin-left:70%">></span></div>'+
       	                    '</a> '+
                            '</div>'+
                        	'</li>'].join("");
					}
					$("#zhuul").append($(html));
				}
			}
        });
    }
    function mingxi(sid){
    	currentPage=1;
    	zhuzi=false;
    	mingxi_d(sid,true);
    }
    //查询发票明细
	function mingxi_d(sid,tf){
		$('input[name="sid"]').val(sid);
		var zucheng=$('select[name="zucheng"]').val();
		var num1=zucheng.indexOf("(");
		var num2=zucheng.indexOf(")");
		var zi=zucheng.substring(num1+1,num2);
		if(zi.indexOf(';')!=-1){
			var arr=zi.split(";");
			zi=arr[1];
		}
		var dataOne = {
	        'dbid': dbid,
	        'apiId': "cellparam",
	        'usercode': userid,
	        'pcell': zucheng,
	        'pdata': "sid='"+sid+"'",
	        'bebill': 1,
	        'currentPage': 1,
	        'pageSize': 10,
	        'cellid': zi
		};
		console.log(dataOne);
		$.ajax({
			type: 'post',
			url: url+"api/",
			data: dataOne,
			contentType: 'application/x-www-form-urlencoded',
			dataType: 'json',
			async: true,
			success: function (data) {
				var value=data.data.pages.celData;
				totalPage=data.data.pages.totalPage;
				if(tf)
				$('#ziul').html("");
				var html = '';
				for ( var i = 0; i < value.length;i++) {
	                 html  += ['<li>'+
	                            '<div class="div1">'+
           	                    '<div>'+
								'<div style="color:#000000;font-size:20px">' + cels[0].labelString+':'+value[i][0] + '</div>'+
           	                    '<div style="color:#000;padding-top:4px;">'+cels[4].labelString+':'+value[i][4]+'</div>'+
           	                    '<div style="color:#000000;padding-top:4px;">' +cels[8].labelString+':'+ value[i][8]+ '</div>'+
           	                 	'<div style="color:#000000;padding-top:4px;">' + cels[9].labelString+':'+value[i][9] + '</div>'+
           	                    '<div style="color:#000;padding-top:4px;">'+cels[10].labelString+':'+value[i][10]+'</div>'+
           	                    '<div style="color:#000000;padding-top:4px;">' +cels[11].labelString+':'+ value[i][11]+ '</div>'+
           	                    '</div>'+
                                '</div>'+
                            	'</li>'].join("");
				}
				html+=['<a onClick="javascript:fanhui()"><li><div class="div1" style="text-align:center">返回</div></li></a>'].join("");
				$("#ziul").append($(html));
			}
        });
		$("#jiazhai").hide();
		$("#zhu").hide();
		$("#zi").show();
	}
	function fanhui(){
		zhuzi=true;
		$("#zi").hide();
		$("#zhu").show();
	}
</script>
</head>
<body>
<input id="regdate" name="date" type="hidden" value="${regdate}"/>
<input id="sid" name="sid" type="hidden" value=""/>
	 <div id="zhu">
	  	<ul>
	 		<li style="margin-left: 25px;padding-bottom: 20px; font-size:18px; width:88%;margin-left:5%">
	 		<div style="">
		 		报销类别:
		 		<select name="zucheng" onchange="selectzhu(true)" style="font-size: 18px;">
		  			<c:forEach items="${BXLB}" var="bxlb" >
			        	<option value="${bxlb.zucheng}">
			            	${bxlb.name}
			       	 	</option>
		  			</c:forEach>
				</select>
			</div>
			</li>
		 </ul>
		 <ul id="zhuul">
	 		 
		 </ul>
    </div>
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
		if(zhuzi){
			$("#jiazhai").show();
			if(loading){ 
				return;
			}
			loading = true;
			if(currentPage==totalPage){
				$('#jiazhai').hide();
				return;
			}
			currentPage=currentPage+1;
			selectzhu(false);
			loading = false;
			$("#jiazhai").hide();
		}else{
			$("#jiazhai").show();
			if(loading){
				return;
			}
			loading = true;
			if(currentPage==totalPage){
				$("#jiazhai").hide();
				return;
			}
			currentPage=currentPage+1;
			var sid=$('select[name="sid"]').val();
			mingxi_d(sid,false);
			loading = false;
			$('#jiazhai').hide();
		}
	});
</script>
</body>
</html>