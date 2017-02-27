package weixin.servlet.inform;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;
import weixin.connection.message.ShowData;
import weixin.connection.text.OperateTextData;
import weixin.connection.users.OperateUsers;
import weixin.pojo.AccessToken;
import weixin.pojo.Message;
import weixin.pojo.Text;
import weixin.pojo.Users;
import weixin.thread.TokenThread;
import weixin.util.WeixinUtil;

public class ShowTextServlet extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(ShowTextServlet.class);  
	/***
	 * 查询某条通知信息
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String state="";//是否有权限查看
//		String userid = request.getParameter("userid");
		String code = request.getParameter("code");
		String id = request.getParameter("id");
		String wxscmid = request.getParameter("wxscmid");
		TokenThread tokenThread=new TokenThread();
	 	Map<String, AccessToken> map=tokenThread.maplist;
	 	AccessToken accessToken=map.get(wxscmid);
	 	if (!"authdeny".equals(code)&&code!=null) {
			String requestUrl="https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token="+accessToken.getToken()+"&code="+code+"&agentid=1";
			JSONObject jsonObj = WeixinUtil.httpRequest(requestUrl,"GET", null);
	 		log.info(jsonObj+"");
			//获取员工id
			String userid=jsonObj.getString("UserId");
			OperateTextData oT=new OperateTextData();
			Text txt=new Text();
			txt.setId(Integer.parseInt(id));
			txt=oT.showOneT(txt);
			String users=txt.getUsers();
			if(users==null)
				return;
			if(userid==null)
				return;
			boolean tf = users.contains(userid);
			if(tf){
				state="0";
			}else{
				state="1";
			}
			request.setAttribute("state",state);
			request.setAttribute("txt",txt);
			request.getRequestDispatcher("inform/txt.jsp").forward(request, response);
		}
	}
}
