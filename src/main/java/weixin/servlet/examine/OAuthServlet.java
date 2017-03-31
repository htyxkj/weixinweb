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


import com.opslab.util.StringUtil;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weixin.connection.message.ShowData;
import weixin.connection.users.OperateUsers;
import weixin.pojo.AccessToken;
import weixin.pojo.FuJian;
import weixin.pojo.Message;
import weixin.pojo.Users;
import weixin.thread.TokenThread;
import weixin.util.WeixinUtil;

public class OAuthServlet extends HttpServlet {
    private static String SERVER_PATH_BODY = "mydoc/db_01/";
    private static Logger log = LoggerFactory.getLogger(OAuthServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * 点击一条审批信息查看详情
     **/
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String state = request.getParameter("state");
//		String userid=request.getParameter("userid");
        String wxscmid = request.getParameter("wxscmid");
        TokenThread tokenThread = new TokenThread();
        Map<String, AccessToken> map = tokenThread.maplist;
        AccessToken accessToken = map.get(wxscmid);
        //用户同意授权后，能获取到code
        String code = request.getParameter("code");
        String keyid = request.getParameter("keyid");
        Message mess = null;
        try {
            if (!"authdeny".equals(code) && code != null) {
                String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=" + accessToken.getToken() + "&code=" + code;
                JSONObject jsonObj = WeixinUtil.httpRequest(requestUrl, "GET", null);
//				获取员工id
                String userid = jsonObj.getString("UserId");
                if (userid.indexOf("@") != -1) {
                    OperateUsers oU = new OperateUsers();
                    userid = oU.getUserID(userid, wxscmid);
                }
                ShowData show = new ShowData();
                Message data = show.show(keyid);
                OperateUsers o = new OperateUsers();
                Users u = o.showUserName(data.getName(), null, data.getW_corpid());
                List<Message> listM = new ArrayList<Message>();
                List<FuJian> listFuJian = new ArrayList<FuJian>();
                FuJian fujian = null;
                String fujianNames = "";
                String fujianPath = "";
                if (data != null) {
                    if (data.getContent() != null && !"".equals(data.getContent())) {
                        String content = data.getContent();
                        String noFJcontent = "";
                        int fjPathIndex = content.indexOf("附件路径:");
                        int fjNameIndex = content.indexOf("附件名称:");
                        if (fjPathIndex != -1 && fjNameIndex != -1) {
                            noFJcontent = content.substring(0, fjPathIndex);
                            String fjPathStr = content.substring(fjPathIndex, fjNameIndex);
                            String fjNameStr = content.substring(fjNameIndex, content.length());
                            String[] fjPathstrs = fjPathStr.split(";");
                            if (fjPathstrs.length == 1 && !StringUtil.isEmpty(fjPathstrs[0])) {
                                String[] fjPaths = fjPathstrs[0].split(":");
                                if (fjPaths.length > 1) {
                                    fujianPath = fjPaths[1];
                                }
                                String[] fjNamestrs = fjNameStr.split(";");
                                if (fjNamestrs.length > 0) {
                                    for (int i = 0; i < fjNamestrs.length; i++) {
                                        String fjName  = "";
                                        if(i==0){
                                            String[] fjNames = fjNamestrs[0].split(":");
                                            if(fjNames.length>1&&!StringUtil.isEmpty(fjNames[1])){
                                                fjName = fjNames[1];
                                            }
                                        }
                                        if (i > 0) {
                                            if (!StringUtil.isEmpty(fjNamestrs[i])) {
                                                fjName = fjNamestrs[i];
                                            }
                                        }
                                        if(!StringUtil.isEmpty(fjName)){
                                            FuJian fj = new FuJian();
                                            fj.setServerPathHead(accessToken.getServerurl());
                                            fj.setServerPathBody(SERVER_PATH_BODY);
                                            fj.setFilePath(fujianPath);
                                            fj.setFileName(fjName);
                                            fj.setFullPath(accessToken.getServerurl() + SERVER_PATH_BODY + fujianPath + fjName);
                                            log.info(fj.getFullPath());
                                            listFuJian.add(fj);
                                        }
                                    }
                                }
                            }
                        }
                        if (!StringUtil.isEmpty(noFJcontent)) {
                            data.setContenthuanhang(noFJcontent.replaceAll(";", "<\\br>"));
                        } else {
                            data.setContenthuanhang(data.getContent().replaceAll(";", "<\\br>"));
                        }
                    }
                    if (u != null) {
                        data.setName(u.getUsername());
                        if (u.getImgurl() != null && !u.getImgurl().equals(""))
                            data.setTuUrl(u.getImgurl());
                    }
                    List<Message> list = show.showjilu(data.getDocumentsid(), data.getW_corpid(), "jl");
                    data.setState1wenzi(show.showStateWZ(data.getState1()));
                    //Message message=null;
                    for (int i = 0; i < list.size(); i++) {
                        Message message = new Message();
                        message = list.get(i);
                        u = new Users();
                        if (i == 0) {
                            mess = new Message();
                            mess = list.get(0).copy();
                            u = o.showUserName(message.getName(), null, message.getW_corpid());
                            if (u != null) {
                                mess.setName(u.getUsername());
                                mess.setTuUrl(u.getImgurl());
                            }
                        }
                        u = new Users();
                        u = o.showUserName(message.getSpname(), null, message.getW_corpid());
                        if (u != null) {
                            message.setSpname(u.getUsername());
                            message.setState1wenzi(show.showStateWZ(message.getState1()));
                            message.setTuUrl(u.getImgurl());
                        }
                        listM.add(message);
                    }
                }
                data.setServerurl(accessToken.getServerurl());
                request.setAttribute("_right", listM.get(listM.size() - 1).getState());
                if (state != null && state.equals("3")) {
                    request.setAttribute("dateks", mess);
                    request.setAttribute("listM", listM);
                    request.setAttribute("wxscmid", wxscmid);
                    request.setAttribute("data", data);
                    request.setAttribute("usercode", userid);
                    request.setAttribute("fujians", listFuJian);
                    request.getRequestDispatcher("examine/wodexq.jsp").forward(request, response);
                } else if (userid.equals(data.getSpweixinid())) {
                    request.setAttribute("dateks", mess);
                    request.setAttribute("listM", listM);
                    request.setAttribute("wxscmid", wxscmid);
                    request.setAttribute("data", data);
                    request.setAttribute("usercode", userid);
                    request.setAttribute("fujians", listFuJian);
                    request.getRequestDispatcher("examine/shenpi.jsp").forward(request, response);
                } else {
                    request.setAttribute("jg", 2);
                    request.getRequestDispatcher("examine/jieguo.jsp").forward(request, response);
                }
            }
        } catch (Exception e) {
            log.info(e + "");
            //获取员工信息失败   重新获取
            String _url = "" + accessToken.getDomainName()
                    + "/weixinweb/OAuthServlet?wxscmid=" + wxscmid
                    + "&keyid=" + keyid;
            _url = URLEncoder.encode(_url, "UTF-8");
            _url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
                    + wxscmid
                    + "&redirect_uri="
                    + _url;
            _url += "&response_type=code&scope=snsapi_base#wechat_redirect";
            response.sendRedirect(_url);
            return;
        }
    }
}