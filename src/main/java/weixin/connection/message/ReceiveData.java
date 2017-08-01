package weixin.connection.message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weixin.pojo.Message;
import weixin.util.BaseDao;

public class ReceiveData extends BaseDao<Message>{
	private static Logger log = LoggerFactory.getLogger(ReceiveData.class);
	/***
	 * 用于接收平台传过来的审批数据 保存到数据库里
	 * @param mess Message对象
	 * @return zt 状态是否成功
	 * @throws ParseException
	 */
	public String jieshou(Message mess) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		int count=0;
		String zt="";
		String sql = "INSERT INTO message(title,name,spweixinid,spname,content,yjcontent,tjtime,documentsid,documentstype,tablename,sbuid,state,state0,state1,dbid,scm,department,appid,wapno,w_corpid,smake)VALUES ('" + 
				mess.getTitle() + "','" + mess.getName() + "','" + mess.getSpweixinid() + "','" + mess.getSpname() + "','" + mess.getContent() + "','" 
				+ mess.getYjcontent() + "','" + sdf.format(mess.getTjtime()) + "','" + mess.getDocumentsid() + "','" + mess.getDocumentstype() + "','" + mess.getTablename() + "','" + mess.getSbuId() + "',0,'"+mess.getState0()+"','" + mess.getState1() + "','" + mess.getDbid() + "','" + mess.getScm() + "','" + mess.getDepartment() + "','" + mess.getAppid()+ "','" + mess.getWapno()+ "','" + mess.getW_corpid()+ "','" + mess.getSmake()+ "');";
		try {
			statement=connection.prepareStatement(sql, statement.RETURN_GENERATED_KEYS);  
			statement.executeUpdate();
			resultSet=statement.getGeneratedKeys();
			if(resultSet.next()){
				count = resultSet.getInt(1);//返回录入数据的id
				zt="ok";
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return zt;
	}

	public void updateState(Message mess) {
		Connection connection = getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		String state = "1";
		if("1".equals(mess.getState0())) {
			state = "2";
		}
		String sql = "UPDATE message set state = '"+state+"' WHERE state='0' and tjtime=(SELECT * FROM (SELECT MAX(tjtime) FROM message WHERE documentsid = '"+mess.getDocumentsid()+"') as v)" +
				"and documentsid = '"+mess.getDocumentsid()+"' ";
		try {
			statement=connection.prepareStatement(sql, statement.RETURN_GENERATED_KEYS);
			statement.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
	}
}