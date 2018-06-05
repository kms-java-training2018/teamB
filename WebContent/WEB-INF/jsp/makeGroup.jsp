<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>グループ作成</title>
</head>
<body>
	<header> ようこそ<br>
	${sessionScope.userName}さん<br>
	<form action="/chat/logout" method="POST">
		<button type='submit' name='action' value='logout'>ログアウト</button>
	</form>
	</header>

	<h1>チャット研修プログラム</h1>
	<h2>グループ作成</h2>
	<p>${errorMessage}</p>
	<p>グループ名</p>
	<input type="text" name="inputGroupName" class="inputGroupCreate">
	<p>メンバーを選ぶ</p>

	<!-- 【グループ作成画面、グループメンバー選択箇所】 -->
	<form action="/chat/makeGroup" method="post">
		<!-- 自分 -->
		<label><input type="checkbox" name="selectMember"
			value="${sessionScope.userNo}" checked="checked" required>${sessionScope.userName}さん</label>

		<!-- 他ユーザ -->
		<c:forEach var="nom" items="${allUserNo}" varStatus="status">
			<c:if test="${nom != sessionScope.userNo}">

				<label><input type="checkbox" name="selectMember"
					value="${nom}">${allUserName[status.index]} さん</label><br>
			</c:if>
		</c:forEach>
		<button type='submit' name='action' value='groupCreate'>グループ作成</button>
	</form>



	<form action="/chat/main" method="POST">
		<input type="submit" value="メインメニューに戻る">
	</form>
</body>
</html>

