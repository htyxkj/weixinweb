package weixin.servlet.oaggtz.task;

import java.util.List;

import weixin.connection.users.OperateUsers;
import weixin.util.SendTxtToUser;

public class MyTask implements NoticeI {
	private String corpid,appid,scm;
	public static List<String> ListSU=null;
	public void run() {
		try { 
			OperateUsers oU=new OperateUsers();
			ListSU=oU.getListUid("",corpid);
	    	for (int j = 0; j < ListSU.size(); j++) {
	    		String tous=""+ListSU.get(j);
				//统计每个人有多少条未读消息？
				//进行发送
				SendTxtToUser txttou=new  SendTxtToUser();
				txttou.informUser("您有新的公告请查看!",tous, corpid,appid, scm);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init(String corpid,String appid,String scm) {
		this.corpid = corpid;
		this.appid = appid;
		this.scm = scm;
		new Thread(this).start();
	}
}