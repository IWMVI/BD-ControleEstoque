<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="./css/styles.css">
<title>Produtos</title>
</head>
<body class="d-flex flex-column min-vh-100">

	<jsp:include page="menu.jsp"></jsp:include>

	<div class="container text-center my-4">
		<form action="produto" method="post">
			<h2>Produtos</h2>
			<div class="form-group">
				<label for="id">ID:</label> <input type="text" id="id" name="id"
					class="form-control" placeholder="ID do Produto" />
			</div>
			<div class="form-group">
				<label for="nome">Nome:</label> <input type="text" id="nome"
					name="nome" class="form-control" placeholder="Nome do Produto" />
			</div>
			<div class="form-group">
				<label for="valor">Valor:</label> <input type="number" id="valor"
					name="valor" class="form-control" step="0.01" placeholder="Valor" />
			</div>
			<div class="form-group">
				<label for="quantidade">Quantidade:</label> <input type="number"
					id="quantidade" name="quantidade" class="form-control" step="1"
					placeholder="Quantidade" />
			</div>
			<div class="form-group">
				<input class="btn btn-success" type="submit" name="enviar" value="Cadastrar" />
				<input class="btn btn-primary" type="submit" name="enviar" value="Alterar" />
			 	<input class="btn btn-info" type="submit" name="enviar" value="Buscar" />
			 	<input class="btn btn-warning" type="submit" name="enviar" value="Listar" />
				<input class="btn btn-secondary" type="submit" name="enviar" value="Entrada/Saída" />
			</div>
		</form>
	</div>


	<c:if test="${not empty saida}">
		<div class="container text-center">
			<h2 class="text-danger">
				<c:out value="${saida}" />
			</h2>
		</div>
	</c:if>

	<c:if test="${not empty produtos}">
		<div class="container text-center">
			<table class="table">
				<thead>
					<tr>
						<th>ID</th>
						<th>Produto</th>
						<th>Valor</th>
						<th>Quantidade</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="p" items="${produtos}">
						<tr>
							<td>${p.id}</td>
							<td>${p.nome}</td>
							<td>${p.valorFormatado}</td>
							<td>${p.quantidade}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</c:if>
</body>
</html>