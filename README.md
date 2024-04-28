# Projeto LabPCP

Projeto final do primeiro módulo do curso Floripa Mais Tec - Turma Fullstack - Education

Api Rest para gestão de cursos, turmas, alunos e docentes utilizando o framework Spring.

## Documentação da API

### Configuração do Banco de Dados

O banco de dados utilizado é o PostgreSQL. É possível usar via docker ou criar uma conexão direto na máquina


```
docker run -d --name meu-postgres -e POSTGRES_PASSWORD=minhaSenha  -e POSTGRES_USER=meuUsuario  -e POSTGRES_DB=labpcp  -p 1432:5432  postgres:latest
```
#### server.port = 8081

### Inicialização da Aplicação

Ao iniciar, a aplicação criará automaticamente a tabela 'papel' com os valores: 

- ADM / id_papel 1

- PEDAGOGICO / id_papel 2

- RECRUITER / id_papel 3

 - PROFESSOR / id_papel 4

- ALUNO / id_papel 5

Também será criado um usuário do tipo ADM que poderá fazer os cadastros dos demais usuários

#### Utilizar o seguinte cadastro ao se logar 

-  login: admin / senha: admin

Apenas o login não necessita de token

Ao se logar o usuário receberá um token que deverá ser utilizado nas demais requisições.

### Ordem de criação de entidades

- Utilize o token gerado pelas credenciais do usuário administrador ao se logar (login: admin / senha: admin) para os demais acessos
- Cadastro de Usuário - faça a criação de mais usuários. Criando usuários do tipo adm, pedagogico, recruiter, professor e aluno
- Docente - cadastre os usuários que já estão cadastrados no sistema com papel de professor, pedagogico, recruiter e professor nos endpoints de docente
- Curso - crie cursos nos endponts de curso
- Matéria - crie matéria nos endpoints de matéria
- Turma - crie turma nos endpoints de turma
- Aluno - crie alunos nos endpoints de aluno (usuários que já estão cadastrados no sistema com papel do tipo aluno)
- Notas - lançe notas a um aluno utilizando os endpoints de nota

### Logar no sistema
URL:

```http
POST  http://localhost:8081/login
```

Exemplo de envio como JSON: 

```
{
	"login": "admin",
	"senha": "admin"
}

```

### Cadastro de novo usuário

```http
POST  http://localhost:8081/cadastros
```

Exemplo de envio como JSON: 

```
{
	"login": "anapaula",
	"senha": "123",
	"papel": "Professor"
}


```
### Criar docente

```http
POST  http://localhost:8081/docentes
```

Exemplo de envio como JSON: 

```
{
	"nome": "Ana Paula",
	"data_entrada": "10/10/2020",
	"login": "anapaula"
}


```
### Buscar docente por ID
```http
GET  http://localhost:8081/docentes/1
```
### Listar docentes cadastrados
```http
GET  http://localhost:8081/docentes
```


### Atualizar docente
```http
PUT  http://localhost:8081/docentes/1
```

Exemplo de envio como JSON: 

```
{
	"nome": "Ana Paula Maria",
	"data_entrada": "10/10/2020",
	"login": "anapaula"
}


```
### Excluir docente por ID
```http
DELETE  http://localhost:8081/docentes/1
```

### Criar curso
```http
POST  http://localhost:8081/cursos
```

Exemplo de envio como JSON: 

```
{
	"nome": "Java"
}

```
### Buscar curso por ID
```http
GET  http://localhost:8081/cursos/1
```
### Listar cursos cadastrados no sistema
```http
GET  http://localhost:8081/cursos
```
### Atualizar curso por ID

```http
PUT  http://localhost:8081/cursos/1
```

Exemplo de envio como JSON: 

```
{
	"nome": "Javascript"
}

```

### Deletar curso por ID
```http
DELETE  http://localhost:8081/cursos/1
```
### Listar matérias de um curso por ID
```http
GET  http://localhost:8081/cursos/1/materias
```


### Criar turma
```http
POST  http://localhost:8081/turmas
```

Exemplo de envio como JSON: 

```
{
	"nome": "Turma 3",
	"nomeProfessor": "Ana Paula Maria",
	"nomeCurso": "javascript"
}

```
### Buscar turma por ID
```http
GET  http://localhost:8081/turmas/1
```
### Listar turmas cadastradas no sistema
```http
GET  http://localhost:8081/turmas
```
### Atualizar turma por ID
```http
PUT  http://localhost:8081/turmas/1
```

Exemplo de envio como JSON: 

```
{
	"nome": "Turma 33",
	"nomeProfessor": "Ana Paula Maria",
	"nomeCurso": "javascript"
}

```
### Deletar turma por ID
```http
DELETE  http://localhost:8081/turmas/1
```




### Criar materia

```http
POST  http://localhost:8081/materias
```

Exemplo de envio como JSON: 

```
{
	"nome": "Materia 1",
	"nomeCurso": "javascript"

}
```
### Buscar materia por ID
```http
GET  http://localhost:8081/materias/1
```
### Listar materias cadastradas no sistema
```http
GET  http://localhost:8081/materias
```
### Atualizar matéria por ID
```http
PUT  http://localhost:8081/materias/1
```

Exemplo de envio como JSON: 

```
{
	"nome": "Materia 12",
	"nomeCurso": "javascript"

}
```
### Deletar materia por ID

```http
DELETE  http://localhost:8081/materias/1
```




### Criar aluno
```http
POST  http://localhost:8081/alunos
```

Exemplo de envio como JSON: 

```
{
	"nome": "João",
	"data_nascimento": "10/10/1990",
	"login": "joao",
	"nomeTurma": "Turma 33"
}
```

### Buscar aluno por ID
```http
GET  http://localhost:8081/alunos/1
```

### Listar alunos cadastrados no sistema
```http
GET  http://localhost:8081/alunos
```

### Atualizar aluno por ID
```http
PUT  http://localhost:8081/alunos/1
```

Exemplo de envio como JSON: 

```
{
	"nome": "João Roberto",
	"data_nascimento": "10/10/1990",
	"login": "joao",
	"nomeTurma": "Turma 33"
}
```

### Deletar aluno por ID
```http
DELETE  http://localhost:8081/alunos/1
```

### Lista de notas de um aluno por ID
```http
GET  http://localhost:8081/alunos/1/notas
```

### Pontuação total de um aluno por ID
```http
GET  http://localhost:8081/alunos/1/pontuacao
```

### Criar nota

```http
POST  http://localhost:8081/notas
```

Exemplo de envio como JSON: 

```
{
	"nomeAluno": "João Roberto",
	"nomeProfessor": "Ana Paula Maria",
	"nomeMateria": "Materia 12",
	"valor": 8.8,
	"data": "20/04/2024"
}
```

### Buscar nota por ID
```http
GET  http://localhost:8081/notas/1
```

### Listar notas cadastradas no sistema
```http
GET  http://localhost:8081/notas
```

### Atualizar nota por ID

```http
PUT  http://localhost:8081/notas/1
```

Exemplo de envio como JSON: 

```
{
	"nomeAluno": "João Roberto",
	"nomeProfessor": "Ana Paula Maria",
	"nomeMateria": "Materia 12",
	"valor": 9.8,
	"data": "20/04/2024"
}
```

### Deletar nota por ID
```http
DELETE  http://localhost:8081/notas/1
```
