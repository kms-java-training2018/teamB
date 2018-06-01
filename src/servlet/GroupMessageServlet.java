package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.MessageInfoModel;

public class GroupMessageServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

		req.getRequestDispatcher("/WEB-INF/jsp/groupMessage.jsp").forward(req, res);


//		//セッションにログイン情報が保持されていない場合、エラーページに遷移
//		HttpSession session = req.getSession();
//		if (session.getAttribute("userName") == null) {
//			System.out.println("セッションがありませんでした");
//			req.getRequestDispatcher("/WEB-INF/jsp/ersrorPage.jsp").forward(req, res);
//		}
	}


	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

		// 初期化
		MessageInfoModel model = new MessageInfoModel();
		boolean result;


		// セッションの値を取得
		HttpSession session = req.getSession();
		String userName = (String)session.getAttribute("userName");
		String userNo = (String)session.getAttribute("userNo");

		// パラメーターの値を取得
		String action = req.getParameter("action");
		switch (action) {

// 【ページ遷移時】-----------------------------------------------------------------------------------------------------
//		メインメニューでグループ名のリンクを押下することで実行される処理。
//		セッションにあるログイン情報とグループ情報テーブルにある会員情報を比較して不正な遷移ではないかチェックした後、
//		会話情報を取得してグループメッセージ画面に遷移する。

		case "toGroupMeesage":


			// (1)-1 セッションの存在チェックを行う。
			// (1)-2 チェックでエラーが発生した場合、不正な遷移と判断して、エラー画面に遷移する。
			if (userName == null) {
	//			System.out.println("セッションがありませんでした");
				req.getRequestDispatcher("/WEB-INF/jsp/errorPage.jsp").forward(req, res);
			}

			// (1)-3 グループ番号をパラメータとして持っているか確認する。
			// (1)-4 パラメータが足りなかった場合、不正な遷移と判断して、エラー画面に遷移する。
			String groupNo  = req.getParameter("groupNo");
			if (groupNo == null) {
				req.getRequestDispatcher("/WEB-INF/jsp/errorPage.jsp").forward(req, res);
			}

			// (1)-5 セッションに保持している会員番号、パラメータとして受け取ったグループ番号を条件に
			//       グループ情報テーブルから自身のデータを取得する
			try {
	//			bean = model.authentication(bean);
			} catch (Exception e) {
				e.printStackTrace();
			}


			// (1)-6 データが取得できなかった場合、グループのメンバーでないと判断してエラー画面に遷移する




			req.getRequestDispatcher("/WEB-INF/jsp/groupMessage.jsp").forward(req, res);
		break;



// 【メッセージ送信時】-------------------------------------------------------------------------------------------------
//		メッセージ画面で送信ボタンを押したときに実行される処理。
//		パラメータチェックに成功した場合、会話情報テーブルにメッセージを登録する。
//		あわせて、最新の会話情報テーブルの情報を表示

		case "sendMessage":

			// メッセージ登録用のメソッドを呼び出し(登録処理を実行し)、
			// 【登録処理が失敗した場合(メソッドの戻り値がfalseの場合)】
			if (model.entryMessage("1", "arigatou", "1", 1) == false) {
				// TODO セッションの情報を削除する

				// エラーページに遷移
				req.getRequestDispatcher("/WEB-INF/jsp/errorPage.jsp").forward(req, res);
			}


			// 【登録処理が成功した場合】
			// TODO 最新の会話情報を表示するメソッドを呼び出す


			// TODO リクエストにパラメーターをセット


			req.getRequestDispatcher("/WEB-INF/jsp/groupMessage.jsp").forward(req, res);
		break;

// 【メッセージ削除時】-------------------------------------------------------------------------------------------------
//		グループ画面で削除ボタンを押したときに実行される処理。
//		ダイアログで論理削除確認後(JSP)、会話情報を論理削除する。
//		あわせて、最新の会話情報テーブルの情報を表示

		case "deleteMessage":

			// メッセージ削除用のメソッドを呼び出し(削除処理を実行し)、
			// 【削除処理が失敗した場合(メソッドの戻り値がfalseの場合)】
			if (model.deleteMessage("1") == false) {
				// TODO セッションの情報を削除する


				// エラーページに遷移
				req.getRequestDispatcher("/WEB-INF/jsp/errorPage.jsp").forward(req, res);
			}


			// 【削除処理が成功した場合】
			// TODO 最新の会話情報を表示するメソッドを呼び出す


			// TODO リクエストにパラメーターをセット


			req.getRequestDispatcher("/WEB-INF/jsp/groupMessage.jsp").forward(req, res);
		break;

// 【グループ脱退時】---------------------------------------------------------------------------------------------------
//		グループ画面で脱退ボタンを押したときに実行される処理。
//		ダイアログで論理削除確認後、グループ情報テーブルから論理削除する。
//		成功した場合、メインメニュー画面に遷移

		case "leaveGroup":

			// (2)-1 セッション情報の会員番号とをグループ番号を条件に、会話情報テーブルから論理削除する
			try {
				bean = model.authentication(bean);
			} catch (Exception e) {
				e.printStackTrace();
			}


			// (2)-2 レコードを論理削除できなかった場合、エラー画面に遷移する。


			req.getRequestDispatcher("/WEB-INF/jsp/groupMessage.jsp").forward(req, res);
		break;

// 【どのcaseにも当てはまらない時】-------------------------------------------------------------------------------------

		default:

			System.out.println("不正なパラメーターが送られました。＠GroupMessageServlet");

		break;
		}
	}
}
