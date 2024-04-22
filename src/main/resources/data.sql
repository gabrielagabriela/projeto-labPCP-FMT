INSERT INTO papel(papel_id, nome)
SELECT 1, 'ADM'
    WHERE NOT EXISTS (SELECT 1 FROM papel WHERE papel_id = 1);

INSERT INTO papel(papel_id, nome)
SELECT 2, 'PEDAGOGICO'
    WHERE NOT EXISTS (SELECT 1 FROM papel WHERE papel_id = 2);

INSERT INTO papel(papel_id, nome)
SELECT 3, 'RECRUITER'
    WHERE NOT EXISTS (SELECT 1 FROM papel WHERE papel_id = 3);

INSERT INTO papel(papel_id, nome)
SELECT 4, 'PROFESSOR'
    WHERE NOT EXISTS (SELECT 1 FROM papel WHERE papel_id = 4);

INSERT INTO papel(papel_id, nome)
SELECT 5, 'ALUNO'
    WHERE NOT EXISTS (SELECT 1 FROM papel WHERE papel_id = 5);

