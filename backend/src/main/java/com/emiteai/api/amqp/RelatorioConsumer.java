// src/main/java/com/emiteai/api/amqp/RelatorioConsumer.java
package com.emiteai.api.amqp;

import com.emiteai.api.config.RabbitConfig;
import com.emiteai.api.model.entity.Relatorio;
import com.emiteai.api.repository.RelatorioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RelatorioConsumer {

    private final RelatorioRepository repo;

    @PersistenceContext
    private EntityManager em;

    @RabbitListener(queues = RabbitConfig.QUEUE_RELATORIO)
    public void processa(Long relatorioId) {

        Relatorio rel = repo.findById(relatorioId).orElse(null);
        if (rel == null) return;

        rel.setStatus("PROCESSANDO");
        repo.save(rel);

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> pessoas = em.createNativeQuery("""
                    SELECT nome, telefone, cpf, cep, numero,
                           complemento, bairro, cidade, estado
                      FROM pessoa
                     ORDER BY id
                    """).getResultList();

            File dir = new File("/reports");
            if (!dir.exists()) dir.mkdirs();

            Path csvPath = Path.of(dir.getAbsolutePath(),
                    "relatorio-" + relatorioId + ".csv");
            try (BufferedWriter w = new BufferedWriter(new FileWriter(csvPath.toFile()));
                 CSVPrinter csv = new CSVPrinter(w, CSVFormat.DEFAULT
                         .withHeader("nome", "telefone", "cpf", "cep",
                                 "numero", "complemento", "bairro", "cidade", "estado"))) {

                for (Object[] p : pessoas) {
                    csv.printRecord(p);
                }
            }

            rel.setCaminho(csvPath.toString());
            rel.setStatus("PRONTO");
            rel.setProntoEm(Instant.now());
            repo.save(rel);

            log.info("Relatório #{} gerado em {}", relatorioId, csvPath);

        } catch (Exception e) {
            log.error("Falha ao gerar relatório {}", relatorioId, e);
            rel.setStatus("ERRO");
            repo.save(rel);
        }
    }
}
