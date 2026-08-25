-- V8 sembró travel_advisor con IDs explícitos (1,2,3) sin avanzar la secuencia
-- asociada a la columna serial, así que cualquier INSERT nuevo (p.ej. crear una
-- asesora desde el admin) intenta reutilizar el id=1 y falla con
-- "duplicate key value violates unique constraint travel_advisor_pkey".
SELECT setval('travel_advisor_id_seq', (SELECT COALESCE(MAX(id), 1) FROM travel_advisor));
