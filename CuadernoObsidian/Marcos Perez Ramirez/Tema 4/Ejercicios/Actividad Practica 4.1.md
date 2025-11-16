**Instrucciones**
1. Crea una carpeta llamada datos en tu proyecto.
2. Crea una clase Java llamada AccesoAleatorioEjercicio .
3. Implementa un programa que:
Escriba tres registros con texto en un archivo binario usando writeUTF .
Lea y muestre el primer y segundo registro usando seek y readUTF .
Mida y muestre la posición del puntero antes y después de cada operación.

**Código de ejemplo para completar:**

`import java.io.*;`
`public class AccesoAleatorioEjercicio {`
`public static void main(String[] args) {`
`try {`
`// Escribir registros`
`RandomAccessFile raf = new`
`RandomAccessFile("datos/registros.dat", "rw");`
`raf.writeUTF("Registro 1");`
`raf.writeUTF("Registro 2");`
`raf.writeUTF("Registro 3");`
`raf.seek(0); // Volver al inicio`
`// Leer primer registro`
`System.out.println("Posición antes de leer 1: " +`
`raf.getFilePointer());`
`String r1 = raf.readUTF();`
`System.out.println("Registro 1: " + r1);`
`System.out.println("Posición después de leer 1: " +`
`raf.getFilePointer());`
`// Leer segundo registro`
`System.out.println("Posición antes de leer 2: " +`
`raf.getFilePointer());`
`String r2 = raf.readUTF();`

`System.out.println("Registro 2: " + r2);`
`System.out.println("Posición después de leer 2: " +`
`raf.getFilePointer());`
`raf.close();`
`} catch (IOException e) {`
`System.out.println("Error: " + e.getMessage());`
`}`
`}`
`}`

**Preguntas de reflexión**

**Pregunta 1: ¿Qué indica el valor que devuelve getFilePointer() ?**
Devuelve la posición actual del puntero dentro del archivo en bytes, es decir, marca donde será la próxima lectura o escritura.

**Pregunta 2: ¿Qué sucede si cambias el orden de lectura?**
Si intentamos leer el registro en un orden diferente sin mover el puntero con seek(), leerá datos incorrectos o provocará un error.

**Pregunta 3: ¿Por qué RandomAccessFile no es recomendable para archivos de texto plano sin estructura?**
Porque RandomAccessFile trabaja en modo binario y necesita una estructura fija o
delimitadores claros para saber dónde empieza y termina cada dato.
Un archivo de texto plano no tiene delimitadores binarios consistentes, por lo que resulta difícil posicionarse en un lugar exacto del archivo.

**Pregunta 4: ¿Cómo podrías modificar solo el tercer registro sin afectar los demás?**
Tendríamos que conocer la posición exacta del inicio del tercer registro utilizando el seek().

Esta es la ejecución del código:

![[Pasted image 20251006210218.png]]
