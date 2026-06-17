param(
    [string]$DeployDir = ""
)

$ProjectRoot = Split-Path -Parent $PSCommandPath
$Src = "$ProjectRoot\src\main\java"
$WebApp = "$ProjectRoot\src\main\webapp"
$TomcatLib = "C:\Users\IWMVI\Downloads\apache-tomcat-11.0.4\lib"
$LibDir = "$WebApp\WEB-INF\lib"

if (-not $DeployDir) {
    $DeployDir = "C:\Users\IWMVI\eclipse-workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\BD-ControleEstoque"
}

$BuildDir = Join-Path $env:TEMP "opencode-build"
if (Test-Path $BuildDir) { Remove-Item -Path "$BuildDir" -Recurse -Force }
New-Item -ItemType Directory -Path "$BuildDir" -Force | Out-Null

Write-Host "=== Compilando com javac + Lombok ===" -ForegroundColor Cyan

$Classpath = "$TomcatLib\servlet-api.jar;$LibDir\h2-2.2.224.jar;$LibDir\lombok-1.18.46.jar;$LibDir\jakarta.servlet.jsp.jstl-3.0.0.jar;$LibDir\jakarta.servlet.jsp.jstl-api-3.0.0.jar;$LibDir\jakarta.annotation-api-2.1.1.jar;$LibDir\jakarta.el-api-4.0.0.jar;$LibDir\jakarta.xml.bind-api-4.0.0.jar;$LibDir\jtds-1.3.1.jar"

$JavaFiles = Get-ChildItem -Recurse -Filter "*.java" -Path $Src | ForEach-Object { "`"$($_.FullName)`"" }

$Result = javac --release 21 --processor-path "`"$LibDir\lombok-1.18.46.jar`"" -cp "`"$Classpath`"" -d "`"$BuildDir`"" $JavaFiles 2>&1

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERRO de compilacao:" -ForegroundColor Red
    $Result | Where-Object { $_ -match "error:" } | ForEach-Object { Write-Host "  $_" -ForegroundColor Red }
    exit 1
}

Write-Host "Compilacao OK!" -ForegroundColor Green

Write-Host "=== Copiando WEB-INF/classes ===" -ForegroundColor Cyan
if (-not (Test-Path "$DeployDir\WEB-INF\classes")) { New-Item -ItemType Directory -Path "$DeployDir\WEB-INF\classes" -Force | Out-Null }
Copy-Item -Path "$BuildDir\*" -Destination "$DeployDir\WEB-INF\classes" -Recurse -Force

Write-Host "=== Copiando JARs ===" -ForegroundColor Cyan
if (-not (Test-Path "$DeployDir\WEB-INF\lib")) { New-Item -ItemType Directory -Path "$DeployDir\WEB-INF\lib" -Force | Out-Null }
Copy-Item -Path "$LibDir\*" -Destination "$DeployDir\WEB-INF\lib" -Force

Write-Host "=== Copiando JSPs, CSS, web.xml ===" -ForegroundColor Cyan
Copy-Item -Path "$WebApp\views\*" -Destination "$DeployDir\views" -Recurse -Force
Copy-Item -Path "$WebApp\css\*" -Destination "$DeployDir\css" -Recurse -Force
Copy-Item -Path "$WebApp\WEB-INF\web.xml" -Destination "$DeployDir\WEB-INF\web.xml" -Force
Copy-Item -Path "$WebApp\index.jsp" -Destination "$DeployDir\index.jsp" -Force
Copy-Item -Path "$WebApp\META-INF\*" -Destination "$DeployDir\META-INF" -Force

Remove-Item -Path "$BuildDir" -Recurse -Force

Write-Host "=== Deploy concluido! ===" -ForegroundColor Green
Write-Host "Se o servidor estava rodando, a aplicacao pode levar alguns segundos para recarregar." -ForegroundColor Yellow
