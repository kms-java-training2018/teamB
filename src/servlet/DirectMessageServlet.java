package servlet;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.DirectMessageBean;
import bean.MainBean;
import bean.SessionBean;
import model.DirectMessageModel;
import model.MessageInfoModel;

public class DirectMessageServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		System.out.println("DirectMessageServletにきました");

		// 文字コード指定
		req.setCharacterEncoding("utf-8");

		//      // 【デバッグ用】-------------------------------------
		//      // セッション切れた状況を作り出す
		//      HttpSession session = req.getSession(true);
		//      session.invalidate();
		//      ------------------------------------------------------

		// 【セッションが開始しているかどうかの判定】
		HttpSession session = req.getSession(false);
		// ---開始していない場合(タイムアウト含む)
		if (session == null) {
			//nullならセッションは切れている。
			// エラー画面に遷移
			req.setAttribute("errorMessage", "セッションが開始されていない、もしくはタイムアウトになりました。");
			req.getRequestDispatcher("/WEB-INF/jsp/errorPage.jsp").forward(req, res);
			// ---すでに開始している場合
		} else {
			// 開始済みセッションを取得
			session = req.getSession(true);
		}

		// 【セッション内にログイン情報を保持しているかどうかの判定】
		SessionBean sessionBean = (SessionBean) session.getAttribute("session");
		// ---保持されていない場合
		if (sessionBean == null
				|| sessionBean.getUserNo().equals(null)
				|| sessionBean.getUserName().equals(null)) {
			// セッションを削除
			session.invalidate();
			// エラー画面に遷移
			req.setAttribute("errorMessage", "ログインされていません。");
			req.getRequestDispatcher("/WEB-INF/jsp/errorPage.jsp").forward(req, res);
		}

		////////////////////////////////////////////////////////////////////////
		//		初期化
		////////////////////////////////////////////////////////////////////////

		// ページの行き先変更変数
		String direction = "/WEB-INF/jsp/directMessage.jsp";

		/** クラスMainBeanの初期化 */

		/** クラスDirectMessageBeanの初期化 */
		DirectMessageBean directMessageBean = new DirectMessageBean();
		/** クラスDirectMessageModelLookのインスタンス取得　*/
		DirectMessageModel directMessageModel = new DirectMessageModel();
		/** jspに持っていくArrayList（会話内容、judge、会話番号、表示名）初期化　*/
		ArrayList<DirectMessageBean> directMessageList = new ArrayList<DirectMessageBean>();

		MainBean mainBean = new MainBean();

		////////////////////////////////////////////////////////////////////////
		//		ここからdirectMessageBeanに必要な値を入れる
		////////////////////////////////////////////////////////////////////////

		// SessionBeanからログインユーザの会員番号取得
		directMessageBean.setUserNo(sessionBean.getUserNo());
		System.out.println("UserNo：" + directMessageBean.getUserNo());

		// SessionBeanからログインユーザの表示名取得
		directMessageBean.setUserName(sessionBean.getUserName());
		System.out.println("UserName：" + directMessageBean.getUserName());

		// セッションスコープから送信対象者の会員番号取得
		directMessageBean.setToSendUserNo(req.getParameter("otherUserNo")/*mainBean.getOtherNo()*/);////////////////////////
		String toSendUserNo = directMessageBean.getToSendUserNo();
		System.out.println("ToSendUserNo：" + toSendUserNo);



/** ここから修正 */

		// パラメータ送信対象者の会員番号が存在しない場合エラー画面に遷移する//////////////////////////////////////////////
		//デバッグ用
		ArrayList<MainBean> toSendUserNoCheck = new ArrayList<MainBean>();
		toSendUserNoCheck = (ArrayList<MainBean>) session.getAttribute("toSendUserList");

		// 送信対象者の会員番号が存在するか確認する
		int notToSendUserNo = 0;

		// 送信対象者の会員番号が存在するか確認する
		if(toSendUserNo == null) {
			System.out.println("会員番号が存在しません");
			direction = "/WEB-INF/jsp/errorPage.jsp";
			// セッションを削除
			session.invalidate();
			// エラー画面に遷移
			req.setAttribute("errorMessage", "会員番号が存在しません");
			req.getRequestDispatcher("/WEB-INF/jsp/errorPage.jsp").forward(req, res);
		}

		for (int i = 0; i < toSendUserNoCheck.size(); i++) {
			mainBean = toSendUserNoCheck.get(i);
			if (toSendUserNo.equals(mainBean.getOtherNo())) {
				notToSendUserNo = 1;
				System.out.println("toSendUserNo：" + toSendUserNo + " は存在します");
			}
		}

		if (notToSendUserNo == 0) {
			System.out.println("不正な会員番号です");
			direction = "/WEB-INF/jsp/errorPage.jsp";
			// セッションを削除
			session.invalidate();
			// エラー画面に遷移
			req.setAttribute("errorMessage", "不正な会員番号です");
			req.getRequestDispatcher("/WEB-INF/jsp/errorPage.jsp").forward(req, res);
		}


/** ここまで修正 */



		// リクエストスコープから送信対象者の表示名取得
		directMessageBean.setOtherName(req.getParameter("otherUserName")/*mainBean.getOtherName()*/);/////////////////////
		System.out.println("OtherName：" + directMessageBean.getOtherName());

		// セッションスコープにdirectMessageBeanをセット
		session.setAttribute("dMBean", directMessageBean);

		////////////////////////////////////////////////////////////////////////
		//		ここまでdirectMessageBeanに必要な値を入れる
		////////////////////////////////////////////////////////////////////////

		// DirectMessageModelLookへ移動会話情報取得処理
		try {
			System.out.println("DirectMessageModelLookにいきます");
			directMessageList = directMessageModel.lookMessage(directMessageBean);
			if (!directMessageList.get(directMessageList.size() - 1).getUserNo().equals(sessionBean.getUserNo())) {
				session.setAttribute("message_no", directMessageList.get(directMessageList.size() - 1).getMessageNo());
			} else {
				session.setAttribute("message_no", "fake");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DirectMessageServlet、認証処理キャッチ");
			// セッションを削除
			session.invalidate();
			// エラー画面に遷移
			req.setAttribute("errorMessage", "DB接続中にエラーが発生しました。");
			req.getRequestDispatcher("/WEB-INF/jsp/errorPage.jsp").forward(req, res);
		}
		System.out.println("DirectMessageServletに帰ってきました");

		// レコードが取得出来なかった場合エラー画面に遷移する
//		if (directMessageList == null) {
//			System.out.println("レコードがないです");
//			session = req.getSession(false);
//			session = null;
//			direction = "/WEB-INF/jsp/errorPage.jsp";
//		}

		// リクエストスコープにいれてjspに送る
		req.setAttribute("directMessageList", directMessageList);
		req.setAttribute("directMessageBean", directMessageBean);
		System.out.println("表示終わり");
		req.getRequestDispatcher(direction).forward(req, res);
	}

	//------------------------------------------------------------------------------
	// ここからdoPost
	//------------------------------------------------------------------------------
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		System.out.println("DirectMessageServletにきました");

		// 文字コード指定
		req.setCharacterEncoding("utf-8");

		//      // 【デバッグ用】-------------------------------------
		//      // セッション切れた状況を作り出す
		//      HttpSession session = req.getSession(true);
		//      session.invalidate();
		//      ------------------------------------------------------

		// 【セッションが開始しているかどうかの判定】
		HttpSession session = req.getSession(false);
		// ---開始していない場合(タイムアウト含む)
		if (session == null) {
			//nullならセッションは切れている。
			// エラー画面に遷移
			req.setAttribute("errorMessage", "セッションが開始されていない、もしくはタイムアウトになりました。");
			req.getRequestDispatcher("/WEB-INF/jsp/errorPage.jsp").forward(req, res);
			// ---すでに開始している場合
		} else {
			// 開始済みセッションを取得
			session = req.getSession(true);
		}

		// 【セッション内にログイン情報を保持しているかどうかの判定】
		SessionBean sessionBean = (SessionBean) session.getAttribute("session");
		// ---保持されていない場合
		if (sessionBean == null
				|| sessionBean.getUserNo().equals(null)
				|| sessionBean.getUserName().equals(null)) {
			// セッションを削除
			session.invalidate();
			// エラー画面に遷移
			req.setAttribute("errorMessage", "ログインされていません。");
			req.getRequestDispatcher("/WEB-INF/jsp/errorPage.jsp").forward(req, res);
		}

		String action = req.getParameter("action");

		/** クラスDirectMessageBeanの初期化 */
		DirectMessageBean directMessageBean = (DirectMessageBean) session.getAttribute("dMBean");

		switch (action) {

		////////////////////////////////////////////////////////////////////////
		//		ここからsendMessage処理
		////////////////////////////////////////////////////////////////////////

		case "sendMessage":
			System.out.println("sendMessage処理にきました");
			/** クラスMessageInfoModelのインスタンス取得　*/
			MessageInfoModel entryMessage = new MessageInfoModel();

			String sendUserNo = directMessageBean.getUserNo();//req.getParameter("userNo");
			String toSendAddress = directMessageBean.getToSendUserNo();//req.getParameter("toSendUserNo");
			int judgeAddress = 0;
			String message = req.getParameter("inputMessage");
			System.out.println("メッセージ「 " + message + " 」を登録します");
			System.out.println("メッセージのバイト数は " + message.getBytes(Charset.forName("UTF-8")).length + " です。");
			// 文字数チェック
			if (message.getBytes(Charset.forName("UTF-8")).length <= 0
					|| 100 < message.getBytes(Charset.forName("UTF-8")).length) {
				System.out.println("バイト数が間違っています");
				req.setAttribute("errorMessage", "バイト数は1バイト以上100バイト以下にしてください");
			} else {
				System.out.println("バイト数は正しいです");

				try {
					// 文字数チェックを通過した場合メッセージ登録
					boolean result1 = entryMessage.entryMessage(sendUserNo, message, toSendAddress, judgeAddress);
					if (result1 == false) {
						System.out.println("会話情報を登録できません");
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("会話情報を登録できません");
					session.invalidate();
					// エラー画面に遷移
					req.setAttribute("errorMessage", "DB接続中にエラーが発生しました。");
					req.getRequestDispatcher("/WEB-INF/jsp/errorPage.jsp").forward(req, res);
				}
			}
			System.out.println("会話情報を登録しました");


			break;
		////////////////////////////////////////////////////////////////////////
		//		ここまでsendMessage処理
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		//		ここからdeleteMessage処理
		////////////////////////////////////////////////////////////////////////
		case "deleteMessage":
			System.out.println("deleteMessage処理にきました");
			// パラメーターを受け取る
			String messageNo = req.getParameter("messageNo");
			System.out.println("会話番号「 " + messageNo + " 」を論理削除します");

			// メッセージの論理削除処理を実行
			// 【メッセージの論理削除処理が失敗した場合】
			try {
				MessageInfoModel deleteMessage = new MessageInfoModel();
				if (deleteMessage.deleteMessage(messageNo) == false) {
					System.out.println("会話情報を論理削除できません");
				}
			} catch (Exception e) {
				// セッションを削除
				session.invalidate();
				// エラー画面に遷移
				req.setAttribute("errorMessage", "DB接続中にエラーが発生しました。");
				System.out.println("会話情報を論理削除できません");
				req.getRequestDispatcher("/WEB-INF/jsp/errorPage.jsp").forward(req, res);
			}
			System.out.println("会話情報を論理削除しました");
			// 【メッセージの論理削除処理が成功した場合】
			// switch文を抜けて、ページ表示処理をする。
			break;
		////////////////////////////////////////////////////////////////////////
		//		こまでdeleteMessage処理
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		//		どれにも当てはまらない場合
		////////////////////////////////////////////////////////////////////////

		default:

			System.out.println("不正なパラメーターが送られました。＠GroupMessageServlet");

			break;
		}

		////////////////////////////////////////////////////////////////////////
		//		ここから遷移時の処理
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		//		初期化
		////////////////////////////////////////////////////////////////////////

		// ページの行き先変更変数
		String direction = "/WEB-INF/jsp/directMessage.jsp";

		/** クラスDirectMessageModelLookのインスタンス取得　*/
		DirectMessageModel model = new DirectMessageModel();
		/** jspに持っていくArrayList（会話内容、judge、会話番号）初期化　*/
		ArrayList<DirectMessageBean> directMessageList = new ArrayList<DirectMessageBean>();

		// ログイン判定処理
		//　セッションがない場合エラー画面に移動
		if (sessionBean.getUserNo().equals(null) || sessionBean.getUserName().equals(null)) {
			System.out.println("セッションがないです");
			session = req.getSession(false);
			session = null;
			direction = "/WEB-INF/jsp/errorPage.jsp";
		}

		// パラメータ送信対象者の会員番号が存在しない場合エラー画面に遷移する//////////////////////////////////////////////

		MainBean mainBean = new MainBean();

		ArrayList<MainBean> toSendUserNoCheck = new ArrayList<MainBean>();
		toSendUserNoCheck = (ArrayList<MainBean>) session.getAttribute("toSendUserList");

		// 送信対象者の会員番号が存在するか確認する
		int notToSendUserNo = 0;

		// セッションスコープから送信対象者の会員番号取得
//		directMessageBean.setToSendUserNo(req.getParameter("otherUserNo")/*mainBean.getOtherNo()*/);////////////////////////
		String toSendUserNo = directMessageBean.getToSendUserNo();
		System.out.println("ToSendUserNo：" + toSendUserNo);

		// 送信対象者の会員番号が存在するか確認する
		if(toSendUserNo == null) {
			System.out.println("会員番号が存在しません");
			direction = "/WEB-INF/jsp/errorPage.jsp";
			// セッションを削除
			session.invalidate();
			// エラー画面に遷移
			req.setAttribute("errorMessage", "会員番号が存在しません");
			req.getRequestDispatcher("/WEB-INF/jsp/errorPage.jsp").forward(req, res);
		}

		for (int i = 0; i < toSendUserNoCheck.size(); i++) {
			mainBean = toSendUserNoCheck.get(i);
			if (toSendUserNo.equals(mainBean.getOtherNo())) {
				notToSendUserNo = 1;
				System.out.println("toSendUserNo：" + toSendUserNo + " は存在します");
			}
		}

		if (notToSendUserNo == 0) {
			System.out.println("不正な会員番号です");
			direction = "/WEB-INF/jsp/errorPage.jsp";
			// セッションを削除
			session.invalidate();
			// エラー画面に遷移
			req.setAttribute("errorMessage", "不正な会員番号です");
			req.getRequestDispatcher("/WEB-INF/jsp/errorPage.jsp").forward(req, res);
		}

		// DirectMessageModelLookへ移動会話情報取得処理
		try {
			System.out.println("DirectMessageModelLookにいきます");
			directMessageList = model.lookMessage(directMessageBean);
			if (!directMessageList.get(directMessageList.size() - 1).getUserNo().equals(sessionBean.getUserNo())) {
				session.setAttribute("message_no", directMessageList.get(directMessageList.size() - 1).getMessageNo());
			} else {
				session.setAttribute("message_no", "fake");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DirectMessageServlet、認証処理キャッチ");
            // セッションを削除
            session.invalidate();
            // エラー画面に遷移
            req.setAttribute("errorMessage", "DB接続中にエラーが発生しました。");
            req.getRequestDispatcher("/WEB-INF/jsp/errorPage.jsp").forward(req, res);
		}
		System.out.println("DirectMessageServletに帰ってきました");

		// レコードが取得出来なかった場合エラー画面に遷移する
//		if (directMessageList == null) {
//			System.out.println("レコードがないです");
//			session = req.getSession(false);
//			session = null;
//			direction = "/WEB-INF/jsp/errorPage.jsp";
//		}

		// リクエストスコープにいれてjspに送る
		req.setAttribute("directMessageList", directMessageList);
		req.setAttribute("directMessageBean", directMessageBean);
		System.out.println("更新終わり");
		req.getRequestDispatcher(direction).forward(req, res);
	}
}