-- ==============================================================================
-- V15__fix_shared_admin_password_hashes.sql
-- Viajes Carolina — Corrige el hash de contraseña compartido entre los 3 usuarios
-- administrativos sembrados en V14 (hallazgo de auditoría DB-001 / SEC-004).
-- Cada usuario recibe un hash Argon2id único; las contraseñas en texto plano se
-- generaron una sola vez de forma aleatoria y se entregaron fuera de este
-- repositorio. Deben rotarse en el primer login.
-- ==============================================================================

UPDATE admin_user SET password_hash = '$argon2id$v=19$m=65536,t=3,p=4$mXKJ4vxZotLESwhVKiKS+A$6bS2ZEIbJWuHRlBoCjW0Y0ow6VD6rlY6Cf+314nPlo4'
WHERE username = 'admin';

UPDATE admin_user SET password_hash = '$argon2id$v=19$m=65536,t=3,p=4$An430GDL3eJ3rL6tE2tW8A$93pk8uE/AuID/+foSXiWfT5CxXCJx1SUsOPCWGGEE5M'
WHERE username = 'editor';

UPDATE admin_user SET password_hash = '$argon2id$v=19$m=65536,t=3,p=4$7MLzg5pOtVyTGHODblqnWA$oTk3ffspYzubto6HmKTHGSGZ4MwHTrSEkUYlDcZ8nqs'
WHERE username = 'carolina';
