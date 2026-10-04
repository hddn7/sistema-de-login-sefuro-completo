# Sistema de Login Seguro

Projeto de estudo com Java 17, Spring Boot, Spring Security, Thymeleaf e MongoDB. A estrutura separa telas, regras de negócio e acesso a dados para facilitar adaptações futuras.

## Requisitos

- JDK 17 ou superior
- Maven 3.6 ou superior
- Uma instância MongoDB local ou uma conta MongoDB Atlas

## Configurar o MongoDB Atlas

1. Crie um cluster no MongoDB Atlas e um usuário de banco de dados.
2. Em **Network Access**, permita o endereço IP que será usado para a conexão.
3. Copie a URI de conexão do Atlas.
4. Substitua usuário, senha e nome do cluster na URI. Se a senha tiver caracteres especiais, faça a codificação própria para URI.
5. Defina `MONGODB_URI` no terminal. Não salve a URI real no Git.

No PowerShell, defina as variáveis antes de iniciar:

```powershell
$env:MONGODB_URI = "mongodb+srv://USUARIO:SENHA@SEU-CLUSTER.mongodb.net/login_seguro?retryWrites=true&w=majority"
$env:APP_TEMA = "padrao"
$env:ADMIN_EMAIL = "admin@seudominio.com"
$env:ADMIN_PASSWORD = "use-uma-senha-forte-com-mais-de-8-caracteres"
```

`ADMIN_EMAIL` e `ADMIN_PASSWORD` são opcionais. Se forem informados, o sistema cria o primeiro administrador se ainda não existir. A senha nunca é gravada sem hash.

Para executar com MongoDB local, não é necessário definir `MONGODB_URI`; a aplicação usa `mongodb://localhost:27017/login_seguro`.

## Executar

Na pasta do projeto:

```powershell
mvn spring-boot:run
```

Acesse `http://localhost:8080`. O cadastro público cria usuários com o perfil `ALUNO`. As sessões HTTP e os usuários são armazenados no MongoDB.

## Perfis

- `ADMIN`: acesso a `/admin`.
- `PROFESSOR`: acesso a `/professor`.
- `ALUNO`: acesso a `/aluno`.

O cadastro não permite escolher o próprio perfil. O administrador pode listar usuários, alterar seus perfis e excluir outras contas pela página `/admin`. O sistema impede que o administrador altere ou exclua a própria conta e que a última conta ADMIN seja removida.

## Temas

As páginas Thymeleaf ficam em `src/main/resources/templates` e o CSS em `src/main/resources/static/css`. O tema é escolhido por `APP_TEMA`; por exemplo, `APP_TEMA=padrao` carrega `padrao.css`. Uma nova aparência pode ser adicionada criando outro arquivo CSS, sem alterar o controle de acesso.

## Estrutura do código

- `config`: regras do Spring Security, tema e criação opcional do administrador.
- `controller`: rotas e páginas.
- `form`: campos e validações do cadastro.
- `model`: documento de usuário salvo no MongoDB.
- `repository`: consultas ao MongoDB.
- `service`: cadastro e busca do usuário para autenticação.
- `templates`: páginas HTML Thymeleaf.

## Segurança

O Spring Security protege as rotas e mantém a proteção CSRF ativada. As senhas são codificadas com BCrypt. A aplicação usa cookie de sessão `HttpOnly`; em produção, publique sempre atrás de HTTPS e restrinja os IPs permitidos no Atlas. Não use credenciais de desenvolvimento em produção.

## Documentação acadêmica

O material editável para a documentação está em `docs/documentacao.html`. Ele usa uma folha de impressão com margens e tipografia acadêmicas como base ABNT. Complete os campos de autoria, instituição, curso, cidade e ano antes da entrega e exporte a página para PDF pelo navegador.

## Git e GitHub

O `.gitignore` exclui arquivos de ambiente e a pasta de compilação. Um fluxo Gitflow básico começa com `main` para versões estáveis e `develop` para integração. Crie branches `feature/nome-da-tarefa` a partir de `develop`, integre-as de volta em `develop` e publique versões estáveis em `main`.

Para publicar em um repositório pessoal separado do PFC, crie um repositório público vazio no GitHub e execute os comandos abaixo, substituindo `SEU-USUARIO` pelo nome da sua conta:

```powershell
git init --initial-branch=main
git add .
git commit -m "chore: inicia sistema de login seguro"
git switch -c develop
git remote add origin https://github.com/SEU-USUARIO/login-seguro.git
git push -u origin main
git push -u origin develop
```

Antes de publicar, confirme que a URI do Atlas e senhas não estão em nenhum commit. Nunca faça commit de um arquivo `.env` com credenciais reais.
