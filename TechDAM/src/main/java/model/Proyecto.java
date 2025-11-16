package model;

import java.math.BigDecimal;
import java.time.LocalDate;

// Clase que representa un proyecto en la empresa
public class Proyecto {
    // Identificador único del proyecto
    private int idProyecto;
    // Nombre del proyecto
    private String nombre;
    // Fecha en la que empieza el proyecto
    private LocalDate fecha_inicio;
    // Fecha en la que termina el proyecto
    private LocalDate fecha_fin;
    // Presupuesto destinado al proyecto
    private BigDecimal presupuesto;

    // Constructor que recibe todos los datos
    public Proyecto(int idProyecto, String nombre, LocalDate fecha_inicio, LocalDate fecha_fin, BigDecimal presupuesto) {
        this.idProyecto = idProyecto;
        this.nombre = nombre;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.presupuesto = presupuesto;
    }
    // Constructor vacío, para poder rellenar luego los datos como queramos
    public Proyecto(){}

    // Getter y setter del id del proyecto
    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    // Getter y setter del nombre
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getter y setter de la fecha de inicio
    public LocalDate getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(LocalDate fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    // Getter y setter de la fecha de fin
    public LocalDate getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(LocalDate fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    // Getter y setter del presupuesto
    public BigDecimal getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(BigDecimal presupuesto) {
        this.presupuesto = presupuesto;
    }

    // Metodo para que puedas imprimir el objeto y ver sus valores fácilmente
    @Override
    public String toString() {
        return "Proyecto{" +
                "idProyecto=" + idProyecto +
                ", nombre='" + nombre + '\'' +
                ", fecha_inicio=" + fecha_inicio +
                ", fecha_fin=" + fecha_fin +
                ", presupuesto=" + presupuesto +
                '}';
    }
}