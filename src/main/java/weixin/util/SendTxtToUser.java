package weixin.util;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;
import weixin.connection.message.ShowData;
import weixin.connection.number.OperateNum;
import weixin.connection.users.OperateUsers;
import weixin.pojo.AccessToken;
import weixin.pojo.Num;
import weixin.thread.TokenThread;

public class SendTxtToUser extends BaseDao{
	private static Logger log = LoggerFactory.getLogger(SendTxtToUser.class);
	/**
	 * 发送消息 
	 * @param conetnt 消息内容
	 * @param weixinid 接收人 企业号内id
	 * @param wxscmid 企业号标识
	 * @param appid	企业号内应用id
	 * @return 执行状态 
	 */
	public String informUser(String conetnt,String weixinid,String wxscmid,String appid,String scm){
		String zt="";
		try {
			if(weixinid.indexOf("@")==-1){
				OperateUsers oU=new OperateUsers();
				String email=oU.getEmail(weixinid, wxscmid);
				if(email!=null&&!email.equals("")&&!email.equals("null")&&!email.equals("\"null\""))
					weixinid=email;
			}
			TokenThread tokenThread=new TokenThread();
		 	Map<String, AccessToken> map=tokenThread.maplist;
		 	AccessToken acc=map.get(wxscmid);
		 	if(acc!=null){
			String requestUrl="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+acc.getToken();
			String outputStr="{\"touser\": \""+weixinid+"\","//
					+ "\"msgtype\": \"text\",  "
					+ "\"agentid\": "+appid+", "
					+ "\"text\": { \"content\": \""+conetnt+"\" },"
					+ "\"safe\":0}"; 
			String requestMethod="POST";
			JSONObject jsonObject=WeixinUtil.httpRequest(requestUrl, requestMethod, outputStr);
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
		}catch (Exception e) {
			e.printStackTrace();
		}
		return zt;
	}
	/**
	 * 执行 发送 消息 并统计待审数据条数
	 */
	public String tosend(String keyid,String weixinid,String wxscmid,String appid,String scm){
		String zt="";
		try {
			//使用erpcode作为微信账号 不检测邮箱账号
//			if(weixinid.indexOf("@")==-1){
//				OperateUsers oU=new OperateUsers();
//				String email=oU.getEmail(weixinid, wxscmid);
//				if(email!=null&&!email.equals("")&&!email.equals("null")&&!email.equals("\"null\""))
//					weixinid=email;
//			}
			ShowData show=new ShowData();
			TokenThread tokenThread=new TokenThread();
			String str[]=weixinid.split("\\|");
			for (String string : str) {
				int num1=show.showNum(string, "0",wxscmid);
			 	Map<String, AccessToken> map=tokenThread.maplist;
			 	AccessToken acc=map.get(wxscmid);
			 	if(acc!=null){
			 		String content="";
		 			if(num1==0){
		 				content="您暂无待审批任务";
		 			}else{
		 				content="您有"+num1+"条待审批任务";
		 			}
		 			if(keyid!=null)
		 				content=keyid;
					String requestUrl="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+acc.getToken();
					String outputStr="{\"touser\": \""+string+"\","//
							+ "\"msgtype\": \"text\",  "
							+ "\"agentid\": "+appid+", "
							+ "\"text\": { \"content\": \""+content+"\" },"
							+ " \"safe\":0}";
					String requestMethod="POST";
					JSONObject jsonObject=WeixinUtil.httpRequest(requestUrl, requestMethod, outputStr);
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
}