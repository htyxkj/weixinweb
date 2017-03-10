package weixin.servlet.examine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weixin.connection.message.ShowData;
import weixin.connection.message.UpdateDate;
import weixin.pojo.Message;
import weixin.util.SendTxtToUser;

public class SpServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	/**
	 *进行审批  同意 驳回
	 **/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			Message message = new Message();
			String yjcontent = request.getParameter("yjcontent");// 审批yj内容88
			String id = request.getParameter("id");// 消息编号88
			Date sptime = new Date();// 消息审批时间88
			String state = request.getParameter("results");// 审批结果88
			String documentsid = request.getParameter("documentsid");// 单据消息id
			String state0 = request.getParameter("state0");// 审批结果
			String spname=request.getParameter("spname");
			String state1=request.getParameter("state1");
			String wxscmid=request.getParameter("w_corpid");
			String scm=request.getParameter("scm");
			String appid=request.getParameter("appid");

			message.setW_corpid(wxscmid);
			message.setDocumentsid(documentsid);
			message.setState0(state0);
			message.setState1(state1);
			message.setId(Integer.parseInt(id));// 消息编号88
			message.setSptime(sptime);// 审批时间88
			message.setState(Integer.parseInt(state));// 审批结果88
			message.setYjcontent(yjcontent);// 审批意见内容88
			message.setSpname(spname);

			int rows =0;
			ShowData showData=new ShowData();
			List<Message> lm=showData.showjilu(documentsid,wxscmid,"tz");
			ShowData sh=new ShowData();
			SendTxtToUser sendTxt=new SendTxtToUser();
			UpdateDate upDate = new UpdateDate();
			rows = upDate.update(message);
			upDate.BipTuiHui(message);
			if (rows != 0) {
				for(Message m :lm){
					sendTxt.tosend(null, m.getSpname(),m.getW_corpid(),m.getAppid(),m.getScm());
				}
				String str="{\"success\":\"ok\"}";
				out = response.getWriter();
				out.append(str);
			}else{
				String str="{\"success\":\"no\"}";
				out = response.getWriter();
				out.append(str);
			}
		}catch (Exception e) {
			e.printStackTrace();
			out.close();
		}finally{
			out.close();
		}
	}
}