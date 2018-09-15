package weixin.connection.message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
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
		String zt="";
		int count = 0;
		String sql = "INSERT INTO message(title,name,spweixinid,spname,content,yjcontent,tjtime,documentsid,documentstype,tablename"
				+ ",sbuid,state,state0,state1,dbid,scm,department,w_appid,d_appid,wapno,w_corpid,d_corpid,smake)VALUES ("
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try {
			statement=connection.prepareStatement(sql, statement.RETURN_GENERATED_KEYS);  
			statement.setString(1, mess.getTitle());
			statement.setString(2, mess.getName());
			statement.setString(3, mess.getSpweixinid());
			statement.setString(4, mess.getSpname());
			statement.setString(5, mess.getContent());
			statement.setString(6, mess.getYjcontent());
			statement.setTimestamp(7, new Timestamp(mess.getTjtime().getTime()));  
			statement.setString(8, mess.getDocumentsid());
			statement.setString(9, mess.getDocumentstype());
			statement.setString(10, mess.getTablename());
			statement.setString(11, mess.getSbuId());
			statement.setInt(12, mess.getState());
			statement.setString(13, mess.getState0());
			statement.setString(14, mess.getState1());
			statement.setString(15, mess.getDbid());
			statement.setString(16, mess.getScm());
			statement.setString(17, mess.getDepartment());
			statement.setString(18, mess.getW_appid());
			statement.setString(19, mess.getD_appid());
			statement.setString(20, mess.getWapno());
			statement.setString(21, mess.getW_corpid());
			statement.setString(22, mess.getD_corpid());
			statement.setString(23, mess.getSmake());
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