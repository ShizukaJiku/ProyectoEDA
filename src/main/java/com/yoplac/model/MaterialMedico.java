package com.yoplac.model;

public class MaterialMedico {
    private String nombre;
    private int cantidad;

    public MaterialMedico(String nombre, int cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean addCantidad(int cant) {
        if(cant <= 0) return false;

        cantidad += cant;

        return true;
    }

    public boolean removeCantidad(int cant) {
        if(cant <= 0 || cant >= cantidad) return false;

        cantidad -= cant;

        return true;
    }

    public int getCantidad() {
        return cantidad;
    }

    @Override
    public String toString() {
        return nombre + " - " + cantidad + " unit";
    }
}
