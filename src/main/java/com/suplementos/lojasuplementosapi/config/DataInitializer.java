package com.suplementos.lojasuplementosapi.config;

import com.suplementos.lojasuplementosapi.domain.Categoria;
import com.suplementos.lojasuplementosapi.domain.Suplemento;
import com.suplementos.lojasuplementosapi.repository.CategoriaRepository;
import com.suplementos.lojasuplementosapi.repository.SuplementoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private SuplementoRepository suplementoRepository;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            // Só inserir se não existir dados
            if (categoriaRepository.count() == 0) {
                
                // Criar categorias
                Categoria wheyProtein = new Categoria();
                wheyProtein.setNome("Whey Protein");
                wheyProtein.setDescricao("Proteínas em pó para ganho de massa muscular");
                wheyProtein = categoriaRepository.save(wheyProtein);

                Categoria creatina = new Categoria();
                creatina.setNome("Creatina");
                creatina.setDescricao("Suplementos de creatina para força e potência");
                creatina = categoriaRepository.save(creatina);

                Categoria preTreino = new Categoria();
                preTreino.setNome("Pré-Treino");
                preTreino.setDescricao("Suplementos energéticos para antes do treino");
                preTreino = categoriaRepository.save(preTreino);

                Categoria bcaa = new Categoria();
                bcaa.setNome("BCAA");
                bcaa.setDescricao("Aminoácidos essenciais para recuperação muscular");
                bcaa = categoriaRepository.save(bcaa);

                Categoria vitaminas = new Categoria();
                vitaminas.setNome("Vitaminas");
                vitaminas.setDescricao("Suplementos vitamínicos e minerais");
                vitaminas = categoriaRepository.save(vitaminas);

                // Criar suplementos
                Suplemento wheyIsolado = new Suplemento();
                wheyIsolado.setNome("Whey Protein Isolado 1kg");
                wheyIsolado.setMarca("Optimum Nutrition");
                wheyIsolado.setDescricao("Proteína isolada de alta qualidade para ganho de massa muscular");
                wheyIsolado.setPreco(new BigDecimal("89.90"));
                wheyIsolado.getCategorias().add(wheyProtein);
                wheyIsolado.setQuantidadeEstoque(50);
                suplementoRepository.save(wheyIsolado);

                Suplemento wheyConcentrado = new Suplemento();
                wheyConcentrado.setNome("Whey Protein Concentrado 900g");
                wheyConcentrado.setMarca("Dymatize");
                wheyConcentrado.setDescricao("Proteína concentrada sabor chocolate");
                wheyConcentrado.setPreco(new BigDecimal("69.90"));
                wheyConcentrado.getCategorias().add(wheyProtein);
                wheyConcentrado.setQuantidadeEstoque(30);
                suplementoRepository.save(wheyConcentrado);

                Suplemento creatinaMono = new Suplemento();
                creatinaMono.setNome("Creatina Monohidratada 300g");
                creatinaMono.setMarca("Universal Nutrition");
                creatinaMono.setDescricao("Creatina pura para aumento de força e potência");
                creatinaMono.setPreco(new BigDecimal("45.90"));
                creatinaMono.getCategorias().add(creatina);
                creatinaMono.setQuantidadeEstoque(25);
                suplementoRepository.save(creatinaMono);

                Suplemento preTreinoPump = new Suplemento();
                preTreinoPump.setNome("Pré-Treino Pump 300g");
                preTreinoPump.setMarca("C4 Original");
                preTreinoPump.setDescricao("Energético com cafeína e beta-alanina");
                preTreinoPump.setPreco(new BigDecimal("79.90"));
                preTreinoPump.getCategorias().add(preTreino);
                preTreinoPump.setQuantidadeEstoque(20);
                suplementoRepository.save(preTreinoPump);

                Suplemento bcaaSuplemento = new Suplemento();
                bcaaSuplemento.setNome("BCAA 2:1:1 120 caps");
                bcaaSuplemento.setMarca("Scivation");
                bcaaSuplemento.setDescricao("Aminoácidos essenciais em cápsulas");
                bcaaSuplemento.setPreco(new BigDecimal("55.90"));
                bcaaSuplemento.getCategorias().add(bcaa);
                bcaaSuplemento.setQuantidadeEstoque(40);
                suplementoRepository.save(bcaaSuplemento);

                Suplemento multivitaminico = new Suplemento();
                multivitaminico.setNome("Multivitamínico 60 caps");
                multivitaminico.setMarca("Centrum");
                multivitaminico.setDescricao("Complexo vitamínico completo");
                multivitaminico.setPreco(new BigDecimal("35.90"));
                multivitaminico.getCategorias().add(vitaminas);
                multivitaminico.setQuantidadeEstoque(60);
                suplementoRepository.save(multivitaminico);

                System.out.println("Dados de teste inseridos com sucesso!");
            }
        };
    }
}
