package weixin.servlet.userandscm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import weixin.util.SignUtil;


public class AttentionServlet extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(AttentionServlet.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
//			log.info("hhhhhhhhh");
//			String xml = "";
//			InputStream is = request.getInputStream();
//			InputStreamReader isr = new InputStreamReader(is, "utf-8");  
//			BufferedReader br = new BufferedReader(isr);
//			xml=br.readLine();
//			xml=URLDecoder.decode(xml,"UTF-8");
//			StringReader read = new StringReader(xml);
//			//创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
//			InputSource source = new InputSource(read);
//			//创建一个新的SAXBuilder
//			SAXReader saxReader = new SAXReader();
//			Document document = saxReader.read(source);
//			String xpath="/xml/ToUserName";
//			Element element = (Element) document.selectSingleNode(xpath);
//			System.out.println("ToUserName:"+element.getText());
//			log.info("ToUserName:"+element.getText());
//			xpath="/xml/FromUserName";
//			element = (Element) document.selectSingleNode(xpath);
//			System.out.println("FromUserName:"+element.getText());
//			log.info("FromUserName:"+element.getText());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}