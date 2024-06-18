<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Funcionário</title>
<link rel="stylesheet" href="./css/styles.css">
</head>
<body>
	<%@ include file="menu.jsp"%>

	<div class="container">
		<form action="funcionario" method="post">
			<p class="title">
				<b>Funcionário</b>
			</p>
			<table>
				<tr>
					<td><label for="id">ID:</label> <input type="text" id="id"
						name="id" placeholder="ID do funcionário"></td>
				</tr>
				<tr>
					<td><label for="nome">Nome:</label> <input type="text"
						id="nome" name="nome" placeholder="Nome do funcionário"></td>
				</tr>
				<tr>
					<td><label for="username">Username</label> <input type="text"
						id="username" name="username" placeholder="Username"></td>
				</tr>
				<tr>
					<td><label for="password">Senha</label> <input type="password"
						id="password" name="password" placeholder="Senha"></td>
				</tr>
				<tr>
					<td colspan="4">
						<input type="submit" name="botao" value="Cadastrar"> 
						<input type="submit" name="botao" value="Listar"> 
						<input type="submit" name="botao" value="Buscar">
						<input type="submit" name="botao" value="Excluir">
						<input type="submit" name="botao" value="Quantidade em vendas">
						<input type="submit" name="botao" value="Valor em vendas">
					</td>
				</tr>
			</table>
		</form>
	</div>

	<c:if test="${ not empty saida }">
		<div class="container">
			<h2 class="error">
				<c:out value="${ saida }" />
			</h2>
		</div>
	</c:if>

	<c:if test="${ not empty funcionarios }">
		<div class="container">
			<table>
				<thead>
					<tr class="table-header">
						<th>ID</th>
						<th>Nome</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="p" items="${ funcionarios }">
						<tr>
							<td>${ p.id }</td>
							<td>${ p.nome }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</c:if>
</body>
</html>