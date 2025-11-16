### 1. ¿Qué es un fichero?

Un fichero (o archivo) es una **secuencia de bytes almacenada de forma persistente** en un dispositivo de almacenamiento. Se identifica mediante una ruta o «path», y puede contener cualquier tipo de información.

Los ficheros son gestionados por el **sistema de archivos** del sistema operativo, el cual se encarga de:

- Asignar una ruta y un nombre único dentro de un directorio.
    
- Proteger el acceso concurrente mediante permisos.
    
- Garantizar la integridad de los datos.

## Tipos de Acceso a la Información

- **Acceso Secuencial:** Este es el modo más **básico y común** de acceder a ficheros de texto. Se lee el archivo **desde el principio hasta el final**, **línea a línea o carácter a carácter**, en orden.
##### Ventajas:

- Simple de implementar.
    
- Ideal para archivos pequeños o de lectura completa (como logs, listas, etc).
    

##### Inconvenientes:

- No se puede «saltar» directamente a una línea concreta.
    
- Poco eficiente si solo necesitas una parte específica del archivo.

Ejemplo de Lectura Secuencial en Java:

![[Pasted image 20251006165314.png]]

`import java.io.*;`

`public class LecturaSecuencial {`
    `public static void main(String[] args) throws IOException {`
        `BufferedReader br = new BufferedReader(new FileReader("datos.txt"));`
        `String linea;`

        `while ((linea = br.readLine()) != null) {`
            `System.out.println(linea);`
        `}`

        `br.close();`
    `}`
`}`

**Acceso Aleatorio:** Este modo permite **acceder directamente a una posición específica del archivo**, sin tener que recorrerlo desde el principio.

Para ello se usa la clase **RandomAccessFile**, que funciona como una combinación de lector y escritor. Se puede mover el «puntero» a cualquier parte del archivo con **.seek(posicion)**.

##### Ventajas:

- Muy útil en estructuras con **registros de tamaño fijo**.
    
- Permite **lectura y escritura en cualquier parte del archivo**.
    

##### Inconvenientes:

- Solo se puede usar en ficheros de tipo binario o texto con estructuras muy controladas.
    
- Requiere conocer la estructura exacta del contenido.
    

Ejemplo de Lectura Aleatoria en Java:

![[Pasted image 20251006165929.png]]

`import java.io.*;`

`public class AccesoAleatorio {`
    `public static void main(String[] args) throws IOException {`
        `RandomAccessFile raf = new RandomAccessFile("datos.txt", "rw");`

        `raf.seek(20); // Mover el puntero a la posición 20 (byte 20)`
        `String linea = raf.readLine(); // Leer desde ahí`
        `System.out.println("Contenido desde byte 20: " + linea);`

        `raf.close();`
    `}`
`}`
#### Diferencias Entre Los Tipos de Acceso A La Información

|Característica|Acceso Secuencial|Acceso Aleatorio (`RandomAccessFile`)|
|---|---|---|
|Tipo de lectura|Desde el inicio hasta el final|Desde una posición específica|
|Lectura parcial|❌ No directa|✅ Sí|
|Escritura|✅ Sí, al final normalmente|✅ Sí, en cualquier posición|
|Clase principal|`BufferedReader`, `Scanner`|`RandomAccessFile`|
|Ideal para|Archivos de texto|Archivos binarios o con registros fijos|
`// Acceso secuencial`
`BufferedReader br = new BufferedReader(new FileReader("datos.txt"));`
`String linea;`
`while ((linea = br.readLine()) != null) {`
    `System.out.println(linea);`
`}`
`br.close();`

`// Acceso aleatorio`
`RandomAccessFile raf = new RandomAccessFile("datos.txt", "rw");`
`raf.seek(20); // ir al byte 20`
`String linea = raf.readLine();`
`raf.close();`


## Sistema de Ficheros y Rutas

Una **ruta** es el camino que el sistema operativo necesita para acceder a un archivo o carpeta.

En Java, al trabajar con ficheros, es necesario indicar la **ruta completa** del archivo al que queremos acceder, ya sea para **leerlo**, **escribirlo**, **modificarlo** o **eliminarlo**.

#### Tipos de rutas en Java

 **Absolutas**: especifican todo el camino desde la raíz del sistema.

Es la ruta **completa** desde el origen del sistema de archivos (la raíz).

- En **Windows**:
    
    `C:\\Users\\Marcos\\Documentos\\datos.txt`
    
- En **Linux/Mac**:
    
    `/home/marcos/documentos/datos.txt`
    

**Ventaja**: Es muy precisa.

**Desventaja**: No es portable entre sistemas ni usuarios.

 **Relativas**: se refieren a la ubicación del archivo respecto al directorio actual del programa.

Es la ruta en relación al **directorio actual del proyecto Java**.

Por ejemplo, si nuestro proyecto está en:

C:/Users/Marcos/IdeaProjects/AccesoADatos/

Y usamos la ruta relativa:

"datos/alumnos.txt"

Java buscará el archivo en:

C:/Users/Madrid/IdeaProjects/AccesoADatos/datos/alumnos.txt

**Ventaja**: Es portable entre sistemas y ordenadores.

**Desventaja**: Puede ser difícil de localizar si no se sabe cuál es el directorio actual.

#### Cómo obtener el directorio actual del proyecto

`String base = System.getProperty("user.dir");`
`System.out.println("Ruta base del proyecto: " + base);`

#### Portabilidad con File.separator.

Los separadores de carpetas son diferentes según el sistema operativo:

- Windows → `\\`
    
- Linux/Mac → `/`
    

En lugar de escribirlo manualmente, usamos:

`String separador = File.separator;`

Ejemplo de la Portabilidad  con FIle.separator.

![[Pasted image 20251006170544.png]]

`import java.io.File;`

`public class RutasEjemplo {`
    `public static void main(String[] args) {`
        `String base = System.getProperty("user.dir");`
        `String sep = File.separator;`

        `String rutaAbsoluta = base + sep + "datos" + sep + "ejemplo.txt";`
        `System.out.println("Ruta absoluta generada: " + rutaAbsoluta);`

        `File archivo = new File(rutaAbsoluta);`
        `System.out.println("¿Existe el archivo? " + archivo.exists());`
    `}`
`}`

#### Diferencias Entre Acceso Secuencial VS Aleatorio

|Característica|Acceso Secuencial|Acceso Aleatorio|
|---|---|---|
|Tipo de uso|Lectura lineal, texto|Datos binarios estructurados|
|Clases usadas|FileReader, BufferedReader|RandomAccessFile|
|Velocidad de lectura|Lenta para datos al final|Alta (si conoces la posición)|
|Modificación|Reescribir todo el archivo|Modificar por posiciones|
|Uso de punteros|No|Sí (`seek()` y `getFilePointer()`)|
|Consumo de memoria|Bajo|Moderado|

