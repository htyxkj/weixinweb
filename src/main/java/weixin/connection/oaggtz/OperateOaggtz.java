package weixin.connection.oaggtz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weixin.pojo.Oaggtz;
import weixin.pojo.Users;
import weixin.util.BaseDao;

public class OperateOaggtz  extends BaseDao {
	private static final Log log = LogFactory.getLog(OperateOaggtz.class);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//页面数据条数常量 --期望值
	private static Integer PAGE_SIZE = 10;
	//滚动加载数据每次加载数据数量 --期望值
	private static Integer RESULT_SIZE = 10;	
	/**
	 * 查询 某人 的某个状态的数据，根据offset 偏移量和 RESULT_SIZE 期望返回结果数量
	 * @param spweixinid       某人的微信编号
	 * @param state            状态 (0:未读,1:已读,2:我发布的)
	 * @param w_corpid         企业号标识
	 * @return List<Oaggtz>
	 */
	public List<Oaggtz> ShowAll(String spweixinid,String scm,String read,String w_corpid,String d_corpid,Integer offset){
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Oaggtz> list = new ArrayList<Oaggtz>();
		try {
		String sql="";
		 if(read.equals("0")){
			sql="select * from oaggtz where (w_corpid=? or d_corpid=?) and (susr like ? or sorgto like ? or susr like '%All%' or sorgto like '%All%') and sid not in(select sid from oaggtzread where (w_corpid=? or d_corpid=?) and usercode=?) order by mkdate desc LIMIT ? OFFSET ? ";
			statement = connection.prepareStatement(sql);
			statement.setString(1,w_corpid);
			statement.setString(2,d_corpid);
			statement.setString(3,"%"+spweixinid+"%");
			statement.setString(4,"%"+scm+"%");
			statement.setString(5,w_corpid);
			statement.setString(6,d_corpid);
			statement.setString(7,spweixinid);
			statement.setInt(8,RESULT_SIZE);
			statement.setInt(9,offset);
		}else if(read.equals("1")){
			sql="select * from oaggtz where (w_corpid=? or d_corpid=?) and (susr like ? or sorgto like ? or susr like '%All%' or sorgto like '%All%') and sid in(select sid from oaggtzread where (w_corpid=? or d_corpid=?) and usercode=?) order by mkdate desc LIMIT ? OFFSET ? ";
			statement = connection.prepareStatement(sql);
			statement.setString(1,w_corpid);
			statement.setString(2,d_corpid);
			statement.setString(3,"%"+spweixinid+"%");
			statement.setString(4,"%"+scm+"%");
			statement.setString(5,w_corpid);
			statement.setString(6,d_corpid);
			statement.setString(7,spweixinid);
			statement.setInt(8,RESULT_SIZE);
			statement.setInt(9,offset);
		}else if(read.equals("2")){
			sql="select * from oaggtz where (w_corpid=? or d_corpid=?) and smaker like ? order by mkdate desc LIMIT ? OFFSET ? ";
			statement = connection.prepareStatement(sql);
			statement.setString(1,w_corpid);
			statement.setString(2,d_corpid);
			statement.setString(3,"%"+spweixinid+"%");
			statement.setInt(4,RESULT_SIZE);
			statement.setInt(5,offset);
		}
			resultSet = statement.executeQuery();
			resultSet.last(); //移到最后一行
			Integer rowCount = resultSet.getRow(); //得到当前行号 记录最大数据
			resultSet.beforeFirst();
			Integer resultOffset = offset + rowCount;
			Oaggtz oaggtz=null;
			while (resultSet.next()) {
				oaggtz=new Oaggtz();
				oaggtz=(Oaggtz) this.Field(oaggtz, resultSet);
				oaggtz.setCkdate(sdf.parse(resultSet.getString("ckdate")));
				oaggtz.setMkdate(sdf.parse(resultSet.getString("mkdate")));
				oaggtz.setOffset(resultOffset);
				list.add(oaggtz);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeAll(connection, statement, resultSet);
		}
		return list;
	}
	/**
	 * 根据编号查询详细信息
	 * @param keyid 主键ID
	 * @return
	 */
	public Oaggtz selectOne(String keyid){
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Oaggtz oaggtz=null;
		try{
			String sql="select * from oaggtz where id=?";
			statement = connection.prepareStatement(sql);
			statement.setString(1,keyid);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				oaggtz=new Oaggtz();
				oaggtz=(Oaggtz) this.Field(oaggtz, resultSet);
				oaggtz.setCkdate(sdf.parse(resultSet.getString("ckdate")));
				oaggtz.setMkdate(sdf.parse(resultSet.getString("mkdate")));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			closeAll(connection, statement, resultSet);
		}
		return oaggtz;
	}
	/**
	 * 修改为已读
	 * @param keyid 主键ID
	 */
	public void updateRad(String sid,String userid,String w_corpid,String d_corpid){
		Connection connection = getConnection();
		PreparedStatement statement = null;
		try{
			String sql="insert into oaggtzread(sid,usercode,`read`,w_corpid,d_corpid)values(?,?,?,?,?)";
			statement = connection.prepareStatement(sql);
			statement.setString(1,sid);
			statement.setString(2,userid);
			statement.setInt(3,0);
			statement.setString(4,w_corpid);
			statement.setString(5,d_corpid);
			statement.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			closeAll(connection, statement, null);
		}
	}
	public void insertOaggtz(Oaggtz oagg){
		Connection connection = getConnection();
		PreparedStatement statement = null;
		try{
			String sql="insert into oaggtz (C_corp,sid,slb,smaker,sorgto,susr,title,content,mkdate,fj_root,suri,sbuid,state,schk,ckdate,"
					+ "sorg,coll_cc,scm,xxgs,`read`,w_appid,wapno,w_corpid,Serverurl,source,dbid,d_appid,d_corpid) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			statement = connection.prepareStatement(sql);
			statement.setInt(    1,oagg.getC_corp());
			statement.setString( 2,oagg.getSid());
			statement.setString( 3,oagg.getSlb());
			statement.setString( 4,oagg.getSmaker());
			statement.setString( 5,oagg.getSorgto());
			statement.setString( 6,oagg.getSusr());
			statement.setString( 7,oagg.getTitle());
			statement.setString( 8,oagg.getContent());
			statement.setString( 9,sdf.format(oagg.getMkdate()));
			statement.setString(10,oagg.getFj_root());
			statement.setString(11,oagg.getSuri());
			statement.setString(12,oagg.getSbuid());
			statement.setInt(	13,oagg.getState());
			statement.setString(14,oagg.getSchk());
			statement.setString(15,sdf.format(oagg.getCkdate()));
			statement.setString(16,oagg.getSorg());
			statement.setString(17,oagg.getColl_cc());
			statement.setString(18,oagg.getScm());
			statement.setString(19,oagg.getXxgs());
			statement.setString(20,oagg.getRead());
			statement.setString(21,oagg.getW_appid());
			statement.setString(22,oagg.getWapno());
			statement.setString(23,oagg.getW_corpid());
			statement.setString(24,oagg.getServerurl());
			statement.setString(25,oagg.getSource());
			statement.setString(26, oagg.getDbid());
			statement.setString(27, oagg.getD_appid());
			statement.setString(28, oagg.getD_corpid());
			statement.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			closeAll(connection, statement, null);
		}
	}
	/**
	 * 查询公告接收 员工  根据 部门 公司查询
	 */
	public List<Users> selectUserID(String scm){
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Users users=null;
		List<Users> listU=new ArrayList<Users>();
		try{
			String sql="select * from users where scm like ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1,scm+"%");
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				users=new Users();
				users=(Users) this.Field(users, resultSet);
				listU.add(users);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			closeAll(connection, statement, resultSet);
		}
		return listU;
	}
	/**
	 * 平台提交退回后删除本条公告信息
	 * @param sid		单据编号
	 * @param W_corpid	微信企业号标识
	 */
	public void deleteOagg(String sid,String W_corpid){
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try{
			String sql="delete from oaggtz where sid=? and w_corpid=?";
			statement = connection.prepareStatement(sql);
			statement.setString(1,sid);
			statement.setString(2,W_corpid);
			statement.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			closeAll(connection, statement, resultSet);
		}
	} 
}
