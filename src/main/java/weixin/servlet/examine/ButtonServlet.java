package weixin.servlet.examine;

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
import weixin.connection.users.OperateUsers;
import weixin.pojo.AccessToken;
import weixin.pojo.Message;
import weixin.pojo.Users;
import weixin.thread.TokenThread;
import weixin.util.StringUtil;
import weixin.util.WeixinUtil;

public class ButtonServlet extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(ButtonServlet.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * 点击按钮待审 已审 驳回
	 **/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String state = request.getParameter("state");// 0待审 1//已审(通过) 2//驳回 3再见提交的记录
		String currentPage=request.getParameter("currentPage");
		log.info("currentPage"+currentPage);
		if(currentPage==null)
			currentPage="1";
		String pageSize=request.getParameter("pageSize");
		log.info("pageSize"+pageSize);
		if(pageSize==null)
			pageSize="10";
		log.info("state"+state);
		if (state == null || state.equals(""))
			state = "0";
		String title = "审批中心";
		String wxscmid = request.getParameter("wxscmid");
		if(wxscmid==null||wxscmid.equals(""))
			return;
		Map<String, AccessToken> map = TokenThread.maplist;
		AccessToken accessToken = map.get(wxscmid);
		//用户同意授权后，能获取到code
		String code = request.getParameter("code");
		try {
			if (!"authdeny".equals(code) && code != null) {
				String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token="
						+ accessToken.getToken()
						+"&code="
						+code;
				JSONObject jsonObj = WeixinUtil.httpRequest(requestUrl, "GET",null);
				//log.info(jsonObj.toString());
				//获取用户id
				String userid = jsonObj.getString("UserId");
				if(userid.indexOf("@")!=-1){
					OperateUsers oU=new OperateUsers();
					userid=oU.getUserID(userid, wxscmid);
				}
				ShowData show = new ShowData();
				//
//				Message msg=new Message();
//				msg.setSpweixinid(userid);
//				msg.setState(Integer.parseInt(state));
//				msg.setW_corpid(wxscmid);
//				PageInfo<Message> page=new PageInfo<Message>();
//				page.setCurrentPage(Integer.parseInt(currentPage));
//				page.setPageSize(Integer.parseInt(pageSize));
//				page.setCondition(msg);
//				PageInfo<Message> pageinfo = show.showState1(page);
//				List<Message> listM = pageinfo.getRows();
				//查询某个  状态  下用户的记录条数
				List<Message> listM = show.showState(userid, state, wxscmid);
				log.info("条数"+listM.size()+"id"+userid+"状态"+state+"企业号标识"+wxscmid);
				List<Message> listMNew = new ArrayList<Message>();
				for (Message message : listM) {
					OperateUsers o = new OperateUsers();
					//查询用户名
					Users u = o.showUserName(message.getName(),null,message.getW_corpid());
					if (u != null){
						message.setName(u.getUsername());
						if(u.getImgurl()!=null&&!u.getImgurl().equals(""))
						message.setTuUrl(u.getImgurl());
					}
					message.setTjtimeStr(StringUtil.timePass(message.getTjtime(), 4));
					listMNew.add(message);
				}
				//详情 按钮链接
				String xq_url1 = "" +accessToken.getDomainName()
						+ "/weixinweb/OAuthServlet?wxscmid=" + wxscmid
						+ "&keyid=";
				xq_url1 = URLEncoder.encode(xq_url1, "UTF-8");
				String xq_url0 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
						+ wxscmid
						+ "&redirect_uri="
						+ xq_url1;
				String xq_url2 = "&response_type=code&scope=snsapi_base#wechat_redirect";
				request.setAttribute("xqurl", xq_url0);
				request.setAttribute("url2", xq_url2);
				//待审  已审   驳回  按钮链接
				String bu_url1 = "" + accessToken.getDomainName()
						+ "/weixinweb/ButtonServlet?wxscmid=" + wxscmid
						+ "&state=";
				bu_url1 = URLEncoder.encode(bu_url1, "UTF-8");
				
				String bu_url0 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
						+ wxscmid
						+ "&redirect_uri="
						+ bu_url1;
				String bu_url2 = "&response_type=code&scope=snsapi_base#wechat_redirect";
				request.setAttribute("bu_url0", bu_url0);
				request.setAttribute("bu_url2", bu_url2);

				String currentPageStr="&currentPage=";
				currentPageStr = URLEncoder.encode(currentPageStr, "UTF-8");
				request.setAttribute("currentPageStr", currentPageStr);
				request.setAttribute("title", title);
				request.setAttribute("listM", listMNew);
//				request.setAttribute("page", page);
				if (state.equals("0")) {//待审页面
					request.getRequestDispatcher("examine/daishen.jsp").forward(request, response);
				}
				if (state.equals("1")) {//已审页面
					request.getRequestDispatcher("examine/yishen.jsp").forward(request,response);
				}
				if (state.equals("2")) {//驳回页面
					request.getRequestDispatcher("examine/bohui.jsp").forward(request,response);
				}
				if (state.equals("3")) {//我提交的审批信息
					request.getRequestDispatcher("examine/wode.jsp").forward(request,response);
				}
			}
		} catch (Exception e) {
			log.info(e+"");
			String _url="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+wxscmid+"&redirect_uri=";
			String url="";
			url=""+accessToken.getDomainName()+"/weixinweb/ButtonServlet?state="+state+"&wxscmid="+wxscmid;
			url=URLEncoder.encode(url, "UTF-8");
			_url+=url;
			_url+="&response_type=code&scope=snsapi_base#wechat_redirect";
			response.sendRedirect(_url);
			return;
		}
	}
}