package com.yoplac.system;

import com.yoplac.model.MaterialMedico;
import com.yoplac.model.Registro;
import com.yoplac.model.TipoRegistro;
import com.yoplac.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class SistemaInventario {
    private final List<MaterialMedico> materiales = new ArrayList<>();
    private final List<Registro> registros = new ArrayList<>();
    private final Usuario usuario;

    public SistemaInventario() {
        this.usuario = iniciarSesion();
    }

    private Usuario iniciarSesion() {
        Usuario userTest = new Usuario("admin", "admin");
        Usuario user;

        int intentos = 3;
        boolean inicioExitoso = false;

        SistemaUI.mostrarLogin();

        do {
            user = SistemaUI.solicitarCredenciales();

            if(user.equals(userTest)) {
                inicioExitoso = true;
            } else {
                intentos--;

                if(intentos > 0) {
                    SistemaUI.errorIntentoFallido();
                } else {
                    SistemaUI.errorIntentosFallidos();
                }
            }
        } while (intentos > 0 && !inicioExitoso);

        if(!inicioExitoso) {
            user = null;
        }

        return user;
    }

    public void start() {
        if(usuario == null) {
            return;
        }

        SistemaUI.mostrarBienvenida(usuario);

        do {
            String respuesta = SistemaUI.menuPrincipal();

            switch (respuesta) {
                case "1": procesoRetirarMaterial(SistemaUI.mostrarRetirarMaterial(this));
                    break;

                case "2": procesoIngresarMaterial(SistemaUI.mostrarIngresarMaterial(this));
                    break;

                case "3": SistemaUI.mostrarInventarioGeneral(this);
                    break;

                case "4": procesoMenuRegistros();
                    break;

                default:
                    respuesta = "0";
                    break;
            }

            SistemaAlertas.generarAlertas(this);

            if(respuesta.equals("0")) break;
        } while (true);

        SistemaUI.mostrarDespedida();
    }

    private void procesoMenuRegistros() {
        do {
            String respuesta = SistemaUI.menuRegistros();

            switch (respuesta) {
                case "1": SistemaUI.mostrarRegistroSalidas(this);
                    break;

                case "2": SistemaUI.mostrarRegistroEntradas(this);
                    break;

                case "3": SistemaUI.mostrarRegistroCompleto(this);
                    break;

                default:
                    respuesta = "0";
                    break;
            }

            if(respuesta.equals("0")) break;
        } while (true);
    }

    private void procesoIngresarMaterial(String materialIndex) {
        int index = 0;

        try {
            index = Integer.parseInt(materialIndex) - 1;
        }catch(Exception e) {
            SistemaUI.errorOpcionInvalida();
            return;
        }

        if(index == materiales.size()) {
            nuevoMaterial(SistemaUI.mostrarNuevoMaterial());
            return;
        }

        if(index > materiales.size()) {
            SistemaUI.errorOpcionInvalida();
            return;
        }

        MaterialMedico material = materiales.get(index);
        agregarStockMaterial(material, SistemaUI.agregarStockMaterial(material));
    }

    private void procesoRetirarMaterial(String materialIndex) {
        int index;

        try {
            index = Integer.parseInt(materialIndex) - 1;
        }catch(Exception e) {
            SistemaUI.errorOpcionInvalida();
            return;
        }

        if(index == -1) return;

        if(index == materiales.size()) {
            int indexEliminar = SistemaUI.mostrarEliminarMaterial() - 1;
            eliminarMaterial(materiales.get(indexEliminar));
            return;
        }

        if(index > materiales.size()) {
            SistemaUI.errorOpcionInvalida();
            return;
        }

        MaterialMedico material = materiales.get(index);

        int cant = SistemaUI.retirarStockMaterial(material);

        retirarStockMaterial(material, cant);
    }

    private void nuevoMaterial(MaterialMedico material) {
        if(!materiales.contains(material)) {
            materiales.add(material);
            addRegistro(new Registro(material, TipoRegistro.CREAR, usuario));

            int cant = material.getCantidad();

            if(cant > 0) {
                addRegistro(new Registro(material, TipoRegistro.AGREGAR, cant, usuario));
            }
        }
    }

    private void eliminarMaterial(MaterialMedico material) {
        if(materiales.contains(material)) {
            materiales.remove(material);
            addRegistro(new Registro(material, TipoRegistro.ELIMINAR, usuario));
        }
    }

    private void agregarStockMaterial(MaterialMedico material, int cant) {
        Registro registro = new Registro(material, TipoRegistro.AGREGAR, cant, usuario);

        if(!material.addCantidad(cant)) {
            SistemaUI.errorCantidad(registro);
        } else {
            addRegistro(registro);
        }
    }

    private void retirarStockMaterial(MaterialMedico material, int cant) {
        Registro registro = new Registro(material, TipoRegistro.RETIRAR, cant, usuario);

        if(!material.removeCantidad(cant)) {
            SistemaUI.errorCantidad(registro);
        }else {
            addRegistro(registro);
        }
    }

    private void addRegistro(Registro registro) {
        registros.add(registro);
        SistemaUI.mostrarEvento(registro);
    }

    public List<MaterialMedico> getMateriales() {
        return materiales;
    }

    public List<Registro> getRegistros() {
        return registros;
    }
}
