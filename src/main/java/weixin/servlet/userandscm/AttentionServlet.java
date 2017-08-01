package weixin.servlet.userandscm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weixin.util.SignUtil;


public class AttentionServlet extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(AttentionServlet.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		
//		// 微信加密签名  
//        String signature =URLDecoder.decode(request.getParameter("signature"),"utf-8");  
//        // 时间戳  
//        String timestamp = URLDecoder.decode(request.getParameter("timestamp"),"utf-8");   
//        // 随机数  
//        String nonce = URLDecoder.decode(request.getParameter("nonce"),"utf-8");   
//        // 随机字符串  
//        String echostr = URLDecoder.decode(request.getParameter("echostr"),"utf-8");
//		String sEchoStr; //需要返回的明文
//		try {
//			 SignUtil wxcpt = new SignUtil("htyxkj08","", "");
//			sEchoStr = wxcpt.VerifyURL(signature, timestamp,
//					nonce, echostr);
//			System.out.println("verifyurl echostr: " + sEchoStr);
//			OutputStream outputStream = response.getOutputStream();
//	        //注意编码格式，防止中文乱码  
//	        outputStream.write(sEchoStr.getBytes("UTF-8"));
//	        outputStream.close();
//		}catch(Exception e){
//			e.printStackTrace();
//			log.info(e.toString());
//		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
}