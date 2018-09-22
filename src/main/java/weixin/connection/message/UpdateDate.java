package weixin.connection.message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weixin.pojo.Message;
import weixin.util.BaseDao;

public class UpdateDate extends BaseDao<Message> {
	private static Logger log = LoggerFactory.getLogger(UpdateDate.class);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 进行审批 修改message状态
	 * 
	 * @param message
	 *            单据消息
	 * @return 受影响行数
	 */
	public int update(Message message) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		int rows = 0;
		String sql = "update  message set yjcontent='" + message.getYjcontent()
				+ "', sptime='" + sdf.format(message.getSptime())
				+ "', state='" + message.getState() + "' where id="
				+ message.getId() + ";";
		try {
			statement = connection.prepareStatement(sql);
			rows = statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(connection, statement, null);
		}
		return rows;
	}

	/**
	 * 平台进行审批(同意,驳回)后 修改message状态
	 * 
	 * @param message
	 *            单据消息
	 * @return 受影响行数
	 */
	public String BipUpdate(Message message) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		int rows = 0;
//		String sql = "update message set yjcontent='" + message.getYjcontent()
//				+ "', sptime='" + sdf.format(message.getSptime())
//				+ "', state='" + message.getState() + "' where documentsid ='"
//				+ message.getDocumentsid() + "' and state1='"
//				+ message.getState1() + "' and (w_corpid='"
//				+ message.getW_corpid() + "' or d_corpid = '"+message.getD_corpid()+"' )and spname='"
//				+ message.getSpname() + "' and state=0";
		String sql = "update message set yjcontent='" + message.getYjcontent()
		+ "', sptime='" + sdf.format(message.getSptime())
		+ "', state='" + message.getState() + "' where documentsid ='"
		+ message.getDocumentsid() + "' and (w_corpid='"
		+ message.getW_corpid() + "' or d_corpid = '"+message.getD_corpid()+"' )and spname='"
		+ message.getSpname() + "' and state=0";
		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			rows = statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(connection, statement, null);
		}
		return rows + "";
	}

	/**
	 * 审批人为多个时 一个人进行审批后 将其他人的单据删除
	 * 
	 * @param message
	 *            单据消息
	 * @return 受影响行数
	 */
	public String BipTuiHui(Message message) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		int rows = 0;
		String sql = "delete from  message  where documentsid ='"
				+ message.getDocumentsid() + "'  and state1='"
				+ message.getState1() + "' and spname <> '"
				+ message.getSpname() + "' and (w_corpid='"
				+ message.getW_corpid() + "' or d_corpid='"
				+ message.getD_corpid() + "' ) and  state=0";
		try {
			statement = connection.prepareStatement(sql);
			rows = statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(connection, statement, null);
		}
		return rows + "";
	}

	/**
	 * 将审批信息重置为 未读状态(审批退回时)
	 * 
	 * @param message
	 *            单据消息
	 * @return 受影响行数
	 */
	public String ToWeiDu(Message message) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		int rows = 0;
		String sql = "update message set yjcontent='', sptime=null,state=0 where documentsid ='"
				+ message.getDocumentsid()
				+ "'  and spname='"
				+ message.getSpname()
				+ "' and ( w_corpid='"
				+ message.getW_corpid()
				+ "' or d_corpid = '"+ message.getD_corpid() +"') and state1='"
				+ message.getState1() + "'";
		try {
			statement = connection.prepareStatement(sql);
			rows = statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(connection, statement, null);
		}
		return rows + "";
	}

	/**
	 * 平台执行退回后 将统一单据 状态为0 的删除掉
	 * 
	 * @param message
	 *            单据消息
	 * @return 受影响行数
	 */
	public String TuiHuiDelete(Message message) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		int rows = 0;
		String sql = "delete from  message  where documentsid ='"
				+ message.getDocumentsid() + "' and w_corpid='"
				+ message.getW_corpid() + "' and  state=0";
		try {
			statement = connection.prepareStatement(sql);
			rows = statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(connection, statement, null);
		}
		return rows + "";
	}

	/**
	 * 平台执行退回后 返回退回时删除的单据 审批人
	 * 
	 * @param message  单据消息
	 * @return spname
	 */
	public String TuiHuiSpName(Message message) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String spname="";
		String sql = "select spweixinid,spname from  message  where documentsid ='"
				+ message.getDocumentsid()
				+ "' and w_corpid='"
				+ message.getW_corpid() + "' and  state=0";
		try { 
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				spname += resultSet.getString("spname")+"|";
			}
			spname = spname.substring(0,spname.length()-1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(connection, statement, null);
		}
		return spname;
	}
	/**
	 * 统计发送通知记录数
	 * 
	 * @param documentsid
	 *            单据编号
	 * @param w_corpid
	 *            企业号标识
	 * @param state
	 *            状态
	 * @param state1
	 *            状态1
	 * @param spname
	 *            审批人
	 * 
	 *            public void ToHistory(String documentsid,String
	 *            w_corpid,String state,String state1,String spname){ Connection
	 *            connection = getConnection(); PreparedStatement statement =
	 *            null; int rows = 0; String sql =
	 *            "INSERT INTO history SELECT * FROM message where 1=1";
	 *            if(documentsid!=null&&!documentsid.equals(""))
	 *            sql+=" and documentsid='"+documentsid+"'";
	 *            if(w_corpid!=null&&!w_corpid.equals(""))
	 *            sql+=" and w_corpid='"+w_corpid+"'";
	 *            if(state!=null&&!state.equals(""))
	 *            sql+=" and state='"+state+"'";
	 *            if(state1!=null&&!state1.equals(""))
	 *            sql+=" and state1='"+state1+"'";
	 *            if(spname!=null&&!spname.equals(""))
	 *            sql+=" and spname = '"+spname+"'"; try { statement =
	 *            connection.prepareStatement(sql); log.info(sql); rows =
	 *            statement.executeUpdate(); } catch (Exception e) {
	 *            e.printStackTrace(); } finally { closeAll(connection,
	 *            statement, null); } }
	 */
}