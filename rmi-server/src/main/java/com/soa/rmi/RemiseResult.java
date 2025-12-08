package com.soa.rmi;

import java.io.Serializable;

public class RemiseResult implements Serializable {
    private double remise;
    private double montantFinal;

    public RemiseResult(double remise, double montantFinal) {
        this.remise = remise;
        this.montantFinal = montantFinal;
    }

    public double getRemise() { return remise; }
    public double getMontantFinal() { return montantFinal; }

    @Override
    public String toString() {
        return String.format("Remise: %.2f€, Total: %.2f€", remise, montantFinal);
    }
}