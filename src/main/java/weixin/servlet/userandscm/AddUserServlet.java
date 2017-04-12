package weixin.servlet.userandscm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.ParseException;
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
import weixin.pojo.Users;
import weixin.thread.TokenThread;
import weixin.util.WeixinUtil;

public class AddUserServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(AddUserServlet.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	/**
	 *将平台用户批量添加到微信端
	 **/
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String zt="";
		String str="";
		try {
		String jsonstr ="";
		//获取post参数
		InputStream is = request.getInputStream();
		InputStreamReader isr = new InputStreamReader(is, "utf-8");  
		BufferedReader br = new BufferedReader(isr);
		jsonstr=br.readLine();
		jsonstr=URLDecoder.decode(jsonstr,"UTF-8");
		//去除数据中的换行符
		jsonstr=jsonstr.replaceAll("\r\n", "");
		jsonstr=jsonstr.replaceAll("\r", "");
		jsonstr=jsonstr.replaceAll("\n", "");
		jsonstr=jsonstr.replaceAll("\t", "");
		JSONObject jsonobj = null;
		JSONObject jsonObject = JSONObject.fromObject(jsonstr);
		JSONArray arry = JSONArray.fromObject(jsonObject.get("users"));
		Users users=null;
		for (int i = 0; i < arry.size(); i++) {
			users=new Users();
			OperateUsers o=new OperateUsers();
			//微信链接信息
			JSONObject jsonuser = arry.getJSONObject(i);
			String wxscmid=jsonuser.getString("w_corpid");
			TokenThread tokenThread = new TokenThread();
			Map<String, AccessToken>  map = TokenThread.maplist;
			AccessToken acc = (AccessToken) map.get(wxscmid);
			String userid = null;
			if(jsonuser.getString("email")!=null&&!jsonuser.getString("email").equals("")&&!jsonuser.getString("email").equals("\"null\"")){
				userid=jsonuser.getString("email");
			}else{
				userid=jsonuser.getString("usrcode");
			}
			//数据--添加
			String jsonString="{\"userid\": \""+userid+"\",\"name\": \""+jsonuser.get("usrname")+"\",\"department\": [1],\"mobile\":\""+jsonuser.get("tel")+"\",\"email\":\""+jsonuser.get("email")+"\"}";
			//数据--修改
			String xgString="{\"userid\": \""+userid+"\",\"name\": \""+jsonuser.get("usrname")+"\",\"mobile\":\""+jsonuser.get("tel")+"\",\"email\":\""+jsonuser.get("email")+"\"}";
			System.out.println(jsonString);System.out.println(xgString);
			System.out.println("=========================================");
			users.setUserid(jsonuser.getString("usrcode"));
			users.setUsername(jsonuser.getString("usrname"));
			users.setTel(jsonuser.getString("tel"));
			users.setScm(jsonuser.getString("scm"));
			users.setEmail(jsonuser.getString("email"));
			users.setW_corpid(jsonuser.getString("w_corpid"));
			//查询企业号内是否有该员工   有进行修改   没有进行添加
			if(acc!=null){
				String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token="
						+ acc.getToken() + "&userid=" + jsonuser.get("usrcode");
				jsonobj = WeixinUtil.httpRequest(requestUrl, "POST", jsonString);
				if(jsonobj.get("errcode").equals("50002")){
					zt="-1;成员不在权限范围";
				}
				if (jsonobj.get("errmsg").equals("ok")) {//有
					requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token="
							+ acc.getToken();
					jsonobj = WeixinUtil.httpRequest(requestUrl, "POST", xgString);
					if(jsonobj.get("errcode").toString().equals("0")){
						zt="0;同步完成";
					}
				} else {//没有
					requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token="
							+ acc.getToken();
					jsonobj = WeixinUtil.httpRequest(requestUrl, "POST", jsonString);
					if(jsonobj.get("errcode").toString().equals("0")){
						zt="0;同步完成";
					}
					if(jsonobj.get("errcode").toString().equals("60103")){
						str+="-1;"+jsonuser.get("usrname")+",手机号码格式不正确";
					}
					if(jsonobj.get("errcode").toString().equals("60104")){
						str+="-1;手机号："+jsonuser.getString("tel")+"已存在！";
					}
				}
				//员工关注  获得头像url		
				requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token="
						+ acc.getToken()
						+ "&userid="
						+ jsonuser.getString("usrcode");
				jsonobj = WeixinUtil.httpRequest(requestUrl, "GET", null);
				if (jsonobj.containsKey("avatar") == true) {
					users.setImgurl(jsonobj.getString("avatar"));
				} else {
					users.setImgurl("img/ren.png");
				}
			}
			//查询本地数据库是否有该员工
			Users us=o.showUserName(users.getUserid(),users.getScm(),users.getW_corpid());
			if(us!=null){
				o.uodateUsers(users);
				o.uodateUsersImgUrl(users);
			}else if(us==null){
				o.insertUser(users);
			}
		}
		if(!str.equals(""))
			zt=str;
		log.info(str);
		OutputStream outputStream = response.getOutputStream();  
        outputStream.write(zt.getBytes("UTF-8"));
        outputStream.close(); 
        isr.close();
		is.close();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}