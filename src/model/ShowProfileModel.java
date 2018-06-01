//package model;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//import bean.SessionBean;
//import bean.ShowProfileBean;
//
//public class ShowProfileModel {
//	public ShowProfileBean ShowProfile(SessionBean sessionBean) {
//		// 初期化
//		ShowProfileBean showProfileBean = new ShowProfileBean();
//		String userNo = sessionBean.getUserNo();
//
//		StringBuilder sb = new StringBuilder(); // SQL文の格納用
//		Connection conn = null;
//		String url = "jdbc:oracle:thin:@192.168.51.67:1521:XE";
//		String user = "DEV_TEAM_B";
//		String dbPassword = "B_DEV_TEAM";
//
//		// JDBCドライバーのロード
//		try {
//			Class.forName("oracle.jdbc.driver.OracleDriver");
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		// 接続を作成
//		try {
//			conn = DriverManager.getConnection(url, user, dbPassword);
//
//			// 表示名と自己紹介文を取得
//			// SQL作成
//			sb.append("SELECT ");
//			sb.append("user_name ");
//			sb.append(" ,my_page_text ");
//			sb.append("FROM ");
//			sb.append(" m_user ");
//			sb.append(" WHERE ");
//			sb.append(" user_no = '" + userNo + "' ");
//
//			// SQL実行
//			Statement stmt = conn.createStatement();
//			ResultSet rs = stmt.executeQuery(sb.toString());
//
//			// SQL実行結果に1行目があるかどうか(DBに該当データがあるかどうか)
//			if (rs.next()) { // あった場合
//				showProfileBean.setUserName(rs.getString("user_name"));
//				showProfileBean.setMyPageText(rs.getString("my_page_text"));
//			} else { // なかった場合
//				// beanに、DB会員マスタの表示名と自己紹介文、空文字を代入
//				showProfileBean.setUserName("");
//				showProfileBean.setMyPageText("");
//
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			//SQLの接続は絶対に切断
//		} finally {
//			try {
//				conn.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return showProfileBean;
//	}
//
//}
