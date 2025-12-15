package com.empresa.horas.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tarea {
    private Empleado empleado;
    private Proyecto proyecto;
    private int horas;
}