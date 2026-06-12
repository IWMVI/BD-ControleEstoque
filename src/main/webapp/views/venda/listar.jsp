<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=DM+Serif+Display:ital@0;1&display=swap" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
<title>Vendas - Joalheria Brilho Mais</title>
</head>
<body class="d-flex flex-column min-vh-100">

<%@ include file="../menu.jsp"%>

<c:if test="${h2Fallback}">
    <div class="container" style="margin-top: 16px; margin-bottom: 0; padding: 12px 18px;">
        <div class="alert-warning">
            <strong>&#9888; Modo tempor&aacute;rio:</strong> Banco H2 em mem&oacute;ria ativo.
            Os dados n&atilde;o ser&atilde;o persistidos entre reinicializa&ccedil;&otilde;es.
            Conecte-se a um SGBD (SQL Server) para opera&ccedil;&atilde;o normal.
        </div>
    </div>
</c:if>

<div class="page-header">
    <h2>Vendas</h2>
    <div class="divider"></div>
</div>

<div class="container">
	<form action="${pageContext.request.contextPath}/venda" method="post">
		<div style="display: grid; grid-template-columns: 1fr 1fr; gap: 0 20px;">
			<div class="form-group">
				<label for="clienteId">Cliente ID</label>
				<input type="text" id="clienteId" name="clienteId" class="form-control" placeholder="ID do Cliente" required />
			</div>
			<div class="form-group">
				<label for="funcionarioId">Funcionário ID</label>
				<input type="text" id="funcionarioId" name="funcionarioId" class="form-control" placeholder="ID do Funcionário" required />
			</div>
		</div>
		<div id="itens-venda">
			<div class="form-group">
				<label>Produto ID</label>
				<input type="text" name="produtoId" class="form-control" placeholder="ID do Produto" required />
				<label style="margin-top: 14px;">Quantidade</label>
				<input type="number" name="quantidade" class="form-control" step="1" placeholder="Quantidade" required />
			</div>
		</div>
		<div class="btn-group">
			<button type="button" class="btn btn-secondary" onclick="adicionarItem()">+ Adicionar Item</button>
			<input class="btn" type="submit" name="enviar" value="Registrar Venda" />
		</div>
	</form>
</div>

<script>
function adicionarItem() {
	const div = document.createElement('div');
	div.className = 'form-group';
	div.style.marginTop = '16px';
	div.style.paddingTop = '16px';
	div.style.borderTop = '1px solid #EDE9E3';
	div.innerHTML =
		'<label>Produto ID</label>' +
		'<input type="text" name="produtoId" class="form-control" placeholder="ID do Produto" required />' +
		'<label style="margin-top: 14px;">Quantidade</label>' +
		'<input type="number" name="quantidade" class="form-control" step="1" placeholder="Quantidade" required />';
	document.getElementById('itens-venda').appendChild(div);
}
</script>

<c:if test="${not empty erro}">
	<div class="container">
		<h2 class="text-danger"><c:out value="${erro}" /></h2>
	</div>
</c:if>

<c:if test="${not empty saida}">
	<div class="container">
		<h2 class="text-success"><c:out value="${saida}" /></h2>
	</div>
</c:if>

<c:if test="${not empty vendas}">
	<div class="container">
		<h2 class="section-title">Lista de Vendas</h2>
		<div class="table-wrapper">
			<table>
				<thead>
					<tr>
						<th>ID</th>
						<th>Cliente</th>
						<th>Funcionário</th>
						<th>Data</th>
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
							<td>R$ ${venda.total}</td>
							<td>
								<c:forEach var="item" items="${venda.itens}">
									Produto #${item.produtoID} (${item.quantidade}x)<br />
								</c:forEach>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</c:if>

<footer>
    <p>&copy; 2026 <span>Joalheria Brilho Mais</span> &mdash; Controle de Estoque</p>
</footer>

</body>
</html>
