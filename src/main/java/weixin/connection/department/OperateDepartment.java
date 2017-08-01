package weixin.connection.department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import weixin.pojo.Department;
import weixin.util.BaseDao;

public class OperateDepartment  extends BaseDao<Department> {
	/**
	 *查询部门名称 
	 * @param orgcode
	 * @param w_corpid
	 * @return
	 */
	public Department selectDepartment(String orgcode,String w_corpid){
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Department depart=null;
		try{
			String sql="select * from Department where  orgcode=? and w_corpid=?";
			statement = connection.prepareStatement(sql);
			statement.setString(1,orgcode);
			statement.setString(2,w_corpid);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				depart=new Department();
				depart=(Department) this.Field(depart, resultSet);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			closeAll(connection, statement, resultSet);
		}
		return depart;
	}
}
