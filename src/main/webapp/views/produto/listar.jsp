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
    <title>Produtos - Joalheria Brilho Mais</title>
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
    <h2>Produtos</h2>
    <div class="divider"></div>
</div>

<div class="container">
    <form action="${pageContext.request.contextPath}/produto" method="post">
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 0 20px;">
            <div class="form-group">
                <label for="id">ID</label>
                <input type="text" id="id" name="id" class="form-control" placeholder="ID do Produto" />
            </div>
            <div class="form-group">
                <label for="nome">Nome</label>
                <input type="text" id="nome" name="nome" class="form-control" placeholder="Nome do Produto" />
            </div>
            <div class="form-group">
                <label for="valor">Valor</label>
                <input type="number" id="valor" name="valor" class="form-control" step="0.01" placeholder="Valor" />
            </div>
            <div class="form-group">
                <label for="quantidade">Quantidade</label>
                <input type="number" id="quantidade" name="quantidade" class="form-control" step="1" placeholder="Quantidade" />
            </div>
        </div>
        <div class="btn-group">
            <input class="btn" type="submit" name="enviar" value="Cadastrar" />
            <input class="btn" type="submit" name="enviar" value="Alterar" />
            <input class="btn" type="submit" name="enviar" value="Buscar" />
            <input class="btn" type="submit" name="enviar" value="Listar" />
            <input class="btn" type="submit" name="enviar" value="Entrada/Saída" />
            <input class="btn" type="submit" name="enviar" value="Listar Vendidos" />
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

<c:if test="${not empty produtos}">
    <div class="container">
        <h2 class="section-title">Produtos</h2>
        <div class="table-wrapper">
            <table>
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
    </div>
</c:if>

<footer>
    <p>&copy; 2026 <span>Joalheria Brilho Mais</span> &mdash; Controle de Estoque</p>
</footer>

</body>
</html>
