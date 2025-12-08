package com.soa.reservationservice.jms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
@Service
public class JMSConsumer {
    @JmsListener(destination = "reservation.queue")
    public void receive(String message) {
        System.out.println("📩 JMS message reçu : " + message);
        // TODO: parser JSON -> lancer traitement
    }
}