package weixin.connection.text;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import weixin.pojo.Message;
import weixin.pojo.Text;
import weixin.util.BaseDao;

public class OperateTextData  extends BaseDao{
	/**
	 * 添加消息
	 * @param txt text对象
	 * @return 插入行的id
	 * @throws Exception
	 */
	public Integer insertTxt(Text txt) throws Exception{
		Connection connection=this.getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		Integer count=0;
		Object[] params =new Object[]{txt.getType(),txt.getTitle(),txt.getContent(),txt.getUsers(),txt.getW_corpid(),txt.getBipappid(),txt.getAppid(),txt.getTime()};
		String sql="insert into text (type,title,content,users,w_corpid,bipappid,appid,time) value (?,?,?,?,?,?,?,?);";
		try {
			statement=connection.prepareStatement(sql, statement.RETURN_GENERATED_KEYS);  
			for (int i = 0; i < params.length; i++) {
				statement.setObject(i+1, params[i]);
			}
			statement.executeUpdate();
			resultSet=statement.getGeneratedKeys();
			if(resultSet.next()){
				count = resultSet.getInt(1);//返回录入数据的id
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return count;
	}
	/**
	 * 查看某条text消息   根据id查询
	 * @param txt text对象 只需要传入id即可    
	 * @return text对象
	 */
	public Text showOneT(Text txt){
		Connection connection=this.getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
//		Integer count=0;
		Object[] params =new Object[]{txt.getId()};
		String sql="select * from text where id =?";
		try {
			statement=connection.prepareStatement(sql);  
			for (int i = 0; i < params.length; i++) {
				statement.setObject(i+1, params[i]);
			}
			resultSet=statement.executeQuery();
			while(resultSet.next()){
				txt=new Text();
				txt.setId(resultSet.getInt("id"));
				txt.setTitle(resultSet.getString("title"));
				txt.setType(resultSet.getString("type"));
				txt.setUsers(resultSet.getString("users"));
				txt.setW_corpid(resultSet.getString("w_corpid"));
				txt.setAppid(resultSet.getString("appid"));
				txt.setContent(resultSet.getString("content"));
				txt.setTime(resultSet.getDate("time"));
				txt.setBipappid(resultSet.getString("bipappid"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return txt;
	};
}