package weixin.servlet.examine;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weixin.connection.message.ShowData;
import weixin.pojo.Message;

public class SelectOneMessage extends HttpServlet {
 
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
 
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String keyid=request.getParameter("id");
		ShowData show=new ShowData();
		PrintWriter out = null;
		Message data=show.show(keyid);
		if(data!=null){
			String str="{\"success\":\"ok\"}";
			out = response.getWriter();
			out.append(str);
		}else{
			String str="{\"success\":\"no\"}";
			out = response.getWriter();
			out.append(str);
		}
		out.close();
	}

}
