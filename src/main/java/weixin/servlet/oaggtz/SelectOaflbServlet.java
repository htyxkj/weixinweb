package weixin.servlet.oaggtz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import weixin.connection.department.OperateDepartment;
import weixin.connection.oaggtz.OperateOaflb;
import weixin.connection.users.OperateUsers;
import weixin.pojo.AccessToken;
import weixin.pojo.Department;
import weixin.pojo.Oaflb;
import weixin.pojo.Users;
import weixin.thread.TokenThread;
import weixin.util.HttpUtil;

public class SelectOaflbServlet extends HttpServlet {


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String wxscmid = request.getParameter("wxscmid");
		if(wxscmid==null||wxscmid.equals(""))
			return;
		Map<String, AccessToken> map = TokenThread.maplist;
		AccessToken accessToken = map.get(wxscmid);
		//用户同意授权后，能获取到code
		String code = request.getParameter("code");
		try {
			if (!"authdeny".equals(code) && code != null) {
				String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token="
						+ accessToken.getW_accessToken()
						+"&code="
						+code;
				JSONObject jsonObj = HttpUtil.httpRequest(requestUrl, "GET",null);
				//获取用户id
				OperateUsers oU=new OperateUsers();
				String userid = jsonObj.getString("UserId");
//				String userid="0990006";
				if(userid.indexOf("@")!=-1){
					userid=oU.getUserID(userid, wxscmid);
				}
				HttpSession session = request.getSession();
				session.setAttribute("userId",userid);
				session.setAttribute("wxscmid",wxscmid);
				//得到用户消息
				Users user=oU.getUser(userid, wxscmid);
				//获取用户所在部门以及子部门
				List<Department> listD=new ArrayList<Department>();
				List<String> listS=oU.getListScm(user.getScm(), wxscmid);
				Department d=null;
				for (int i = 0; i < listS.size(); i++) {
					d=new Department();
					OperateDepartment oD=new OperateDepartment();
					d=oD.selectDepartment(listS.get(i), wxscmid);
					if(d!=null)
					listD.add(d);
				}
				//获取用户所在部门以及子部门的人员消息
				List<Users> listU=oU.getListUser(user.getScm(), wxscmid);
				//获取消息类别
				OperateOaflb oaflb=new OperateOaflb();
				List<Oaflb> listO=new ArrayList<Oaflb>();
				listO=oaflb.selectOaflb();
				request.setAttribute("listO", listO);
				request.setAttribute("userId",userid);
				request.setAttribute("scm",user.getScm());
				request.setAttribute("listD",listD);
				request.setAttribute("listU",listU);
				request.getRequestDispatcher("oaggtz/oaggtzInsert.jsp").forward(request, response);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}