package weixin.connection.number;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weixin.pojo.Num;
import weixin.util.BaseDao;

public class OperateNum extends BaseDao<Num> {
	private static final Log log = LogFactory.getLog(OperateNum.class);
	/**
	 * 公司启用微信端后 统计发送消息次数
	 * 
	 * @param num
	 * @throws Exception
	 */
	public void insert(Num num) throws Exception {
		Connection connection = this.getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		java.sql.Date sqlDate = new java.sql.Date(num.getTime().getTime());
		Object[] params = new Object[] { num.getW_corpid(), num.getAppid(),
				num.getScm(), num.getWeixinid(), sqlDate,num.getContent()};
		String sql = "insert into countnum (w_corpid,appid,scm,weixinid,time,content) values (?,?,?,?,?,?);";
		try {
			log.info(sql);
			statement = connection.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				statement.setObject(i + 1, params[i]);
			}
			statement.executeUpdate();
		} catch (Exception e) {
			log.info(e);
			e.printStackTrace();
		} finally {
			closeAll(connection, statement, resultSet);
		}
	}
}