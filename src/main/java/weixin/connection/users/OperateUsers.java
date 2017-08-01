package weixin.connection.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weixin.pojo.Users;
import weixin.util.BaseDao;

public class OperateUsers extends BaseDao{
	private static Logger log = LoggerFactory.getLogger(OperateUsers.class);
	/**
	 * 用于接收平台传过来的User 保存到数据库里
	 * @param user user消息
	 * @throws ParseException 
	 */
	public void insertUser(Users user) throws ParseException{
		if(user.getEmail().equals("\"null\""));
		user.setEmail(null);
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		String sql = "INSERT INTO users(userid,username,tel,scm,w_corpid,email)VALUES ('" + 
				user.getUserid() + "','" + user.getUsername() + "','" + user.getTel()+"','" + user.getScm()+"','" + user.getW_corpid()+"','" + user.getEmail()+"');";
		try {
			statement=connection.prepareStatement(sql);  
			statement.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
	}
	/**
	 * 获取员工姓名
	 * @param userid 员工编号
	 * @param scm 公司(部门)
	 * @param w_corpid	企业号标识
	 * @return Users对象
	 * @throws ParseException
	 */
	public Users showUserName(String userid,String scm,String w_corpid) throws ParseException{
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		String sql="select * from users where userid ='"+userid+"'";
		if(scm!=null){
			sql+="  and scm='"+scm+"' ";
		}
		sql+=" and w_corpid='"+w_corpid+"'";
//		log.info(sql);
		Users user=null;
		try {
			statement=connection.prepareStatement(sql);  
			resultSet=statement.executeQuery();
			if(resultSet.next()){
				user=new Users();
				user.setUserid(resultSet.getString("userid"));
				user.setTel(resultSet.getString("tel"));
				user.setUsername(resultSet.getString("username"));
				user.setImgurl(resultSet.getString("imgurl"));
				user.setEmail(resultSet.getString("email"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return user;
	}
	/**
	 * 修改员工信息
	 * @param user user对象
	 * @throws ParseException
	 */
	public void uodateUsers(Users user) throws ParseException{
		if(user.getEmail().equals("\"null\""));
			user.setEmail(null);
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		String sql = "update users  set imgurl='"+user.getImgurl()+"',username='"+user.getUsername()+"',tel='"+user.getTel()+"',email='"+user.getEmail()+"' where userid='"+user.getUserid()+"' and scm='"+user.getScm()+"' and w_corpid='"+user.getW_corpid()+"' ";
		try {
			statement=connection.prepareStatement(sql);  
			statement.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
	}
	/**
	 * 修改员工头像路径
	 * @param user user对象
	 * @throws ParseException
	 */
	public void uodateUsersImgUrl(Users user) throws ParseException{
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		String sql = "update users  set  imgurl='"+user.getImgurl()+"' where userid='"+user.getUserid()+"' and w_corpid='"+user.getW_corpid()+"' ";
		try {
			statement=connection.prepareStatement(sql);  
			statement.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
	}
	/**
	 * 根据员工编号查询邮箱信息
	 * @param userid员工编号
	 * @return邮箱信息
	 * @throws Exception
	 */
	public String getEmail(String userid,String w_corpID)throws Exception{
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		String email="";
		String sql="select email from users where userid ='"+userid+"' and w_corpid='"+w_corpID+"'";
		try {
			statement=connection.prepareStatement(sql);  
			resultSet=statement.executeQuery();
			if(resultSet.next()){
				email=resultSet.getString("email");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return email;
	}
	/**
	 * 根据邮箱信息查询员工编号
	 * @param email邮箱信息
	 * @return员工编号
	 * @throws Exception
	 */
	public String getUserID(String email,String w_corpID)throws Exception{
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		String userid="";
		String sql="select userid from users where email ='"+email+"' and w_corpid='"+w_corpID+"'";
		try {
			statement=connection.prepareStatement(sql);  
			resultSet=statement.executeQuery();
			if(resultSet.next()){
				userid=resultSet.getString("userid");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return userid;
	}
	/**
	 * 根据员工编号查询员工信息
	 * @param userid
	 * @param w_corpID
	 * @return
	 * @throws Exception
	 */
	public Users getUser(String userid,String w_corpID)throws Exception{
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		Users user=null;
		try {
			String sql="select * from users where userid =? and w_corpid=?";
			statement=connection.prepareStatement(sql);
			statement.setString(1,userid);
			statement.setString(2,w_corpID);
			resultSet=statement.executeQuery();
			if(resultSet.next()){
				user=new Users();
				user=(Users) this.Field(user, resultSet);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return user;
	}
	/**
	 * 获取用户所在部门以及子部门的人员消息
	 * @param scm
	 * @param w_corpID
	 * @return
	 * @throws Exception
	 */
	public List<Users> getListUser(String scm,String w_corpID)throws Exception{
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		Users user=null;
		List<Users> listU=new ArrayList<Users>();
		try {
			String sql="select * from users where scm like ? and w_corpid=? order by username";
			statement=connection.prepareStatement(sql);
			statement.setString(1,scm+"%");
			statement.setString(2,w_corpID);
			resultSet=statement.executeQuery();
			while(resultSet.next()){
				user=new Users();
				user=(Users) this.Field(user, resultSet);
				listU.add(user);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return listU;
	}
	
	/**
	 * 获取用户所在部门以及子部门
	 * @param scm
	 * @param w_corpID
	 * @return
	 * @throws Exception
	 */
	public List<String> getListScm(String scm,String w_corpID)throws Exception{
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		List<String> listS=new ArrayList<String>();
		try {
			String sql="select scm from users where scm like ? and w_corpid=? group by scm";
			statement=connection.prepareStatement(sql);
			statement.setString(1,scm+"%");
			statement.setString(2,w_corpID);
			resultSet=statement.executeQuery();
			while(resultSet.next()){
				listS.add(resultSet.getString("scm"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return listS;
	}
	public List<String> getListUid(String scm,String w_corpID)throws Exception{
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		List<String> listUI=new ArrayList<String>();
		try {
			String sql="";
			if(!scm.equals(""))
				sql="select userid from users where scm in ("+scm+") and w_corpid=?";
			if(scm.equals(""))
				sql="select userid from users where  w_corpid=?";
			statement=connection.prepareStatement(sql);
			statement.setString(1,w_corpID);
			resultSet=statement.executeQuery();
			while(resultSet.next()){
				listUI.add(resultSet.getString("userid"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return listUI;
	}
}
