<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<header> ようこそ<br>
	${sessionScope.userName}さん<br>
	<form action="/chat/logout" method="POST">
		<button type='submit' name='action' value='logout'>ログアウト</button>
	</form>
	</header>
	<h1>チャット研修プログラム</h1>
	<h2>マイページ</h2>
	<p>${errorMessage}</p>

	<form action="/chat/myPage" method="POST">
		<p>表示名</p>
		<p>
			<input type="text" name="inputUserName" value="${userName}"
				class="inputUserProf">
		</p>
		<p>自己紹介</p>
		<p>
			<input type="text" name="inputUserSelfIntro" value="${myPageText}"
				class="inputUserProf">
		</p>


		<button type='submit' name='action' value='profileUpdate'>プロフィールを更新</button>

	</form>
	<form action="/chat/main" method="POST">
		<input type="submit" value="メインメニューに戻る">
	</form>

</body>
</html>