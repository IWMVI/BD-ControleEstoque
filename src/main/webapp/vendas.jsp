<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="./css/styles.css">
<title>Vendas</title>
</head>
<body class="d-flex flex-column min-vh-100">

	<jsp:include page="menu.jsp"></jsp:include>

	<div class="container text-center my-4">
		<form action="venda" method="post">
			<h2>Registrar Venda</h2>
			<div class="form-group">
				<label for="clienteId">Cliente ID:</label> <input type="text"
					id="clienteId" name="clienteId" class="form-control"
					placeholder="ID do Cliente" required />
			</div>
			<div class="form-group">
				<label for="funcionarioId">Funcionário ID:</label> <input
					type="text" id="funcionarioId" name="funcionarioId"
					class="form-control" placeholder="ID do Funcionário" required />
			</div>
			<div id="itens-venda">
				<div class="form-group">
					<label for="produtoId">Produto ID:</label> <input type="text"
						name="produtoId" class="form-control" placeholder="ID do Produto"
						required /> <label for="quantidade">Quantidade:</label> <input
						type="number" name="quantidade" class="form-control" step="1"
						placeholder="Quantidade" required />
				</div>
			</div>
			<button type="button" class="btn btn-secondary"
				onclick="adicionarItem()">Adicionar Item</button>
			<div class="form-group mt-3">
				<input class="btn btn-success" type="submit" name="enviar"
					value="Registrar Venda" />
			</div>
		</form>
	</div>

	<!-- Adicionar mais itens -->
	<script>
    function adicionarItem() {
        const div = document.createElement('div');
        div.className = 'form-group';
        div.innerHTML = `
            <label for="produtoId">Produto ID:</label>
            <input type="text" name="produtoId" class="form-control" placeholder="ID do Produto" required />
            <label for="quantidade">Quantidade:</label>
            <input type="number" name="quantidade" class="form-control" step="1" placeholder="Quantidade" required />
        `;
        document.getElementById('itens-venda').appendChild(div);
    }
	</script>

	<c:if test="${not empty erro}">
		<div class="container text-center">
			<h2 class="text-danger">
				<c:out value="${erro}" />
			</h2>
		</div>
	</c:if>

	<c:if test="${not empty saida}">
		<div class="container text-center">
			<h2 class="text-success">
				<c:out value="${saida}" />
			</h2>
		</div>
	</c:if>

	<c:if test="${not empty vendas}">
		<div class="container text-center">
			<h2>Lista de Vendas</h2>
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>ID</th>
						<th>Cliente</th>
						<th>Funcionário</th>
						<th>Data da Venda</th>
						<th>Total</th>
						<th>Produtos</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="venda" items="${vendas}">
						<tr>
							<td>${venda.id}</td>
							<td>${venda.clienteNome}</td>
							<td>${venda.funcionarioNome}</td>
							<td>${venda.dataVenda}</td>
							<td>${venda.total}</td>
							<td>
								<c:forEach var="item" items="${venda.itens}">
									Produto ID: ${item.produtoID}, Quantidade: ${item.quantidade} <br />
								</c:forEach>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</c:if>

</body>
</html>
