package com.example.productor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SenalVital {
    private int ritmoCardiaco;
    private double temperatura;
    private int presionSistolica;
    private int presionDiastolica;
}
