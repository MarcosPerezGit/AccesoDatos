package com.empresa.horas.console;

import com.empresa.horas.service.EmpresaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MenuConsola implements CommandLineRunner {

    private final EmpresaService service;

    public MenuConsola(EmpresaService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("""
                \n--- MENÚ ---
                1. Registrar horas trabajadas
                2. Consultar horas por empleado
                3. Consultar horas por proyecto
                4. Listar tareas
                0. Salir
                """);

            opcion = sc.nextInt();

            switch (opcion) {
                case 1 -> registrarHoras(sc);
                case 2 -> consultarEmpleado(sc);
                case 3 -> consultarProyecto(sc);
                case 4 -> listarTareas();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    private void registrarHoras(Scanner sc) {
        System.out.print("ID Empleado: ");
        int emp = sc.nextInt();

        System.out.print("ID Proyecto: ");
        int pro = sc.nextInt();

        System.out.print("Horas trabajadas: ");
        int horas = sc.nextInt();

        service.registrarTarea(emp, pro, horas);
        System.out.println("Tarea registrada.");
    }

    private void consultarEmpleado(Scanner sc) {
        System.out.print("ID Empleado: ");
        int id = sc.nextInt();
        System.out.println("Total horas: " + service.horasPorEmpleado(id));
    }

    private void consultarProyecto(Scanner sc) {
        System.out.print("ID Proyecto: ");
        int id = sc.nextInt();
        System.out.println("Total horas: " + service.horasPorProyecto(id));
    }

    private void listarTareas() {
        service.getTareas().forEach(System.out::println);
    }
}