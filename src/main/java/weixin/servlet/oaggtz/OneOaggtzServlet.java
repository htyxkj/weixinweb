package weixin.servlet.oaggtz;

import java.awt.event.KeyAdapter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;
import weixin.connection.message.ShowData;
import weixin.connection.oaggtz.OperateOaggtz;
import weixin.connection.users.OperateUsers;
import weixin.pojo.AccessToken;
import weixin.pojo.FuJian;
import weixin.pojo.Message;
import weixin.pojo.Oaggtz;
import weixin.pojo.Users;
import weixin.thread.TokenThread;
import weixin.util.WeixinUtil;
/**
 * 
 * @author Administrator
 *查询单个公告
 */
public class OneOaggtzServlet extends HttpServlet {
//	private static String SERVER_PATH_BODY = "/db_40/";
	private static Logger log = LoggerFactory.getLogger(OneOaggtzServlet.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			this.doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String read = request.getParameter("read");
        String w_appid = request.getParameter("w_appid");
//		String userid=request.getParameter("userid");
        String wxscmid = request.getParameter("wxscmid");
        TokenThread tokenThread = new TokenThread();
        Map<String, AccessToken> map = tokenThread.maplist;
        AccessToken accessToken = map.get(wxscmid+"-"+w_appid);
        //用户同意授权后，能获取到code
        String code = request.getParameter("code");
        String keyid = request.getParameter("keyid");
        try {
            if (!"authdeny".equals(code) && code != null) {
                String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=" + accessToken.getToken() + "&code=" + code;
                JSONObject jsonObj = WeixinUtil.httpRequest(requestUrl, "GET", null);
//				获取员工id
                String userid = jsonObj.getString("UserId");
                if (userid.indexOf("@") != -1) {
                    OperateUsers oU = new OperateUsers();
                    userid = oU.getUserID(userid, wxscmid);
                }
                OperateOaggtz oaggtz=new OperateOaggtz();
                Oaggtz data= oaggtz.selectOne(keyid);
                
                OperateUsers o = new OperateUsers();
                Users u = o.showUserName(data.getSmaker(), null, data.getW_corpid());
                List<FuJian> listFuJian = new ArrayList<FuJian>();
                if(u!=null)
                data.setSmaker(u.getUsername());
                if (data != null) {
                    if (data.getFj_root() != null && data.getSuri()!=null&& !data.getFj_root().equals("")&& !data.getSuri().equals("")) {
                        String fjPathStr =data.getFj_root();
                        String fjNameStr =data.getSuri();
                        FuJian fj = new FuJian();
                        fj.setServerPathBody("");
                        fj.setFilePath(fjPathStr);
                        fj.setFileName(fjNameStr);
                        if(data.getSource().equals("W")){
                        	fj.setServerPathHead(accessToken.getDomainName()+"weixinweb");
                        	fj.setFullPath(accessToken.getDomainName()+"\\weixinweb" + fjPathStr + fjNameStr);
                        }else if(data.getSource().equals("B")){
                        	fj.setServerPathHead(accessToken.getServerurl());
                        	fj.setFullPath(accessToken.getServerurl()+"fileupdown?fud=1&rid=4&isweb=1&dbid="+data.getDbid()+"&filepath=" + fjPathStr + fjNameStr);
                        }
                        log.info(fj.getFullPath());
                        listFuJian.add(fj);
                    }
                }
                String content=data.getContent();
                data.setContent(content.replaceAll("\\|", "<\\br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
                data.setServerurl(accessToken.getServerurl());
                request.setAttribute("data", data);
                request.setAttribute("fujians", listFuJian);
                request.getRequestDispatcher("oaggtz/oneOaggtz.jsp").forward(request, response);
            }
        } catch (Exception e) {
            log.info(e + "");
            //获取员工信息失败   重新获取
            String _url = "" +accessToken.getDomainName()
                    + "/weixinweb/OneOaggtzServlet?w_appid="+w_appid+"&wxscmid=" + wxscmid
                    + "&keyid=" + keyid;
            _url = URLEncoder.encode(_url, "UTF-8");
            _url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
                    + wxscmid
                    + "&redirect_uri="
                    + _url;
            _url += "&response_type=code&scope=snsapi_base#wechat_redirect";
            response.sendRedirect(_url);
            return;
        }
	}
}