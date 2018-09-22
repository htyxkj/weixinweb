package weixin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 对资源文件的修改，添加
 * 
 */
public class T {
	public static void main(String[] args) throws UnsupportedEncodingException {
		String a =URLEncoder.encode("娃哈哈","utf-8");
		System.err.println(a);
		
//		String jsonstr="{\"c_corp\": \"0\",\"companyid\": \"0\",\"w_corpid\": \"wx33a1a7296219a4fb\",\"w_secret\": \"D_8QaDT3r9Xrnp0lNCPskvaKvp6Ab0tEQF4inRCMV5U\",\"w_trusturl\": \"anjili.bip-soft.com:83\",\"serverurl\": \"http://115.28.191.203:8080/dtpmp/\",\"app\": [{\"wapno\": \"00\",\"w_applyid\": \"00\",\"companyid\": \"0\",\"w_appsecret\": \"D_8QaDT3r9Xrnp0lNCPskvaKvp6Ab0tEQF4inRCMV5U\"}, {\"wapno\": \"01\",\"w_applyid\": \"6\",\"companyid\": \"0\",\"w_appsecret\": \"Lpl05DHvgxA9zfDrXiGpyQkXVoUtG6-tLVndxG_eAJM\"}, {\"wapno\": \"03\",\"w_applyid\": \"10\",\"companyid\": \"0\",\"w_appsecret\": \"Dtev6RpofafZMcAhYJWDrDMYNYY0WGzo3MEZwdU-ttI\"}, {\"wapno\": \"04\",\"w_applyid\": \"8\",\"companyid\": \"0\",\"w_appsecret\": \"mVsRafXYgvau_LW28qxwoOOR7-jZdAcC93h0vHHREPw\"}]}";
//		JSONObject jsonObject = JSONObject.fromObject(jsonstr);
//		System.out.println(jsonstr);
//		String dbid="01";
//		if(jsonObject.containsKey("dbid"))
//			dbid = jsonObject.getString("dbid");
//		System.out.println(dbid);
	}
}