package weixin.servlet.userandscm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import weixin.connection.users.OperateUsers;
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
		log.info("收到删除用户请求！");
		try {
			//流里面拿
			String jsonstr = "";
			InputStream is = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			jsonstr=br.readLine();
			jsonstr=URLDecoder.decode(jsonstr,"UTF-8");
			isr.close();
			is.close();
			JSONObject jsonObject = JSONObject.fromObject(jsonstr);
			log.info(jsonstr);
			String userid=jsonObject.getString("userid");//集团编码
			String wxscmid=jsonObject.getString("corpid");//企业号标识
			TokenThread tokenThread=new TokenThread();
			Map<String, AccessToken> map=tokenThread.maplist;
			AccessToken acc=map.get(wxscmid+"-00");
			String requestUrl="https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token="+acc.getToken()+"&userid="+userid;
			JSONObject jsonobj=WeixinUtil.httpRequest(requestUrl, "GET", null);
			log.info(jsonobj);
			String _out="";
			OperateUsers oU=new OperateUsers();
			if(jsonobj.getString("errcode").equals("0")){
				oU.delUser(userid, wxscmid);
				_out="1;删除成功!";
			}else if(jsonobj.getString("errcode").equals("60111")){
				oU.delUser(userid, wxscmid);
				_out="1;删除成功!";
			}else{
				_out="-1;删除失败!";
			}
			PrintWriter out = response.getWriter();
			out.write(_out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}