<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Relatórios</title>
</head>
<body>
	<%@ include file="menu.jsp"%>

	<div class="container text-center my-4">
		<form action="relatorio" method="post">
			<h2>Relatório</h2>
			<table>
				<tr>
					<td><input class="listar" type="submit" name="botao" value="Listar" /></td>
					<td><input class="contar" type="submit" name="botao" value="Vendas cliente" /></td>
					<td><input class="total" type="submit" name="botao" value="Total" /></td>
				</tr>
			</table>
		</form>
	</div>

	<c:if test="${ not empty relatorios }">
		<div class="container text-center">
			<table class="table">
				<thead>
					<tr>
						<th class="table-header">ID</th>
						<th class="table-header">Descrição</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="relatorio" items="${ relatorios }">
						<tr>
							<td>${ relatorio.id }</td>
							<td>${ relatorio.descricao }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</c:if>
</body>
</html>
