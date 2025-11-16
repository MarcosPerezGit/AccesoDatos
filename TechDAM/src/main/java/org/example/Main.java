package org.example;

import dao.EmpleadoDAO;
import dao.ProyectoDAO;
import service.ProcedimientosService;
import service.TransaccionesService;

// Clase principal que inicia la aplicación
public class Main {
    public static void main(String[] args) {
        // Se crean las instancias de los DAOs y servicios necesarios
        EmpleadoDAO empleadoDAO = new EmpleadoDAO();
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        ProcedimientosService procedimientosService = new ProcedimientosService();
        TransaccionesService transaccionesService = new TransaccionesService();

        // Se crea el menú y se le pasan los DAOs y servicios para que los use en las opciones
        Menu menu = new Menu(empleadoDAO, proyectoDAO, procedimientosService, transaccionesService);
        // Se llama al metodo para mostrar el menú y empezar a interactuar con el usuario
        menu.mostrarMenu();
    }
}