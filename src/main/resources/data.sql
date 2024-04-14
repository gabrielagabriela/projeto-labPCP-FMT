INSERT INTO papel(papel_id, nome)
VALUES (1, 'ADM'),
       (2, 'PEDAGOGICO'),
       (3, 'RECRUITER'),
       (4, 'PROFESSOR'),
       (5, 'ALUNO');

INSERT INTO usuario(usuario_id, login, senha, papel_id)
VALUES(1, 'admin', 'admin', 1);
