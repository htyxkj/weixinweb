package weixin.servlet.userandscm;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;
import weixin.connection.users.OperateUsers;
import weixin.pojo.AccessToken;
import weixin.pojo.Users;
import weixin.thread.TokenThread;
import weixin.util.WeixinUtil;

public class SubscribeServlet extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(SubscribeServlet.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException { 
		String ToUserName = request.getParameter("ToUserName");//企业号CorpID
		String FromUserName = request.getParameter("FromUserName");//成员UserID
		String CreateTime = request.getParameter("CreateTime");//消息创建时间 （整型）
		String MsgType = request.getParameter("MsgType");//消息类型，此时固定为：event
		String Event = request.getParameter("Event");//事件类型，subscribe(订阅)、unsubscribe(取消订阅)
		String AgentID = request.getParameter("AgentID");//企业应用的id，整型。可在应用的设置页面获取；如果id为0，则表示是整个企业号的关注/取消关注事件
		log.info(ToUserName+"//"+FromUserName+"//"+AgentID);
		try {
			TokenThread tokenThread = new TokenThread();
			Map<String, AccessToken>  map = TokenThread.maplist;
			AccessToken acc = (AccessToken) map.get(ToUserName);
			Users user =new Users();
			OperateUsers o=new OperateUsers();
			user.setUserid(FromUserName);
			user.setW_corpid(ToUserName);
			//员工关注  获得头像url		
			String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token="
					+ acc.getToken()
					+ "&userid="
					+ FromUserName;
			
			JSONObject jsonobj = WeixinUtil.httpRequest(requestUrl, "GET", null);
			log.info(jsonobj.toString());
			if (jsonobj.containsKey("avatar") == true) {
				user.setImgurl(jsonobj.getString("avatar"));
			} else {
				user.setImgurl("img/ren.png");
			}
			o.uodateUsersImgUrl(user);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
