package com.empresa.horas.service;

import com.empresa.horas.model.*;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmpresaService {

    private Map<Integer, Empleado> empleados = new HashMap<>();
    private Map<Integer, Proyecto> proyectos = new HashMap<>();
    @Getter
    private List<Tarea> tareas = new ArrayList<>();

    public EmpresaService() {
        inicializarDatos();
    }

    private void inicializarDatos() {
        for (int i = 1; i <= 5; i++) {
            empleados.put(i, new Empleado(i, "Empleado " + i));
        }

        for (int i = 1; i <= 10; i++) {
            proyectos.put(i, new Proyecto(i, "Proyecto " + i));
        }
    }

    public Collection<Empleado> getEmpleados() {
        return empleados.values();
    }

    public Collection<Proyecto> getProyectos() {
        return proyectos.values();
    }

    public void registrarTarea(int idEmpleado, int idProyecto, int horas) {
        Empleado e = empleados.get(idEmpleado);
        Proyecto p = proyectos.get(idProyecto);

        if (e != null && p != null) {
            tareas.add(new Tarea(e, p, horas));
        }
    }

    public int horasPorEmpleado(int idEmpleado) {
        return tareas.stream()
                .filter(t -> t.getEmpleado().getId() == idEmpleado)
                .mapToInt(Tarea::getHoras)
                .sum();
    }

    public int horasPorProyecto(int idProyecto) {
        return tareas.stream()
                .filter(t -> t.getProyecto().getId() == idProyecto)
                .mapToInt(Tarea::getHoras)
                .sum();
    }

}