package weixin.servlet.oaggtz;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OaggtzServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(OaggtzServlet.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}
 
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String read = request.getParameter("read");
		if(read == null)
			read ="0";
		String xq_url1 ="./OneOaggtzServlet?keyid=";
		//已读 未读 我发布  按钮链接
		String bu_url1 = "./OaggtzServlet?read=";
		request.setAttribute("bu_url0", bu_url1);
		request.setAttribute("xqurl", xq_url1);
		if(read.equals("0"))
			request.getRequestDispatcher("oaggtz/oaggtz.jsp").forward(request,response);
		if(read.equals("1"))
			request.getRequestDispatcher("oaggtz/oaggtzyd.jsp").forward(request,response);
		if(read.equals("2"))
			request.getRequestDispatcher("oaggtz/oaggtzwd.jsp").forward(request,response);
	}
}