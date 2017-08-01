package weixin.connection.oaggtz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weixin.pojo.Oaflb;
import weixin.pojo.Oaggtz;
import weixin.pojo.Users;
import weixin.util.BaseDao;

public class OperateOaflb  extends BaseDao<Oaflb> {
	private static final Log log = LogFactory.getLog(OperateOaflb.class);
	/**
	 * 查询信息类别
	 */
	public List<Oaflb> selectOaflb(){
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Oaflb oaflb=null;
		List<Oaflb> listO=new ArrayList<Oaflb>();
		try{
			String sql="select sbm,smc from oaflb where clb=4 and sbm<>'499' order by sbm";
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				oaflb=new Oaflb();
				oaflb=(Oaflb) this.Field(oaflb, resultSet);
				listO.add(oaflb);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			closeAll(connection, statement, resultSet);
		}
		return listO;
	}
}
