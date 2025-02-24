package com.example.productor.service;

import com.example.productor.model.SenalVital;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class SenalVitalService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "topic_s8";  // Tópico para todas las señales vitales
    private static final String ALERT_TOPIC = "topic_alertas";  // Tópico solo para anomalías

    private final Random random = new Random();

    public SenalVitalService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Genera y envía señales vitales aleatorias cada minuto.
     */
    @Scheduled(fixedRate = 60000)  // Se ejecuta cada 60,000 ms (1 minuto)
    public void enviarSenalesAutomaticamente() {
        SenalVital senale = generarSenales();
        System.out.println("📡 Enviando señales vitales a Kafka: " + senale);
        enviarSenalVital(senale);
    }

    /**
     * Genera señales vitales con valores aleatorios.
     */
    public SenalVital generarSenales() {
        return new SenalVital(
                random.nextInt(60) + 40,   // Ritmo cardíaco entre 40 - 100 bpm
                35.5 + (random.nextDouble() * 3),  // Temperatura entre 35.5 - 38.5°C
                random.nextInt(60) + 80,  // Presión sistólica entre 80 - 140 mmHg
                random.nextInt(40) + 50    // Presión diastólica entre 50 - 90 mmHg
        );
    }

    /**
     * Envía las señales vitales al tópico correcto.
     */
    public void enviarSenalVital(SenalVital senalVital) {
        try {
            String mensaje = new ObjectMapper().writeValueAsString(senalVital);

            kafkaTemplate.send(TOPIC, mensaje);
            System.out.println("✅ Señal vital enviada a Kafka en " + TOPIC);

            if (esAnomalia(senalVital)) {
                kafkaTemplate.send(ALERT_TOPIC, mensaje);
                System.out.println("⚠️ Alerta enviada a Kafka en " + ALERT_TOPIC + " 🚨");
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("❌ Error al serializar la señal vital", e);
        }
    }

    private boolean esAnomalia(SenalVital senal) {
        return senal.getRitmoCardiaco() < 60 || senal.getRitmoCardiaco() > 100 ||
               senal.getTemperatura() < 36.0 || senal.getTemperatura() > 37.5 ||
               senal.getPresionSistolica() < 90 || senal.getPresionSistolica() > 140 ||
               senal.getPresionDiastolica() < 60 || senal.getPresionDiastolica() > 90;
    }
}
