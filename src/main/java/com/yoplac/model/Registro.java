package com.yoplac.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Registro {
    private final LocalDateTime fecha = LocalDateTime.now();
    private final MaterialMedico material;
    private final TipoRegistro tipoRegistro;
    private int cantidad;
    private final Usuario usuario;

    public Registro(MaterialMedico material, TipoRegistro tipoRegistro, Usuario usuario) {
        this.material = material;
        this.tipoRegistro = tipoRegistro;
        this.usuario = usuario;
    }

    public Registro(MaterialMedico material, TipoRegistro tipoRegistro, int cantidad, Usuario usuario) {
        this.material = material;
        this.tipoRegistro = tipoRegistro;
        this.usuario = usuario;
        this.cantidad = cantidad;
    }

    public TipoRegistro getTipoRegistro() {
        return tipoRegistro;
    }

    public MaterialMedico getMaterial() {
        return material;
    }

    public int getCantidad() {
        return cantidad;
    }

    @Override
    public String toString() {
        String date = fecha.format(DateTimeFormatter.ISO_LOCAL_TIME);
        String nombre = material.getNombre();
        StringBuilder register = new StringBuilder();

        register.append(date);
        register.append("\t-\tUsuario: ").append(usuario);
        register.append("\t-\tMeterial: ").append(nombre);
        register.append("\t-\tAccion: ").append(tipoRegistro);

        if(cantidad == 0) {
            return register.toString();
        }

        register.append("\t-\tCantidad: ").append(cantidad);

        return register.toString();
    }
}
