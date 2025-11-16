package model;

import java.time.LocalDate;

// Clase que representa la asignación de un empleado a un proyecto
public class Asignacion {
    // Identificador único de la asignación
    private int idAgisnacion;
    // Identificador del empleado asignado
    private int id_Empleado;
    // Identificador del proyecto asignado
    private int id_Proyecto;
    // Fecha en la que se hizo la asignación
    private LocalDate fecha_asignacion;

    // Constructor con parámetros, sirve para crear objetos Asignacion con todos sus datos
    public Asignacion(int idAgisnacion, int id_Empleado, int id_Proyecto, LocalDate fecha_asignacion) {
        this.idAgisnacion = idAgisnacion;
        this.id_Empleado = id_Empleado;
        this.id_Proyecto = id_Proyecto;
        this.fecha_asignacion = fecha_asignacion;
    }

    // Constructor vacío, por si queremos crear el objeto sin datos y luego rellenarlo
    public Asignacion(){}

    // Getter y setter del id de la asignación
    public int getIdAgisnacion() {
        return idAgisnacion;
    }

    public void setIdAgisnacion(int idAgisnacion) {
        this.idAgisnacion = idAgisnacion;
    }

    // Getter y setter del id del empleado
    public int getId_Empleado() {
        return id_Empleado;
    }

    public void setId_Empleado(int id_Empleado) {
        this.id_Empleado = id_Empleado;
    }

    // Getter y setter del id del proyecto
    public int getId_Proyecto() {
        return id_Proyecto;
    }

    public void setId_Proyecto(int id_Proyecto) {
        this.id_Proyecto = id_Proyecto;
    }

    // Getter y setter de la fecha de asignación
    public LocalDate getFecha_asignacion() {
        return fecha_asignacion;
    }

    public void setFecha_asignacion(LocalDate fecha_asignacion) {
        this.fecha_asignacion = fecha_asignacion;
    }

    // Metodo para mostrar el contenido del objeto como un String (útil para depuración)
    @Override
    public String toString() {
        return "Asignacion{" +
                "idAgisnacion=" + idAgisnacion +
                ", id_Empleado=" + id_Empleado +
                ", id_Proyecto=" + id_Proyecto +
                ", fecha_asignacion=" + fecha_asignacion +
                '}';
    }
}