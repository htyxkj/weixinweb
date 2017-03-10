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
	public static void main(String[] args) throws Exception {
			String a="{\"type\":\"1\",\"title\":\"通知加查看\",\"content\":\"居然为我而体育阿斯顿法傻大个撒快乐玩儿体育哦划局快乐玩规划局快乐\",\"users\":\"0990006\",\"w_corpid\":\"wx33a1a7296219a4fb\",\"appid\":\"10\",\"bipappid\":\"03\",\"scm\":\"03\"}";
			a=URLEncoder.encode(a, "UTF-8");
//			String  requestUrl="http://192.168.0.104:9999/jd/weixinInf?tip=99&dbid=40";
//			String  requestUrl="http://139.129.222.205/weixinweb/InformServlet";
			String  requestUrl="http://127.0.0.1:8080/weixinweb/TestServlet";
			String  requestMethod="POST";
	        StringBuffer buffer = new StringBuffer();
	        InputStream inputStream=null;
	        try {
	            URL url = new URL(requestUrl);
	            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
	            httpUrlConn.setDoOutput(true);
	            httpUrlConn.setDoInput(true);
	            httpUrlConn.setUseCaches(false);
	            //设置请求方式（GET/POST）
	            httpUrlConn.setRequestMethod(requestMethod);
	            if ("GET".equalsIgnoreCase(requestMethod))
	                httpUrlConn.connect();
	            // 当有数据需要提交时
	            if (null != a) {
	                OutputStream outputStream = httpUrlConn.getOutputStream();
	                // 注意编码格式，防止中文乱码
	                outputStream.write(a.getBytes("UTF-8"));
	                outputStream.close();
	            }
	            //将返回的输入流转换成字符串
	            inputStream = httpUrlConn.getInputStream();
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String str = null;
	            while ((str = bufferedReader.readLine()) != null) {
	                buffer.append(str);
	            }
	            bufferedReader.close();
	            inputStreamReader.close();
	            // 释放资源    
	            inputStream.close();
	            inputStream = null;
	            httpUrlConn.disconnect();
	            String ab=buffer.toString();
	            System.out.println(ab);
	        } catch (ConnectException ce) {
	              ce.printStackTrace();
	        } catch (Exception e) {
	               e.printStackTrace();  
	        }finally{
	            try {
	                if(inputStream!=null){
	                    inputStream.close();
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	       }
	}
}