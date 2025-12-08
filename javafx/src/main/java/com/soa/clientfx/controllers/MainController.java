package com.soa.clientfx.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soa.clientfx.models.Voiture;
import com.soa.clientfx.services.JMSProducer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MainController {

    @FXML private TableView<Voiture> voituresTable;
    @FXML private TableColumn<Voiture, String> colMatricule;
    @FXML private TableColumn<Voiture, String> colMarque;
    @FXML private TableColumn<Voiture, String> colModele;
    @FXML private TableColumn<Voiture, Double> colPrixJour;
    @FXML private TableColumn<Voiture, String> colStatut;

    @FXML private Button btnRefresh;
    @FXML private Button btnReserver;
    @FXML private Button btnJMS;
    @FXML private Button btnStats;
    @FXML private Label lblStatus;

    private final ObservableList<Voiture> voituresData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadVoitures();
        setupEventHandlers();
    }

    private void setupTableColumns() {
        colMatricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colPrixJour.setCellValueFactory(new PropertyValueFactory<>("prixJour"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }

    private void setupEventHandlers() {
        btnRefresh.setOnAction(e -> loadVoitures());
        btnReserver.setOnAction(e -> reserverVoiture());
        btnJMS.setOnAction(e -> sendTestNotification());
        btnStats.setOnAction(e -> showStatistics());
    }

    private void loadVoitures() {
        lblStatus.setText("⏳ Chargement en cours...");

        new Thread(() -> {
            try {
                List<Voiture> voitures = getVoituresFromAPI();
                Platform.runLater(() -> {
                    voituresData.setAll(voitures);
                    voituresTable.setItems(voituresData);
                    lblStatus.setText("✅ " + voitures.size() + " voitures disponibles");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showError("Erreur de chargement", e.getMessage());
                    lblStatus.setText("❌ Erreur de connexion");
                    // Afficher des données de test
                    showTestData();
                });
            }
        }).start();
    }

    private List<Voiture> getVoituresFromAPI() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/voitures"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(),
                    new TypeReference<List<Voiture>>() {});
        } else {
            throw new RuntimeException("Erreur API: " + response.statusCode());
        }
    }

    private void reserverVoiture() {
        Voiture selected = voituresTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Sélection requise", "Veuillez sélectionner une voiture");
            return;
        }

        // Créer un formulaire de réservation
        Dialog<Map<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Réservation de voiture");
        dialog.setHeaderText("Réservation pour: " + selected.getMatricule());

        // Boutons
        ButtonType reserverButton = new ButtonType("Réserver", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(reserverButton, ButtonType.CANCEL);

        // Formulaire
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField txtNom = new TextField();
        txtNom.setPromptText("Votre nom");
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("votre@email.com");
        DatePicker dateDebut = new DatePicker();
        dateDebut.setValue(LocalDate.now());
        DatePicker dateFin = new DatePicker();
        dateFin.setValue(LocalDate.now().plusDays(3));

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(txtNom, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(txtEmail, 1, 1);
        grid.add(new Label("Du:"), 0, 2);
        grid.add(dateDebut, 1, 2);
        grid.add(new Label("Au:"), 0, 3);
        grid.add(dateFin, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Focus sur le premier champ
        Platform.runLater(txtNom::requestFocus);

        // Convertir résultat
        dialog.setResultConverter(button -> {
            if (button == reserverButton) {
                Map<String, String> result = new HashMap<>();
                result.put("nom", txtNom.getText());
                result.put("email", txtEmail.getText());
                result.put("dateDebut", dateDebut.getValue().toString());
                result.put("dateFin", dateFin.getValue().toString());
                return result;
            }
            return null;
        });

        // Traiter la réservation
        Optional<Map<String, String>> result = dialog.showAndWait();
        result.ifPresent(data -> {
            try {
                creerReservation(selected.getMatricule(), data);
            } catch (Exception e) {
                showError("Erreur", e.getMessage());
            }
        });
    }

    private void creerReservation(String matricule, Map<String, String> data) throws Exception {
        String reservationJson = String.format("""
            {
                "dateDebut": "%s",
                "dateFin": "%s",
                "client": {
                    "nom": "%s",
                    "email": "%s"
                },
                "voiture": {
                    "matricule": "%s"
                }
            }
            """,
                data.get("dateDebut"),
                data.get("dateFin"),
                data.get("nom"),
                data.get("email"),
                matricule
        );

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/reservations"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(reservationJson))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            showAlert("Succès", "✅ Réservation créée avec succès!");

            // Envoyer notification JMS
            int jours = (int) java.time.temporal.ChronoUnit.DAYS.between(
                    LocalDate.parse(data.get("dateDebut")),
                    LocalDate.parse(data.get("dateFin"))
            ) + 1;

            JMSProducer.envoyerReservationNotification(data.get("nom"), matricule, jours);

            // Rafraîchir la liste
            loadVoitures();

        } else {
            showError("Erreur", "❌ Échec: " + response.body());
        }
    }

    private void sendTestNotification() {
        try {
            String message = """
                {
                    "event": "test_javafx",
                    "timestamp": "%s",
                    "status": "active"
                }
                """.formatted(java.time.LocalDateTime.now());

            JMSProducer.envoyerNotification(message);
            showAlert("Notification", "📨 Message JMS envoyé!");

        } catch (Exception e) {
            showError("Erreur JMS", e.getMessage());
        }
    }

    private void showStatistics() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:5000/stats/top3marques"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            String stats = response.statusCode() == 200 ?
                    response.body() : "Statistiques non disponibles";

            TextArea textArea = new TextArea(stats);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            VBox vbox = new VBox(textArea);
            vbox.setPrefSize(400, 300);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Statistiques");
            alert.setHeaderText("Top 3 marques réservées");
            alert.getDialogPane().setContent(vbox);
            alert.showAndWait();

        } catch (Exception e) {
            showAlert("Statistiques", "📊 Données de test:\n- Hyundai: 45 réservations\n- Renault: 32\n- Kia: 28");
        }
    }

    private void showTestData() {
        List<Voiture> testData = List.of(
                new Voiture("123-TN-456", "Hyundai", "Tucson", 50.0, "DISPONIBLE"),
                new Voiture("456-TN-222", "Kia", "Sportage", 55.0, "DISPONIBLE"),
                new Voiture("789-TN-111", "Renault", "Clio", 60.0, "DISPONIBLE")
        );
        voituresData.setAll(testData);
        voituresTable.setItems(voituresData);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}