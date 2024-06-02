<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Controle Estoque</title>
</head>
<body>
	<div class="container" align="center">
		<h1>Controle Estoque</h1>

		<form action="login" method="post">
			<input type="text" id="username" name="username" placeholder="Usuário" required><br>
			<input type="password" id="password" name="password" placeholder="Senha" required><br>
			<input type="submit" id="entrar" value="Entrar">
		</form>
		<c:if test="${not empty erro}">
			<div class="error-message">${erro}</div>
		</c:if>
	</div>
</body>
</html>