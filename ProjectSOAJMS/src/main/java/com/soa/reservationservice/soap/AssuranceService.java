package com.soa.reservationservice.soap;
import com.soa.reservationservice.soap.models.ReservationRequest;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
@WebService(serviceName = "AssuranceService")
public class AssuranceService {
    @WebMethod
    public double calculerFraisAssurance(ReservationRequest request) {
        double tarifBase = 15.0;
        if ("Complet".equalsIgnoreCase(request.getNiveauAssurance()))
            tarifBase += 10.0;
        double coutFinal = tarifBase * request.getDureeJours();
        double tva = coutFinal * 0.10;
        return coutFinal + tva;
    }
}