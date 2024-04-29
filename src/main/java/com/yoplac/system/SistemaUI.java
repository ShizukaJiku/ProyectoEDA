package com.yoplac.system;

import com.yoplac.model.MaterialMedico;
import com.yoplac.model.Registro;
import com.yoplac.model.TipoRegistro;
import com.yoplac.model.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemaUI {
    private final static String SEPARATOR = "----------------------------------------------------";
    private final static Scanner input = new Scanner(System.in);

    public static String menuPrincipal() {
        mostrarTitulo("Menu Principal - Sistema Inventario");

        System.out.println("1. Retirar material");
        System.out.println("2. Ingresar material");
        System.out.println("3. Mostrar inventario general");
        System.out.println("4. Menu registros");
        System.out.println("Otra. Salir del programa");

        return leerOpcion();
    }

    public static String mostrarRetirarMaterial(SistemaInventario sistema) {
        mostrarTitulo("Retirar material - Sistema Inventario");

        var listMateriales = sistema.getMateriales();

        listarMateriales(listMateriales);

        if(listMateriales.isEmpty()) {
            return "0";
        }

        System.out.println((listMateriales.size() + 1) + ". Eliminar material medico");

        return leerOpcion();
    }

    public static String mostrarIngresarMaterial(SistemaInventario sistema) {
        mostrarTitulo("Ingresar material - Sistema Inventario");

        var listMateriales = sistema.getMateriales();

        listarMateriales(listMateriales);

        System.out.println((listMateriales.size() + 1) + ". Ingresar nuevo material medico");

        return leerOpcion();
    }

    public static void mostrarInventarioGeneral(SistemaInventario sistema) {
        mostrarTitulo("Inventario General - Sistema Inventario");
        listarMateriales(sistema.getMateriales());
    }

    public static String menuRegistros() {
        mostrarTitulo("Menu Registros - Sistema Inventario");
        System.out.println("1. Mostrar salidas");
        System.out.println("2. Mostrar entradas");
        System.out.println("3. Registro completo");
        System.out.println("Otra. Regresar al menu principal");

        return leerOpcion();
    }

    public static void mostrarRegistroSalidas(SistemaInventario sistema) {
        mostrarTitulo("Registro Salidas - Sistema Inventario");

        var salidas = new ArrayList<Registro>();

        for (Registro registro : sistema.getRegistros()) {
            if(registro.getTipoRegistro() == TipoRegistro.RETIRAR)
                salidas.add(registro);
        }

        listarRegistros(salidas);
    }

    public static void mostrarRegistroEntradas(SistemaInventario sistema) {
        mostrarTitulo("Registro Entradas - Sistema Inventario");

        var entradas = new ArrayList<Registro>();

        for (Registro registro : sistema.getRegistros()) {
            if(registro.getTipoRegistro() == TipoRegistro.AGREGAR)
                entradas.add(registro);
        }

        listarRegistros(entradas);
    }

    public static void mostrarRegistroCompleto(SistemaInventario sistema) {
        mostrarTitulo("Registro Completo - Sistema Inventario");
        listarRegistros(sistema.getRegistros());
    }

    private static void listarMateriales(List<MaterialMedico> materiales) {
        int sizeMateriales = materiales.size();

        if (sizeMateriales == 0) {
            System.out.println("¡¡¡Lista de materiales vacia!!!");
        }

        for (int i = 0; i < sizeMateriales; i++) {
            var index = i + 1;
            System.out.print(index + ". ");

            var material = materiales.get(i);
            System.out.println(material);
        }
    }

    private static void listarRegistros(List<Registro> registros) {
        int size = registros.size();

        if (size == 0) {
            System.out.println("Lista de registros vacia");
        }

        for(var registro : registros) {
            System.out.println(registro);
        }
    }

    private static String leerOpcion() {
        System.out.println(SEPARATOR);
        System.out.print("Ingrese su opción --> ");

        return input.nextLine();
    }

    private static void mostrarTitulo(String titulo) {
        System.out.println();
        System.out.println(SEPARATOR);
        System.out.println("\t\t" + titulo);
        System.out.println(SEPARATOR);
    }

    public static int retirarStockMaterial(MaterialMedico material) {
        System.out.println(SEPARATOR);
        System.out.print("Ingrese la cantidad a retirar --> ");

        try {
            return Integer.parseInt(input.nextLine());
        }catch (Exception e) {
            return 0;
        }
    }

    public static int agregarStockMaterial(MaterialMedico material) {
        System.out.println(SEPARATOR);
        System.out.print("Ingrese la cantidad a agregar --> ");

        try {
            return Integer.parseInt(input.nextLine());
        }catch (Exception e) {
            return 0;
        }
    }

    public static MaterialMedico mostrarNuevoMaterial() {
        System.out.println(SEPARATOR);
        System.out.print("Ingrese el nombre del nuevo material --> ");
        String nombre = input.nextLine();
        System.out.print("Ingrese el stock inicial del nuevo material --> ");

        int stock;

        try {
            stock = Integer.parseInt(input.nextLine());
        }catch (Exception e) {
            stock = 0;
        }

        return new MaterialMedico(nombre, stock);
    }

    public static int mostrarEliminarMaterial() {
        System.out.println(SEPARATOR);
        System.out.print("Ingrese el indice del material a eliminar (Esta accion es irrevertible) --> ");

        try {
            return Integer.parseInt(input.nextLine());
        }catch (Exception e) {
            return 0;
        }
    }

    public static void errorOpcionInvalida() {
        System.out.println(SEPARATOR);
        System.out.println("ERROR! La opcion es invalida");
    }

    public static void errorCantidad(Registro registro) {
        int cantidad = registro.getCantidad();
        var accion = registro.getTipoRegistro();
        var material = registro.getMaterial();

        if(accion == TipoRegistro.RETIRAR || accion == TipoRegistro.AGREGAR) {
            if(cantidad < 0) {
                System.out.println("ERROR! La cantidad debe ser mayor que 0");
            }

            if(cantidad > material.getCantidad()) {
                System.out.println("ERROR! La cantidad excede el maximo stock");
            }
        }
    }

    public static void mostrarEvento(Registro registro) {
        int cantidad = registro.getCantidad();
        var accion = registro.getTipoRegistro();
        String nombre = registro.getMaterial().getNombre();

        StringBuilder evento = new StringBuilder().append(SEPARATOR).append("\n").append(" -->\t");

        String verbo = switch (accion) {
            case TipoRegistro.RETIRAR -> "retirado";
            case TipoRegistro.AGREGAR -> "agregado";
            case TipoRegistro.CREAR -> "creado";
            case TipoRegistro.ELIMINAR -> "eliminado";
        };

        evento.append("Se ha ").append(verbo);

        if(accion == TipoRegistro.RETIRAR || accion == TipoRegistro.AGREGAR) evento.append(" ").append(cantidad).append(" unit de ");
        if(accion == TipoRegistro.CREAR || accion == TipoRegistro.ELIMINAR) evento.append(" el material ");

        evento.append(nombre);

        System.out.println(evento);
    }

    public static void mostrarAlerta(MaterialMedico material) {
        int minStock = SistemaAlertas.MIN_STOCK;

        System.out.println(SEPARATOR);
        System.out.println("Alerta: El material " + material.getNombre() + " está por debajo de " + minStock + " unidades. Se recomienda reponer stock.");
    }

    public static Usuario solicitarCredenciales() {
        System.out.println(SEPARATOR);
        System.out.print("Username --> ");
        String usuario = input.nextLine();
        System.out.print("Password --> ");
        String password = input.nextLine();

        return new Usuario(usuario, password);
    }

    public static void errorIntentosFallidos() {
        System.out.println(SEPARATOR);
        System.out.println("ERROR! Demasidos intentos fallidos");
        System.out.println(SEPARATOR);
    }

    public static void errorIntentoFallido() {
        System.out.println(SEPARATOR);
        System.out.println("Las credenciales son incorrectas, intente de nuevo");
    }

    public static void mostrarLogin() {
        mostrarTitulo("Login - Sistema Inventario");
        System.out.println("Ingrese sus credenciales");
    }

    public static void mostrarBienvenida(Usuario usuario) {
        mostrarTitulo("Bienvenido - Usuario: " + usuario);
    }

    public static void mostrarDespedida() {
        mostrarTitulo("Saliendo del Sistema Inventario...");
    }
}
