package weixin.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;
import weixin.T1;
import weixin.connection.message.ReceiveData;
import weixin.connection.message.UpdateDate;
import weixin.key.SRegServ;
import weixin.pojo.Message;
import weixin.util.SendTxtToUser;

public class TestServlet extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(TestServlet.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	/**
	 *平台 提交退回  
	 *state=1 :提交
	 *state=-1:退回
	 **/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			String jsonstr = "";
			InputStream is = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");  
			BufferedReader br = new BufferedReader(isr);
			jsonstr=br.readLine();
			jsonstr=URLDecoder.decode(jsonstr,"UTF-8");
	 		isr.close();
			is.close();
			//去除数据中的换行符
			jsonstr=jsonstr.replaceAll("\r\n", "%0d%0a");
			jsonstr=jsonstr.replaceAll("\r", "%0d");
			jsonstr=jsonstr.replaceAll("\n", "%0a");
			jsonstr=jsonstr.replaceAll("null", "");
			
			JSONObject jsonObject = JSONObject.fromObject(jsonstr);
			String state=jsonObject.getString("state");
			String zt="";
			if(state.equals("1")){
				zt=tj(jsonObject);
			}else if(state.equals("-1")){
				zt=th(jsonObject);
				if(Integer.parseInt(zt)>0){
					zt="ok";
				}
			}
			if(zt.equals("ok")){
				zt="ok";
			}else{
				zt="no";
			}
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
	/**
	 *提交方法
	 **/
	public String tj(JSONObject jsonObject) throws Exception{
		Message mess=new Message();
		String str=jsonObject.getString("spname");
		String zt="";
		ReceiveData r=new ReceiveData();
		SendTxtToUser sendTxt=new SendTxtToUser();
		String[] array=str.split("\\|");
		UpdateDate up=new UpdateDate();
		for(String spweixinid : array) {
			mess.setTitle(jsonObject.getString("title"));//标题**/
			mess.setName(jsonObject.getString("name"));//提交人姓名**/
			mess.setSpweixinid(spweixinid);//审批人微信**/
			mess.setSpname(spweixinid);//审批人姓名**/
			
			
			mess.setContent(URLDecoder.decode(jsonObject.getString("content"),"UTF-8"));// 审批内容 **/
			mess.setTjtime(new Date());//提交时间**/
			mess.setGs(jsonObject.getString("scm"));//公司**/
			mess.setScm(jsonObject.getString("scm"));//公司**/
			mess.setState1(jsonObject.getString("state1"));//目标状态**/
			mess.setState0(jsonObject.getString("state0"));//来源状态**/
			mess.setDocumentsid(jsonObject.getString("documentsid"));//单据编号**/
			mess.setDocumentstype(jsonObject.getString("documentstype"));//单据类型**/
			mess.setTablename(jsonObject.getString("tablename"));//表名**/
			mess.setSbuId(jsonObject.getString("sbuid"));//业务号**/
			mess.setDepartment(jsonObject.getString("department"));//公司**/
			mess.setDbid(jsonObject.getString("dbid"));//公司**/
			mess.setAppid(jsonObject.getString("appid"));//应用id**/
			mess.setWapno(jsonObject.getString("wapno"));//平台定义应用id**/
			mess.setW_corpid(jsonObject.getString("w_corpid"));//微信企业号标识**/
			mess.setSmake(jsonObject.getString("smake"));//制单人
			//为解决上级审批可能出现的垃圾数据，进行数据清洗
//			updateState(mess);
			//录入数据
			zt=r.jieshou(mess);
			//通知微信端
			zt=sendTxt.tosend(null,spweixinid,mess.getW_corpid(),mess.getScm(),mess.getAppid());
		}
		//统计发送记录
//		up.ToHistory(mess.getDocumentsid(), mess.getW_corpid(), "0", mess.getState1(),null);
		return zt;
	}

	private void updateState(Message mess) {
		ReceiveData r=new ReceiveData();
		r.updateState(mess);
	}

	/**
	 *退回方法
	 **/
	public String th(JSONObject jsonObject){
		log.info(jsonObject+"");
		String documentsid=jsonObject.getString("documentsid");
		String w_corpid=jsonObject.getString("w_corpid");
		String str=jsonObject.getString("spweixinid");
		UpdateDate up=new UpdateDate();
		SendTxtToUser sendTxt=new SendTxtToUser();
		Message message=new Message();
		message.setDocumentsid(documentsid);
		message.setW_corpid(w_corpid);
		String[] array=str.split("\\|");
//		up.ToHistory(message.getDocumentsid(), message.getW_corpid(), "0",null,null);
		String num=up.TuiHuiDelete(message);
		if(str.equals(""))
			str=jsonObject.getString("spname");
		for (String string : array) {
			sendTxt.tosend(null, string, jsonObject.getString("w_corpid"),jsonObject.getString("scm"),jsonObject.getString("appid"));
		}
		return num;
	}
}