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
<title>Dashboard - Joalheria Brilho Mais</title>
</head>
<body>

<%@ include file="menu.jsp"%>

<c:if test="${h2Fallback}">
    <div class="container" style="margin-top: 16px; margin-bottom: 0; padding: 12px 18px;">
        <div class="alert-warning">
            <strong>&#9888; Modo tempor&aacute;rio:</strong> Banco H2 em mem&oacute;ria ativo.
            Os dados n&atilde;o ser&atilde;o persistidos entre reinicializa&ccedil;&otilde;es.
        </div>
    </div>
</c:if>

<div class="dashboard-hero">
    <div class="dashboard-hero-content">
        <h1>Joalheria Brilho Mais</h1>
        <p class="dashboard-hero-subtitle">Sistema de Controle de Estoque</p>
        <p class="dashboard-hero-welcome">
            Bem-vindo, <strong>${sessionScope.funcionario.nome}</strong>!
        </p>
    </div>
</div>

<div class="dashboard-grid">
    <a href="${pageContext.request.contextPath}/views/produto/listar.jsp" class="dashboard-card">
        <div class="dashboard-card-icon" style="background: rgba(184, 148, 75, 0.12);">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="#B8944B" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                <path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"/>
                <line x1="3" y1="6" x2="21" y2="6"/>
                <path d="M16 10a4 4 0 0 1-8 0"/>
            </svg>
        </div>
        <div class="dashboard-card-content">
            <h3>Produtos</h3>
            <p>Gerenciar cat&aacute;logo de produtos, pre&ccedil;os e estoque</p>
        </div>
        <span class="dashboard-card-action">Acessar &rarr;</span>
    </a>

    <a href="${pageContext.request.contextPath}/views/cliente/listar.jsp" class="dashboard-card">
        <div class="dashboard-card-icon" style="background: rgba(99, 102, 241, 0.12);">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="#6366F1" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
                <circle cx="9" cy="7" r="4"/>
                <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
                <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
            </svg>
        </div>
        <div class="dashboard-card-content">
            <h3>Clientes</h3>
            <p>Cadastro e acompanhamento de clientes</p>
        </div>
        <span class="dashboard-card-action">Acessar &rarr;</span>
    </a>

    <a href="${pageContext.request.contextPath}/views/funcionario/listar.jsp" class="dashboard-card">
        <div class="dashboard-card-icon" style="background: rgba(16, 185, 129, 0.12);">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="#10B981" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                <circle cx="12" cy="7" r="4"/>
            </svg>
        </div>
        <div class="dashboard-card-content">
            <h3>Funcion&aacute;rios</h3>
            <p>Gest&atilde;o de colaboradores e registro de vendas</p>
        </div>
        <span class="dashboard-card-action">Acessar &rarr;</span>
    </a>

    <a href="${pageContext.request.contextPath}/views/venda/listar.jsp" class="dashboard-card">
        <div class="dashboard-card-icon" style="background: rgba(245, 158, 11, 0.12);">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="#F59E0B" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                <rect x="1" y="4" width="22" height="16" rx="2" ry="2"/>
                <line x1="1" y1="10" x2="23" y2="10"/>
            </svg>
        </div>
        <div class="dashboard-card-content">
            <h3>Vendas</h3>
            <p>Registrar vendas e visualizar hist&oacute;rico</p>
        </div>
        <span class="dashboard-card-action">Acessar &rarr;</span>
    </a>

    <a href="${pageContext.request.contextPath}/views/relatorio/listar.jsp" class="dashboard-card">
        <div class="dashboard-card-icon" style="background: rgba(139, 92, 246, 0.12);">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="#8B5CF6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                <line x1="18" y1="20" x2="18" y2="10"/>
                <line x1="12" y1="20" x2="12" y2="4"/>
                <line x1="6" y1="20" x2="6" y2="14"/>
            </svg>
        </div>
        <div class="dashboard-card-content">
            <h3>Relat&oacute;rios</h3>
            <p>Relat&oacute;rios e totais do sistema</p>
        </div>
        <span class="dashboard-card-action">Acessar &rarr;</span>
    </a>
</div>

<footer>
    <p>&copy; 2026 <span>Joalheria Brilho Mais</span> &mdash; Controle de Estoque</p>
</footer>

</body>
</html>
