<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="./css/styles.css">
    <title>Clientes</title>
</head>
<body class="d-flex flex-column min-vh-100">

    <jsp:include page="menu.jsp"></jsp:include>

    <div class="container text-center my-4">
        <form action="cliente" method="post">
            <h2>Clientes</h2>
            <div class="form-group">
                <label for="id" class="text-left">ID:</label>
                <input type="text" id="id" name="id" class="form-control" placeholder="ID do Cliente" />
            </div>
            <div class="form-group">
                <label for="nome" class="text-left">Nome:</label>
                <input type="text" id="nome" name="nome" class="form-control" placeholder="Nome do Cliente" />
            </div>
            <div class="btn-group mt-3">
                <input class="btn btn-success" type="submit" name="botao" value="Cadastrar" />
                <input class="btn btn-primary" type="submit" name="botao" value="Listar" />
                <input class="btn btn-info" type="submit" name="botao" value="Atualizar" />
                <input class="btn btn-warning" type="submit" name="botao" value="Contar" />
            </div>
        </form>
    </div>

    <c:if test="${not empty saida}">
        <div class="container text-center">
            <h2 class="text-danger"><c:out value="${saida}" /></h2>
        </div>
    </c:if>

    <c:if test="${not empty clientes}">
        <div class="container text-center">
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${clientes}">
                        <tr>
                            <td>${p.id}</td>
                            <td>${p.nome}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>

    <c:if test="${not empty quantidade}">
        <div class="container text-center">
            <h2 class="text-success">Quantidade de Clientes: <c:out value="${quantidade}" /></h2>
        </div>
    </c:if>

    <c:if test="${not empty erro}">
        <div class="container text-center">
            <h2 class="text-danger"><c:out value="${erro}" /></h2>
        </div>
    </c:if>

</body>
</html>
