# Sistema de Login Seguro

Este é um projeto de estudo para fazer cadastro e login de usuários. Ele usa Java 17, Spring Boot, Spring Security, Thymeleaf e MongoDB. As telas ficam separadas das regras do sistema, então dá para mudar o visual sem refazer o login.

## O que precisa instalar

- JDK 17 ou superior
- Maven 3.6 ou superior
- Uma instância MongoDB local ou uma conta MongoDB Atlas

## Configurar o MongoDB Atlas

1. Crie um cluster e um usuário de banco no MongoDB Atlas.
2. Em **Network Access**, libere o endereço IP do computador que vai executar o projeto.
3. Copie a URI de conexão. Troque os exemplos pelo usuário, senha e endereço do seu cluster.
4. Se a senha tiver caracteres especiais, codifique-os para usar na URI.
5. Guarde a URI em uma variável de ambiente. Não coloque sua senha no código nem no GitHub.

No PowerShell, defina as variáveis antes de iniciar o sistema:

```powershell
$env:MONGODB_URI = "mongodb+srv://USUARIO:SENHA@SEU-CLUSTER.mongodb.net/login_seguro?retryWrites=true&w=majority"
$env:APP_TEMA = "padrao"
$env:ADMIN_EMAIL = "admin@seudominio.com"
$env:ADMIN_PASSWORD = "use-uma-senha-forte-com-mais-de-8-caracteres"
```

`ADMIN_EMAIL` e `ADMIN_PASSWORD` são opcionais. Se você informar os dois, o sistema cria a conta de administrador na primeira inicialização. A senha é salva com hash, não em texto aberto.

Se tiver o MongoDB instalado no computador, pode deixar `MONGODB_URI` sem definir. O projeto usa `mongodb://localhost:27017/login_seguro` como padrão.

## Executar

Com o terminal aberto na pasta do projeto, rode:

```powershell
mvn spring-boot:run
```

Depois, abra `http://localhost:8080` no navegador. O formulário de cadastro cria uma conta de aluno. As contas e as sessões ficam salvas no MongoDB.

## Perfis

- `ADMIN`: acesso a `/admin`.
- `PROFESSOR`: acesso a `/professor`.
- `ALUNO`: acesso a `/aluno`.

Quem se cadastra recebe o perfil `ALUNO`; não dá para escolher um perfil com mais acesso no formulário. O administrador pode ver as contas, trocar os perfis e excluir outros usuários em `/admin`. Para evitar bloqueio, o sistema não deixa o administrador excluir a própria conta nem remover o último administrador.

## Temas

As páginas ficam em `src/main/resources/templates`, e os estilos ficam em `src/main/resources/static/css`. A variável `APP_TEMA` escolhe o arquivo de estilo. Por exemplo, `APP_TEMA=padrao` carrega `padrao.css`. Para testar outro visual, crie outro CSS e troque o valor da variável.

## Estrutura do código

- `config`: regras do Spring Security, tema e criação opcional do administrador.
- `controller`: rotas e páginas.
- `form`: campos e validações do cadastro.
- `model`: documento de usuário salvo no MongoDB.
- `repository`: consultas ao MongoDB.
- `service`: cadastro e busca do usuário para autenticação.
- `templates`: páginas HTML Thymeleaf.

## Segurança

O Spring Security cuida do login, dos perfis e da proteção CSRF dos formulários. O BCrypt protege as senhas antes de elas irem para o banco. Em um sistema publicado de verdade, use HTTPS e limite os IPs autorizados no Atlas. Não reutilize a senha de teste em produção.

## Documentação acadêmica

O texto da documentação pode ser alterado em `docs/documentacao.html`; o PDF pronto está em `docs/documentacao.pdf`. A capa já está preenchida. O documento usa A4, Arial 12 e espaçamento 1,5. Antes de entregar, confira se a sua instituição pede algum ajuste específico.

## Git e GitHub

O Git já está organizado com as branches `main` e `develop`. A `main` guarda a versão estável, e a `develop` é onde as mudanças são reunidas. Para começar uma tarefa, crie uma branch com nome como `feature/minha-alteracao` a partir de `develop`.

O projeto está publicado no repositório público [sistema-de-login-sefuro-completo](https://github.com/hddn7/sistema-de-login-sefuro-completo). Para baixar uma cópia, use:

```powershell
git clone https://github.com/hddn7/sistema-de-login-sefuro-completo.git
```

Antes de publicar, confirme que a URI do Atlas e senhas não estão em nenhum commit. Nunca faça commit de um arquivo `.env` com credenciais reais.
