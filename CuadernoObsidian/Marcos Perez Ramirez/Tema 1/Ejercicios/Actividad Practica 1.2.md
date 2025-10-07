Preguntas de reflexión a cerca del ejercicio:

**Pregunta 1: ¿Qué ocurriría si usas una ruta absoluta y cambias de ordenador o carpeta?**
El código dejaría de funcionar porque la ruta absoluta hace referencia a un lugar fijo en un
disco, no es muy flexible.
Si el directorio no existe en el nuevo ordenador, el archivo no se encontraría.
Veríamos una IOException si intentamos abrirlo para leer

**Pregunta 2: ¿Por qué es importante usar rutas relativas en aplicaciones reales?**
Es importante usar las rutas relativas porque se adaptan al entorno en el que se ejecute la aplicación.
Si se distribuye la aplicación, los usuarios pueden instalarla en distintas carpetas o sistemas
operativos sin que tengamos que reconfigurar rutas.
Esto se hace para que el software sea más portable y fácil de mantener.

**Pregunta 3: ¿Qué ventaja tiene usar File.separator frente a escribir / o \\ directamente?**
File.separator adapta el separador de carpetas al sistema operativo:
En Linux / Mac es /
En Windows es \
Igualmente, el código funciona en cualquier sistema sin cambios.

**Extra (Para los Avanzados):**

- Sustituye System.out.println(...) por un log personalizado.
- Muestra el nombre del archivo con archivo.getName() .
- Usa archivo.getParent() para ver dónde está contenido.

Este es el código con los cambios realizados:

`package paquete;`
`import java.io.File;`
`public class RutaRelativa {`
`public static void main(String[] args) {`
`// Log personalizado`
`java.util.function.Consumer<String> log = (mensaje) -> {`
`System.out.println("[LOG] " + mensaje);`
`};`
`// Obtener ruta base del proyecto`
`String rutaBase = System.getProperty("user.dir");`
`String separador = File.separator;`
`// Construir ruta completa relativa`
`String rutaRelativa = rutaBase + separador + "datos" +`
`separador + "ejemplo.txt";`
`// Crear objeto File con esa ruta`
`File archivo = new File(rutaRelativa);`
`// Mostrar información`
`log.accept("Ruta base del proyecto: " + rutaBase);`
`log.accept("Separador de carpetas del sistema: " + separador);`
`log.accept("Ruta relativa completa: " + rutaRelativa);`
`log.accept("¿Existe el archivo? " + archivo.exists());`
`log.accept("Ruta absoluta real: " +`
`archivo.getAbsolutePath());`
`// Extras`
`log.accept("Nombre del archivo: " + archivo.getName());`
`log.accept("Carpeta contenedora: " + archivo.getParent());`
`}`
`}`

Esta es la ejecución del programa:

![[Pasted image 20251006202637.png]]
