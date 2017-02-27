package weixin.servlet.inform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weixin.connection.message.ReceiveData;
import weixin.connection.message.ShowData;
import weixin.connection.text.OperateTextData;
import weixin.pojo.AccessToken;
import weixin.pojo.Message;
import weixin.pojo.PageInfo;
import weixin.pojo.Text;
import weixin.thread.TokenThread;
import weixin.util.SendTxtToUser;
import net.sf.json.JSONObject;

public class InformServlet extends HttpServlet {
	/**
	 * 消息通知
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		try {
			String zt="";
			//流里面拿平台提交数据
			String jsonstr = "";
			InputStream is = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			jsonstr=br.readLine();
			jsonstr=URLDecoder.decode(jsonstr,"UTF-8");
	 		isr.close();
			is.close();
			JSONObject jsonObject = JSONObject.fromObject(jsonstr);
			//{"title":"XXXX","type":"XXXX","content":"XXXX","users":"XXXX|XXXX|XXXX","w_corpid":"XXXX","appid":"XXXX","bipappid":"XXXX"}
			String conetnt=jsonObject.getString("content");
			String users=jsonObject.getString("users");
			String appid=jsonObject.getString("appid");;
			String w_corpid=jsonObject.getString("w_corpid");
			String type=jsonObject.getString("type");
			String title=jsonObject.getString("title");
			String bipappid=jsonObject.getString("bipappid");
			String scm=jsonObject.getString("scm");
			Text txt=new Text();
			txt.setType(type);
			txt.setTitle(title);
			txt.setContent(conetnt);
			txt.setUsers(users);
			txt.setW_corpid(w_corpid);
			txt.setAppid(appid);
			txt.setBipappid(bipappid);
			txt.setScm(scm);
			
			TokenThread tokenThread=new TokenThread();
		 	Map<String, AccessToken> map=tokenThread.maplist;
		 	AccessToken accessToken=map.get(w_corpid);
		 	
			OperateTextData oTxtD=new OperateTextData();
			
			/**如果type 是仅通知  conetnt=conetnt  数据不写入数据库 
			  *如果type 是通知加查看URL conetnt=生成url 数据写入数据库
			  */
//			if(type.equals("1")){
//				
//			}else if(type.equals("0")){
//				Date time=new Date();
//				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//				txt.setTime(sdf.parse(sdf.format(time)));
//				Integer id=oTxtD.insertTxt(txt);//刚刚插入的数据id
//				//生成url
//				String _url = "" + accessToken.getDomainName()
//						+ "/weixinweb/ShowTextServlet?wxscmid=" + w_corpid
//						+ "&id="+id;
//				_url = URLEncoder.encode(_url, "UTF-8");
//				_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
//						+ w_corpid
//						+ "&redirect_uri="
//						+ _url;
//				_url += "&response_type=code&scope=snsapi_base#wechat_redirect";
//				conetnt=_url;
//			}
			//通知Users
			SendTxtToUser sendTxt=new SendTxtToUser();
			zt=sendTxt.informUser(conetnt, users, w_corpid, appid, scm);
			JSONObject json = new JSONObject();
			json.put("zt", zt);
			OutputStream outputStream = response.getOutputStream();
	        //注意编码格式，防止中文乱码
			outputStream.write(json.toString().getBytes("UTF-8"));
	        outputStream.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
