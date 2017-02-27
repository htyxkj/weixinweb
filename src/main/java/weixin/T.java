package weixin;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import weixin.connection.users.OperateUsers;
import weixin.pojo.AccessToken;
import weixin.pojo.Users;
import weixin.thread.TokenThread;
import weixin.util.WeixinUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class T {
	public static String randomString(int i){
		Random ran=new Random(i);
		StringBuilder sb=new StringBuilder();
		for(int n=0;;n++){
			int k=ran.nextInt(27);
			if(k==0)
				break;
			sb.append((char)('`'+k));
		}
		return sb.toString();
	}
	public static void main(String[] args) {
		System.out.println(randomString(-229985452)+randomString(-147909649));
		for(int i=0;i<100;i++){ 
			System.out.println(ofRandom(0,255));} 
	}
	private static char[] ofRandom(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}
}