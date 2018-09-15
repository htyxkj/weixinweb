package weixin.servlet.userandscm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weixin.connection.users.OperateUsers;
import weixin.pojo.AccessToken;
import weixin.pojo.Users;
import weixin.thread.TokenThread;
import weixin.util.HttpUtil;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiUserCreateRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.request.OapiUserUpdateRequest;
import com.dingtalk.api.response.OapiUserCreateResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.dingtalk.api.response.OapiUserUpdateResponse;
import com.taobao.api.ApiException;

/**
 * 
 * ClassName: AddUserServlet
 * @Description: 功能描述: 同步平台用户至微信 钉钉
 * company:北京斯坦德科技发展有限公司
 * @date 2018年9月10日下午4:41:24
 */
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
		String zt="0;同步完成";
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
		JSONObject jsonObject = JSONObject.fromObject(jsonstr);
		JSONArray arry = JSONArray.fromObject(jsonObject.get("users"));
		for (int i = 0; i < arry.size(); i++) {
			//单条信息
			JSONObject jsonuser = arry.getJSONObject(i);
			System.out.println(jsonuser);
			str += wxUser(jsonuser);
			str += ddUser(jsonuser);
			Users users = new Users();
			users.setUserid(jsonuser.getString("usrcode"));
			users.setUsername(jsonuser.getString("usrname"));
			users.setTel(jsonuser.getString("tel"));
			users.setScm(jsonuser.getString("scm"));
			users.setEmail(jsonuser.getString("email"));
			users.setW_corpid(jsonuser.getString("w_corpid"));
			users.setD_corpid(jsonuser.getString("d_corpid"));
			//查询本地数据库是否有该员工
			OperateUsers o=new OperateUsers();
			Users us=o.showUserName(users);
			if(us!=null){
				o.uodateUsers(users);
			}else if(us==null){
				users.setD_imgurl("img/ren.png");
				users.setW_imgurl("img/ren.png");
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
	/**
	 * 同步微信用户
	 */
	private static String wxUser(JSONObject jsonuser){
		String userid = null;
		JSONObject jsonobj = null;
		String zt = "";
		String wxscmid=jsonuser.getString("w_corpid");
		AccessToken acc = TokenThread.maplist.get(wxscmid+"-"+"00");
		userid=jsonuser.getString("usrcode");
		//数据--添加
		String jsonString="{\"userid\": \""+userid+"\",\"name\": \""+jsonuser.get("usrname")+"\",\"department\": [1],\"mobile\":\""+jsonuser.get("tel")+"\",\"email\":\""+jsonuser.get("email")+"\"}";
		//数据--修改
		String xgString="{\"userid\": \""+userid+"\",\"name\": \""+jsonuser.get("usrname")+"\",\"mobile\":\""+jsonuser.get("tel")+"\",\"email\":\""+jsonuser.get("email")+"\"}";
		//查询企业号内是否有该员工   有进行修改   没有进行添加
		if(acc!=null){
			String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token="
					+ acc.getW_accessToken() + "&userid=" + userid;
			jsonobj = HttpUtil.httpRequest(requestUrl, "POST", jsonString);
			if(jsonobj.get("errcode").equals("50002")){
				zt="-1;成员不在权限范围";
			}
			if (jsonobj.get("errmsg").equals("ok")) {//有
				requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token=" + acc.getW_accessToken();
				jsonobj = HttpUtil.httpRequest(requestUrl, "POST", xgString); 
			} else {//没有
				requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=" + acc.getW_accessToken();
				jsonobj = HttpUtil.httpRequest(requestUrl, "POST", jsonString);
			}
			log.info(jsonobj);
			if(!jsonobj.get("errcode").toString().equals("0")){
				zt="-1;"+jsonuser.get("usrname")+"同步失败";
			} 
		} 
		return zt;
	}
	/**
	 * 同步钉钉用户
	 */
	private static String ddUser(JSONObject jsonuser){
		String zt = "";
		try {
			String ddscmid=jsonuser.getString("d_corpid");
			String userid=jsonuser.getString("usrcode");
			AccessToken acc = TokenThread.maplist.get("dd-"+ddscmid);
			DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get");
			OapiUserGetRequest request = new OapiUserGetRequest();
			request.setUserid(userid);
			request.setTopHttpMethod("GET");
			OapiUserGetResponse response = client.execute(request, acc.getD_accessToken());
			if(response.getUserid() == null){
				DingTalkClient creatClient = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/create");
				OapiUserCreateRequest crearRequest = new OapiUserCreateRequest();
				crearRequest.setUserid(userid);
				crearRequest.setMobile((String) jsonuser.get("tel"));
				crearRequest.setName((String) jsonuser.get("usrname"));
				// 需要用字符串， "[59869009,60345027]" 这种格式 
				crearRequest.setDepartment("[1]");
				OapiUserCreateResponse creresponse = creatClient.execute(crearRequest, acc.getD_accessToken());
				if(!creresponse.getErrcode().equals("0")){
					zt="-1;"+jsonuser.get("usrname")+"同步失败";
				}
			}else{
				DingTalkClient upClient = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/update");
				OapiUserUpdateRequest upRequest = new OapiUserUpdateRequest();
				upRequest.setUserid(userid);
				upRequest.setName((String) jsonuser.get("usrname"));
				upRequest.setMobile((String) jsonuser.get("tel"));
				OapiUserUpdateResponse upresponse = upClient.execute(upRequest,  acc.getD_accessToken());
				if(!upresponse.getErrcode().equals("0")){
					zt="-1;"+jsonuser.get("usrname")+"同步失败";
				}
			}
		} catch (ApiException e) {
			e.printStackTrace();
		} 
		return zt;
	}
}