package com.soa.reservationservice.config;

import com.soa.reservationservice.soap.AssuranceService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.xml.ws.Endpoint;

@Configuration
public class WebServiceConfig {

    @Autowired
    private Bus bus;

    @Bean
    public Endpoint assuranceEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, new AssuranceService());
        endpoint.publish("/services/assurance"); // URL du service SOAP
        return endpoint;
    }
}