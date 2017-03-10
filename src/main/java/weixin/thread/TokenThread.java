package weixin.thread;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weixin.connection.accessToken.AccessTokenDo;
import weixin.pojo.AccessToken;
import weixin.util.WeixinUtil;

public class TokenThread implements Runnable{
	 	private static final Log log = LogFactory.getLog(TokenThread.class);
	 	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    // 第三方用户唯一凭证  
	    public static String appid;  
	    // 第三方用户唯一凭证密钥  
	    public static String appsecret;  
	    public static AccessToken accessToken = null;  
	    public Map<String, AccessToken> map=null;
	    public static Map<String, AccessToken> maplist=new HashMap<String, AccessToken>();
	    public void run() {
	    	while (true) {  
	            try {
	            	//从数据库里拿每个公司微信配置信息
	            	AccessTokenDo access=new AccessTokenDo();
	            	List<AccessToken> atd=access.getListAcc(null);
	            	for(int i=0;i<atd.size();i++){
	            		accessToken = WeixinUtil.getAccessToken(atd.get(i).getCorpIDid(), atd.get(i).getSecret());
	            		if (null != accessToken) {  
	            			accessToken.setApplyId(atd.get(i).getApplyId());//应用id
		            		accessToken.setCompanyId(atd.get(i).getCompanyId());//公司标识
		            		accessToken.setCorpIDid(atd.get(i).getCorpIDid());//企业号标识
		            		accessToken.setDomainName(atd.get(i).getDomainName());//访问域名
		            		accessToken.setServerurl(atd.get(i).getServerurl());//信息来源地址
		                    log.info("获取access_token成功,获取时间:"+sdf.format(new Date())+",有效时长"+accessToken.getExpiresIn()+"秒 token:"+accessToken.getToken()+"");
		                }else {
		                	 //未获得链接令牌  休眠5秒后重新获取
			                 Thread.sleep(5000);  
						}
	            		if(accessToken!=null)
	            			maplist.put(accessToken.getCorpIDid(), accessToken);
	            	}
	            	// 休眠7000秒  
                    Thread.sleep(7000000);  
	            }catch (Exception ex) {
	            	try {
	            		//出现异常10秒后重新获取
						Thread.sleep(10000);
						StackTraceElement stackTraceElement= ex.getStackTrace()[0];
						log.error("File="+stackTraceElement.getFileName());
						log.error("Line="+stackTraceElement.getLineNumber());
						log.error("Method="+stackTraceElement.getMethodName());
						log.error("https request error:{}", ex);  
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}  
	        }  
	    }
}