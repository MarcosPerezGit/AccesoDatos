La lectura y escritura secuencial implica acceder a un archivo desde el principio hasta el final, en orden. Es el modo más habitual para trabajar con archivos de texto donde se procesan líneas completas o caracteres de forma lineal.

Java proporciona diferentes clases dentro del paquete **java.io** para realizar estas tareas de forma eficiente, utilizando buffers que reducen el acceso físico al disco y mejoran el rendimiento.

## Escritura secuencial de ficheros

Para escribir texto en un archivo utilizamos:

- `FileWriter`: conecta el programa con el archivo.
    
- `BufferedWriter`: permite escribir texto en bloque (más eficiente).

#### Ejemplo de Escritura Secuencial de Ficheros

![[Pasted image 20251006182220.png]]

`import java.io.*;`

`public class EscrituraFichero {`
    `public static void main(String[] args) {`
        `try {`
            `FileWriter fw = new FileWriter("datos/salida.txt");`
            `BufferedWriter bw = new BufferedWriter(fw);`

            `bw.write("Primera línea");`
            `bw.newLine();`
            `bw.write("Segunda línea");`
            `bw.newLine();`

            `bw.flush(); // Forzar la escritura`
            `bw.close(); // Cerrar el buffer`

            `System.out.println("Archivo escrito correctamente.");`
        `} catch (IOException e) {`
            `System.out.println("Error al escribir: " + e.getMessage());`
        `}`
    `}`
`}`
## Lectura secuencial de ficheros

Para leer archivos línea a línea:

- `FileReader`: abre el archivo.
    
- `BufferedReader`: permite leer una línea completa con `readLine()`.

#### Ejemplo de Lectura Secuencial de Ficheros

![[Pasted image 20251006182422.png]]

`import java.io.*;`

`public class LecturaFichero {`
    `public static void main(String[] args) {`
        `try {`
            `FileReader fr = new FileReader("datos/salida.txt");`
            `BufferedReader br = new BufferedReader(fr);`

            `String linea;`
            `while ((linea = br.readLine()) != null) {`
                `System.out.println("> " + linea);`
            `}`

            `br.close();`
        `} catch (IOException e) {`
            `System.out.println("Error al leer: " + e.getMessage());`
        `}`
    `}`
`}`
## Tips a tener en cuenta

- Siempre cerrar los streams (`close()`).
    
- Utilizar `try-catch` para capturar errores de entrada/salida.
    
- `flush()` garantiza que lo que está en el buffer se escribe en disco.
    
- Las rutas deben existir o crearse antes de escribir.
    

Usar `newLine()` en lugar de `\n` asegura compatibilidad multiplataforma.

## Tabla Aclarativa

|Clase|Función|
|---|---|
|`FileWriter`|Escribe caracteres en un archivo|
|`BufferedWriter`|Mejora el rendimiento usando un buffer|
|`FileReader`|Lee caracteres desde un archivo|
|`BufferedReader`|Permite leer líneas completas eficientemente|
