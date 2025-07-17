-- Script para corrigir a restrição CHECK da tabela usuarios
-- Execute este script no PostgreSQL para resolver o problema de roles

-- 1. Primeiro, vamos verificar a restrição atual
SELECT conname, consrc
FROM pg_constraint
WHERE conrelid = 'usuarios'::regclass
AND contype = 'c';

-- 2. Remover a restrição CHECK existente (se existir)
ALTER TABLE usuarios DROP CONSTRAINT IF EXISTS usuarios_role_check;

-- 3. Criar uma nova restrição CHECK que aceita ambos os valores
ALTER TABLE usuarios ADD CONSTRAINT usuarios_role_check 
CHECK (role IN ('ROLE_ADMIN', 'ROLE_USER'));

-- 4. Verificar se a restrição foi criada corretamente
SELECT conname, consrc
FROM pg_constraint
WHERE conrelid = 'usuarios'::regclass
AND contype = 'c';

-- 5. Verificar a estrutura da tabela
\d+ usuarios;
