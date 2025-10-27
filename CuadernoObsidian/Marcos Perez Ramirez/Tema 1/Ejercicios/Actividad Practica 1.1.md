#### Programa 1: Acceso secuencial con BufferedReader

`import java.io.*;`
`public class LecturaSecuencial {`
`public static void main(String[] args) {`
`try {`
`File archivo = new File(&quot;datos.txt&quot;);`
`BufferedReader br = new BufferedReader(new`
`FileReader(archivo));`
`String linea;`
`System.out.println(&quot;Lectura completa del archivo (modo`
`secuencial):&quot;);`
`while ((linea = br.readLine()) != null) {`
`System.out.println(&quot;&gt; &quot; + linea);`
`}`
`br.close();`
`} catch (IOException e) {`
`System.out.println(&quot;Error al leer el archivo: &quot; +`
`e.getMessage());`
`}`
`}`
`}`

El resultado de la ejecución del programa es:

![[Pasted image 20251006195550.png]]

#### Programa 2: Acceso aleatorio con RandomAccessFile

`import java.io.*;`
`public class AccesoAleatorio {`
`public static void main(String[] args) {`
`try {`
`RandomAccessFile raf = new RandomAccessFile(&quot;datos.txt&quot;,`
`&quot;r&quot;);`
`// Cambia el valor para probar: 0, 10, 15, 30, etc.`
`long posicion = 15;`
`raf.seek(posicion); // Mover el puntero al byte 15`
`System.out.println(&quot;Lectura desde byte &quot; + posicion + &quot;:&quot;);`
`String linea = raf.readLine(); // Leer desde esa posición`
`System.out.println(&quot;&gt; &quot; + linea);`
`raf.close();`
`} catch (IOException e) {`
`System.out.println(&quot;Error en acceso aleatorio: &quot; +`
`e.getMessage());`
`}`
`}`
`}`

Los resultados cambian dependiendo del byte que seleccionemos utilizando seek().
Seek(0): 

![[Pasted image 20251006195735.png]]

Seek(10): 

![[Pasted image 20251006195759.png]]

Seek(15): 

![[Pasted image 20251006195818.png]]

Seek(30):

![[Pasted image 20251006195840.png]]

#### ¿Aparece una línea cortada?

Si, aparece una línea cortada cuando utilizamos seek(15).

#### ¿Por qué cambia el resultado según el valor de seek()?

Porque seek() mueve el puntero de lectura a un byte específico del archivo.
Cuando nosotros ponemos seek(0) estamos leyendo toda la linea porque te encuentras en el inicio. Si nosotros utilizamos el byte que se encuentra en la mitad de la cadena, en este caso seek(15), leerá solo desde el punto que está apuntando hasta el final de la línea.
