package com.soa.clientfx.services;

import javax.jms.*;  // Utilise javax.jms (pas jakarta.jms)
import org.apache.activemq.ActiveMQConnectionFactory;

public class JMSProducer {
    private static final String URL = "tcp://localhost:61616";
    private static final String QUEUE = "reservation.queue";

    public static void envoyerNotification(String message) {
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(URL);
            factory.setTrustAllPackages(true);

            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(QUEUE);
            MessageProducer producer = session.createProducer(destination);

            TextMessage msg = session.createTextMessage(message);
            producer.send(msg);

            System.out.println("✅ Message JMS envoyé : " + message);

            producer.close();
            session.close();
            connection.close();

        } catch (JMSException e) {
            System.err.println("❌ Erreur JMS : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void envoyerReservationNotification(String client, String matricule, int jours) {
        String json = String.format(
                "{\"type\":\"RESERVATION\",\"client\":\"%s\",\"voiture\":\"%s\",\"duree\":%d,\"timestamp\":\"%s\"}",
                client, matricule, jours, java.time.LocalDateTime.now()
        );
        envoyerNotification(json);
    }
}