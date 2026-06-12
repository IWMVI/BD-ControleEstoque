<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=DM+Serif+Display:ital@0;1&display=swap" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
<title>Clientes - Joalheria Brilho Mais</title>
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
    <h2>Clientes</h2>
    <div class="divider"></div>
</div>

<div class="container">
    <form action="${pageContext.request.contextPath}/cliente" method="post">
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 0 20px;">
            <div class="form-group">
                <label for="id">ID</label>
                <input type="text" id="id" name="id" class="form-control" placeholder="ID do Cliente" />
            </div>
            <div class="form-group">
                <label for="nome">Nome</label>
                <input type="text" id="nome" name="nome" class="form-control" placeholder="Nome do Cliente" />
            </div>
        </div>
        <div class="btn-group">
            <input class="btn" type="submit" name="botao" value="Cadastrar" />
            <input class="btn" type="submit" name="botao" value="Listar" />
            <input class="btn" type="submit" name="botao" value="Atualizar" />
            <input class="btn" type="submit" name="botao" value="Contar" />
        </div>
    </form>
</div>

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

<c:if test="${not empty clientes}">
    <div class="container">
        <h2 class="section-title">Clientes</h2>
        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>Total de Compras</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="cliente" items="${clientes}">
                        <tr>
                            <td>${cliente.id}</td>
                            <td>${cliente.nome}</td>
                            <td>${cliente.totalCompras}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</c:if>

<c:if test="${qtdClientes > 0}">
    <div class="container" style="text-align: center;">
        <span class="stat-badge">Quantidade de clientes cadastrados: ${qtdClientes}</span>
    </div>
</c:if>

<footer>
    <p>&copy; 2026 <span>Joalheria Brilho Mais</span> &mdash; Controle de Estoque</p>
</footer>

</body>
</html>
