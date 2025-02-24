package com.example.productor.controller;

import com.example.productor.service.SenalVitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/senales")
public class SenalVitalController {

    @Autowired
    private final SenalVitalService senalVitalService;

    public SenalVitalController(SenalVitalService senalVitalService) {
        this.senalVitalService = senalVitalService;
    }

    //si nos piden mas de un paciente, o que funcione para todos, vamos a tener que meter bases de datos aquí, con info del paciente.
    @GetMapping("/enviarVitalesActuales")
    public String obtenerSenalesActuales() {
        return "Vitales enviadas : " + senalVitalService.generarSenales();
    }
}
