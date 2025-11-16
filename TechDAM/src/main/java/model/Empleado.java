package model;

import java.math.BigDecimal;
import java.time.LocalDate;

// Clase que representa a un empleado de la empresa
public class Empleado {
    // Identificador único del empleado
    private int idEmpleado;
    // Nombre del empleado
    private String nombre;
    // Departamento donde trabaja el empleado
    private String departamento;
    // Salario del empleado (usando BigDecimal por tema de decimales)
    private BigDecimal salario;
    // Fecha de contratación del empleado
    private LocalDate fecha_contratacion;
    // Indica si el empleado está activo o no
    private boolean activo;

    // Constructor con todos los valores
    public Empleado(int idEmpleado, String nombre,String departamento, BigDecimal salario, LocalDate fecha_contratacion, boolean activo){
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.departamento = departamento;
        this.salario = salario;
        this.fecha_contratacion = fecha_contratacion;
        this.activo = activo;
    }

    // Constructor vacío, para poder crear el objeto e ir añadiendo luego los datos
    public Empleado(){}

    // Getter y setter del id del empleado
    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    // Getter y setter de si está activo el empleado
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // Getter y setter de la fecha de contratación
    public LocalDate getFecha_contratacion() {
        return fecha_contratacion;
    }

    public void setFecha_contratacion(LocalDate fecha_contratacion) {
        this.fecha_contratacion = fecha_contratacion;
    }

    // Getter y setter del salario
    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    // Getter y setter del nombre
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getter y setter del departamento
    public String getDepartamento(){
        return departamento;
    }

    public void setDepartamento(String departamento){
        this.departamento = departamento;
    }

    // Metodo para mostrar el contenido del objeto como un String (útil para depuración y pruebas)
    @Override
    public String toString() {
        return "Empleado{" +
                "idEmpleado=" + idEmpleado +
                ", nombre='" + nombre + '\'' +
                ", departamento='" + departamento + '\'' +
                ", salario=" + salario +
                ", fecha_contratacion=" + fecha_contratacion +
                ", activo=" + activo +
                '}';
    }
}