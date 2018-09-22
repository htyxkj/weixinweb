package weixin.thread;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weixin.connection.accessToken.AccessTokenDo;
import weixin.pojo.AccessToken;
import weixin.util.HttpUtil;

public class TokenThread implements Runnable{
 	private static final Log log = LogFactory.getLog(TokenThread.class);
 	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public static AccessToken accessToken = null;  
    public static Map<String, AccessToken> maplist=new HashMap<String, AccessToken>();
    public void run() {
    	while (true){
        	try {
				wxAccToken();
				ddAccToken();
				Thread.sleep(7000000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}  
    	}
    }
    /**
     * 获取微信AccessToken
     */
    public static void wxAccToken(){
    	log.info("获取微信AccessToken");
    	//从数据库里拿每个公司微信配置信息
    	AccessTokenDo access=new AccessTokenDo();
    	List<AccessToken> atd=access.getListWxAccess();
    	for(int i=0;i<atd.size();i++){
    		if(atd.get(i).getW_corpIDid()==null||atd.get(i).getW_corpIDid().equals("")||atd.get(i).getW_corpIDid().indexOf("wx")==-1){
    			continue;
    		}
    		accessToken = HttpUtil.getWxAccessToken(atd.get(i).getW_corpIDid(), atd.get(i).getW_secret());
    		if (null != accessToken) {
    			accessToken.setW_applyId(atd.get(i).getW_applyId());//应用id
        		accessToken.setCompanyId(atd.get(i).getCompanyId());//公司标识
        		accessToken.setW_corpIDid(atd.get(i).getW_corpIDid());//企业号标识
        		accessToken.setDomainName(atd.get(i).getDomainName());//访问域名
        		accessToken.setServerurl(atd.get(i).getServerurl());//信息来源地址
        		accessToken.setDbid(atd.get(i).getDbid());//数据库连接标识
        		maplist.put("wx-"+accessToken.getW_corpIDid()+"-"+atd.get(i).getW_applyId(), accessToken);
            }
    	}
    }
    /**
     * 获取钉钉AccessToken
     */
    public static void ddAccToken(){
    	log.info("获取钉钉AccessToken");
    	//从数据库里拿每个公司微信配置信息
    	AccessTokenDo access=new AccessTokenDo();
    	List<AccessToken> atd=access.getListDdAccess();
    	for(int i=0;i<atd.size();i++){
    		if(atd.get(i).getD_corpIDid()==null||atd.get(i).getD_corpIDid().equals("")||atd.get(i).getD_corpIDid().indexOf("ding")==-1){
    			continue;
    		}
    		accessToken = HttpUtil.getDdAccessToken(atd.get(i).getD_corpIDid(), atd.get(i).getD_secret());
    		if (null != accessToken) {
    			accessToken.setD_corpIDid(atd.get(i).getD_corpIDid());
    			accessToken.setD_secret(atd.get(i).getD_secret());
        		accessToken.setCompanyId(atd.get(i).getCompanyId());//公司标识
        		accessToken.setDomainName(atd.get(i).getDomainName());//访问域名
        		accessToken.setServerurl(atd.get(i).getServerurl());//信息来源地址
        		accessToken.setDbid(atd.get(i).getDbid());//数据库连接标识
        		maplist.put("dd-"+accessToken.getD_corpIDid(), accessToken);
            }
    	}
    }
    
}