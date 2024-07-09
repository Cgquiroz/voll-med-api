alter table pacientes add column activo tinyint;
update pacientes set activo = 1;