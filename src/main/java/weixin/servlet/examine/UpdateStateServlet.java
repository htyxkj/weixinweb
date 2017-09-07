package weixin.servlet.examine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;
import weixin.connection.message.ShowData;
import weixin.connection.message.UpdateDate;
import weixin.pojo.Message;
import weixin.util.SendTxtToUser;

public class UpdateStateServlet extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(UpdateStateServlet.class);  
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	/**
	 * 平台  同意，驳回  重置审批信息。
	 **/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			log.info("UpdateStateServlet");
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			String jsonstr = "";
			InputStream is = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");  
			BufferedReader br = new BufferedReader(isr);
			jsonstr=br.readLine();
			jsonstr=URLDecoder.decode(jsonstr,"UTF-8");
			log.info(jsonstr);
			jsonstr=jsonstr.replaceAll("\r\n", "");
			jsonstr=jsonstr.replaceAll("\r", "");
			jsonstr=jsonstr.replaceAll("\n", "");
			jsonstr=jsonstr.replaceAll("\t", "");
			jsonstr=jsonstr.replaceAll("null", "");
			JSONObject jsonObject = JSONObject.fromObject(jsonstr);
			String w_corpid,scm,documentsid;
			w_corpid=jsonObject.getString("w_corpid");
			scm=jsonObject.getString("scm");
			documentsid=jsonObject.getString("documentsid");
			String rows ="";
			ShowData sh=new ShowData();
			UpdateDate up=new UpdateDate();
			Message message=new Message();
			SendTxtToUser sendTxt=new SendTxtToUser();
			String state1=jsonObject.getString("state1");
			String spname=jsonObject.getString("spname");
			String yjcontent=jsonObject.getString("yjcontent");
			if("\"null\"".equals(yjcontent) || yjcontent==null){
				yjcontent = "";
			}
			message.setDocumentsid(documentsid);
			message.setState1(state1);
			message.setState(jsonObject.getInt("state"));
			message.setSpname(spname);
			message.setW_corpid(w_corpid);
			message.setYjcontent(yjcontent);
			log.info(message.getState()+"");
			if(jsonObject.getInt("state")==0){//重置审批单据
				String zt=weidu(jsonObject);
			}else if(jsonObject.getInt("state")==-1){//驳回
				ShowData showData=new ShowData();
				List<Message> lm=showData.showjilu(documentsid,w_corpid,"tz");
				message.setSptime(new Date());
				message.setState(2);
				rows =up.BipUpdate(message);
				rows = up.BipTuiHui(message);
//				String name=sh.showName(w_corpid,scm,documentsid);
//				sendTxt.tosend("您有一条被驳回消息,请去平台处理！", name,jsonObject.getString("w_corpid"),jsonObject.getString("appid"),scm);
				for(Message m :lm){
					sendTxt.tosend(null, m.getSpname(),jsonObject.getString("w_corpid"),scm,m.getAppid());
				}
			}else{
				//同意
				ShowData showData=new ShowData();
				List<Message> lm=showData.showjilu(documentsid,w_corpid,"tz");
				message.setSptime(new Date());
				rows =up.BipUpdate(message);
				rows = up.BipTuiHui(message);
				log.info(lm.size()+"");
				for(Message m :lm){
					sendTxt.tosend(null, m.getSpname(),jsonObject.getString("w_corpid"),scm,m.getAppid());
				}
			}
			isr.close();
			is.close();
			JSONObject json = new JSONObject();
			json.put("zt", "ok");
			OutputStream outputStream = response.getOutputStream();  
			// 注意编码格式，防止中文乱码  
			outputStream.write(json.toString().getBytes("UTF-8"));
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e+"");
		}
	}
	/**
	 *将审批数据重置为未读状态
	 **/
	public String weidu(JSONObject jsonObject){
		UpdateDate up=new UpdateDate();
		Message message=new Message();
		message.setDocumentsid(jsonObject.getString("documentsid"));
		message.setState1(jsonObject.getString("state1"));
		message.setSpname(jsonObject.getString("spname"));
		message.setW_corpid(jsonObject.getString("w_corpid"));
		String num=up.ToWeiDu(message);
		SendTxtToUser sendTxt=new SendTxtToUser();
		sendTxt.tosend(null, jsonObject.getString("spweixinid"),jsonObject.getString("w_corpid"),jsonObject.getString("scm"),jsonObject.getString("appid"));
//		up.ToHistory(message.getDocumentsid(), message.getW_corpid(), null,message.getState1()+"",message.getSpname());
		return num;
	}
}