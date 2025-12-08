package com.soa.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            // 1. Créer le registre RMI sur le port 1099
            Registry registry = LocateRegistry.createRegistry(1099);

            // 2. Créer l'instance du service
            RemiseService remiseService = new RemiseServiceImpl();

            // 3. Publier le service dans le registre
            registry.rebind("RemiseService", remiseService);

            System.out.println("✅ Serveur RMI démarré sur le port 1099");
            System.out.println("📡 Service disponible: rmi://localhost:1099/RemiseService");
            System.out.println("👂 En attente de connexions...");

        } catch (Exception e) {
            System.err.println("❌ Erreur serveur RMI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
