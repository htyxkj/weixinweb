package weixin.servlet.oaggtz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weixin.connection.oaggtz.OperateOaggtz;
import weixin.connection.users.OperateUsers;
import weixin.pojo.AccessToken;
import weixin.pojo.FuJian;
import weixin.pojo.Oaggtz;
import weixin.pojo.Users;
import weixin.thread.TokenThread;
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
        Users user = (Users) request.getSession().getAttribute("sessionUser");
        AccessToken accessToken = null;
        if(user.getLoginType().equals("w"))
        	accessToken = TokenThread.maplist.get("wx-"+user.getW_corpid()+"-"+user.getLoginAppid());
        else
        	accessToken = TokenThread.maplist.get("dd-"+user.getD_corpid());
        //用户同意授权后，能获取到code
        String keyid = request.getParameter("keyid");
        try {
                OperateOaggtz oaggtz=new OperateOaggtz();
                Oaggtz data= oaggtz.selectOne(keyid);
                OperateUsers o = new OperateUsers();
		    	Users uu = new Users();
		    	uu.setUserid(data.getSmaker());
		    	uu.setD_corpid(data.getD_corpid());
		    	uu.setW_corpid(data.getW_corpid());
                Users u = o.showUserName(uu);
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
        } catch (Exception e) {
            log.info(e + "");
            e.printStackTrace();
            return;
        }
	}
}