package weixin.servlet.login;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weixin.connection.users.OperateUsers;
import weixin.pojo.AccessToken;
import weixin.pojo.Users;
import weixin.thread.TokenThread;
import weixin.util.APIAddr;
import weixin.util.HttpUtil;

/**
 * 
 * ClassName: DLoginServlet
 * 
 * @Description: 功能描述: 钉钉登录 company:北京斯坦德科技发展有限公司
 * @date 2018年9月11日上午11:18:51
 */
public class DLoginServlet extends HttpServlet {
	private static final long serialVersionUID = -6080111682538491505L;
	private static Logger log = LoggerFactory.getLogger(DLoginServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject retJSON = new JSONObject();
		String toUrl = "";
		try {
			String code = request.getParameter("code");
			String appid = request.getParameter("appId");
			String corpId = request.getParameter("corpId");
			String bipAppId = request.getParameter("bipAppId");
			// 获取钉钉token
			AccessToken acct = TokenThread.maplist.get("dd-" + corpId);
			if(acct == null || code ==null){
				retJSON.put("toUrl", "../error.html");
				retJSON.put("code", "0");
				return;
			}
			// 构建获取用户ID连接
			String url = APIAddr.DD_USER_CODE;
			url = url.replace("ACCESS_TOKEN", acct.getD_accessToken()).replace(
					"CODE", code);
			JSONObject json = HttpUtil.httpRequest(url, "GET", null);
			log.info(json.toString());
			if (json.getInt("errcode") == 0) {
				String userid = json.getString("userid");
				OperateUsers userServ = new OperateUsers();
				Users user = userServ.getUser(userid, corpId);
				if (user != null) {
					user.setLoginType("d");
					user.setLoginAppid(appid);
					user.setW_corpid("n-u-l-l");
					request.getSession().setAttribute("sessionUser", user);
				} else {
					retJSON.put("code", "-1");
					return;
				}
				// 获取钉钉用户详细信息 更新用户头像
				url = APIAddr.DD_USER_INFO;
				url = url.replace("ACCESS_TOKEN", acct.getD_accessToken())
						.replace("USERID", userid);
				json = HttpUtil.httpRequest(url, "GET", null);
				if (json.getInt("errcode") == 0) {
					String avatar = json.getString("avatar");
					if(avatar != null && !avatar.equals("")){
						user.setD_imgurl(avatar);
						userServ.uodateUsersImgUrl(user, "d");
					}
				}
				String redirect_domain = acct.getDomainName();//"http://192.168.0.200:8080/weixinweb/";
				String home = redirect_domain+"login/dlogin.jsp?corpId="+corpId+"&appId="+appid+"&bipAppId="+bipAppId;			 
				Cookie cookie = new Cookie("loginURL", home);  
				cookie.setPath(request.getContextPath());  
				response.addCookie(cookie);
				// 拼接登录成功后跳转连接
				if (bipAppId.equals("01")) {
					toUrl = "../ButtonServlet";
				} else if (bipAppId.equals("04")) {
					toUrl = "../OaggtzServlet";
				}
				retJSON.put("toUrl", toUrl);
				retJSON.put("code", "0");
			} else {
				retJSON.put("code", "-1");
				return;
			}
		} catch (Exception e) {
			retJSON.put("code", "-1");
			e.printStackTrace();
		} finally {
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(retJSON.toString().getBytes("UTF-8"));
			outputStream.close();
		}
	}

//	 public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		 try {
//			 String userid = request.getParameter("userid");
//			 String corpId = request.getParameter("corpId");
//			 String appid = request.getParameter("appId");
//			 String bipAppId = request.getParameter("bipAppId");
//			 OperateUsers userServ = new OperateUsers();
//			 Users user = userServ.getUser(userid, corpId);
//			 user.setLoginType("d");
//			 user.setW_corpid("n-u-l-l");
//			 user.setLoginAppid(appid);
//			 request.getSession().setAttribute("sessionUser", user);
//			 
//			 AccessToken acct = TokenThread.maplist.get("dd-" + corpId);
//			 String redirect_domain = acct.getServerurl();//"http://192.168.0.200:8080/weixinweb/";
//			 String home = redirect_domain+"login/dlogin.jsp?corpId="+corpId+"&appId="+appid+"&bipAppId="+bipAppId;			 
//			 Cookie cookie = new Cookie("loginURL", home);  
//		     cookie.setPath(request.getContextPath());  
//		     response.addCookie(cookie);
//
//			 // 拼接登录成功后跳转连接
//			 if (bipAppId.equals("01")) {
//				 response.sendRedirect("ButtonServlet");
//			 } else if (bipAppId.equals("04")) {
//				 response.sendRedirect("OaggtzServlet");
//			 }
//		 } catch (Exception e) {
//		 // TODO Auto-generated catch block
//			 e.printStackTrace();
//		 }
//	 }
}
