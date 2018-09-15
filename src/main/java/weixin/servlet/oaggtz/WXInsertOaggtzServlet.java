//package weixin.servlet.oaggtz;
//
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.ConnectException;
//import java.net.URL;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//
//import javax.net.ssl.HttpsURLConnection;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.apache.commons.fileupload.FileItem;
//import org.apache.commons.fileupload.disk.DiskFileItemFactory;
//import org.apache.commons.fileupload.servlet.ServletFileUpload;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import weixin.connection.accessToken.AccessTokenDo;
//import weixin.connection.oaggtz.OperateOaggtz;
//import weixin.connection.users.OperateUsers;
//import weixin.pojo.Oaggtz;
//import weixin.util.SendTxtToUser;
//
//
//public class WXInsertOaggtzServlet extends HttpServlet {
//	private static Logger log = LoggerFactory.getLogger(WXInsertOaggtzServlet.class);  
//	private static final long serialVersionUID = 1L;  
//    File tempPathFile;
//	public void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		doPost(request, response);
//	}
//
//	public void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		request.setCharacterEncoding("utf-8");
//		response.setCharacterEncoding("utf-8");
//		//获取项目所在地路径
//		String pathroot=request.getSession().getServletContext().getRealPath("/");
//		HttpSession session = request.getSession();
//        String wxscmid = (String) session.getAttribute("wxscmid");
////      String userId= (String) session.getAttribute("userId");
//		Oaggtz oaggtz=new Oaggtz();
//		OperateOaggtz oOaggta=new OperateOaggtz();
//		 try {
//	            DiskFileItemFactory factory = new DiskFileItemFactory();  
////	            factory.setSizeThreshold(40960); // 设置缓冲区大小，这里是40kb
////	            factory.setRepository(tempPathFile);// 设置缓冲区目录
//	            ServletFileUpload upload = new ServletFileUpload(factory);
//	            upload.setSizeMax(41943040); // 设置最大文件尺寸，这里是40MB
//	            List<FileItem> items = upload.parseRequest(request);// 得到所有的文件
//	            Iterator<FileItem> i = items.iterator();
//	            String username="";
//            	String scm="";
//            	String uploadPath="\\fj\\"+wxscmid;
//	            while (i.hasNext()) {
//	                FileItem fi = (FileItem) i.next();
//	                if (fi.isFormField()) {
//		                //如果是普通表单字段 
//		                String name = fi.getFieldName();
//		                String value = new String((fi.getString("iso8859-1")).getBytes("iso8859-1"),"UTF-8");
//		                if(name.equals("scm")){
//		                	oaggtz.setSorg(value);
//		                }else if(name.equals("UserId")){
//		                	oaggtz.setSmaker(value);
//		                }else if(name.equals("title")){
//		                	oaggtz.setTitle(value);
//		                }else if(name.equals("xxgs")){
//		                	oaggtz.setXxgs(value);
//		                }else if(name.equals("slb")){
//		                	oaggtz.setSlb(value);
//		                }else if(name.equals("content")){
//		                	oaggtz.setContent(value);
//		                }else if(name.equals("username")){
//		                	username+=value+"|";
//		                }else if(name.equals("sorgto")){
//		                	scm+=value+"|";
//		                }
//		            } else {
//		            	//附件上传路径
//		            	uploadPath="\\fj\\"+wxscmid+"\\"+oaggtz.getSorg()+"\\";
//		            	//判断路径是否存在不存在就创建
//		            	this.getPath(pathroot+uploadPath);
//	            		//如果是文件字段
//	                    String fileName = fi.getName();
//	                    if(fileName.equals(""))
//	                    	continue;
//	                    String[] fileNamearr =fileName.split("\\."); // 文件名
//	                    for(int j=1;j<999999999;j++){ //循环判断文件名是否存在
//	                    	File files = new File(pathroot+uploadPath,fileName);
//		                    if(files.exists()){
//		                    	fileName = fileNamearr[0]+"("+j+")."+fileNamearr[1];
//		                    } else{
//		                    	break;
//		                    }
//	                    }
//	                    File savedFile = new File(pathroot+uploadPath, fileName);
//	                    oaggtz.setSuri(fileName);
//	                    oaggtz.setFj_root(uploadPath);
// 	                    fi.write(savedFile);
//		            }
//	            }
//	            oaggtz.setW_corpid(wxscmid);
//	            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
//	            oaggtz.setMkdate(sdf.parse(sdf.format(new Date())));
//	            oaggtz.setCkdate(sdf.parse(sdf.format(new Date())));
//
//	            oaggtz.setState(6);
//	            oaggtz.setSorgto(scm);
//                oaggtz.setSusr(username);
//                oaggtz.setId(100);
//                oaggtz.setSource("W");
//                String tous="";
//                if((scm==null||scm.equals(""))&&(username==null||username.equals(""))){
//	                //根据公司(scm)查询员工编号
//		            OperateUsers oU=new OperateUsers();
//	                //发给所有人
//		            List<String> ListSU=oU.getListUid("", wxscmid);
//	            	for (int j = 0; j < ListSU.size(); j++) {
//						tous+=""+ListSU.get(j)+"|";
//					}
//	            	oaggtz.setSusr(tous);
//                }
//	            oOaggta.insertOaggtz(oaggtz);
//	            
//	            //查询微信应用ID
//	            AccessTokenDo accd=new AccessTokenDo();
//            	String id=accd.selectAPPID(wxscmid, "02");
//	            
//	            //根据页面选择的  员工 部门 进行操作
////	            if(!scm.equals("")){
////	            	String[] scmarr=scm.split("\\|");
////	            	String strscm="";
////	            	for (int j = 0; j < scmarr.length; j++) {
////	            		strscm+="'"+scmarr[j]+"',";
////					}
////	            	strscm+="'-1'";
////	            	List<String> ListSU=oU.getListUid(strscm, wxscmid);
////	            	for (int j = 0; j < ListSU.size(); j++) {
////						tous+=""+ListSU.get(j)+"|";
////					}
////	            }
////	            tous+=username;
////	            this.toUser("您有新的公告信息,请前往“公告通知”查看", tous, wxscmid, id, strscm);
//            	this.httpRequest("", "POST", oaggtz.toString());            	
//	            this.toUser("您有新的公告信息,请前往“公告通知”查看", tous, wxscmid, id, "0");
//	        } catch (Exception e) {
//	        	e.printStackTrace();
//	        }
//	}
//	//通知用户有新的公告
//	public static void toUser(String conetnt,String weixinid,String wxscmid,String appid,String scm){
//		SendTxtToUser txttou=new  SendTxtToUser();
//		txttou.wxInformUser(conetnt, weixinid, wxscmid, appid, scm);
//	}
//	//判断路径是否存在   不存在就创建 并返回路径
//	public static void getPath(String path){
//        String [] paths=path.split("\\\\");  
//        StringBuffer fullPath=new StringBuffer();  
//        for (int i = 0; i < paths.length; i++) {  
//            fullPath.append(paths[i]).append("\\\\");  
//            File file=new File(fullPath.toString());  
//            if(!file.exists()){  
//                file.mkdir();  
//            }
//        }
//	}
//	//将公告信息传到平台
//	 public static void httpRequest(String requestUrl, String requestMethod, String outputStr) {
//	        StringBuffer buffer = new StringBuffer();  
//	        try {
//	            //创建SSLContext对象,并使用我们指定的信任管理器初始化
//	            URL url = new URL(requestUrl);  
//	            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();  
//	            httpUrlConn.setDoOutput(true);  
//	            httpUrlConn.setDoInput(true);  
//	            httpUrlConn.setUseCaches(false);  
//	            // 设置请求方式（GET/POST）  
//	            httpUrlConn.setRequestMethod(requestMethod);  
//	            if ("GET".equalsIgnoreCase(requestMethod))  
//	                httpUrlConn.connect();  
//	            // 当有数据需要提交时  
//	            if (null != outputStr) {  
//	                OutputStream outputStream = httpUrlConn.getOutputStream();  
//	                // 注意编码格式，防止中文乱码  
//	                outputStream.write(outputStr.getBytes("UTF-8"));  
//	                outputStream.close();  
//	            }
//	            //将返回的输入流转换成字符串  
//	            InputStream inputStream = httpUrlConn.getInputStream();  
//	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
//	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
//	            String str = null;  
//	            while ((str = bufferedReader.readLine()) != null) {  
//	                buffer.append(str);
//	            }
//	            bufferedReader.close();  
//	            inputStreamReader.close();  
//	            // 释放资源  
//	            inputStream.close();  
//	            inputStream = null;  
//	            httpUrlConn.disconnect();  
//	        } catch (ConnectException ce) {  
//	            log.error("Weixin服务器连接超时。");  
//	        } catch (Exception e) {  
//	            log.error("https request error:{}", e);  
//	        }  
//	    } 
//}