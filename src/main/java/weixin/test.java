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
import weixin.util.WeixinUtil;

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
			String a="{\"kf_account\" : \"wangyudong@wx515d2c7b6c6a2339\",\"nickname\" : \"王玉东\" }";
			String  requestUrl="https://api.weixin.qq.com/customservice/kfaccount/add?access_token=9I7U1Q0CAStcKYil5x-bd1kyQFTQfM6j_w4G_NrYCcE3dt5C5mNDOSiD4qzYe_GLY5W8ljyJ3lt0d6dVhexzvZJ6xVzXTiUoyy7ctk0S2D3PZRsvW1rYcDm-6zwWr3SPFZLdACAJDP";
			String  requestMethod="POST";
	        StringBuffer buffer = new StringBuffer();
	        InputStream inputStream=null;
	        WeixinUtil.httpRequest(requestUrl, requestMethod, a);
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