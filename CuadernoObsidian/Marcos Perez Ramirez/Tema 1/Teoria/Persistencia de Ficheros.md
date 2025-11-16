La **persistencia en ficheros** es una técnica fundamental que permite almacenar información de forma permanente en el sistema de archivos, fuera de la memoria volátil de la aplicación. A diferencia de la persistencia en bases de datos, esta se realiza directamente sobre ficheros locales, utilizando diferentes formatos como texto plano, binario o XML.

Esta técnica es especialmente útil cuando se necesita:

- Guardar datos entre ejecuciones de un programa.
    
- Registrar eventos o logs.
    
- Generar informes o respaldos.
    
- Trabajar con configuraciones o datos estructurados en disco.
## Tipos de Ficheros

- **Ficheros de texto**: uso de **FileWriter**, **BufferedWriter**, **FileReader**, **BufferedReader** para lectura y escritura de datos en formato legible.
    
- **Ficheros binarios**: uso de **DataInputStream**, **DataOutputStream** para almacenar datos en formato binario, ideal para mayor eficiencia o interoperabilidad.
    
- **Ficheros serializados**: uso de **ObjectOutputStream**, **ObjectInputStream** y clases **Serializable** para guardar objetos completos de Java.
    
- **Ficheros XML**: uso de **DocumentBuilder**, **Element**, **NodeList**, **XPath**, **Unmarshaller**, **Marshaller** para representar y procesar estructuras jerárquicas de datos con formato estándar y compatible.