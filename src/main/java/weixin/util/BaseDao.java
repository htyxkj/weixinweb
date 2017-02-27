package weixin.util;

import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class BaseDao<T> {
	String path = getClass().getClassLoader().getResource("/").getPath();
	Locale locale = Locale.getDefault();  
	ResourceBundle bundle = ResourceBundle.getBundle("db", locale);
	public Connection getConnection() {
	    String className = bundle.getString("classname");
	    String url = bundle.getString("url");
	    String user = bundle.getString("user");
	    String password = bundle.getString("password");
		Connection connection=null;
		try {
			Class.forName(className);
			connection=DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	public void closeAll(Connection connection,Statement statement,ResultSet resultSet) {
		try {
			if(resultSet!=null){
			resultSet.close();
			}
			if (statement!=null) {
				statement.close();
			}
			if (connection!=null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 对数据进行修改
	 * @param sql sql语句
	 * @param params 条件内容
	 * @return
	 */
	public int executeUpdate(String sql,Object[] params) {
		int rows=0;
		Connection connection=getConnection();
		PreparedStatement statement=null;
		try {
			statement=connection.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				statement.setObject(i+1, params[i]);
			}
			rows=statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, null);
		}
		return rows;
	}
}
