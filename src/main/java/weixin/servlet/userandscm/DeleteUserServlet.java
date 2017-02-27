package weixin.servlet.userandscm;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;
import weixin.pojo.AccessToken;
import weixin.thread.TokenThread;
import weixin.util.WeixinUtil;

public class DeleteUserServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(DeleteUserServlet.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	/**
	 *删除企业号的成员  
	 **/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String wxscmid=request.getParameter("wxscmid");
		String userid=request.getParameter("userid");
		TokenThread tokenThread=new TokenThread();
	 	Map<String, AccessToken> map=tokenThread.maplist;
	 	AccessToken acc=map.get(wxscmid);
	 	String requestUrl="https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token="+acc.getToken()+"&userid="+userid;
	 	JSONObject jsonobj=WeixinUtil.httpRequest(requestUrl, "GET", null);
	 	log.info(jsonobj);
	 	JSONObject json = new JSONObject();  
	 	System.out.println(jsonobj);
		json.put("zt", jsonobj.get("errcode"));  
        PrintWriter out = response.getWriter();  
        out.write(json.toString());  
	}
}
