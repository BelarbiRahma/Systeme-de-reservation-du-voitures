package com.soa.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemiseServiceImpl extends UnicastRemoteObject
        implements RemiseService {

    public RemiseServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public RemiseResult calculerRemise(String typeClient, double montant)
            throws RemoteException {

        double taux = switch(typeClient.toLowerCase()) {
            case "vip" -> 0.20;      // 20% de remise
            case "fidele" -> 0.15;   // 15% de remise
            case "normal" -> 0.05;   // 5% de remise
            default -> 0.0;          // Pas de remise
        };

        double remise = montant * taux;
        double montantFinal = montant - remise;

        System.out.println("💰 RMI: Remise calculée pour " + typeClient +
                ": " + remise + "€");

        return new RemiseResult(remise, montantFinal);
    }
}