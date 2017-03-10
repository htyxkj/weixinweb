package weixin.servlet.userandscm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weixin.connection.accessToken.AccessTokenDo;
import weixin.pojo.AccessToken;
import weixin.pojo.Insorg;
import weixin.pojo.Inswaplist;
import weixin.thread.TokenThread;
import weixin.util.WeixinUtil;

public class AddScmServlet extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(AddScmServlet.class);
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	/**
	 *有新的公司加入  获取操作令牌
	 **/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String zt="";
		//流里面拿
		String jsonstr = "";
		InputStream is = request.getInputStream();
		InputStreamReader isr = new InputStreamReader(is, "utf-8");
		BufferedReader br = new BufferedReader(isr);
		jsonstr=br.readLine();
		jsonstr=URLDecoder.decode(jsonstr,"UTF-8");
 		isr.close();
		is.close();
		//去除数据中的换行符
		jsonstr=jsonstr.replaceAll("\r\n", "");
		jsonstr=jsonstr.replaceAll("\r", "");
		jsonstr=jsonstr.replaceAll("\n", "");
		jsonstr=jsonstr.replaceAll("\t", "");
		//String json = "{\"w_corpid\": \"微信企业号ID\",\"w_secret\": \"secret序列号\",\"companyid\": \"公司标识\",\"w_trusturl\": \"移动端地址串（域名）\",\"app\": [{\"wapno\": \"平台应用编号  01  02 \",\"w_applyid\": \"微信应用ID\"},{\"wapno\": \"平台应用编号  01  02 \",\"w_applyid\": \"微信应用ID\"},{\"wapno\": \"平台应用编号  01  02 \",\"w_applyid\": \"微信应用ID\"}]}";
		JSONObject jsonObject = JSONObject.fromObject(jsonstr);
		String c_corp=jsonObject.getString("c_corp");//集团编码
		String CorpIDid=jsonObject.getString("w_corpid");//企业号标识
		String Secret=jsonObject.getString("w_secret");//管理组标识
		String CompanyId=jsonObject.getString("companyid");//公司标识
		String w_trusturl=jsonObject.getString("w_trusturl");//url(域名)
		if(w_trusturl.indexOf("http://")==-1)
			w_trusturl="http://"+w_trusturl;
		String serverurl=jsonObject.getString("serverurl");//信息来源地址
		AccessToken accessToken=new AccessToken();
//		TokenThread tokenThread = new TokenThread();
		WeixinUtil wu=new WeixinUtil();
		accessToken = WeixinUtil.getAccessToken(CorpIDid, Secret);
		accessToken.setServerurl(serverurl);
		accessToken.setDomainName(w_trusturl);
		if(accessToken.getToken().equals("40013")){
	        zt="-1;微信企业号ID录入错误";
			OutputStream outputStream = response.getOutputStream();
	        //注意编码格式，防止中文乱码  
	        outputStream.write(zt.getBytes("UTF-8"));
	        outputStream.close(); 
	        return;
		}
		if(accessToken.getToken().equals("40001")){
			zt="-1;Secret序列号录入错误";
			OutputStream outputStream = response.getOutputStream();
	        //注意编码格式，防止中文乱码  
	        outputStream.write(zt.getBytes("UTF-8"));
	        outputStream.close(); 
	        return;
		}
		//获取操作令牌
		if (null != accessToken) {
            log.info("获取access_token成功，获取时间："+sdf.format(new Date())+"有效时长{}秒 token:{}", accessToken.getExpiresIn(), accessToken.getToken());
        	TokenThread.maplist.put(CorpIDid, accessToken);
		}
		//检查公司微信息是否储存在本地数据库
		AccessTokenDo acd=new AccessTokenDo();
		Insorg insorg=new Insorg();
		Inswaplist inswaplist=null;
		JSONArray arry = JSONArray.fromObject(jsonObject.get("app"));
		//将公司微信息储存到本地数据库
		insorg.setOrgcode(CompanyId);
		insorg.setW_corpid(CorpIDid);
		insorg.setW_secret(Secret);
		insorg.setC_corp(c_corp);
		insorg.setW_trusturl(w_trusturl);
		insorg.setServerurl(serverurl);
		String state=acd.SelectScm(insorg);
		for (int i = 0; i < arry.size(); i++) {
			JSONObject json = arry.getJSONObject(i);
			inswaplist=new Inswaplist();
			inswaplist.setW_applyid(json.getString("w_applyid"));
			inswaplist.setWapno(json.getString("wapno"));
			inswaplist.setOrgcode(json.getString("companyid"));
			String str[]=null;
				String requestUrl="https://qyapi.weixin.qq.com/cgi-bin/agent/get?access_token="+accessToken.getToken()+"&agentid="+json.getString("w_applyid");
				JSONObject jsobj=wu.httpRequest(requestUrl, "GET", null);
				if(jsobj.get("errcode").equals("40056")){
					zt="-1;应用id录入错误:"+json.getString("w_applyid");
					OutputStream outputStream = response.getOutputStream();
			        //注意编码格式，防止中文乱码  
			        outputStream.write(zt.getBytes("UTF-8"));
			        outputStream.close(); 
			        return;
				}else{
					state=updateAppUrl(jsonObject.getString("w_corpid"),json.getString("w_applyid"),json.getString("wapno"),w_trusturl);
					str=state.split(",");
					if(str[0].equals("60011")){
						zt="-1;管理组权限不足";
						OutputStream outputStream = response.getOutputStream();
				        //注意编码格式，防止中文乱码  
				        outputStream.write(zt.getBytes("UTF-8"));
				        outputStream.close(); 
				        return;
			        }
					if(str[0].equals("0")){
						inswaplist.setW_wapurl(str[1]);
						acd.SelectApp(inswaplist);
						zt="0;完成";
					}
				}
		}
		OutputStream outputStream = response.getOutputStream();
        //注意编码格式，防止中文乱码  
        outputStream.write(zt.getBytes("UTF-8"));
        outputStream.close(); 
	}
	public String updateAppUrl(String wxscmid,String appid,String bipAppid,String redirect_domain) throws UnsupportedEncodingException{
		TokenThread tokenThread = new TokenThread();
		Map<String, AccessToken>  map = TokenThread.maplist;
		AccessToken acc = (AccessToken) map.get(wxscmid);
		WeixinUtil wu=new WeixinUtil();
		//应用指向的url
		String home_url="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+wxscmid+"&redirect_uri=";
		String url="";
		//01审批中心
		if(bipAppid.equals("01")){
			url=""+redirect_domain+"/weixinweb/ButtonServlet?state=0&wxscmid="+wxscmid;
		}
		//02任务中心
		if(bipAppid.equals("02")){
			url=""+redirect_domain+"/weixinweb/ButtonServlet?state=1&wxscmid="+wxscmid;
		}
		//03消息通知
		if(bipAppid.equals("03")){
		}
		url=URLEncoder.encode(url, "UTF-8");
		home_url+=url;
		home_url+="&response_type=code&scope=snsapi_base#wechat_redirect";
		String outputStr = "{\"agentid\": \""+appid+"\",\"home_url\":\""+home_url+"\",\"redirect_domain\":\""+redirect_domain.replaceAll("http://", "")+"\"}";
		String requestMethod = "POST";
		String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/agent/set?access_token="+acc.getToken();
		JSONObject jsobj=wu.httpRequest(requestUrl, requestMethod, outputStr);
		return jsobj.get("errcode")+","+home_url;
	}
}