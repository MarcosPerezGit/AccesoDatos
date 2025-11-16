package org.example;

import dao.EmpleadoDAO;
import dao.ProyectoDAO;
import service.ProcedimientosService;
import service.TransaccionesService;

import java.math.BigDecimal;
import java.util.*;

// Clase que gestiona todos los menús de la aplicación (menú principal y submenús)
public class Menu {

    // Atributos para trabajar con los DAOs y servicios
    private final EmpleadoDAO empleadoDAO;
    private final ProyectoDAO proyectoDAO;
    private final ProcedimientosService procedimientosService;
    private final TransaccionesService transaccionesService;
    // Scanner para leer la entrada del usuario
    private final Scanner entrada = new Scanner(System.in);

    // Constructor donde se pasan los DAOs y servicios
    public Menu(EmpleadoDAO empleadoDAO,ProyectoDAO proyectoDAO,ProcedimientosService procedimientosService,TransaccionesService transaccionesService) {
        this.empleadoDAO = empleadoDAO;
        this.proyectoDAO = proyectoDAO;
        this.procedimientosService = procedimientosService;
        this.transaccionesService = transaccionesService;
    }

    // Metodo para mostrar el menú principal y controlar las opciones del usuario
    public void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n---- MENÚ PRINCIPAL ----");
            System.out.println("1. Gestión de empleados");
            System.out.println("2. Gestión de proyectos");
            System.out.println("3. Procedimientos almacenados");
            System.out.println("4. Transacciones");
            System.out.println("0. Salir");
            System.out.print("Elige opción: ");

            // Si el usuario pone algo que no es número, se pone -1 (así no crashea)
            opcion = entrada.hasNextInt() ? entrada.nextInt() : -1;
            entrada.nextLine(); // limpieza del input para evitar errores

            // Switch para según la opción, ir al submenú correspondiente o salir
            switch (opcion) {
                case 1:
                    submenuEmpleados();
                    break;
                case 2:
                    submenuProyectos();
                    break;
                case 3:
                    submenuProcedimientos();
                    break;
                case 4:
                    submenuTransacciones();
                    break;
                case 0:
                    System.out.println("Saliendo de la aplicación...");
                    break;
                default:
                    System.out.println("Opción incorrecta.");
            }
        } while (opcion != 0);

        entrada.close(); // Cuando se sale, se cierra el scanner
    }

    // Submenú para gestionar los empleados
    private void submenuEmpleados() {
        int opcion;
        do {
            System.out.println("\n--- Gestión de empleados ---");
            System.out.println("1. Mostrar empleados");
            System.out.println("2. Insertar empleado");
            System.out.println("3. Modificar empleado");
            System.out.println("4. Eliminar empleado");
            System.out.println("0. Volver");
            System.out.print("Elige opción: ");

            opcion = entrada.hasNextInt() ? entrada.nextInt() : -1;
            entrada.nextLine();

            switch (opcion) {
                case 1:
                    // Muestra todos los empleados por pantalla
                    empleadoDAO.obtenerTodos().forEach(System.out::println);
                    break;
                case 2:
                    // Crea un empleado nuevo
                    int nuevoId = empleadoDAO.crearEmpleado();
                    if (nuevoId > 0)
                        System.out.println("Empleado creado con ID " + nuevoId);
                    else
                        System.out.println("No se pudo crear el empleado.");
                    break;
                case 3:
                    // Actualiza (modifica) un empleado
                    if (empleadoDAO.actualizarEmpleado())
                        System.out.println("Empleado modificado correctamente.");
                    else
                        System.out.println("No se pudo modificar el empleado.");
                    break;
                case 4:
                    // Borra un empleado
                    if (empleadoDAO.eliminarEmpleado())
                        System.out.println("Empleado eliminado correctamente.");
                    else
                        System.out.println("No se pudo eliminar el empleado.");
                    break;
                case 0:
                    // Volver atrás (salir del submenú)
                    break;
                default:
                    System.out.println("Opcion incorrecta.");
            }
        } while (opcion != 0);
    }

    // Submenú para gestionar proyectos
    private void submenuProyectos() {
        int opcion;
        do {
            System.out.println("\n--- Gestión de proyectos ---");
            System.out.println("1. Mostrar proyectos");
            System.out.println("2. Insertar proyecto");
            System.out.println("3. Modificar proyecto");
            System.out.println("4. Eliminar proyecto");
            System.out.println("0. Volver");
            System.out.print("Elige opcion: ");

            opcion = entrada.hasNextInt() ? entrada.nextInt() : -1;
            entrada.nextLine();

            switch (opcion) {
                case 1:
                    // Muestra todos los proyectos
                    proyectoDAO.obtenerTodos().forEach(System.out::println);
                    break;
                case 2:
                    // Crea proyecto nuevo
                    int nuevoId = proyectoDAO.crearProyecto();
                    if (nuevoId > 0)
                        System.out.println("Proyecto creado con ID " + nuevoId);
                    else
                        System.out.println("No se pudo crear el proyecto.");
                    break;
                case 3:
                    // Modifica proyecto existente
                    if (proyectoDAO.actualizarProyecto())
                        System.out.println("Proyecto modificado correctamente.");
                    else
                        System.out.println("No se pudo modificar el proyecto.");
                    break;
                case 4:
                    // Elimina un proyecto
                    if (proyectoDAO.eliminarProyecto())
                        System.out.println("Proyecto eliminado correctamente.");
                    else
                        System.out.println("No se pudo eliminar el proyecto.");
                    break;
                case 0:
                    // Volver atrás
                    break;
                default:
                    System.out.println("Opcion incorrecta.");
            }
        } while (opcion != 0);
    }

    // Submenú para procedimientos almacenados (llama a métodos de servicio que hacen cosas en la base de datos)
    private void submenuProcedimientos() {
        int opcion;
        do {
            System.out.println("\n--- Procedimientos almacenados ---");
            System.out.println("1. Actualizar salario por departamento");
            System.out.println("2. Empleados por Proyecto");
            System.out.println("0. Volver");
            System.out.print("Elige opcion: ");
            opcion = entrada.hasNextInt() ? entrada.nextInt() : -1;
            entrada.nextLine();

            switch (opcion) {
                case 1:
                    // Ejecuta el procedimiento para actualizar salarios por departamento
                    procedimientosService.actualizarSalarioDepartamento();
                    break;
                case 2:
                    // Ejecuta el procedimiento para mostrar empleados en un proyecto
                    procedimientosService.empleadosPorProyecto();
                    break;
                case 0:
                    // Volver atrás
                    break;
                default:
                    System.out.println("Opcion incorrecta.");
            }
        } while (opcion != 0);
    }

    // Submenú para transacciones (operaciones que implican cambios en la base de datos de forma segura)
    private void submenuTransacciones() {
        int opcion;
        do {
            System.out.println("\n--- Transacciones ---");
            System.out.println("1. Transferir presupuesto entre proyectos");
            System.out.println("2. Asignar empleados a proyecto");
            System.out.println("0. Volver");
            System.out.print("Elige opcion: ");
            opcion = entrada.hasNextInt() ? entrada.nextInt() : -1;
            entrada.nextLine();

            switch (opcion) {
                case 1:
                    // Para transferir dinero de un proyecto a otro
                    System.out.print("Id de proyecto origen: ");
                    int idOrigen = entrada.nextInt();
                    entrada.nextLine();
                    System.out.print("Id de proyecto destino: ");
                    int idDestino = entrada.nextInt();
                    entrada.nextLine();
                    System.out.print("Monto a transferir: ");
                    BigDecimal monto = new BigDecimal(entrada.nextLine());
                    boolean transferido = transaccionesService.transferirPresupuesto(idOrigen, idDestino, monto);
                    System.out.println(transferido ? "Transferencia realizada correctamente." : "No se pudo realizar la transferencia.");
                    break;
                case 2:
                    // Para asignar empleados a un proyecto usando savepoint
                    System.out.print("Id de proyecto destino: ");
                    int idProyectoSP = entrada.nextInt();
                    entrada.nextLine();
                    System.out.print("Introduce los IDs de empleados separados por coma: ");
                    String ids = entrada.nextLine();
                    List<Integer> idList = new ArrayList<>();
                    Arrays.stream(ids.split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .forEach(s -> {
                                try {
                                    idList.add(Integer.parseInt(s));
                                } catch (NumberFormatException ignored) {
                                }
                            });
                    // Llama al metodo para asignar empleados usando savepoint
                    transaccionesService.asignarEmpleadosConSavepoint(idProyectoSP, idList);
                    break;
                case 0:
                    // Volver atrás
                    break;
                default:
                    System.out.println("Opcion incorrecta.");
            }
        } while (opcion != 0);
    }
}