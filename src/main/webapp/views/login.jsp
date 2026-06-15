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
<title>Login - Joalheria Brilho Mais</title>
</head>
<body class="login-page">
	<div class="login-container" id="loginContainer">
		<h1>Brilho Mais</h1>
		<p class="subtitle">Controle de Estoque</p>

		<c:if test="${h2Fallback}">
			<div class="alert-warning" style="margin-bottom: 20px;">
				<strong>&#9888;</strong> Banco H2 em mem&oacute;ria ativo
				(backup por indisponibilidade do SGBD).<br>
				Os dados n&atilde;o ser&atilde;o persistidos ap&oacute;s reinicializa&ccedil;&atilde;o.
			</div>
		</c:if>

		<c:if test="${not empty saida}">
			<div class="success-message">${saida}</div>
		</c:if>

		<c:if test="${not empty erro}">
			<div class="error-message">${erro}</div>
		</c:if>

		<form action="${pageContext.request.contextPath}/login" method="post" id="loginForm" class="${showRegister ? 'hidden' : ''}">
			<input type="hidden" name="action" value="login">
			<div class="form-group">
				<label for="username">Usuário</label>
				<input type="text" id="username" name="username" placeholder="Seu usuário" required>
			</div>
			<div class="form-group">
				<label for="password">Senha</label>
				<input type="password" id="password" name="password" placeholder="Sua senha" required>
			</div>
			<input type="submit" value="Entrar">
			<p class="toggle-link">
				<a href="#" onclick="toggleForm(); return false;">Criar conta</a>
			</p>
		</form>

		<form action="${pageContext.request.contextPath}/login" method="post" id="registerForm" class="${showRegister ? '' : 'hidden'}">
			<input type="hidden" name="action" value="register">
			<div class="form-group">
				<label for="regNome">Nome</label>
				<input type="text" id="regNome" name="nome" placeholder="Seu nome completo" required>
			</div>
			<div class="form-group">
				<label for="regUsername">Usuário</label>
				<input type="text" id="regUsername" name="username" placeholder="Nome de usuário" required>
			</div>
			<div class="form-group">
				<label for="regPassword">Senha</label>
				<input type="password" id="regPassword" name="password" placeholder="Mínimo 8 caracteres" required>
			</div>
			<div class="form-group">
				<label for="regConfirm">Confirmar senha</label>
				<input type="password" id="regConfirm" name="confirmPassword" placeholder="Repita a senha" required>
			</div>
			<input type="submit" value="Cadastrar">
			<p class="toggle-link">
				<a href="#" onclick="toggleForm(); return false;">Voltar ao login</a>
			</p>
		</form>
	</div>

	<script>
		function toggleForm() {
			var loginForm = document.getElementById('loginForm');
			var registerForm = document.getElementById('registerForm');
			loginForm.classList.toggle('hidden');
			registerForm.classList.toggle('hidden');
		}

		var showRegister = ${showRegister != null && showRegister ? 'true' : 'false'};
		if (showRegister) {
			document.getElementById('loginForm').classList.add('hidden');
			document.getElementById('registerForm').classList.remove('hidden');
		}
	</script>
</body>
</html>
