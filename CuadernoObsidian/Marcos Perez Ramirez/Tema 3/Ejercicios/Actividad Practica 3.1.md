#### **Instrucciones**
1. Crea una carpeta llamada datos en la raíz del proyecto.
2. Crea una clase Java llamada GestorFicheroTexto .
3. Implementa los siguientes pasos en el método main :
Escribe 3 líneas de texto en un archivo llamado datos/registro.txt .
Lee el contenido de ese archivo y muéstralo por consola.
Usa BufferedWriter y BufferedReader .

#### **Código de ejemplo para completar**

`import java.io.*;`
`public class GestorFicheroTexto {`
`public static void main(String[] args) {`
`try {`
`// Escritura`
`FileWriter fw = new FileWriter("datos/registro.txt");`
`BufferedWriter bw = new BufferedWriter(fw);`
`bw.write("Registro 1");`
`bw.newLine();`
`bw.write("Registro 2");`
`bw.newLine();`
`bw.write("Registro 3");`
`bw.newLine();`
`bw.flush();`
`bw.close();`
`System.out.println("Archivo escrito con éxito.");`
`// Lectura`
`FileReader fr = new FileReader("datos/registro.txt");`
`BufferedReader br = new BufferedReader(fr);`

`String linea;`
`System.out.println("Contenido del archivo:");`
`while ((linea = br.readLine()) != null) {`
`System.out.println("> " + linea);`
`}`
`br.close();`
`} catch (IOException e) {`
`System.out.println("Error: " + e.getMessage());`
`}`
`}`
`}`

#### **Preguntas de reflexión**

**Pregunta 1: ¿Qué ocurre si se vuelve a ejecutar el programa sin cambiar el nombre del archivo?**
Teniendo en cuenta que la clase se llama igual que el archivo nos muestra lo siguiente por consola.

**Pregunta 2: ¿Cómo podrías añadir texto sin borrar el contenido anterior?**
Si utilizamos el constructor de FileWriter con el segundo parámetro true podríamos hacerlo, de esta forma, cada ejecución del programa va sumando nuevas líneas al archivo en vez de borrarlo.

**Pregunta 3: ¿Qué diferencias observas si eliminas el BufferedWriter y usas solo FileWriter ?**
FileWriter escribe directamente en el archivo carácter a carácter lo que es muy ineficiente.
El BufferedWriter utiliza un buffer en memoria, agrupando los datos antes de escribirlos en el disco, lo que mejora el rendimiento.

**Pregunta 4: ¿Por qué es importante cerrar los buffers después de usarlos?**
Porque si no se cierran, se puede perder información y además dejamos recursos abiertos en el sistema, lo que puede provocarnos errores o consumo innecesario de memoria.

**Ejecución del Proyecto:** 

![[Pasted image 20251006205215.png]]
