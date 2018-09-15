package weixin.servlet.userandscm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import weixin.pojo.AccessToken;
import weixin.pojo.Users;
import weixin.thread.TokenThread;
import weixin.util.HttpUtil;

public class ShowUsersAll extends HttpServlet {
 
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
 
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String wxscmid=request.getParameter("wxscmid");
		String w_appid=request.getParameter("w_appid");
		List<Users> ltu=new ArrayList<Users>();
		TokenThread tokenThread = new TokenThread();
		Map<String, AccessToken>  map = TokenThread.maplist;	
		AccessToken acc = (AccessToken) map.get(wxscmid+"-"+w_appid);
		String requestUrl="https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token="+acc.getW_accessToken()+"&department_id=1&fetch_child=1&status=0,1,2,4";
		String jsonString = null;
		JSONObject jsonobj = HttpUtil.httpRequest(requestUrl, "GET", jsonString);
		JSONArray arry = JSONArray.fromObject(jsonobj.get("userlist"));
		Users user=null;
		for (int i = 0; i < arry.size(); i++) {
			user=new Users();
			JSONObject jsonuser = arry.getJSONObject(i);
			user.setUserid(jsonuser.getString("userid"));
			user.setUsername(jsonuser.getString("name"));
			if(jsonuser.containsKey("email"))
			user.setEmail(jsonuser.getString("email"));
			if(jsonuser.containsKey("mobile"))
			user.setTel(jsonuser.getString("mobile"));
			if(jsonuser.containsKey("avatar"))
			user.setW_imgurl(jsonuser.getString("avatar"));
			user.setStatus(jsonuser.getString("status"));
			ltu.add(user);
		}
		request.setAttribute("ul", ltu);
		request.setAttribute("wxscmid", wxscmid);
		request.getRequestDispatcher("userandscm/userlist.jsp").forward(request,response);
	}
}