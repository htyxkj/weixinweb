package weixin.connection.exp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import weixin.pojo.Exp;
import weixin.util.BaseDao;

public class OperateExp extends BaseDao<Exp>{
	/**
	 * 添加报销单主表
	 * @param exp
	 * @return
	 */
	public String insertExp(Exp exp){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		Object[] params = new Object[] {exp.getCorpid(),exp.getSid_b(),exp.getSopr(),exp.getSmake(),exp.getP_account(),exp.getP_bank(),exp.getRemark(),exp.getState(),exp.getFcy(),exp.getMkdate(),exp.getHpdate(),exp.getBxlb()};
		String count="";
		String sql="INSERT INTO exp(corpid,sid_b,sopr,smake,p_account,p_bank,remark,state,fcy,mkdate,hpdate,bxlb) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
		try {
			statement=connection.prepareStatement(sql, statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < params.length; i++) {
				statement.setObject(i + 1, params[i]);
			}
			statement.executeUpdate();
			resultSet=statement.getGeneratedKeys();
			if(resultSet.next()){
				count = resultSet.getString(1);//返回录入数据的id
			}
		}catch (Exception e) {
			e.printStackTrace();
			return "no";
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return count;
	}
}