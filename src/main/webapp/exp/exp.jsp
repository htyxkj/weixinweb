<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>
  <head>
    <base href="<%=basePath%>">
    <title>报销申请</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=yes">
	<link href="css/bootstrap.css" rel="stylesheet" media="screen">
	<link href="css/bootstrap-datetimepicker.css" rel="stylesheet" media="screen">
 
    <link href='css/Audit_center20170306.css' rel="stylesheet"/>
    
	<script src='js/jquery-2.1.4.js'></script>
	<script src='js/bootstrap.js'></script>
	<script src='js/bootstrap-datetimepicker.js'></script>
	<script type="text/javascript">
		var dbid1='<%=request.getAttribute("dbid")%>';
		var userid1='<%=request.getAttribute("userid")%>';
		var url ='<%=request.getAttribute("url")%>';
		$(function(){
			//登录所需信息
			var outlogin = {"dbid": dbid1,"usercode": userid1,"apiId": "outlogin"};
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
				}
	        });
			var message=$('input[name="message"]').val();
			if(message!=""&&message!=null){
				alert(message);
			}
			var regdate=$('input[name="regdate"]').val();
			if(regdate!=""&&regdate!=null){
				alert(regdate);
			}
			var bank=$('input[name="p_bank"]').val();
			var account=$('input[name="p_account"]').val();
			if(bank==""||account==""){
				$("#pank").show();
			}
			$('input[name="hpdate"]').val(new Date().Format("yyyy-MM-dd"));
			$("#add").click(function(){
				var html = '';
				//初始化
                html = html + [ '<div class="dvi2">'+
		  	 					'<div><a style="text-decoration:none;" name="mxnum">&nbsp;&nbsp;&nbsp;&nbsp;发票明细</a><a style="float:right;text-decoration:none;"  onClick="showNo(this)">收　起　</a></div>'+
		  	 					'<div class="fpmx1" style="height:auto;margin-top:10px;">'+
		  	 					'<ul class="ul2">'+
		  	 					'<li class="li2">报销项目:　<select class="input2" name="idxno">'+
								'<c:forEach items="${BXXM}" var="bxxm">'+
			     				'<option value="${bxxm.sid}">${bxxm.name}</option>'+
			     				'</c:forEach>'+
			     	    		'</select></li>'+
			     				'<li class="li2">发票编号:　<input class="input2" type="text" name="inv_no"/></li>'+
			     				'<li class="li2"> 可抵扣否:　<select class="input2" name="deduction">'+
								'<option value="0" selected="selected" >可抵扣</option>'+  
								'<option value="1" >不可抵扣</option>'+
				       			'</select></li>'+
			     				'<li class="li2"> 发票类别:　<select class="input2" name="inv_type">'+
								'<c:forEach items="${FPLB}" var="fplb">'+							
				        		'<option value="${fplb.sid}">${fplb.name}'+  
				        		'</option></c:forEach>'+
					  			'</select></li>'+
			     				'<li class="li2"> 特殊票别:　<select class="input2" name="sp_tax">'+
								'<option value="1" selected="selected" >正常</option>'+
								'<option value="2" >高速公路费</option>'+
								'<option value="3" >农副产品</option>'+
					   			'</select></li>'+
			     				'<li class="li2">金额　　:　<input class="input2" type="text" name="fcys"  oninput="jine(this)"/></li>'+
			     				'<li class="li2">增值税率:　<input class="input2" type="text" name="addtaxrt"   oninput="shuil(this)"/></li>'+
			     				'<li class="li2">增值税金:　<input class="input2" type="text" name="addtax" readOnly="true"  value="0" style="border:0px;width:auto;"/></li>'+
			     				'<li class="li2">无税金额:　<input class="input2" type="text" name="rmbhs" readOnly="true"  value="0" style="border:0px;width:auto;"/></li>'+
			     				'<li class="li2">摘要　　:　<input class="input2" type="text" name="remarks"/><a  name="del" style="float:right;text-decoration:none;"  onClick="delmx(this)">删除&nbsp;&nbsp;&nbsp;</a></li>'+
			     				'<li>&nbsp;</li></ul></div></div>'
							].join("");
						$("#div1").append($(html));
						var ps = document.getElementsByName("mxnum");
						for(var i = 0; i < ps.length; i++){
							ps[i].innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;发票明细"+convertToChinese(i+1);
						}
			});
		});
		//删除发票明细
		function delmx(obj){
			var msg='您真的确定要删除吗？\n\n请确认！';
			if (confirm(msg)==true){
				//重新计算总金额
				var fcys=$(obj).parent().siblings().children('input[name="fcys"]').val();//当前明细金额
				var countfcy=$('input[name="fcy"]').val();//总金额
				$('input[name="fcy"]').val(parseInt(countfcy==""?0:countfcy)-parseInt(fcys==""?0:fcys));
				$(obj).parent().parent().parent().parent().remove();
				
				var ps = document.getElementsByName("mxnum");
				for(var i = 0; i < ps.length; i++){
					ps[i].innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;发票明细"+convertToChinese(i+1);
				}
			 }else{
				return false; 
			 }
		};
		//判断金额输入是否正确，计算总金额
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
		//判断增值税率，计算增值税金
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
		};
		function yhxx(){
			var khh=$('input[name="khh"]').val();
			var zh=$('input[name="zh"]').val();
			
			$('input[name="p_bank"]').val(khh);
			$('input[name="p_account"]').val(zh);
			$("#pank").hide();
		};
		//提交判断非空验证
		function submint () {
			var bl=true;
			var inv_no=$('input[name="inv_no"]');//发票编号
			var fcy=$('input[name="fcys"]');//金额
			var addtaxrt=$('input[name="addtaxrt"]');//增值税率
			for(var i=0;i<fcy.size();i++){
				if(inv_no[i].value==""){
					bl=false;
				}
				if(fcy[i].value==""){
					bl=false;
				}
				if(addtaxrt[i].value==""){
					bl=false;
				}
			}
			if(bl){
				 $('#insertExp').submit();//apiId=savedata&dbid="+dbid+"&usercode="+userid+"&datatype=1&pcell="+pcell	
			}
		};
	</script>
	<style type="text/css">
		.fpmx1 { height:300px;overflow:hidden;display:show;}
		.ul1{margin:0px;padding:0px;width:80%;margin-left:10%;margin-top:15px;}
		.ul2{margin:0px;padding:0px;margin-left:10%;font-size:12px;}
		.dvi2{margin:0px;padding:0px;width:80%;margin-left:10%;margin-top:15px;color:#7E7E7E;border:1px solid;padding-top:4px;padding-bottom:4px;}
		li{list-style-type:none;margin:0px;padding:0px;color:#7E7E7E;}
		.li1{margin-top:10px;}
		.li2{margin-top:10px;}
		.input1{width:100%;height:25px;}
		.input2{height:25px;}
		.input3{width:100%;height:25px;}
		#pank{width:90%;height:300px;position:absolute;top:30%;left:5%;display: none;}
	</style>
  </head>
  <body>
  <form action="InsertExpServlet" method="post" id="insertExp">
	  <input type="hidden" name="smake" value="${user.userid}"/>
	  <input type="hidden" name="sopr" value="${user.userid}"/>
	  <input type="hidden" name="scm" value="${user.scm}"/>
	  <input type="hidden" name="w_appid" value="${w_appid}"/>
	  <input type="hidden" name="wxscmid" value="${wxscmid}"/>
	  <input type="hidden" name="p_bank" value="${user.exp_bank}"/>
	  <input type="hidden" name="p_account" value="${user.exp_account}"/>
	  <input type="hidden" name="message" value="${message}"/>
	  <input type="hidden" name="regdate" value="${regdate}"/>
	  
	  <div>
	  	<ul class="audit_tab_control">
            <li style="width: 49%;"><a href="${luUrl}" style="text-decoration:none;" class="audit_tabcon_click">报销录入</a></li>
            <li style="width: 49%;"><a href="${cxUrl}" style="text-decoration:none;" class="">报销查询</a></li>
        </ul>
	  	<ul class="ul1">
	  		<li class="li1">报销类别:</li>
	  		<li><select name="sid" class="input3">
		  		<c:forEach items="${BXLB}" var="bxlb">  
			        <option value="${bxlb.sid}">
			            ${bxlb.name}
			        </option>
    			</c:forEach>
				</select></li>
	  		<li class="li1">金额:</li>
	  		<li><input class="input1" style="border:0px;" type="text" name="fcy" readOnly="true" value="0"/></li>
	  		<li class="li1">日期:</li>
	  		<li><input type="text" name="hpdate"  class="input1 form_date" data-date-format="yyyy-MM-dd" readonly /></li>
	  		<li class="li1">报销说明:</li>
	  		<li><input class="input1" type="text" name="remark"/></li>
	  	</ul>
	  </div>
	  <div id="div1">
		  <div class="dvi2">
		  	 <div><a style="text-decoration:none; " name="mxnum">&nbsp;&nbsp;&nbsp;&nbsp;发票明细一</a><a style="float:right;text-decoration:none;"  onClick="showNo(this)">收　起　</a></div>
		  	 <div class="fpmx1" style="height:auto;margin-top:10px;">
		  	 	<ul class="ul2">
		  	 		<li class="li2">报销项目:　<select class="input2" name="idxno">
							<c:forEach items="${BXXM}" var="bxxm">
			     			<option value="${bxxm.sid}">${bxxm.name}</option>
			     			</c:forEach>
			     	    </select></li>
			     	<li class="li2">发票编号:　<input class="input2" type="text" name="inv_no"/></li>
			     	<li class="li2"> 可抵扣否:　<select class="input2" name="deduction">
							<option value="0" selected="selected" >可抵扣</option>  
							<option value="1" >不可抵扣</option>
				       </select></li>
			     	<li class="li2"> 发票类别:　<select class="input2" name="inv_type">
							<c:forEach items="${FPLB}" var="fplb">
				        	<option value="${fplb.sid}">${fplb.name}  
				        	</option></c:forEach>
					  </select></li>
			     	<li class="li2"> 特殊票别:　<select class="input2" name="sp_tax">
							<option value="1" selected="selected" >正常</option>
							<option value="2" >高速公路费</option>
							<option value="3" >农副产品</option>
					   </select></li>
			     	<li class="li2">金额　　:　<input class="input2" type="text" name="fcys"  oninput="jine(this)"/></li>
			     	<li class="li2">增值税率:　<input class="input2" type="text" name="addtaxrt"   oninput="shuil(this)"/></li>
			     	<li class="li2">增值税金:　<input class="input2" type="text" name="addtax" readOnly="true" value="0" style="border:0px;width:auto;"/></li>
			     	<li class="li2">无税金额:　<input class="input2" type="text" name="rmbhs" readOnly="true"  value="0" style="border:0px;width:auto;"/></li>
			     	<li class="li2">摘要　　:　<input class="input2" type="text" name="remarks"/> <a  name="del" style="float:right;text-decoration:none;"  onClick="delmx(this)">删除&nbsp;&nbsp;&nbsp;</a></li>
			     	<li>&nbsp;</li>
		  	 	</ul>
		  	 </div>
		  </div>
	  </div>
	  <div>
	  	<ul class="ul1">
	  		<li><input id="add" type="button" value="添加发票明细"/>      <input type="button" id="sbumit" onclick="javascript:submint()" value="提交"> </li>
	  		<li>&nbsp;</li>
	  	</ul>
	  </div>
	  <div id="pank">
		<div class="panel panel-default" >
			<div class="panel-heading">
				请完善报销信息！
			</div>
			<div class="panel-body">
				<ul>
					<li>收款人开户行</li>
					<li><input type="text" name="khh"> </li>
					<li>收款人账号</li>
					<li><input type="text" name="zh"> </li>
					<li>　</li>
					<li><input type="button" value="确定" onclick="javascript:yhxx()"/> </li>
				</ul>
			</div>
		</div>
	  </div>
  </form>
  </body>
   <script type="text/javascript">
  	  //设置日期选择框
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
	 //收缩隐藏  
	 function showNo(obj){
		 $(obj).parent().siblings().slideToggle("slow","swing");
		 $(obj).text($(obj).text()=='展　开　'?'收　起　':'展　开　');
	 }
	 //将阿拉伯数字转换成一二三
	 var N = ["零", "一", "二", "三", "四", "五", "六", "七", "八", "九"];  
     function convertToChinese(num){
         var str = num.toString();
         var len = num.toString().length;
         var C_Num = [];
         if(len==1){
        	 C_Num.push(N[str.charAt(0)]);
         }else if(len==2){
        	 if(str.charAt(0)==1){
        		 C_Num.push("十"+(str.charAt(1)==0?"":N[str.charAt(1)]));
        	 }else{
        		 C_Num.push(N[str.charAt(0)]+"十"+(str.charAt(1)==0?"":N[str.charAt(1)]));
        	 }
         }else{
	         for(var i = 0; i < len; i++){
	             C_Num.push(N[str.charAt(i)]);
	         }
         }
         return C_Num.join('');
     }
  </script>
</html>