package weixin.connection.exp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import weixin.pojo.Expa;
import weixin.util.BaseDao;

public class OperateExpa extends BaseDao<Expa>{
	/**
	 * 添加报销单子表
	 * @param expa
	 * @return
	 **/
	public String insertExpa(Expa expa){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		Object[] params = new Object[] {expa.getCorpid(),expa.getSid_w(),expa.getSid_b(),expa.getCid(),expa.getIdxno(),expa.getInv_no(),expa.getDeduction(),expa.getInv_type(),expa.getSp_tax(),expa.getFcy(),expa.getAddtaxrt(),expa.getAddtax(),expa.getRmbhs(),expa.getRemark()};
		String count="";
		String sql="INSERT INTO expa(corpid,sid_w,sid_b,cid,idxno,inv_no,deduction,inv_type,sp_tax,fcy,addtaxrt,addtax,rmbhs,remark) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			statement=connection.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				statement.setObject(i + 1, params[i]);
			}
			statement.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
				try {
					sql ="delete from exp where sid_w="+expa.getSid_w();
					statement=connection.prepareStatement(sql);
					statement.executeUpdate();
				} catch (SQLException e1) {
					e1.printStackTrace();
			}
			return "no";
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return count;
	}
}