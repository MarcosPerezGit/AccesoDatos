**Instrucciones**
1. Abre IntelliJ IDEA o tu entorno de desarrollo.
2. Crea un nuevo proyecto Java.
3. Dentro del proyecto, crea una carpeta llamada datos .
4. Crea una clase Java llamada GestionFicheros .
5. Copia y ejecuta el siguiente código.

**Código Java completo:**

`import java.io.File;`
`import java.io.IOException;`
`public class GestionFicheros {`
`public static void main(String[] args) {`
`try {`
`// 1. Crear la ruta al archivo datos/fichero.txt`
`File archivo = new File("datos/fichero.txt");`
`// 2. Si el archivo no existe, créalo`
`if (!archivo.exists()) {`
`archivo.getParentFile().mkdirs(); // Asegurar carpeta`
`archivo.createNewFile();`
`System.out.println("Archivo creado correctamente.");`
`} else {`
`System.out.println("El archivo ya existe.");`
`}`
`// 3. Mostrar información del archivo`
`System.out.println("Nombre: " + archivo.getName());`
`System.out.println("Ruta absoluta: " +`
`archivo.getAbsolutePath());`
`System.out.println("¿Se puede leer? " + archivo.canRead());`

`System.out.println("¿Se puede escribir? " +`
`archivo.canWrite());`
`System.out.println("¿Es un archivo? " + archivo.isFile());`
`// 4. Crear un nuevo directorio llamado datos/pruebas`
`File carpeta = new File("datos/pruebas");`
`if (!carpeta.exists()) {`
`carpeta.mkdir();`
`System.out.println("Carpeta creada.");`
`} else {`
`System.out.println("La carpeta ya existe.");`
`}`
`// 5. Listar contenido de la carpeta datos`
`File carpetaDatos = new File("datos");`
`File[] lista = carpetaDatos.listFiles();`
`System.out.println("Contenido de la carpeta 'datos':");`
`if (lista != null) {`
`for (File f : lista) {`
`System.out.println("- " + f.getName() +`
`(f.isDirectory() ? " (directorio)" : " (archivo)"));`
`}`
`}`
`} catch (IOException e) {`
`System.out.println("Error de entrada/salida: " +`
`e.getMessage());`
`}`
`}`
`}`

**Preguntas de reflexión:**

**Pregunta 1: ¿Qué ocurre si borras datos/fichero.txt y vuelves a ejecutar el programa?**
Que se me vuelve a crear el archivo txt junto con la carpeta datos.

**Pregunta 2: ¿Y si cambias los permisos del archivo para que no se pueda escribir?**
Lo que ocurrirá es que el método archivo.canWrite() devolverá un falso. Esto lo que hará será
que se lance una excepción de acceso denegado, porque le hemos denegado la escritura.

**Pregunta 3: ¿Por qué es importante comprobar si un archivo existe antes de crearlo?**
Para evitar sobrescribir un archivo que ya contenga información valiosa.
También evitamos errores de que el archivo existe etc...

**Pregunta 4: ¿Qué sucede si intentas crear un archivo en una ruta donde no existe la carpeta contenedora?**
Se lanzará una Exception, porque Java nos dice que la carpeta ya tiene que existir

**Este es el código modificado:**

`package paquete;`
`import java.io.File;`
`import java.io.FileWriter;`
`import java.io.IOException;`
`public class GestionFicheros {`
`public static void main(String[] args) {`
`try {`
`// 1. Crear la ruta al archivo datos/fichero.txt`
`File archivo = new File("datos/fichero.txt");`
`// 2. Si el archivo no existe, créalo (asegurando que`
`exista la carpeta)`
`if (!archivo.exists()) {`
`archivo.getParentFile().mkdirs(); // Asegurar carpeta`
`archivo.createNewFile();`
`System.out.println("Archivo creado correctamente.");`
`} else {`
`System.out.println("El archivo ya existe.");`
`}`
`// 3. Mostrar información del archivo`
`System.out.println("Nombre: " + archivo.getName());`
`System.out.println("Ruta absoluta: " +`
`archivo.getAbsolutePath());`
`System.out.println("¿Se puede leer? " +`
`archivo.canRead());`
`System.out.println("¿Se puede escribir? " +`
`archivo.canWrite());`
`System.out.println("¿Es un archivo? " + archivo.isFile());`

`// 🔹 Intento de escritura para comprobar permisos`
`(Pregunta 2)`
`if (archivo.canWrite()) {`
`FileWriter fw = new FileWriter(archivo, true); // modo`
`append`
`fw.write("Probando escritura en el archivo.\n");`
`fw.close();`
`System.out.println("Se pudo escribir en el archivo.");`
`} else {`
`System.out.println("⚠ No se puede escribir en este`
`archivo (permiso denegado).");`
`}`
`// 4. Crear un nuevo directorio llamado datos/pruebas`
`File carpeta = new File("datos/pruebas");`
`if (!carpeta.exists()) {`
`carpeta.mkdir();`
`System.out.println("Carpeta creada.");`
`} else {`
`System.out.println("La carpeta ya existe.");`
`}`
`// 5. Listar contenido de la carpeta datos`
`File carpetaDatos = new File("datos");`
`File[] lista = carpetaDatos.listFiles();`
`System.out.println("Contenido de la carpeta 'datos':");`
`if (lista != null) {`
`for (File f : lista) {`
`System.out.println("- " + f.getName() +`
`(f.isDirectory() ? " (directorio)" : "`
`(archivo)"));`
`}`
`}`
`} catch (IOException e) {`
`System.out.println("Error de entrada/salida: " +`
`e.getMessage());`
`}`
`}`
`}`

**Este es la ejecución del programa:**

![[Pasted image 20251006203335.png]]
