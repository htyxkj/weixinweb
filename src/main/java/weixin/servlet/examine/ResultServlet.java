package weixin.servlet.examine;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weixin.pojo.AccessToken;
import weixin.thread.TokenThread;

public class ResultServlet extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(ButtonServlet.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String success=request.getParameter("success");
		String wxscmid=request.getParameter("wxscmid");
		String w_appid=request.getParameter("w_appid");
		log.info(success+"--"+w_appid+"--"+wxscmid);
		TokenThread tokenThread=new TokenThread();
	 	Map<String, AccessToken> map=tokenThread.maplist;
	 	AccessToken accessToken=map.get(wxscmid+"-"+w_appid);
		//
		String _url = "" + accessToken.getDomainName()
				+ "/weixinweb/ButtonServlet?w_appid="+w_appid+"&state=0&wxscmid=" + wxscmid;
		_url = URLEncoder.encode(_url, "UTF-8");
		_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
				+ wxscmid
				+ "&redirect_uri="
				+ _url;
		_url += "&response_type=code&scope=snsapi_base#wechat_redirect";
		request.setAttribute("url", _url);
		if(success.equals("ok")){
			request.getRequestDispatcher("examine/jieguocg.jsp").forward(request, response);
		}else{
			request.getRequestDispatcher("examine/jieguosb.jsp").forward(request, response);
		}
	}
}
