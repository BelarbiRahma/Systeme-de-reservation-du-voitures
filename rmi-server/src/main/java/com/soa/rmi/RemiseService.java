package com.soa.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemiseService extends Remote {
    RemiseResult calculerRemise(String typeClient, double montant)
            throws RemoteException;
}