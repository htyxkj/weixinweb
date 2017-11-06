package weixin.servlet.exp;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weixin.connection.exp.OperateBasres;
import weixin.key.SRegServ;
import weixin.pojo.AccessToken;
import weixin.pojo.Basres;
import weixin.thread.TokenThread;

public class SelectExpServlet extends HttpServlet {


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String wxscmid = request.getParameter("wxscmid");
		String w_appid = request.getParameter("w_appid");
		Map<String, AccessToken> map = TokenThread.maplist;
		AccessToken accessToken = map.get(wxscmid+"-"+w_appid);
		//用户同意授权后，能获取到code
		String code = request.getParameter("code");
		try {
			SRegServ t=new SRegServ();
			Object[] obj=(Object[]) t.processOperator("isReg",wxscmid);
			if(obj[0].equals("1")||obj[0].equals("-1")){
				request.setAttribute("errorInfo", obj[1]);
				request.getRequestDispatcher("./expired.jsp").forward(request,response);
			}else if(2==(Integer)obj[0]){
				request.setAttribute("regdate", obj[1]);
			}
//			if (!"authdeny".equals(code) && code != null) {
//				//获取员工信息链接
//				String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token="
//						+ accessToken.getToken()+"&code="+code;
//				JSONObject jsonObj = WeixinUtil.httpRequest(requestUrl, "GET",null);
				//获取用户id
				String userid="0050004";
//				if(jsonObj.containsKey("UserId") == true){
//					userid = jsonObj.getString("UserId");
//				}else{
//					//获取员工信息失败   重新获取
//					String _url="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+wxscmid+"&redirect_uri=";
//					String url="";
//					url=""+accessToken.getDomainName()+"/weixinweb/ExpServlet?w_appid="+w_appid+"&wxscmid="+wxscmid;
//					url=URLEncoder.encode(url, "UTF-8");
//					_url+=url;
//					_url+="&response_type=code&scope=snsapi_base#wechat_redirect";
//					response.sendRedirect(_url);
//					return;
//				}
				String luUrl="";
				String cxUrl="";
				String _url="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+wxscmid+"&redirect_uri=";
				String _url1="&response_type=code&scope=snsapi_base#wechat_redirect";
				luUrl=""+accessToken.getDomainName()+"/weixinweb/ExpServlet?w_appid="+w_appid+"&wxscmid="+wxscmid;
				luUrl=URLEncoder.encode(luUrl, "UTF-8");
				luUrl=_url+luUrl+_url1;
				cxUrl=""+accessToken.getDomainName()+"/weixinweb/SelectExpServlet?w_appid="+w_appid+"&wxscmid="+wxscmid;
				cxUrl=URLEncoder.encode(luUrl, "UTF-8");
				cxUrl=_url+cxUrl+_url1;
				request.setAttribute("luUrl",luUrl);
				request.setAttribute("cxUrl",cxUrl);
				OperateBasres ob=new OperateBasres();
				List<Basres> listfplb=ob.getListB("BXLB", wxscmid);
				request.setAttribute("BXLB",listfplb);
				request.setAttribute("userid", userid);
				request.setAttribute("dbid", accessToken.getDbid());
				request.setAttribute("url", accessToken.getServerurl());
				request.getRequestDispatcher("exp/expList.jsp").forward(request, response);
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}