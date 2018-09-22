<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fun" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title></title>
    <script src='../js/jquery-3.0.0.js'></script>
    <script src="../js/dingtalk.open.js"></script>
    <script type="text/javascript">
        $(function() { 
        	var appId = getQueryVariable('appId');
        	var corpId = getQueryVariable('corpId');
        	var bipAppId = getQueryVariable('bipAppId');
        	dd.ready(function() {
        	    dd.runtime.permission.requestAuthCode({
        	        corpId: corpId,
        	        onSuccess: function(info) {
                        $.ajax({
                            type: "get",
                            url: "<%=basePath %>DLoginServlet",
                            data: {
                                code: info.code,
                                appId:appId,
                                corpId:corpId,
                                bipAppId:bipAppId,
                            },
                            success: function (data) {
                            	var obj = eval('(' + data + ')');
                            	if(obj.code == 0){
                            		location.href = obj.toUrl;
                            	}else{
                            		
                            	}
                            }
                        });
        	        },
        	        onFail : function(err) {}
        	  
        	    });
        	}); 
        });
        //获取浏览器地址栏参数
        function getQueryVariable(variable){
               var query = window.location.search.substring(1);
               var vars = query.split("&");
               for (var i=0;i<vars.length;i++) {
                       var pair = vars[i].split("=");
                       if(pair[0] == variable){return pair[1];}
               }
               return(false);
        } 
    </script>
</head>
<body>
<div style="text-align: center; padding-top: 150px; font-size: 2.4rem;">页面正在努力加载中......</div>
</body>
</html>
