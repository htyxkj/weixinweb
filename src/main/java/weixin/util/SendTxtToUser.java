package weixin.util;

import java.util.Date;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weixin.connection.message.ShowData;
import weixin.connection.number.OperateNum;
import weixin.connection.users.OperateUsers;
import weixin.pojo.AccessToken;
import weixin.pojo.Num;
import weixin.thread.TokenThread;

public class SendTxtToUser{
	private static Logger log = LoggerFactory.getLogger(SendTxtToUser.class);
	/**
	 * 发送微信消息 
	 * @param conetnt 消息内容
	 * @param weixinid 接收人 企业号内id
	 * @param wxscmid 企业号标识
	 * @param appid	企业号内应用id
	 * @return 执行状态 
	 */
	public static String wxInformUser(String conetnt,String weixinid,String wxscmid,String appid,String scm){
		String zt="";
		try {
			String str[]=weixinid.split("\\|");
			for (String string : str) {
				weixinid=string;
				if(weixinid.indexOf("@")==-1){
					OperateUsers oU=new OperateUsers();
					String email=oU.getEmail(weixinid, wxscmid);
					if(email!=null&&!email.equals("")&&!email.equals("null")&&!email.equals("\"null\""))
						weixinid=email;
				}
			 	AccessToken acc=TokenThread.maplist.get("wx-"+wxscmid+"-"+appid);
			 	if(acc!=null){
				String requestUrl="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+acc.getW_accessToken();
				String outputStr="{\"touser\": \""+weixinid+"\","//
						+ "\"msgtype\": \"text\",  "
						+ "\"agentid\": "+appid+", "
						+ "\"text\": { \"content\": \""+conetnt+"\" },"
						+ "\"safe\":0}"; 
				String requestMethod="POST";
				JSONObject jsonObject=HttpUtil.httpRequest(requestUrl, requestMethod, outputStr);
				log.info(outputStr+"返回码:"+jsonObject.getString("errcode"));
				zt=jsonObject.getString("errmsg");
				OperateNum num=new OperateNum();
				if(jsonObject.getInt("errcode")==0){
					Num m=new Num();
					m.setAppid(appid);
					m.setW_corpid(wxscmid);
					m.setWeixinid(weixinid);
					m.setScm(scm);
					m.setTime(new Date());
					m.setContent(conetnt);
					num.insert(m);
					zt="ok";
				}else{
					zt="no";
				}
			 	}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return zt;
	}
	/**
	 * 执行 发送 消息 并统计待审数据条数
	 */
	public static String wxToSendSPMsg(String keyid,String weixinid,String wxscmid,String scm,String appid){
		String zt="";
		try {
			ShowData show=new ShowData();
			String str[]=weixinid.split("\\|");
			for (String string : str) {
				if(string==null||string .equals(""))
					continue;
				int num1=show.showNum(string, "0",wxscmid);
			 	AccessToken acc=TokenThread.maplist.get("wx-"+wxscmid+"-"+appid);
			 	if(acc!=null){
			 		String content="";
		 			if(num1==0){
		 				content="您暂无待审批任务";
		 			}else{
		 				content="您有"+num1+"条待审批任务";
		 			}
		 			if(keyid!=null)
		 				content=keyid;
					String requestUrl="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+acc.getW_accessToken();
					String outputStr="{\"touser\": \""+string+"\","//
							+ "\"msgtype\": \"text\",  "
							+ "\"agentid\":"+appid+","
							+ "\"text\": { \"content\": \""+content+"\" },"
							+ " \"safe\":0}";
					String requestMethod="POST";
					JSONObject jsonObject=HttpUtil.httpRequest(requestUrl, requestMethod, outputStr);
					log.info(outputStr+"返回码:"+jsonObject.getString("errcode"));
					zt=jsonObject.getString("errmsg");
					OperateNum num=new OperateNum();
					if(jsonObject.getInt("errcode")==0){
						Num m=new Num();
						m.setAppid(appid);
						m.setW_corpid(wxscmid);
						m.setWeixinid(weixinid);
						m.setScm(scm);
						m.setTime(new Date());
						m.setContent(content);
						num.insert(m);
						zt="ok";
					}else{
						zt="no";
					}
			 	}else{
			 		zt="no";
			 	}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return zt;
	}
	
	/**
	 * 发送微信消息 
	 * @param conetnt 消息内容
	 * @param weixinid 接收人 id
	 * @param wxscmid 钉钉企业号标识
	 * @param appid	dd企业号内应用id
	 * @return 执行状态 
	 */
	public static String ddInformUser(String conetnt,String weixinid,String ddscmid,String appid,String scm){
		String zt="";
		try {
			String str[]=weixinid.split("\\|");
			for (String string : str) {
				weixinid=string;
			 	AccessToken acc=TokenThread.maplist.get("dd"+ddscmid);
			 	if(acc!=null){
				String requestUrl=APIAddr.DD_APP_MESSAGE.replace("ACCESS_TOKEN", acc.getW_accessToken());
				String txt = "{\"msgtype\":\"text\",\"text\":{\"content\":\""+conetnt+"\"}}";
				JSONObject json = new JSONObject();
				json.put("agent_id", appid);
				json.put("userid_list", weixinid);
				json.put("msg", txt);
				
				String requestMethod="POST";
				JSONObject jsonObject=HttpUtil.httpRequest(requestUrl, requestMethod, json.toString());
				log.info(json.toString()+"返回码:"+jsonObject.getString("errcode"));
				zt=jsonObject.getString("errmsg");
				OperateNum num=new OperateNum();
				if(jsonObject.getInt("errcode")==0){
					Num m=new Num();
					m.setAppid(appid);
					m.setW_corpid(ddscmid);
					m.setWeixinid(weixinid);
					m.setScm(scm);
					m.setTime(new Date());
					m.setContent(conetnt);
					num.insert(m);
					zt="ok";
				}else{
					zt="no";
				}
			 	}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return zt;
	}
	
	/**
	 * 执行 发送 消息 并统计待审数据条数
	 */
	public static String ddToSendSPMsg(String keyid,String weixinid,String ddscmid,String scm,String appid){
		String zt="";
		try {
			ShowData show=new ShowData();
			String str[]=weixinid.split("\\|");
			for (String string : str) {
				if(string==null||string .equals(""))
					continue;
				int num1=show.showNum(string, "0",ddscmid);
			 	AccessToken acc=TokenThread.maplist.get("dd-"+ddscmid);
			 	if(acc!=null){
			 		String content="";
		 			if(num1==0){
		 				content="您暂无待审批任务";
		 			}else{
		 				content="您有"+num1+"条待审批任务";
		 			}
		 			if(keyid!=null)
		 				content=keyid;
		 			String requestUrl=APIAddr.DD_APP_MESSAGE.replace("ACCESS_TOKEN", acc.getD_accessToken());
					String txt = "{\"msgtype\":\"text\",\"text\":{\"content\":\""+content+"\"}}";
					JSONObject json = new JSONObject();
					json.put("agent_id", appid);
//					json.put("userid_list", weixinid);
					json.put("userid_list", "063632273426289096");
					json.put("msg", txt);
					String requestMethod="POST";
					JSONObject jsonObject=HttpUtil.httpRequest(requestUrl, requestMethod, json.toString());
					log.info(json.toString()+"返回码:"+jsonObject.getString("errcode"));
//					zt=jsonObject.getString("errmsg");
					OperateNum num=new OperateNum();
					if(jsonObject.getInt("errcode")==0){
						Num m=new Num();
						m.setAppid(appid);
						m.setW_corpid(ddscmid);
						m.setWeixinid(weixinid);
						m.setScm(scm);
						m.setTime(new Date());
						m.setContent(content);
						num.insert(m);
						zt="ok";
					}else{
						zt="no";
					}
			 	}else{
			 		zt="no";
			 	}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return zt;
	}
	
	
	
	
	
}