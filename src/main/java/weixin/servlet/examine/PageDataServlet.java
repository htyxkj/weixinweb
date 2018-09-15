package weixin.servlet.examine;

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

import weixin.connection.message.ShowData;
import weixin.pojo.Message;
import weixin.pojo.Users;

import com.opslab.util.StringUtil;


public class PageDataServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(PageDataServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * 滚动加载数据
     **/
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	Users user = (Users) request.getSession().getAttribute("sessionUser");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String pageType = request.getParameter("pageType");
        String offsetStr = request.getParameter("offset");
        List<Message> list;
        //对请求参数进行校验
        if (!StringUtil.isEmpty(pageType) && !StringUtil.isEmpty(offsetStr) && offsetStr.matches("\\d+")) {
            Integer offset = Integer.parseInt(offsetStr);
            String state = "9";
            ShowData sd = new ShowData();
            if (pageType.equals("wode")) {
                state = "3";
            }
            if (pageType.equals("daishen")) {
                state = "0";
            }
            if (pageType.equals("yishen")) {
                state = "1";
            }
            list = sd.showStateByPage(user.getUserid(), state, user.getW_corpid(), user.getD_corpid(), offset);
        }else{
            list = new ArrayList<Message>();
        }
        JSONArray json = JSONArray.fromObject(list);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(json.toString().getBytes("UTF-8"));
        outputStream.close();
    }
}