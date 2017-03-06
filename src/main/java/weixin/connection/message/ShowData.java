package weixin.connection.message;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weixin.pojo.Message;
import weixin.pojo.PageInfo;
import weixin.util.BaseDao;
import weixin.util.StringUtil;

public class ShowData extends BaseDao {
	private static Logger log = LoggerFactory.getLogger(ShowData.class);
	//页面数据条数常量 --期望值
	private static Integer PAGE_SIZE = 10;
	//滚动加载数据每次加载数据数量 --期望值
	private static Integer RESULT_SIZE = 10;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 根据编号查询审批内容
	 * 
	 * @param keyid
	 *            编号
	 * @return message
	 */
	public Message show(String keyid) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Message data = null;
		String sql = "select * from  message where id=" + keyid;
		try {
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				data = new Message();
				data.setId(resultSet.getInt("id"));//主键 
				data.setTitle(resultSet.getString("title"));//标题 
				data.setName(resultSet.getString("name"));//提交人 姓名（编号） 
				data.setSpweixinid(resultSet.getString("spweixinid"));//审批人 微信企业号 UserID
				data.setSpname(resultSet.getString("spname"));//审批人 姓名（编号）
				// data.setSpname2(resultSet.getString("spname2"));/** 审批人// 姓名（编号） **/
				data.setContent(resultSet.getString("content"));//审批内容 
				data.setYjcontent(resultSet.getString("yjcontent"));//审批意见
				data.setTjtime(sdf.parse(resultSet.getString("tjtime")));// 提交时间 
				if (resultSet.getString("sptime") != null) {
					data.setSptime(sdf.parse(resultSet.getString("sptime")));// 审批时间 
				}
				data.setDocumentsid(resultSet.getString("documentsid"));//单据编号 **/
				data.setDocumentstype(resultSet.getString("documentstype"));//单据类型 **/
				data.setTablename(resultSet.getString("tablename"));//单据表名 **/
				data.setSbuId(resultSet.getString("sbuid"));//业务号 **/
				data.setState(resultSet.getInt("state"));//状态 0未审批 1审批通过 2驳回申请 **/
				data.setState0(resultSet.getString("state0"));//状态 **/
				data.setState1(resultSet.getString("state1"));//状态 **/
				// data.setState2(resultSet.getString("state2"));/** 状态**/
				data.setDbid(resultSet.getString("dbid"));//数据库标识 **/
				data.setGs(resultSet.getString("scm"));//公司 **/
				data.setScm(resultSet.getString("scm"));//公司 **/
				data.setDepartment(resultSet.getString("department"));// 部门 **/
				data.setAppid(resultSet.getString("appid"));//应用id **/
				data.setWapno(resultSet.getString("wapno"));//平台定义的应用id **/
				data.setW_corpid(resultSet.getString("w_corpid"));//微信企业号id **/
				data.setSmake(resultSet.getString("smake"));//制单人
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(connection, statement, resultSet);
		}
		return data;
	}

	/**
	 * 查询符合条件的单据审批记录 或者起始记录
	 * 
	 * @param documentsid
	 *            单据id
	 * @param w_corpid
	 *            企业号标识
	 * @param jl
	 *            jl=jl 查询符合条件的单据审批记录 jl=tz 查询符合条件的单据起始记录
	 * @return List<Message>
	 */
	public List<Message> showjilu(String documentsid, String w_corpid, String jl) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Message> list = new ArrayList<Message>();
		Message mess = null;
		String sql;
		if (jl.equals("jl"))
			sql = "select * from  message where documentsid='" + documentsid
					+ "' and w_corpid='" + w_corpid + "'  order by id";
		else
			sql = "select * from message where id in (SELECT MIN(id) FROM  message where documentsid='"
					+ documentsid
					+ "' and w_corpid='"
					+ w_corpid
					+ "' and state=0 GROUP BY spname)";
		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				mess = new Message();
				mess.setId(resultSet.getInt("id"));//主键 **/
				mess.setSpweixinid(resultSet.getString("spweixinid"));//审批人 微信企业号 UserID **/
				mess.setSpname(resultSet.getString("spname"));//审批人 姓名（编号） **/
				mess.setYjcontent(resultSet.getString("yjcontent"));//审批意见 **/
				if (resultSet.getString("sptime") != null)
					mess.setSptime(sdf.parse(resultSet.getString("sptime")));//审批时间 **/
				mess.setDocumentsid(resultSet.getString("documentsid"));//单据编号 **/
				mess.setDocumentstype(resultSet.getString("documentstype"));//单据类型 **/
				mess.setState(resultSet.getInt("state"));//状态 0未审批 1审批通过 2驳回申请 **/
				mess.setState0(resultSet.getString("state0"));
				mess.setState1(resultSet.getString("state1"));
				mess.setContent(resultSet.getString("content"));//审批内容 **/
				mess.setTjtime(sdf.parse(resultSet.getString("tjtime")));//提交时间 **/
				mess.setName(resultSet.getString("name"));//提交人 姓名（编号） **/
				mess.setW_corpid(resultSet.getString("w_corpid"));//企业微信号标识 **/
				mess.setAppid(resultSet.getString("appid"));//企业微信号标识 **/
				mess.setScm(resultSet.getString("scm"));//公司 **/
				mess.setSmake(resultSet.getString("smake"));//制单人
				mess.setTitle(resultSet.getString("title"));//标题
				list.add(mess);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(connection, statement, resultSet);
		}
		return list;
	}

	//获得最大行数
	public Integer getStateDataCount(String spweixinid,String state,String w_corpid){
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
		ca.setTime(new Date()); // 设置时间为当前时间
		ca.add(Calendar.MONTH, -1); // 月份减1
		Date lastMonth = ca.getTime(); // 结果
		String sptime = sdf.format(lastMonth);
		//防错 参数错误的话 默认查询返回零
		String sql = "select 0 as countnum";
		if(state.equals("0")){
			sql = "select count(1) as countnum from " +
					" message t1 left join v_message_start t2 on t1.documentsid = t2.documentsid " +
					" left join users t3 on t3.userid =  t2.NAME and t3.w_corpid = t2.w_corpid " +
					" where t1.w_corpid = '" + w_corpid + "' and t1.state = '" + state + "' and t1.spweixinid = '" + spweixinid+"'";
		}else if(state.equals("1")){
			sql = "select count(1) as countnum from  message t1 "
					+ " left join v_message_start t2 on t1.documentsid = t2.documentsid "
					+ " left join users t3 on t3.userid =  t2.NAME and t3.w_corpid = t2.w_corpid "
					+ " left join v_message_last t4 on t1.documentsid = t4.documentsid "
					+ "where t1.spweixinid='"
					+ spweixinid
					+ "' and t1.state in('"+state+"','2') and t1.w_corpid='"
					+ w_corpid
					+ "' and t1.sptime>='"
					+ sptime
					+ "' and t1.id IN (SELECT MAX(id) FROM  message where  state <>0 and spweixinid='"+ spweixinid +"' GROUP BY documentsid )";
		}else if(state.equals("3")){
			sql = " select count(1) as countnum from " +
					" message t1 left join v_message_last t2 on t1.documentsid = t2.documentsid " +
					" where t1.name='"+ spweixinid +"' and t1.tjtime>= '"+ sptime +"' and t1.w_corpid='"+ w_corpid +"' " +
					" and t1.id IN (SELECT MIN(id) FROM  message GROUP BY documentsid )";
		}
		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				return resultSet.getInt("countnum");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			closeAll(connection, statement, resultSet);
		}
		return 0;
	}
	//规范页码输入，超过最大页码就返回最大页码，页码小于1就等于1；
	public Integer checkPageIndex(Integer pageIndex,Integer maxCountnum){
		//pageIndex 从1开始
		Integer remaindernum = maxCountnum%PAGE_SIZE;
		Integer maxPageIndex = maxCountnum/PAGE_SIZE;
		log.info(remaindernum.toString());
		log.info(maxPageIndex.toString());
		log.info(pageIndex.toString());
		if(remaindernum>0){
			maxPageIndex = maxPageIndex+1;
		}
		if(pageIndex > maxPageIndex){
			return  maxPageIndex;
		}
		if(pageIndex < 1){
			return 1;
		}
		return pageIndex;
	}
	/**
	 * 查询 某人 的某个状态的数据，根据offset 偏移量和 RESULT_SIZE 期望返回结果数量
	 *
	 * @param spweixinid
	 *            某人的微信编号
	 * @param state
	 *            状态 (0:待审,1:已审,2:驳回,3:本人提交记录)
	 * @param w_corpid
	 *            企业号标识
	 * @return List<Message>
	 */
	public List<Message> showStateByPage(String spweixinid,String state,String w_corpid,Integer offset){
		//pageIndex 从1开始
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Message> list = new ArrayList<Message>();
		Message mess = null;
		Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
		ca.setTime(new Date()); // 设置时间为当前时间
		ca.add(Calendar.MONTH, -1); // 月份减1
		Date lastMonth = ca.getTime(); // 结果
		String sql = "";
		String sptime = sdf.format(lastMonth);
		if (state.equals("0"))
			sql = " select t1.*,ifnull(t3.username,t2.NAME) as submit,'-1' as lastState from " +
					" message  t1 left join v_message_start t2 on t1.documentsid = t2.documentsid " +
					" left join users t3 on t3.userid =  t2.NAME and t3.w_corpid = t2.w_corpid " +
					" where t1.w_corpid = '" + w_corpid + "' and t1.state = '" + state + "' and t1.spweixinid = '" + spweixinid + "' ORDER BY t1.tjtime "+
					" LIMIT ? OFFSET ? ";
		if (state.equals("1"))
			sql = "select t1.*,ifnull(t3.username,t2.NAME) as submit, t4.state as lastState from  message t1 "
					+ " left join v_message_start t2 on t1.documentsid = t2.documentsid "
					+ " left join users t3 on t3.userid =  t2.NAME and t3.w_corpid = t2.w_corpid "
					+ " left join v_message_last t4 on t1.documentsid = t4.documentsid "
					+ "where t1.spweixinid='"
					+ spweixinid
					+ "' and t1.state in('"+state+"','2') and t1.w_corpid='"
					+ w_corpid
					+ "' and t1.sptime>='"
					+ sptime
					+ "' and t1.id IN (SELECT MAX(id) FROM  message where state <>0 and spweixinid='"+ spweixinid +"' GROUP BY documentsid )  ORDER BY t1.sptime DESC " +
					"  LIMIT ? OFFSET ? ";
		if (state.equals("3"))
			sql = " select t1.*,'' as submit,t2.state as lastState from " +
					" message t1 left join v_message_last t2 on t1.documentsid = t2.documentsid " +
					" where t1.name='"+ spweixinid +"' and t1.tjtime>= '"+ sptime +"' and t1.w_corpid='"+ w_corpid +"' " +
					" and t1.id IN (SELECT MIN(id) FROM  message GROUP BY documentsid )  ORDER BY t1.tjtime DESC " +
					"  LIMIT ? OFFSET ? ";
		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1,RESULT_SIZE);
			statement.setInt(2,offset);
			resultSet = statement.executeQuery();
			resultSet.last(); //移到最后一行
			Integer rowCount = resultSet.getRow(); //得到当前行号 记录最大数据
			resultSet.beforeFirst();
			Integer resultOffset = offset + rowCount;
			while (resultSet.next()) {
				mess = new Message();
				mess.setId(resultSet.getInt("id"));//主键 **/
				mess.setTitle(resultSet.getString("title"));//标题 **/
				mess.setName(resultSet.getString("name"));//提交人 姓名（编号） **/
				mess.setSpweixinid(resultSet.getString("spweixinid"));//审批人 微信企业号 UserID **/
				mess.setSpname(resultSet.getString("spname"));//审批人 姓名（编号） **/
				mess.setContent(resultSet.getString("content"));//审批内容 **/
				mess.setYjcontent(resultSet.getString("yjcontent"));//审批意见 **/
				mess.setTjtime(sdf.parse(resultSet.getString("tjtime")));// 提交时间 **/
				mess.setDocumentsid(resultSet.getString("documentsid"));//单据编号 **/
				mess.setDocumentstype(resultSet.getString("documentstype"));//单据类型 **/
				mess.setTablename(resultSet.getString("tablename"));//单据表名 **/
				mess.setState(resultSet.getInt("state"));//状态 0未审批 1审批通过 2驳回申请 **/
				mess.setScm(resultSet.getString("scm"));
				mess.setW_corpid(resultSet.getString("w_corpid"));
				mess.setSmake(resultSet.getString("smake"));//制单人
				mess.setSubmit(resultSet.getString("submit"));//制单人
				mess.setLastState(resultSet.getString("lastState"));
				mess.setTjtimeStr(StringUtil.timePass(mess.getTjtime(), 4));
				mess.setOffset(resultOffset);
				list.add(mess);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(connection, statement, resultSet);
		}
		return list;
	}
	/**
	 * 查询 某人 的某个状态的数据内容
	 * 
	 * @param spweixinid
	 *            某人的微信编号
	 * @param state
	 *            状态 (0:待审,1:已审,2:驳回,3:本人提交记录)
	 * @param w_corpid
	 *            企业号标识
	 * @return List<Message>
	 */
	public List<Message> showState(String spweixinid, String state,
			String w_corpid) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Message> list = new ArrayList<Message>();
		Message mess = null;
		Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
		ca.setTime(new Date()); // 设置时间为当前时间
		ca.add(Calendar.MONTH, -1); // 月份减1
		Date lastMonth = ca.getTime(); // 结果
		String sql = "";
		String sptime = sdf.format(lastMonth);
		if (state.equals("0"))
			sql = " select t1.*,ifnull(t3.username,t2.NAME) as submit,'-1' as lastState from " +
			" message  t1 left join v_message_start t2 on t1.documentsid = t2.documentsid " +
			" left join users t3 on t3.userid =  t2.NAME and t3.w_corpid = t2.w_corpid " + 
			" where t1.w_corpid = '" + w_corpid + "' and t1.state = '" + state + "' and t1.spweixinid = '" + spweixinid + "' ORDER BY t1.tjtime";
			
//			sql = "select * from  message where spweixinid='" + spweixinid
//					+ "' and state=" + state + " and w_corpid='" + w_corpid
//					+ "' ORDER BY tjtime ";
		if (state.equals("1"))
//			sql = " select t1.*,ifnull(t3.username,t2.NAME) as submit,'-1' as lastState FROM message t1 " +
//			" left join v_message_start t2 on t1.documentsid = t2.documentsid " +
//			" left join users t3 on t3.userid =  t2.NAME and t3.w_corpid = t2.w_corpid " +
//			" WHERE t1.id in( " +
//								" select max(id) FROM message WHERE documentsid in( " +
//								" select documentsid FROM message where spweixinid = '"+spweixinid+"' " +
//									" and w_corpid = '"+ w_corpid +"' and state <> 0 group by documentsid)" +
//										"group by documentsid)  and t1.sptime>= '"+ sptime +"' ORDER BY t1.tjtime DESC ";
			sql = "select t1.*,ifnull(t3.username,t2.NAME) as submit, t4.state as lastState from  message t1 "
					+ " left join v_message_start t2 on t1.documentsid = t2.documentsid "
					+ " left join users t3 on t3.userid =  t2.NAME and t3.w_corpid = t2.w_corpid "
					+ " left join v_message_last t4 on t1.documentsid = t4.documentsid "
					+ "where t1.spweixinid='"
					+ spweixinid
					+ "' and t1.state in('"+state+"','2') and t1.w_corpid='"
					+ w_corpid
					+ "' and t1.sptime>='"
					+ sptime
					+ "' and t1.id IN (SELECT MAX(id) FROM  message where state <>0 and spweixinid='"+spweixinid+"' GROUP BY documentsid )  ORDER BY t1.sptime DESC";
//			sql = "select * FROM message WHERE id in(" 
//					+"select max(id) FROM message WHERE documentsid in("
//						+"select documentsid FROM message where spweixinid = '"+spweixinid
//							+"' and w_corpid = '"+w_corpid+"' and state <> 0 group by documentsid) "
//								+"group by documentsid) ORDER BY sptime DESC";
//			sql = "select *,'' as submit from  message where spweixinid='"
//					+ spweixinid
//					+ "' and state="
//					+ state
//					+ " and w_corpid='"
//					+ w_corpid
//					+ "' and sptime>='"
//					+ sptime
//					+ "' and id IN (SELECT MAX(id) FROM  message where state <>0 GROUP BY documentsid )  ORDER BY sptime DESC";
		if (state.equals("2"))
			sql = "select *,'' as submit,'-1' as lastState  from  message where spweixinid='"
					+ spweixinid
					+ "' and state="
					+ state
					+ " and w_corpid='" 
					+ w_corpid
					+ "' and sptime>='"
					+ sptime
					+ "' and id IN (SELECT MIN(id) FROM  message GROUP BY documentsid )  ORDER BY sptime DESC";
		if (state.equals("3"))
			sql = " select t1.*,'' as submit,t2.state as lastState from " + 
			" message t1 left join v_message_last t2 on t1.documentsid = t2.documentsid " +
			" where t1.name='"+ spweixinid +"' and t1.tjtime>= '"+ sptime +"' and t1.w_corpid='"+ w_corpid +"' " +
			" and t1.id IN (SELECT MIN(id) FROM  message GROUP BY documentsid )  ORDER BY t1.tjtime DESC ";
//			sql="select * from message where smake='"+ spweixinid + "' and w_corpid='"+ w_corpid+ "' and tjtime>='"	+ sptime +"ORDER BY tjtime DESC";
//			sql = "select *,'' as submit from  message where name='"
//					+ spweixinid
//					+ "' and w_corpid='"
//					+ w_corpid
//					+ "' and tjtime>='"
//					+ sptime
//					+ "' and id IN (SELECT MIN(id) FROM  message GROUP BY documentsid )  ORDER BY tjtime DESC";
		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				mess = new Message();
				mess.setId(resultSet.getInt("id"));//主键 **/
				mess.setTitle(resultSet.getString("title"));//标题 **/
				mess.setName(resultSet.getString("name"));//提交人 姓名（编号） **/
				mess.setSpweixinid(resultSet.getString("spweixinid"));//审批人 微信企业号 UserID **/
				mess.setSpname(resultSet.getString("spname"));//审批人 姓名（编号） **/
				mess.setContent(resultSet.getString("content"));//审批内容 **/
				mess.setYjcontent(resultSet.getString("yjcontent"));//审批意见 **/
				mess.setTjtime(sdf.parse(resultSet.getString("tjtime")));// 提交时间 **/
				mess.setDocumentsid(resultSet.getString("documentsid"));//单据编号 **/
				mess.setDocumentstype(resultSet.getString("documentstype"));//单据类型 **/
				mess.setTablename(resultSet.getString("tablename"));//单据表名 **/
				mess.setState(resultSet.getInt("state"));//状态 0未审批 1审批通过 2驳回申请 **/
				mess.setScm(resultSet.getString("scm"));
				mess.setW_corpid(resultSet.getString("w_corpid"));
				mess.setSmake(resultSet.getString("smake"));//制单人
				mess.setSubmit(resultSet.getString("submit"));//制单人
				mess.setLastState(resultSet.getString("lastState"));
				list.add(mess);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(connection, statement, resultSet);
		}
		return list;
	}

	/**
	 * 统计某人的待审任务数量 被驳回任务数量
	 * 
	 * @param spweixinid
	 *            某人的微信编号
	 * @param state
	 *            状态 (0:待审,2:驳回)
	 * @param w_corpid
	 *            企业号标识
	 * @return 统计的记录数
	 */
	public Integer showNum(String spweixinid, String state, String w_corpid) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int num = 0;
		String sql = "select count(id) countid from  message where spweixinid='"
				+ spweixinid
				+ "' and state="
				+ state
				+ " and w_corpid='"
				+ w_corpid + "'";
		try {
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				num = resultSet.getInt("countid");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(connection, statement, resultSet);
		}
		return num;
	}

	/**
	 * 查询此条记录是否为多人审批记录
	 * 
	 * @param mess
	 *            条件
	 * @return 审批记录List<Message>
	 */
	public List<Message> showSPRenNum(Message mess) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Message data = null;
		List<Message> lM = new ArrayList<Message>();
		String sql = "select id,spname from  message where name='"
				+ mess.getName() + "' and documentsid='"
				+ mess.getDocumentsid() + "'  and w_corpid='"
				+ mess.getW_corpid() + "' and state1='" + mess.getState1()
				+ "'";
		try {
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				data = new Message();
				data.setId(resultSet.getInt("id"));
				/** 主键 **/
				lM.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(connection, statement, resultSet);
		}
		return lM;
	}

	/**
	 * 根据状态编号查找状态文字
	 * 
	 * @param cid
	 *            状态编号
	 * @return 状态文字
	 */
	public String showStateWZ(String cid) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = "select sname from  insstate where cid=" + cid;
		try {
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString("sname");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(connection, statement, resultSet);
		}
		return "";
	}

	/**
	 * 查询被驳回信息的 初始提交人
	 * 
	 * @param w_corpid
	 *            企业号标识
	 * @param scm
	 *            公司(部门)
	 * @param documentsid
	 *            单据号
	 * @return 提交人编号
	 */
	public String showName(String w_corpid, String scm, String documentsid) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String name = "";
		String sql = "SELECT name FROM message WHERE w_corpid='" + w_corpid
				+ "' AND scm='" + scm + "' AND documentsid='" + documentsid
				+ "' ORDER BY id ASC";
		try {
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				name = resultSet.getString("name");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(connection, statement, resultSet);
		}
		return name;
	}

	public PageInfo<Message> showState1(PageInfo<Message> page) {
		Message msg = (Message) page.getCondition();
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Message> list = new ArrayList<Message>();
		Message mess = null;
		Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
		ca.setTime(new Date()); // 设置时间为当前时间
		ca.add(Calendar.MONTH, -1); // 月份减1
		Date lastMonth = ca.getTime(); // 结果
		String sql = "";
		String sptime = sdf.format(lastMonth);
		try {
			if (msg.getState()==0)
				sql = "select count(id) num from  message where spweixinid='"
						+ msg.getSpweixinid() + "' and state=" + msg.getState()
						+ " and w_corpid='" + msg.getW_corpid() + "'";
			if (msg.getState()==1)
				sql = "select count(id) num from  message where spweixinid='"
						+ msg.getSpweixinid()
						+ "' and state="
						+ msg.getState()
						+ " and w_corpid='"
						+ msg.getW_corpid()
						+ "' and sptime>='"
						+ sptime
						+ "' and id IN (SELECT MIN(id) FROM  message GROUP BY documentsid ) ";
			if (msg.getState()==2)
				sql = "select count(id) num from  message where spweixinid='"
						+ msg.getSpweixinid()
						+ "' and state="
						+ msg.getState()
						+ " and w_corpid='"
						+ msg.getW_corpid()
						+ "' and sptime>='"
						+ sptime
						+ "' and id IN (SELECT MIN(id) FROM  message GROUP BY documentsid ) ";
			if (msg.getState()==3)
				sql = "select count(id) num from  message where name='"
						+ msg.getSpweixinid()
						+ "' and w_corpid='"
						+ msg.getW_corpid()
						+ "' and tjtime>='"
						+ sptime
						+ "' and id IN (SELECT MIN(id) FROM  message GROUP BY documentsid ) ";
			log.info("state"+msg.getState());
			log.info("sql"+sql);
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			int count = 0;
			if (resultSet.next())
				count = resultSet.getInt("num");
			log.info(count+"");
			page.setTotal(count);
			int rows = (page.getCurrentPage() - 1) * page.getPageSize();
			int PageSize = page.getPageSize();
			if (msg.getState() == 0)
				sql = "SELECT top " + PageSize
						+ " * FROM message WHERE id NOT IN( SELECT top " + rows
						+ " id  FROM message where spweixinid='"
						+ msg.getSpweixinid() + "' and state=" + msg.getState()
						+ " and w_corpid='" + msg.getW_corpid()
						+ "' ORDER BY tjtime ) and  spweixinid='"
						+ msg.getSpweixinid() + "' and state=" + msg.getState()
						+ " and w_corpid='" + msg.getW_corpid()
						+ "' ORDER BY tjtime ";
			if (msg.getState() == 1)
				sql = "SELECT top "
						+ PageSize
						+ " * FROM message WHERE id NOT IN( SELECT top "
						+ rows
						+ " id  FROM message where spweixinid='"
						+ msg.getSpweixinid()
						+ "' and state="
						+ msg.getState()
						+ " and w_corpid='"
						+ msg.getW_corpid()
						+ "' and sptime>='"
						+ sptime
						+ "' and id IN (SELECT MIN(id) FROM  message GROUP BY documentsid )  ORDER BY tjtime DESC ) and spweixinid='"
						+ msg.getSpweixinid()
						+ "' and state="
						+ msg.getState()
						+ " and w_corpid='"
						+ msg.getW_corpid()
						+ "' and sptime>='"
						+ sptime
						+ "' and id IN (SELECT MIN(id) FROM  message GROUP BY documentsid )  ORDER BY tjtime DESC )";
			if (msg.getState() == 2)
				sql = "SELECT top "
						+ PageSize
						+ " * FROM message WHERE id NOT IN( SELECT top "
						+ rows
						+ " id  FROM message where spweixinid='"
						+ msg.getSpweixinid()
						+ "' and state="
						+ msg.getState()
						+ " and w_corpid='"
						+ msg.getW_corpid()
						+ "' and sptime>='"
						+ sptime
						+ "' and id IN (SELECT MIN(id) FROM  message GROUP BY documentsid )  ORDER BY tjtime DESC) and spweixinid='"
						+ msg.getSpweixinid()
						+ "' and state="
						+ msg.getState()
						+ " and w_corpid='"
						+ msg.getW_corpid()
						+ "' and sptime>='"
						+ sptime
						+ "' and id IN (SELECT MIN(id) FROM  message GROUP BY documentsid )  ORDER BY tjtime DESC";
			if (msg.getState() == 3)
				sql = "SELECT top "
						+ PageSize
						+ " * FROM message WHERE id NOT IN( SELECT top "
						+ rows
						+ " id  FROM message where name='"
						+ msg.getSpweixinid()
						+ "' and w_corpid='"
						+ msg.getW_corpid()
						+ "' and tjtime>='"
						+ sptime
						+ "' and id IN (SELECT MIN(id) FROM  message GROUP BY documentsid )  ORDER BY tjtime DESC) and name='"
						+ msg.getSpweixinid()
						+ "' and w_corpid='"
						+ msg.getW_corpid()
						+ "' and tjtime>='"
						+ sptime
						+ "' and id IN (SELECT MIN(id) FROM  message GROUP BY documentsid )  ORDER BY tjtime DESC";
			log.info("sql"+sql);
			connection = this.getConnection();
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				mess = new Message();
				mess.setId(resultSet.getInt("id"));//主键 **/
				mess.setTitle(resultSet.getString("title"));//标题 **/
				mess.setName(resultSet.getString("name"));//提交人 姓名（编号） **/
				mess.setSpweixinid(resultSet.getString("spweixinid"));//审批人 微信企业号 UserID **/
				mess.setSpname(resultSet.getString("spname"));//审批人 姓名（编号） **/
				mess.setContent(resultSet.getString("content"));//审批内容 **/
				mess.setYjcontent(resultSet.getString("yjcontent"));//审批意见 **/
				mess.setTjtime(sdf.parse(resultSet.getString("tjtime")));//提交时间 **/
				mess.setDocumentsid(resultSet.getString("documentsid"));//单据编号 **/
				mess.setDocumentstype(resultSet.getString("documentstype"));//单据类型 **/
				mess.setTablename(resultSet.getString("tablename"));//单据表名 **/
				mess.setState(resultSet.getInt("state"));//状态 0未审批 1审批通过 2驳回申请 **/
				mess.setScm(resultSet.getString("scm"));
				mess.setW_corpid(resultSet.getString("w_corpid"));
				mess.setSmake(resultSet.getString("smake"));//制单人
				list.add(mess);
			}
			page.setRows(list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(connection, statement, resultSet);
		}
		return page;
	}
}