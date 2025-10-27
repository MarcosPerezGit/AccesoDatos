**Para realizar este supuesto práctico créate primero un fichero de texto llamado productos.txt copiando y pegando este contenido:**

![[Pasted image 20251006210434.png]]

**El código de nuestro proyecto es el siguiente:**

`import java.util.Scanner;`
`import java.io.File;`
`import java.io.FileNotFoundException;`
`public class scanner {`
`public static void main(String[] args) {`
`try {`
`File archivo = new File("productos.txt");`
`if (archivo.exists()) {`
`System.out.println("El archivo existe y ocupa " +`

`archivo.length() + " bytes.");`

`} else {`
`System.out.println("El archivo no existe.");`
`return;`
`}`

`Scanner sc = new Scanner(archivo);`
`int totalArticulos = 0;`
`double sumaPrecios = 0;`
`double importeTotal = 0;`
`while (sc.hasNextLine()) {`
`String linea = sc.nextLine().trim();`
`if (linea.isEmpty()) continue;`
`String[] campos = linea.split(";");`
`String categoria = campos[0];`
`String nombre = campos[1];`
`double precio = Double.parseDouble(campos[2]);`
`int stock = Integer.parseInt(campos[3]);`
`System.out.printf("%s (%s) -- Precio: %.2f € -- Stock:`

`%d%n", nombre, categoria, precio, stock);`
`totalArticulos++;`
`sumaPrecios += precio;`
`importeTotal += precio * stock;`
`}`
`sc.close();`
`double promedioPrecios = sumaPrecios / totalArticulos;`
`System.out.println("\n--- Estadísticas ---");`
`System.out.println("Número total de artículos: " +`

`totalArticulos);`

`System.out.printf("Promedio de precios: %.2f €%n",`

`promedioPrecios);`

`System.out.printf("Importe total (precio*stock): %.2f €%n",`

`importeTotal);`
`} catch (FileNotFoundException e) {`
`System.out.println("Error: archivo no encontrado.");`
`}`
`}`
`}`

**Ejecución del código:**
![[Pasted image 20251006210554.png]]

![[Pasted image 20251006210705.png]]

