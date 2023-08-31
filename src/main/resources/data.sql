INSERT INTO restaurante (id, cep, complemento, nome) VALUES
(1L, '0000001', 'Complemento Endereço MC Donalds', 'MC Donalds'),
(2L, '0000002', 'Complemento Endereço Spoletto', 'Spoletto');

INSERT INTO cliente (id, cep, complemento, nome) VALUES
(1L, '0000001', 'Complemento Endereço Dayane', 'Dayane');

INSERT INTO produto (id, disponivel, nome, valor_unitario, restaurante_id) VALUES
(1L, true, 'Batata M', 6.0, 1L),
(2L, true, 'Big Mac', 18.0, 1L),
(3L, true, 'Macarrão', 20.0, 2L);

INSERT INTO sacola (id, forma_pagamento, fechada, valor_total, cliente_id) VALUES
(1L, 0 , false, 0.0, 1L);