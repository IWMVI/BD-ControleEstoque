<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Controle Estoque</title>
<style>
body {
	font-family: Arial, sans-serif;
	margin: 0;
	padding: 0;
	background-color: #f4f4f4;
}

.container {
	max-width: 400px;
	margin: 0 auto;
	padding: 20px;
	background-color: #fff;
	border-radius: 8px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
	margin-top: 50px;
}

h1 {
	color: #333;
	margin-bottom: 30px;
}

input[type="text"], input[type="password"], input[type="submit"] {
	width: 100%;
	padding: 10px;
	margin-bottom: 15px;
	border: 1px solid #ccc;
	border-radius: 5px;
	box-sizing: border-box;
}

input[type="submit"] {
	background-color: #4CAF50;
	color: white;
	cursor: pointer;
}

input[type="submit"]:hover {
	background-color: #45a049;
}

.error-message {
	color: red;
	margin-top: 10px;
}
</style>
</head>
<body>
	<div class="container" align="center">
		<h1>Joalheria Brilho Mais</h1>

		<form action="login" method="post">
			<input type="text" name="username" placeholder="Usuário" required><br>
			<input type="password" name="password" placeholder="Senha" required><br>
			<input type="submit" value="Entrar">
		</form>
		<c:if test="${not empty erro}">
			<div class="error-message">${erro}</div>
		</c:if>
	</div>
</body>
</html>