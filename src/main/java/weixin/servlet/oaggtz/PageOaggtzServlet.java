package weixin.servlet.oaggtz;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weixin.connection.oaggtz.OperateOaggtz;
import weixin.connection.users.OperateUsers;
import weixin.pojo.Oaggtz;
import weixin.pojo.Users;

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
			Users user = (Users) request.getSession().getAttribute("sessionUser");
			String userId = user.getUserid();
			String wxscmid = user.getW_corpid();
			String ddscmid = user.getD_corpid();
			String pageType = request.getParameter("pageType");
			String offsetStr = request.getParameter("offset");
			List<Oaggtz> list=new ArrayList<Oaggtz>();
			OperateUsers o = new OperateUsers();
			//对请求参数进行校验
			if (!StringUtil.isEmpty(userId) && !StringUtil.isEmpty(wxscmid) && !StringUtil.isEmpty(pageType) && !StringUtil.isEmpty(offsetStr) && offsetStr.matches("\\d+")) {
			    Integer offset = Integer.parseInt(offsetStr);
			    String read = "0";
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
			    list = showOagg.ShowAll(userId,scm, read, wxscmid,ddscmid, offset);
			    Oaggtz data=null;
			    for (int i=0;i<list.size();i++) {
			    	data=new Oaggtz();
			    	data=list.get(i);
			    	Users uu = new Users();
			    	uu.setUserid(data.getSmaker());
			    	uu.setD_corpid(data.getD_corpid());
			    	uu.setW_corpid(data.getW_corpid());
			    	
			    	Users u = o.showUserName(uu);
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
		}
	}
}