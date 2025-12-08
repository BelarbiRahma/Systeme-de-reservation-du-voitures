package com.soa.clientfx.controllers;

import com.soa.clientfx.models.Voiture;
import com.soa.clientfx.services.JMSProducer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservationDialogController {

    @FXML private Label lblVoitureInfo;
    @FXML private TextField txtNomClient;
    @FXML private TextField txtEmail;
    @FXML private DatePicker dateDebut;
    @FXML private DatePicker dateFin;
    @FXML private ComboBox<String> cbAssurance;
    @FXML private Label lblMontantTotal;
    @FXML private Button btnCalculer;
    @FXML private Button btnReserver;
    @FXML private Button btnAnnuler;

    private Voiture voitureSelectionnee;
    private double montantCalcule = 0.0;

    public void setVoitureSelectionnee(Voiture voiture) {
        this.voitureSelectionnee = voiture;
        if (voiture != null) {
            lblVoitureInfo.setText(String.format("🚗 %s %s - %s (%.2f€/jour)",
                    voiture.getMarque(),
                    voiture.getModele(),
                    voiture.getMatricule(),
                    voiture.getPrixJour()));
        }

        // Initialiser les dates
        dateDebut.setValue(LocalDate.now());
        dateFin.setValue(LocalDate.now().plusDays(3));

        // Initialiser le ComboBox d'assurance
        cbAssurance.getItems().addAll("Basique", "Complet", "Premium");
        cbAssurance.setValue("Basique");
    }

    @FXML
    public void initialize() {
        // Écouteurs pour le calcul automatique
        dateDebut.valueProperty().addListener((obs, oldVal, newVal) -> calculerMontant());
        dateFin.valueProperty().addListener((obs, oldVal, newVal) -> calculerMontant());
        cbAssurance.valueProperty().addListener((obs, oldVal, newVal) -> calculerMontant());

        // Validation de l'email
        txtEmail.textProperty().addListener((obs, oldVal, newVal) -> {
            if (isValidEmail(newVal)) {
                txtEmail.setStyle("-fx-border-color: #2ecc71; -fx-border-radius: 3px;");
            } else {
                txtEmail.setStyle("-fx-border-color: #e74c3c; -fx-border-radius: 3px;");
            }
        });

        // Désactiver le bouton réserver jusqu'à calcul
        btnReserver.setDisable(true);
    }

    @FXML
    private void calculerMontant() {
        try {
            if (voitureSelectionnee == null) {
                lblMontantTotal.setText("❌ Aucune voiture sélectionnée");
                return;
            }

            if (dateDebut.getValue() == null || dateFin.getValue() == null) {
                lblMontantTotal.setText("❌ Veuillez sélectionner les dates");
                return;
            }

            LocalDate debut = dateDebut.getValue();
            LocalDate fin = dateFin.getValue();

            if (debut.isAfter(fin)) {
                lblMontantTotal.setText("❌ Date début doit être avant date fin");
                btnReserver.setDisable(true);
                return;
            }

            long jours = ChronoUnit.DAYS.between(debut, fin) + 1;
            if (jours <= 0) {
                lblMontantTotal.setText("❌ Nombre de jours invalide");
                btnReserver.setDisable(true);
                return;
            }

            // Calcul du prix de location
            double prixLocation = jours * voitureSelectionnee.getPrixJour();

            // Calcul des frais d'assurance
            double fraisAssurance = calculerFraisAssurance(cbAssurance.getValue(), jours);

            // Calcul du total
            montantCalcule = prixLocation + fraisAssurance;

            // Afficher le détail
            lblMontantTotal.setText(String.format("""
                💰 DÉTAIL DU CALCUL:
                • Location (%d jours × %.2f€): %.2f€
                • Assurance (%s): %.2f€
                💵 TOTAL: %.2f€
                """,
                    jours, voitureSelectionnee.getPrixJour(), prixLocation,
                    cbAssurance.getValue(), fraisAssurance,
                    montantCalcule
            ));

            // Activer le bouton réserver
            btnReserver.setDisable(false);

        } catch (Exception e) {
            lblMontantTotal.setText("❌ Erreur de calcul: " + e.getMessage());
            btnReserver.setDisable(true);
        }
    }

    @FXML
    private void reserver() {
        try {
            // Validation des champs
            if (txtNomClient.getText().trim().isEmpty()) {
                showAlert("Erreur", "Veuillez entrer votre nom");
                return;
            }

            if (!isValidEmail(txtEmail.getText())) {
                showAlert("Erreur", "Veuillez entrer un email valide");
                return;
            }

            if (montantCalcule <= 0) {
                showAlert("Erreur", "Veuillez calculer le montant d'abord");
                return;
            }

            // Demande de confirmation
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation de réservation");
            confirmation.setHeaderText("Confirmez-vous cette réservation?");
            confirmation.setContentText(String.format("""
                Client: %s
                Email: %s
                Voiture: %s %s
                Période: %s au %s (%d jours)
                Assurance: %s
                Montant total: %.2f€
                """,
                    txtNomClient.getText(),
                    txtEmail.getText(),
                    voitureSelectionnee.getMarque(),
                    voitureSelectionnee.getModele(),
                    dateDebut.getValue(),
                    dateFin.getValue(),
                    ChronoUnit.DAYS.between(dateDebut.getValue(), dateFin.getValue()) + 1,
                    cbAssurance.getValue(),
                    montantCalcule
            ));

            ButtonType btnOui = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
            ButtonType btnNon = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmation.getButtonTypes().setAll(btnOui, btnNon);

            confirmation.showAndWait().ifPresent(response -> {
                if (response == btnOui) {
                    // Envoyer la réservation au backend
                    envoyerReservation();
                }
            });

        } catch (Exception e) {
            showAlert("Erreur", "Impossible de créer la réservation: " + e.getMessage());
        }
    }

    @FXML
    private void annuler() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    private void envoyerReservation() {
        try {
            // Créer l'objet JSON pour la réservation
            long jours = ChronoUnit.DAYS.between(dateDebut.getValue(), dateFin.getValue()) + 1;

            // Ici, tu devrais appeler ton service REST pour enregistrer la réservation
            // Pour l'instant, on simule
            System.out.println("📋 Réservation envoyée:");
            System.out.println("- Client: " + txtNomClient.getText());
            System.out.println("- Email: " + txtEmail.getText());
            System.out.println("- Voiture: " + voitureSelectionnee.getMatricule());
            System.out.println("- Dates: " + dateDebut.getValue() + " à " + dateFin.getValue());
            System.out.println("- Jours: " + jours);
            System.out.println("- Assurance: " + cbAssurance.getValue());
            System.out.println("- Montant: " + montantCalcule + "€");

            // Envoyer notification JMS
            JMSProducer.envoyerReservationNotification(
                    txtNomClient.getText(),
                    voitureSelectionnee.getMatricule(),
                    (int) jours
            );

            showAlert("Succès", "✅ Réservation créée avec succès!");

            // Fermer la fenêtre
            annuler();

        } catch (Exception e) {
            showAlert("Erreur", "Échec de l'envoi: " + e.getMessage());
        }
    }

    private double calculerFraisAssurance(String niveau, long jours) {
        return switch (niveau.toLowerCase()) {
            case "basique" -> 15.0 * jours;
            case "complet" -> 25.0 * jours;
            case "premium" -> 40.0 * jours;
            default -> 0.0;
        };
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}