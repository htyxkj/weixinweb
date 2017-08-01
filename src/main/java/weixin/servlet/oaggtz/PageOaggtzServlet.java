package weixin.servlet.oaggtz;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import weixin.connection.message.ShowData;
import weixin.connection.oaggtz.OperateOaggtz;
import weixin.connection.users.OperateUsers;
import weixin.pojo.AccessToken;
import weixin.pojo.FuJian;
import weixin.pojo.Message;
import weixin.pojo.Oaggtz;
import weixin.pojo.Users;
import weixin.thread.TokenThread;

import com.opslab.util.StringUtil;

public class PageOaggtzServlet extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(OneOaggtzServlet.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
		try {
			HttpSession session = request.getSession();
			String userId = (String) session.getAttribute("userId");
			String wxscmid = (String) session.getAttribute("wxscmid");
			String pageType = request.getParameter("pageType");
			String offsetStr = request.getParameter("offset");
			List<Oaggtz> list=new ArrayList<Oaggtz>();
			OperateUsers o = new OperateUsers();
			Users user=new Users();
			user=o.getUser(userId, wxscmid);
			//对请求参数进行校验
			if (!StringUtil.isEmpty(userId) && !StringUtil.isEmpty(wxscmid) && !StringUtil.isEmpty(pageType) && !StringUtil.isEmpty(offsetStr) && offsetStr.matches("\\d+")) {
			    Integer offset = Integer.parseInt(offsetStr);
			    String read = "";
			    OperateOaggtz showOagg=new OperateOaggtz();
			    if (pageType.equals("wode")) {
			    	read = "2";
			    }
			    if (pageType.equals("weidu")) {
			    	read = "0";
			    }
			    if (pageType.equals("yidu")) {
			    	read = "1";
			    }
			    String scm="";
			    if(user!=null)
			    	scm=user.getScm();
			    list = showOagg.ShowAll(userId,scm, read, wxscmid, offset);
			    Oaggtz data=null;
			    for (int i=0;i<list.size();i++) {
			    	data=new Oaggtz();
			    	data=list.get(i);
			    	Users u = o.showUserName(data.getSmaker(), null, data.getW_corpid());
			    	if(u!=null)
			    	list.get(i).setSmaker(u.getUsername());
			    	list.get(i).setContent(list.get(i).getContent().replaceAll("\\|",""));
				}
			}else{
			    list = new ArrayList<Oaggtz>();
			}
			JSONArray json = JSONArray.fromObject(list);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(json.toString().getBytes("UTF-8"));
			outputStream.close();
		}catch (Exception e) {
			e.printStackTrace();
			log.info(e + "");
            //获取员工信息失败   重新获取
//            String _url = "" +accessToken.getDomainName()
//                    + "/weixinweb/OneOaggtzServlet?wxscmid=" + wxscmid
//                    + "&keyid=" + keyid;
//            _url = URLEncoder.encode(_url, "UTF-8");
//            _url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
//                    + wxscmid
//                    + "&redirect_uri="
//                    + _url;
//            _url += "&response_type=code&scope=snsapi_base#wechat_redirect";
//            response.sendRedirect(_url);
//            return;
		}
	}
}