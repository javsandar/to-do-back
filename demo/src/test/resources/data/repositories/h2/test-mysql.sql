DROP TABLE IF EXISTS todo;
CREATE TABLE todo (
    ID UUID,
    CREATION_DATE DATE,
    EXPIRE_DATE DATE,
    IS_FINISHED BOOLEAN,
    TEXT VARCHAR(20)
);

INSERT INTO todo VALUES ('af690230-84f3-45ea-a960-fc3d4bc99f65','2023-06-05','2023-06-01', false, 'Tarea de prueba 1');
INSERT INTO todo VALUES ('8daccd58-6131-45d4-8a6d-9e14931598bc','2023-06-08','2023-06-02', false, 'Tarea de prueba 2');
INSERT INTO todo VALUES ('b967e9b5-29eb-4b82-84a0-14c06c700e70','2023-06-08','2023-06-03', false, 'Tarea de prueba 3');
INSERT INTO todo VALUES ('b5806ced-08b5-4019-9785-458a0aecd820','2023-06-08','2023-06-04', false, 'Tarea de prueba 4');
INSERT INTO todo VALUES ('6c9f6cb9-1615-4be5-ae97-6b536e23942f','2023-06-08','2023-06-05', false, 'Tarea de prueba 5');
INSERT INTO todo VALUES ('bca8a701-9dfe-4630-8523-6865678eeefb','2023-06-08','2023-06-06', true, 'Tarea de prueba 6');
INSERT INTO todo VALUES ('86e4f1f4-3741-4b52-bf9c-efdd2ca1eaf1','2023-06-08','2023-06-07', true, 'Tarea de prueba 7');
INSERT INTO todo VALUES ('221ec078-bf3f-45cd-8b5a-6fc594978a15','2023-06-08','2023-06-08', true, 'Tarea de prueba 8');
INSERT INTO todo VALUES ('aca63b33-3946-4161-8286-547f197a3915','2023-06-08','2023-06-09', true, 'Tarea de prueba 9');
INSERT INTO todo VALUES ('31a0200d-d1e7-435d-aee7-c04fae5806a0','2023-06-08','2023-06-10', true, 'Tarea de prueba 10');
--
