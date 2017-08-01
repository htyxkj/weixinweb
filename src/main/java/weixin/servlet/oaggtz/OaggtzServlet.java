package weixin.servlet.oaggtz;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weixin.connection.oaggtz.OperateOaggtz;
import weixin.connection.users.OperateUsers;
import weixin.key.SRegServ;
import weixin.pojo.AccessToken;
import weixin.pojo.Oaggtz;
import weixin.servlet.examine.ButtonServlet;
import weixin.thread.TokenThread;
import weixin.util.WeixinUtil;

public class OaggtzServlet extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(OaggtzServlet.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}
 
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String read = request.getParameter("read");
		String w_appid = request.getParameter("w_appid");
		String wxscmid = request.getParameter("wxscmid");
		
		if(wxscmid==null||wxscmid.equals(""))
			return;
		Map<String, AccessToken> map = TokenThread.maplist;
		AccessToken accessToken = map.get(wxscmid+"-"+w_appid);
		//用户同意授权后，能获取到code
		String code = request.getParameter("code");
		try {
			SRegServ t=new SRegServ();
			Object[] obj=(Object[]) t.processOperator("isReg",wxscmid);
			if(1==(Integer)obj[0]||-1==(Integer)obj[0]){
				String regInfo=t.processOperator("RegInfo",wxscmid).toString();
				request.setAttribute("wxscmid", wxscmid);
				request.setAttribute("regInfo", regInfo);
				request.setAttribute("errorInfo", obj[1]);
				request.getRequestDispatcher("./expired.jsp").forward(request,response);
				return;
			}else if(2==(Integer)obj[0]){
				request.setAttribute("regdate", obj[1]);
			}
			if (!"authdeny".equals(code) && code != null) {
				String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token="
						+ accessToken.getToken()
						+"&code="
						+code;
				JSONObject jsonObj = WeixinUtil.httpRequest(requestUrl, "GET",null);
//				获取用户id
				String userid="";
				if(jsonObj.containsKey("UserId") == true){
					userid = jsonObj.getString("UserId");
				}else{
					//获取员工信息失败   重新获取
		            String _url = "" +accessToken.getDomainName()
		                    + "/weixinweb/OaggtzServlet?w_appid="+w_appid+"&wxscmid=" + wxscmid
		                    + "&read=" + read;
		            _url = URLEncoder.encode(_url, "UTF-8");
		            _url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
		                    + wxscmid
		                    + "&redirect_uri="
		                    + _url;
		            _url += "&response_type=code&scope=snsapi_base#wechat_redirect";
		            response.sendRedirect(_url);
		            return;
				}
//				String userid="0990006";
				if(userid.indexOf("@")!=-1){
					OperateUsers oU=new OperateUsers();
					userid=oU.getUserID(userid, wxscmid);
				}
				HttpSession session = request.getSession();
				session.setAttribute("userId",userid);
				session.setAttribute("wxscmid",wxscmid);
				//详情 按钮链接
				String xq_url1 = "" +accessToken.getDomainName()
						+ "/weixinweb/OneOaggtzServlet?w_appid="+w_appid+"&wxscmid=" + wxscmid
						+ "&keyid=";
				xq_url1 = URLEncoder.encode(xq_url1, "UTF-8");
				String xq_url0 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
						+ wxscmid
						+ "&redirect_uri="
						+ xq_url1;
				String xq_url2 = "&response_type=code&scope=snsapi_base#wechat_redirect";
				request.setAttribute("xqurl", xq_url0);
				request.setAttribute("url2", xq_url2);
				//已读，未读，我发布的
				String bu_url1 = "" + accessToken.getDomainName()
						+ "/weixinweb/OaggtzServlet?w_appid="+w_appid+"&wxscmid=" + wxscmid
						+ "&read=";
				bu_url1 = URLEncoder.encode(bu_url1, "UTF-8");
				String bu_url0 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
						+ wxscmid
						+ "&redirect_uri="
						+ bu_url1;
				String bu_url2 = "&response_type=code&scope=snsapi_base#wechat_redirect";
				request.setAttribute("bu_url0", bu_url0);
				request.setAttribute("bu_url2", bu_url2);
				if(read.equals("0"))
					request.getRequestDispatcher("oaggtz/oaggtz.jsp").forward(request,response);
				if(read.equals("1"))
					request.getRequestDispatcher("oaggtz/oaggtzyd.jsp").forward(request,response);
				if(read.equals("2"))
					request.getRequestDispatcher("oaggtz/oaggtzwd.jsp").forward(request,response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e + ""+wxscmid+"-"+w_appid);
            //获取员工信息失败   重新获取
            String _url = "" +accessToken.getDomainName()
                    + "/weixinweb/OaggtzServlet?w_appid="+w_appid+"&wxscmid=" + wxscmid
                    + "&read=" + read;
            _url = URLEncoder.encode(_url, "UTF-8");
            _url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
                    + wxscmid
                    + "&redirect_uri="
                    + _url;
            _url += "&response_type=code&scope=snsapi_base#wechat_redirect";
            response.sendRedirect(_url);
            return;
		}
	}
}