package weixin.servlet;


import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  

import weixin.thread.TokenThread;  
import weixin.util.WeixinUtil;  

import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  

public class InitServlet extends HttpServlet {  
    private static final long serialVersionUID = 1L;  
    private static Logger log = LoggerFactory.getLogger(InitServlet.class); 
    TokenThread tokenThread=new TokenThread();
    /**
     * 获取access_token标识（链接令牌）
     */
    public void init() throws ServletException {
            log.info("InitServlet init() 启动初始化方法...");
        	new Thread(new TokenThread()).start();
    }
}  