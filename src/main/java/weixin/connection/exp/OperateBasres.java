package weixin.connection.exp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import weixin.pojo.Basres;
import weixin.pojo.Department;
import weixin.util.BaseDao;

public class OperateBasres extends BaseDao<Basres>{
	/**
	 * 查询报销类别
	 * @param qid 类别
	 * @param corpid 微信企业号ID
	 * @return
	 */
	public List<Basres> getListB(String qid,String corpid){
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Basres basres=null;
		List<Basres> listb=new ArrayList<Basres>();
		try{
			String sql="select * from basres where qid=? and corpid=? order by sid";
			statement = connection.prepareStatement(sql);
			statement.setString(1,qid);
			statement.setString(2,corpid);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				basres=new Basres();
				basres=(Basres) this.Field(basres, resultSet);
				listb.add(basres);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			closeAll(connection, statement, resultSet);
		}
		return listb;
	}
	
	/**
	 * 查询报销对象信息
	 * @param qid 类别
	 * @param corpid 微信企业号ID
	 * @return
	 */
	public Basres getOneB(String qid,String corpid,String sid){
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Basres basres=null;
		try{
			String sql="select * from basres where qid=? and corpid=? and sid=?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, qid);
			statement.setString(2, corpid);
			statement.setString(3, sid);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				basres=new Basres();
				basres=(Basres) this.Field(basres, resultSet);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			closeAll(connection, statement, resultSet);
		}
		return basres;
	}
}
