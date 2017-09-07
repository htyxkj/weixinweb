package weixin;
import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.io.OutputStream;  
import java.net.ConnectException;  
import java.net.HttpURLConnection;  
import java.net.URL;  
  
import java.net.URLEncoder;
import java.util.List;

import net.sf.json.JSONObject;
import weixin.key.SRegServ;
import weixin.pojo.Message;
import weixin.pojo.PageInfo;

public class test {
	/**  
     * 发起http请求并获取结果  
     * 
     * @param requestUrl 请求地址  
     * @param requestMethod 请求方式（GET、POST）  
     * @param outputStr 提交的数据  
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)  
	 * @throws Exception
     */    
	public final static char DIV_CELL = (char) 0x1F;
	public static void main(String[] args) throws Exception {
		String pcell="190301(190301A)";
		System.out.println(pcell.substring(6+1, 14));
		
		
		
//		String bbbString="1:1002:1013:1024:1035:1046:1057:1068:1079:10810:109";
//		int i = bbbString.indexOf(DIV_CELL);
//		while (i>0) {
//			System.out.println(bbbString.substring(0,i));
//			bbbString = bbbString.substring(i+1);
//			i = bbbString.indexOf(DIV_CELL);
//		}
//		System.out.println(bbbString);
//		JSONObject json=new JSONObject();
//		json.accumulate("apiid", "savedate");
//		json.accumulate("dbid", "2");
//		json.accumulate("usercode", "0990006");
//		json.accumulate("pcell", "190311(190311A)");
//		json.accumulate("datetype", "json");
//		json.accumulate("jsonstr", "\"{\"sid_w\":\"\",\"sid_b\":\"\",\"mkdate\":\"ThuAug3109:03:57CST2017\",\"state\":\"0\",\"sopr\":\"0050004\",\"smake\":\"0050004\",\"p_account\":\"16986565646565\",\"p_bank\":\"交通银行\",\"remark\":\"报销\",\"fcy\":\"75729.0\",\"hpdate\":\"ThuAug3100:00:00CST2017\",\"bxlb\":\"01\",\"corpid\":\"wx33a1a7296219a4fb\",\"child\":[{\"sid_w\":\"\",\"sid_b\":\"\",\"cid\":\"1\",\"idxno\":\"00001\",\"inv_no\":\"fpbh7889878987987\",\"deduction\":\"0\",\"inv_type\":\"01\",\"sp_tax\":\"1\",\"fcy\":\"985.0\",\"addtaxrt\":\"0.2\",\"addtax\":\"164.17\",\"rmbhs\":\"820.83\",\"remark\":\"44\",\"corpid\":\"wx33a1a7296219a4fb\"},{\"sid_w\":\"\",\"sid_b\":\"\",\"cid\":\"2\",\"idxno\":\"00001\",\"inv_no\":\"24545\",\"deduction\":\"0\",\"inv_type\":\"01\",\"sp_tax\":\"1\",\"fcy\":\"74744.0\",\"addtaxrt\":\"0.1\",\"addtax\":\"6794.91\",\"rmbhs\":\"67949.09\",\"remark\":\"45444\",\"corpid\":\"wx33a1a7296219a4fb\"}]}\"");
//		System.out.println(json.toString());
//		
//		String a="{\"apiid\":\"savedate\",\"dbid\":\"2\",\"usercode\":\"0990006\",\"pcell\":\"190311(190311A)\",\"datetype\":\"json\",\"jsonstr\":\"\"{\"sid_w\":\"\",\"sid_b\":\"\",\"mkdate\":\"ThuAug3109:03:57CST2017\",\"state\":\"0\",\"sopr\":\"0050004\",\"smake\":\"0050004\",\"p_account\":\"16986565646565\",\"p_bank\":\"交通银行\",\"remark\":\"报销\",\"fcy\":\"75729.0\",\"hpdate\":\"ThuAug3100:00:00CST2017\",\"bxlb\":\"01\",\"corpid\":\"wx33a1a7296219a4fb\",\"child\":[{\"sid_w\":\"\",\"sid_b\":\"\",\"cid\":\"1\",\"idxno\":\"00001\",\"inv_no\":\"fpbh7889878987987\",\"deduction\":\"0\",\"inv_type\":\"01\",\"sp_tax\":\"1\",\"fcy\":\"985.0\",\"addtaxrt\":\"0.2\",\"addtax\":\"164.17\",\"rmbhs\":\"820.83\",\"remark\":\"44\",\"corpid\":\"wx33a1a7296219a4fb\"},{\"sid_w\":\"\",\"sid_b\":\"\",\"cid\":\"2\",\"idxno\":\"00001\",\"inv_no\":\"24545\",\"deduction\":\"0\",\"inv_type\":\"01\",\"sp_tax\":\"1\",\"fcy\":\"74744.0\",\"addtaxrt\":\"0.1\",\"addtax\":\"6794.91\",\"rmbhs\":\"67949.09\",\"remark\":\"45444\",\"corpid\":\"wx33a1a7296219a4fb\"}]}\"\"}";
//		JSONObject jsonObj=JSONObject.fromObject(a);
//		System.out.println(jsonObj.toString());
				
//		bb.replaceAll(regex, replacement);
//		bb.charAt(index);
//		SRegServ t=new SRegServ();
//		Object[] obj=(Object[]) t.processOperator("isReg","10");
//		System.out.println(obj[0]);
//		System.out.println(obj[1]);
//			String a="{\"type\":\"1\",\"title\":\"通知加查看\",\"content\":\"居然为我而体育阿斯顿法傻大个撒快乐玩儿体育哦划局快乐玩规划局快乐\",\"users\":\"0990006\",\"w_corpid\":\"wx33a1a7296219a4fb\",\"appid\":\"10\",\"bipappid\":\"03\",\"scm\":\"03\"}";
//			a=URLEncoder.encode(a, "UTF-8");
////			String  requestUrl="http://192.168.0.104:9999/jd/weixinInf?tip=99&dbid=40";
////			String  requestUrl="http://139.129.222.205/weixinweb/InformServlet";
//			String  requestUrl="http://127.0.0.1:8080/weixinweb/TestServlet";
//			String  requestMethod="POST";
//	        StringBuffer buffer = new StringBuffer();
//	        InputStream inputStream=null;
//	        try {
//	            URL url = new URL(requestUrl);
//	            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
//	            httpUrlConn.setDoOutput(true);
//	            httpUrlConn.setDoInput(true);
//	            httpUrlConn.setUseCaches(false);
//	            //设置请求方式（GET/POST）
//	            httpUrlConn.setRequestMethod(requestMethod);
//	            if ("GET".equalsIgnoreCase(requestMethod))
//	                httpUrlConn.connect();
//	            // 当有数据需要提交时
//	            if (null != a) {
//	                OutputStream outputStream = httpUrlConn.getOutputStream();
//	                // 注意编码格式，防止中文乱码
//	                outputStream.write(a.getBytes("UTF-8"));
//	                outputStream.close();
//	            }
//	            //将返回的输入流转换成字符串
//	            inputStream = httpUrlConn.getInputStream();
//	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
//	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//	            String str = null;
//	            while ((str = bufferedReader.readLine()) != null) {
//	                buffer.append(str);
//	            }
//	            bufferedReader.close();
//	            inputStreamReader.close();
//	            // 释放资源    
//	            inputStream.close();
//	            inputStream = null;
//	            httpUrlConn.disconnect();
//	            String ab=buffer.toString();
//	            System.out.println(ab);
//	        } catch (ConnectException ce) {
//	              ce.printStackTrace();
//	        } catch (Exception e) {
//	               e.printStackTrace();  
//	        }finally{
//	            try {
//	                if(inputStream!=null){
//	                    inputStream.close();
//	                }
//	            } catch (IOException e) {
//	                e.printStackTrace();
//	            }
//	       }
	}
}