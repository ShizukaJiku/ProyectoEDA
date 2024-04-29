package com.yoplac.system;

import com.yoplac.model.MaterialMedico;

public class SistemaAlertas {
    public static final int MIN_STOCK = 5;

    public static void generarAlertas(SistemaInventario sistema) {
        for (MaterialMedico material : sistema.getMateriales()) {
            if (material.getCantidad() < MIN_STOCK) {
                SistemaUI.mostrarAlerta(material);
            }
        }
    }
}
