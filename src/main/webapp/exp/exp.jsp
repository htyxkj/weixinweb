<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>报销申请</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=yes">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="">
	<meta http-equiv="description" content="">
	<link href="css/bootstrap.css" rel="stylesheet" media="screen">
	<link href="css/bootstrap-datetimepicker.css" rel="stylesheet" media="screen">
	<script src='js/jquery-2.1.4.js'></script>
	<script src='js/bootstrap.js'></script>
	<script src='js/bootstrap-datetimepicker.js'></script>
	<script type="text/javascript">
		$(function(){
			$('input[name="hpdate"]').val(new Date().Format("yyyy-MM-dd"));
			var id=2;
			$("#add").click(function(){
				var html = '';
				//初始化
                html = html + [ '<ul class="ul1">'+
                                '<li class="li2">序&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;&nbsp;&nbsp;&nbsp;:'+id+'<input type="button" name="del" value="删除" style="float:right;"  onClick="delmx(this)" ></li>'+
							    '<li class="li2">报销项目:'+
							    '<select name="idxno" class="input2">'+
		  						'<c:forEach items="${BXXM}" var="bxxm">'+
			        			'<option value="${bxxm.sid}">${bxxm.name}</option>'+
			        			'</c:forEach></select></li>'+
							    '<li class="li2">发票编号:<input class="input2" type="text" name="inv_no"/></li>'+
							    '<li class="li2">可抵扣否:'+
		    				    '<select name="deduction" class="input2">'+
    						    '<option value="0" selected="selected" >可抵扣</option>'+  
    						    '<option value="1" >不可抵扣</option></select></li>'+
								'<li class="li2">发票类别:'+
								'<select name="inv_type" class="input2">'+
								'<c:forEach items="${FPLB}" var="fplb">'+
						        '<option value="${fplb.sid}">${fplb.name}'+  
						        '</option></c:forEach>'+
								'</select></li>'+
								'<li class="li2">特殊票别:'+
								'<select name="sp_tax" class="input2">  '+
    							'<option value="1" selected="selected" >正常</option>  '+
    							'<option value="2" >高速公路费</option>'+
    							'<option value="3" >农副产品</option>'+
								'</select></li>'+
								'<li class="li2">金&nbsp;&nbsp;&nbsp;&nbsp;额&nbsp;&nbsp;&nbsp;&nbsp;:<input class="input2" type="text" name="fcys"  oninput="jine(this)"/></li>'+
								'<li class="li2">增值税率:<input class="input2" type="text" name="addtaxrt"   oninput="shuil(this)"/></li>'+
								'<li class="li2">增值税金:<input class="input2" type="text" name="addtax" readOnly="true" /></li>'+
								'<li class="li2">无税金额:<input class="input2" type="text" name="rmbhs" readOnly="true" /></li>'+
								'<li class="li2">摘&nbsp;&nbsp;&nbsp;&nbsp;要&nbsp;&nbsp;&nbsp;&nbsp;:<input class="input2" type="text" name="remarks"/></li>'+
								'</ul>'
							].join("");
				$("#div1").append($(html));
				id++;
			});
		});
		function delmx(obj){
			var j=0;
			var objinput=$(obj).parent().siblings().children('input');
			for(var i=0;i<objinput.size();i++){
				if(objinput[i].value!=""){
					j++;
				};
			};
			if(j==0){
				$(obj).parent().parent().remove();
			}else{
				var msg='您真的确定要删除吗？\n\n请确认！';
				if (confirm(msg)==true){ 
					$(obj).parent().parent().remove();
				 }else{
					return false; 
				 }
			}
		};
		function jine(obj){
			var fcys=$(obj).val();//金额
			var countfcy=$('input[name="fcys"]');//全部金额
			var addtaxrt=$(obj).parent().siblings().children('input[name="addtaxrt"]').val();//增值税率
			var addtax=$(obj).parent().siblings().children('input[name="addtax"]');//增值税金
			var rmb=$(obj).parent().siblings().children('input[name="rmbhs"]');//无税金额
			if(fcys!=''){
				if(!isNaN(fcys)){
				}else{
				  alert("请填写正确的金额!");
				  $(obj).val("");
				  return;
				}
				var count=parseInt(0);
				for(var i=0;i<countfcy.size();i++){
					if(countfcy[i].value!=""){
						count=parseInt(count)+parseInt(countfcy[i].value);
					}
				}
				$('input[name="fcy"]').val(count);
			}
			if(fcys!=''&&addtaxrt!=''){
				addtax.val(returnFloat(fcys/(1+(addtaxrt*100/100))*(addtaxrt*100/100)));
				rmb.val(returnFloat(fcys/(1+(addtaxrt*100/100))));
			}
		}
		function shuil(obj){
			var fcys=$(obj).parent().siblings().children('input[name="fcys"]').val();//金额
			var addtaxrt=$(obj).val();//增值税率
			var addtax=$(obj).parent().siblings().children('input[name="addtax"]');//增值税金
			var rmb=$(obj).parent().siblings().children('input[name="rmbhs"]');//无税金额
			if(addtaxrt!=''){
				if(!isNaN(addtaxrt)){
					if(addtaxrt>1){
						alert("请填写正确的增值税率!");
						$(obj).val("");
						return;
					}
				}else{
					alert("请填写正确的增值税率!");
					$(obj).val("");
					return;
				}
			}
			if(fcys!=''&&addtaxrt!=''){
				addtax.val(returnFloat(fcys/(1+(addtaxrt*100/100))*(addtaxrt*100/100)));
				rmb.val(returnFloat(fcys/(1+(addtaxrt*100/100))));
			}
		}
		//保留两位小数
		function returnFloat(value){
			 var value=Math.round(parseFloat(value)*100)/100;
			 var xsd=value.toString().split(".");
			 if(xsd.length==1){
			 	value=value.toString()+".00";
			 	return value;
			 }
			 if(xsd.length>1){
				 if(xsd[1].length<2){
					 value=value.toString()+"0";
				 }
			 return value;
			 }
		}
		//日期格式化
		Date.prototype.Format = function (fmt) { //author: meizz 
		    var o = {
		        "M+": this.getMonth() + 1, //月份 
		        "d+": this.getDate(), //日 
		        "h+": this.getHours(), //小时 
		        "m+": this.getMinutes(), //分 
		        "s+": this.getSeconds(), //秒 
		        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
		        "S": this.getMilliseconds() //毫秒 
		    };
		    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
		    for (var k in o)
		    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		    return fmt;
		}
	</script>
	<style type="text/css">
		.ul1{margin:0px;padding:0px;width:80%;margin-left:10%;margin-top:30px;}
		li{list-style-type:none;margin:0px;padding:0px;color:#7E7E7E;}
		.li1{margin-top:10px;}
		.li2{margin-top:10px;}
		.input1{width:100%;height:25px;}
		.input2{width:100%;height:30px;}
	</style>
  </head>
  <body>
  <form action="InsertExpServlet" method="post">
	  <input class="input1" type="hidden" name="smake" value="${user.userid}"/>
	  <input class="input1" type="hidden" name="sopr" value="${user.userid}"/>
	  <div>
	  	<ul class="ul1">
	  		<li class="li1">报销类别:</li>
	  		<li><select name="bxlb" class="input2">
		  		<c:forEach items="${BXLB}" var="bxlb">  
			        <option value="${bxlb.sid}">  
			            ${bxlb.name}  
			        </option>  
    			</c:forEach>  
				</select></li>
	  		<li class="li1">收款人开户行:</li>
	  		<li><input class="input1" type="text" name="p_bank"/></li>
	  		<li class="li1">收款人账号:</li>
	  		<li><input class="input1" type="text" name="p_account"/></li>
	  		<li class="li1">金额:</li>
	  		<li><input class="input1" type="text" name="fcy" readOnly="true"/></li>
	  		<li class="li1">日期:</li>
	  		<li><input type="text" name="hpdate"  class="input1 form_date" data-date-format="yyyy-MM-dd" readonly /></li>
	  		<li class="li1">报销说明:</li>
	  		<li><input class="input1" type="text" name="remark"/></li>
	  	</ul>
	  </div>

	  <div id="div1">
	  	  <ul class="ul1">
	                          <li class="li2">序&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;&nbsp;&nbsp;&nbsp;:1</li>
		    <li class="li2">报销项目:
		    <select name="idxno" class="input2">
					<c:forEach items="${BXXM}" var="bxxm">
	     			<option value="${bxxm.sid}">${bxxm.name}</option>
	     			</c:forEach>
	     	</select>
	     	</li>
		    <li class="li2">发票编号:<input class="input2" type="text" name="inv_no"/></li>
		    <li class="li2">可抵扣否:
					    <select name="deduction" class="input2">
					    <option value="0" selected="selected" >可抵扣</option>  
					    <option value="1" >不可抵扣</option></select></li>
			<li class="li2">发票类别:
			<select name="inv_type" class="input2">
			<c:forEach items="${FPLB}" var="fplb">
	        <option value="${fplb.sid}">${fplb.name}  
	        </option></c:forEach>
			</select></li>
			<li class="li2">特殊票别:
			<select name="sp_tax" class="input2">
						<option value="1" selected="selected" >正常</option>
						<option value="2" >高速公路费</option>
						<option value="3" >农副产品</option>
			</select></li>
			<li class="li2">金&nbsp;&nbsp;&nbsp;&nbsp;额&nbsp;&nbsp;&nbsp;&nbsp;:<input class="input2" type="text" name="fcys"  oninput="jine(this)"/></li>
			<li class="li2">增值税率:<input class="input2" type="text" name="addtaxrt"   oninput="shuil(this)"/></li>
			<li class="li2">增值税金:<input class="input2" type="text" name="addtax" readOnly="true" /></li>
			<li class="li2">无税金额:<input class="input2" type="text" name="rmbhs" readOnly="true" /></li>
			<li class="li2">摘&nbsp;&nbsp;&nbsp;&nbsp;要&nbsp;&nbsp;&nbsp;&nbsp;:<input class="input2" type="text" name="remarks"/></li>
		</ul>
	  </div>
	  <div>
	  	<ul class="ul1">
	  		<li><input id="add" type="button" value="添加发票明细"/>      <input type="submit" value="提交"> </li>
	  		<li>&nbsp;</li>
	  	</ul>
	  </div>
  </form>
  <script type="text/javascript">
  $.fn.datetimepicker.dates['zh-CN'] = {  
          days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],  
          daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],  
          daysMin:  ["日", "一", "二", "三", "四", "五", "六", "日"],  
          months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],  
          monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],  
          today: "今天",
          suffix: [],
          meridiem: ["上午", "下午"]
  };
	 $('.form_date').datetimepicker({
	        weekStart: 7,
			todayHighlight: 1,
			startView: 2,
			minView: 2,
			forceParse: 0,
			language: 'zh-CN',//显示中文 
			format: 'yyyy-mm-dd',//显示格式 
			initialDate: new Date(),//初始化当前日期 
			autoclose: true,//选中自动关闭 
	    });
	 </script>
  </body>
</html>