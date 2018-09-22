package weixin.servlet.oaggtz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import weixin.connection.accessToken.AccessTokenDo;
import weixin.connection.oaggtz.OperateOaggtz;
import weixin.pojo.Users;
/**
 * 
 * @author Administrator
 *修改公告字段
 */
public class UpOaggtzServlet extends HttpServlet {
 
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String id = request.getParameter("id");//公告信息微信端编号
        OperateOaggtz oagg=new OperateOaggtz();
        //编号不为空
        if(id!=null&&!id.equals("")){
        	Users user = (Users) request.getSession().getAttribute("sessionUser");
        	AccessTokenDo accd = new AccessTokenDo();
        	String[] corpid = accd.selCorpid(user.getD_corpid(), user.getW_corpid());
        	//将这条信息设为已读
	        oagg.updateRad(id,user.getUserid(),corpid[1],corpid[0]);
        }else{
        	//平台进行提交退回，删除某条公告信息
        	String jsonstr = "";
			InputStream is = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			jsonstr=br.readLine();
			jsonstr=URLDecoder.decode(jsonstr,"UTF-8");
			isr.close();
			is.close();
			//去除数据中的换行符
			jsonstr=jsonstr.replaceAll("\r\n", "");
			jsonstr=jsonstr.replaceAll("\r", "");
			jsonstr=jsonstr.replaceAll("\n", "");
			jsonstr=jsonstr.replaceAll("\t", "");
			jsonstr=jsonstr.replaceAll("null", "");
			JSONObject jsonObject = JSONObject.fromObject(jsonstr);
			//进行删除
        	oagg.deleteOagg(jsonObject.getString("sid"), jsonObject.getString("w_corpid"));
        }
	}
}
