-- Script para inserir dados de teste

-- Inserir categorias
INSERT INTO categorias (nome, descricao, active, created_at, updated_at) VALUES
('Whey Protein', 'Proteínas em pó para ganho de massa muscular', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Creatina', 'Suplementos de creatina para força e potência', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Pré-Treino', 'Suplementos energéticos para antes do treino', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('BCAA', 'Aminoácidos essenciais para recuperação muscular', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Vitaminas', 'Suplementos vitamínicos e minerais', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserir suplementos
INSERT INTO suplementos (nome, descricao, preco, categoria_id, estoque, active, created_at, updated_at) VALUES
('Whey Protein Isolado 1kg', 'Proteína isolada de alta qualidade para ganho de massa muscular', 89.90, 1, 50, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Whey Protein Concentrado 900g', 'Proteína concentrada sabor chocolate', 69.90, 1, 30, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Creatina Monohidratada 300g', 'Creatina pura para aumento de força e potência', 45.90, 2, 25, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Pré-Treino Pump 300g', 'Energético com cafeína e beta-alanina', 79.90, 3, 20, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('BCAA 2:1:1 120 caps', 'Aminoácidos essenciais em cápsulas', 55.90, 4, 40, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Multivitamínico 60 caps', 'Complexo vitamínico completo', 35.90, 5, 60, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
