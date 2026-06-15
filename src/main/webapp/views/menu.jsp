<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<nav id="menu">
    <div class="menu-inner">
        <a href="${pageContext.request.contextPath}/views/dashboard.jsp" class="menu-brand">
            <span class="menu-brand-name">Brilho Mais</span>
        </a>
        <ul>
            <li><a href="${pageContext.request.contextPath}/views/dashboard.jsp" data-page="dashboard">Dashboard</a></li>
            <li><a href="${pageContext.request.contextPath}/views/produto/listar.jsp" data-page="produto">Produtos</a></li>
            <li><a href="${pageContext.request.contextPath}/views/cliente/listar.jsp" data-page="cliente">Clientes</a></li>
            <li><a href="${pageContext.request.contextPath}/views/funcionario/listar.jsp" data-page="funcionario">Funcionários</a></li>
            <li><a href="${pageContext.request.contextPath}/views/venda/listar.jsp" data-page="venda">Vendas</a></li>
            <li><a href="${pageContext.request.contextPath}/views/relatorio/listar.jsp" data-page="relatorio">Relatórios</a></li>
        </ul>
        <div class="menu-user">
            <span class="menu-user-name">${sessionScope.funcionario.nome}</span>
            <a href="${pageContext.request.contextPath}/login" class="menu-logout" onclick="event.preventDefault(); document.getElementById('logoutForm').submit();">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
                    <polyline points="16 17 21 12 16 7"/>
                    <line x1="21" y1="12" x2="9" y2="12"/>
                </svg>
                Sair
            </a>
        </div>
    </div>
</nav>

<form id="logoutForm" action="${pageContext.request.contextPath}/login" method="post" style="display:none;">
    <input type="hidden" name="action" value="logout">
</form>

<script>
(function() {
    var path = window.location.pathname;
    var links = document.querySelectorAll('#menu ul li a');
    for (var i = 0; i < links.length; i++) {
        var href = links[i].getAttribute('href');
        if (path.endsWith(href)) {
            links[i].classList.add('active');
            break;
        }
    }
})();
</script>
