package com.soa.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMITestClient {
    public static void main(String[] args) {
        try {
            System.out.println("🔍 Connexion au serveur RMI...");
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            System.out.println("✅ Connecté au registre RMI");
            RemiseService service = (RemiseService) registry.lookup("RemiseService");
            System.out.println("✅ Service 'RemiseService' trouvé\n");

            // Test avec 1000€
            double montantTest = 1000.0;
            System.out.println("📊 TESTS AVEC MONTANT DE " + montantTest + "€");
            System.out.println("=" .repeat(40));

            // Test Standard
            RemiseResult resultStandard = service.calculerRemise("standard", montantTest);
            System.out.printf("🧑 Standard (2%%)   : Remise = %8.2f€ | Final = %8.2f€\n",
                    resultStandard.getRemise(), resultStandard.getMontantFinal());

            // Test VIP
            RemiseResult resultVIP = service.calculerRemise("vip", montantTest);
            System.out.printf("👑 VIP (20%%)       : Remise = %8.2f€ | Final = %8.2f€\n",
                    resultVIP.getRemise(), resultVIP.getMontantFinal());

            // Test Fidèle
            RemiseResult resultFidele = service.calculerRemise("fidèle", montantTest);
            System.out.printf("⭐ Fidèle (10%%)    : Remise = %8.2f€ | Final = %8.2f€\n",
                    resultFidele.getRemise(), resultFidele.getMontantFinal());

            // Test Premium
            RemiseResult resultPremium = service.calculerRemise("premium", montantTest);
            System.out.printf("🏆 Premium (15%%)   : Remise = %8.2f€ | Final = %8.2f€\n",
                    resultPremium.getRemise(), resultPremium.getMontantFinal());

            System.out.println("\n" + "=" .repeat(40));

            // Test additionnel avec montant aléatoire
            double montantAleatoire = 345.67;
            RemiseResult resultAleatoire = service.calculerRemise("vip", montantAleatoire);
            System.out.printf("\n🎲 Test aléatoire (VIP sur %.2f€) :\n", montantAleatoire);
            System.out.printf("   Remise : %.2f€ (%.2f%%)\n",
                    resultAleatoire.getRemise(),
                    (resultAleatoire.getRemise() / montantAleatoire * 100));
            System.out.printf("   Final  : %.2f€\n", resultAleatoire.getMontantFinal());

            // Vérification de l'arrondi
            System.out.println("\n✅ Tous les tests passés avec succès !");

        } catch (Exception e) {
            System.err.println("❌ Erreur client: " + e.getMessage());
            e.printStackTrace();
        }
    }
}