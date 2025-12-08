package com.soa.clientfx.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soa.clientfx.models.Voiture;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

// Record pour la réponse REST
record VoitureResponse(String matricule, String marque,
                       String modele, double prixJour,
                       String statutDisponibilite) {}

public class RestClientService {
    private static final String BASE_URL = "http://localhost:8080/api";
    private final HttpClient client;
    private final ObjectMapper mapper;

    public RestClientService() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.mapper = new ObjectMapper();
        mapper.findAndRegisterModules(); // Pour supporter LocalDate, etc.
    }

    // Synchronous
    public List<Voiture> getVoituresDisponibles() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/voitures"))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Erreur serveur: " + response.statusCode());
        }

        List<VoitureResponse> voituresEntities =
                mapper.readValue(response.body(), new TypeReference<>() {});

        return voituresEntities.stream()
                .map(v -> new Voiture(
                        v.matricule(),
                        v.marque(),
                        v.modele(),
                        v.prixJour(),
                        v.statutDisponibilite()
                ))
                .toList(); // Java 16+ .toList()
    }

    // Asynchronous
    public CompletableFuture<List<Voiture>> getVoituresDisponiblesAsync() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/voitures"))
                .header("Accept", "application/json")
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(body -> {
                    try {
                        List<VoitureResponse> responses = mapper.readValue(body, new TypeReference<>() {});
                        return responses.stream()
                                .map(v -> new Voiture(v.matricule(), v.marque(), v.modele(), v.prixJour(), v.statutDisponibilite()))
                                .toList();
                    } catch (Exception e) {
                        throw new RuntimeException("Erreur parsing JSON", e);
                    }
                });
    }

    // Méthode pour créer une réservation
    public String creerReservation(String clientJson) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/reservations"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(clientJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return switch (response.statusCode()) {
            case 201 -> "✅ Réservation créée : " + response.body();
            case 400 -> "❌ Données invalides";
            case 404 -> "❌ Voiture non trouvée";
            default -> "❌ Erreur " + response.statusCode() + " : " + response.body();
        };
    }

    public String getStatistics() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5000/stats/top3marques"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}