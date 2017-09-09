package weixin.servlet.exp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;
import weixin.connection.exp.OperateBasres;
import weixin.connection.users.OperateUsers;
import weixin.pojo.AccessToken;
import weixin.pojo.Basres;
import weixin.thread.TokenThread;
import weixin.util.WeixinUtil;

public class InsertExpServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			WeixinUtil wxutil=new WeixinUtil();
			HttpSession session=request.getSession();
			String corpid=(String) session.getAttribute("wxscmid");
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			String wxscmid = request.getParameter("wxscmid");
			String w_appid = request.getParameter("w_appid");
			String scm = request.getParameter("scm");//公司或部门
			String scm1=scm;
			Map<String, AccessToken> map = TokenThread.maplist;
			AccessToken accessToken = map.get(wxscmid+"-"+w_appid);
			String sid=request.getParameter("sid");
			OperateBasres ob=new OperateBasres();
			Basres basres=ob.getOneB("BXLB", wxscmid,sid);
			String sbuid=basres.getSbuid();//业务号
			String pcell = basres.getZucheng();//平台内的对象
			
			//BXC{A5}{Y2M}%
			String[] arr=scm.split("");
			if(arr.length<5){
				for(int i=0;i<5-arr.length;i++){
					scm="0"+scm;
				}
			}
			sid=sid.replaceAll("\\{A5\\}",scm);
			SimpleDateFormat sdf1=new SimpleDateFormat("yyMM");
			sid=sid.replaceAll("\\{Y2M\\}",sdf1.format(new Date()));
			
			
			String userid =request.getParameter("smake");//制单人，报销人
			String p_bank=request.getParameter("p_bank");//收款人开户行**
			String p_account=request.getParameter("p_account");//收款人账号**
			String fcy=request.getParameter("fcy");//金额
			String hpdate=request.getParameter("hpdate");//日期
			String remark=request.getParameter("remark");//报销说明
			String khh=request.getParameter("khh");
			String zh=request.getParameter("zh");
			if(khh!=""&&zh!=""){
				OperateUsers oU=new OperateUsers();
				oU.upUser(khh, zh, corpid, userid);
			}
			String[] idxno=request.getParameterValues("idxno");//发票项目
			String[] inv_no=request.getParameterValues("inv_no");//发票编号
			String[] deduction=request.getParameterValues("deduction");//可抵扣否
			String[] inv_type=request.getParameterValues("inv_type");//发票类别
			String[] sp_tax=request.getParameterValues("sp_tax");//特殊票别
			String[] addtaxrt=request.getParameterValues("addtaxrt");//增值税率
			String[] fcys=request.getParameterValues("fcys");//金额
			String[] addtax=request.getParameterValues("addtax");//增值税金
			String[] rmbhs=request.getParameterValues("rmbhs");//无税金额
			String[] remarks=request.getParameterValues("remarks");//摘要
			
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			//主对象
			JSONObject json=new JSONObject();
			json.put("sid", sid);
			json.put("scm1", scm1);
			json.put("sbuid", sbuid);
			json.put("mkdate", sdf.format(new Date()));
			json.put("state", "0");
			json.put("sopr", userid);
			json.put("smake", userid);
			json.put("p_account", p_account);
			json.put("p_bank", p_bank);
			json.put("remark", remark);
			json.put("fcy", Double.parseDouble(fcy==""?"0":fcy));
			json.put("hpdate", hpdate);

			JSONObject json1=new JSONObject();
			String ea="";
			for(int i=0;i<idxno.length;i++){
				json1.put("cid", (i+1)+"");
				json1.put("idxno", idxno[i]);
				json1.put("inv_no", inv_no[i]);
				json1.put("deduction",deduction[i]);
				json1.put("inv_type", inv_type[i]);
				json1.put("sp_tax", sp_tax[i]);
				json1.put("fcy", Double.parseDouble(fcys[i]==""?"0":fcys[i]));
				json1.put("addtaxrt", Double.parseDouble(addtaxrt[i]==""?"0":addtaxrt[i]));
				json1.put("addtax", Double.parseDouble(addtax[i]==""?"0":addtax[i]));
				json1.put("remark", remarks[i]);
				json1.put("rmbhs", Double.parseDouble(rmbhs[i]==""?"0":rmbhs[i]));
				if(i==idxno.length-1){
					ea+=json1.toString();
				}else{
					ea+=json1.toString()+",";
				}
			}
			String _zi=pcell.substring(pcell.indexOf("(")+1, pcell.indexOf(")"));
			if(_zi.indexOf(";")==-1){
				json.put(_zi, "["+ea+"]");
			}else{
			}
			String serverUrl=accessToken.getServerurl();
			String dbid=accessToken.getDbid();
			String info = URLEncoder.encode(json.toString(),"utf-8");
			Map map2=new HashMap<String, String>();
			map2.put("apiId", "savedata");
			map2.put("dbid", dbid);
			map2.put("usercode", userid);
			map2.put("datatype", "1");
			map2.put("pcell", pcell);
			map2.put("jsonstr", info);
			
			String outInfo=WeixinUtil.httpclient(serverUrl+"api/",map2);
			
			String message="操作失败！";
			JSONObject json_1=JSONObject.fromObject(outInfo);  
			if(json_1!=null&&json_1.getInt("id")==0){
				message=json_1.getString("message");
			}
			request.setAttribute("message", message);
			String _url="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+wxscmid+"&redirect_uri=";
			String url="";
			url=""+accessToken.getDomainName()+"/weixinweb/ExpServlet?w_appid="+w_appid+"&wxscmid="+wxscmid+"&message="+message;
			url=URLEncoder.encode(url, "UTF-8");
			_url+=url;
			_url+="&response_type=code&scope=snsapi_base#wechat_redirect";
			response.sendRedirect(_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}