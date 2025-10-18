-- 1. Lista los nombres y los precios de todos los productos de la tabla producto

    SELECT nombre , precio FROM producto;
-- 2. Devuelve una lista de Producto completa con el precio de euros convertido a dólares .
    SELECT * , (precio * 1.17) + 'Precio en $' FROM producto;
-- 3. Lista los nombres y los precios de todos los productos, convirtiendo los nombres a mayúscula.
    SELECT UPPER(nombre), precio from producto;
-- 4. Lista el nombre de todos los fabricantes y a continuación en mayúsculas los dos primeros caracteres del nombre del fabricante.
    SELECT  nombre , UPPER(SUBSTR(nombre , 1 ,2)) FROM fabricante;
-- 5. Lista el código de los fabricantes que tienen productos.
    SELECT DISTINCT f.codigo from fabricante f inner join producto p on f.codigo = p.codigo_fabricante;
-- 6. Lista los nombres de los fabricantes ordenados de forma descendente.
    SELECT  f.nombre FROM fabricante f order by f.nombre desc;
-- 7. Lista los nombres de los productos ordenados en primer lugar por el nombre de forma ascendente y en segundo lugar por el precio de forma descendente.
    SELECT nombre , precio FROM producto order by nombre asc , precio desc;
-- 8. Devuelve una lista con los 5 primeros fabricantes.
    SELECT nombre FROM  fabricante limit 5;
-- 9. Devuelve una lista con 2 fabricantes a partir del cuarto fabricante. El cuarto fabricante también se debe incluir en la respuesta
    SELECT nombre FROM fabricante limit 3, 2;
-- 10. Lista el nombre y el precio del producto más baratos
    SELECT p.nombre FROM producto p order by p.precio asc limit 1;
-- 11. Lista el nombre y el precio del producto más caro
    SELECT p.nombre FROM producto p order by p.precio desc limit 1;
-- 12. Lista el nombre de todos los productos del fabricante cuyo código de fabricante es igual a 2.
    SELECT p.nombre FROM producto p WHERE codigo_fabricante = 2;
-- 13. Lista el nombre de los productos que tienen un precio menor o igual a 120€.
    SELECT nombre , precio FROM producto WHERE precio <= 120;

-- 14. Lista los productos que tienen un precio mayor o igual a 400€.
    SELECT  * FROM producto WHERE precio >= 400;
-- 15. Lista todos los productos que tengan un precio entre 80€ y 300€.
    SELECT  * FROM producto WHERE precio >=80 AND precio <=300;
-- 16. Lista todos los productos que tengan un precio mayor que 200€ y que el código de fabricante sea igual a 6.
    SELECT * FROM producto WHERE precio >= 200 AND codigo_fabricante = 6;
-- 17. Lista todos los productos donde el código de fabricante sea 1, 3 o 5 utilizando un Set de codigos de fabricantes para filtrar.
    SELECT * FROM producto WHERE codigo_fabricante IN (1,3,5);
-- 18. Lista el nombre y el precio de los productos en céntimos.
    SELECT nombre , (precio*100) + 'Precio en centimos' FROM producto;
-- 19. Lista los nombres de los fabricantes cuyo nombre empiece por la letra S
    SELECT nombre FROM fabricante WHERE nombre LIKE 'S%';
-- 20. Devuelve una lista con los productos que contienen la cadena Portátil en el nombre.
-- Con el LOWER distingue entre mayuscular y minusculas :
    SELECT nombre FROM producto WHERE LOWER(nombre) LIKE '%portatil%';
-- 21. Devuelve una lista con el nombre de todos los productos que contienen la cadena Monitor en el nombre y tienen un precio inferior a 215 €.
    SELECT nombre FROM producto WHERE LOWER(nombre) LIKE '%Monitor%' AND precio <= 215;
-- 22. Lista el nombre y el precio de todos los productos que tengan un precio mayor o igual a 180€. Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre (en orden ascendente).
    SELECT nombre , precio from producto WHERE precio >= 180 order by precio desc , nombre asc;
-- 23. Devuelve una lista con el nombre del producto, precio y nombre de fabricante de todos los productos de la base de datos. Ordene el resultado por el nombre del fabricante, por orden alfabético.
    SELECT p.nombre as nombre_producto , p.precio, f.nombre as nombre_fabricante FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo ORDER by f.nombre ASC;
-- 24. Devuelve el nombre del producto, su precio y el nombre de su fabricante, del producto más caro.
    SELECT p.nombre as nombre_producto , p.precio, f.nombre as nombre_fabricante FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo ORDER BY p.precio DESC limit 1;
-- 25. Devuelve una lista de todos los productos del fabricante Crucial que tengan un precio mayor que 200€.
    SELECT p.nombre as nombre_producto , p.precio , f.nombre as nombre_fabricante  FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo WHERE f.nombre = 'Crucial' AND p.precio >= 200;
-- 26. Devuelve un listado con todos los productos de los fabricantes Asus, Hewlett-Packard y Seagate.
    SELECT p.nombre as nombre_producto , f.nombre as nombre_fabricante FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo WHERE f.nombre = 'Asus' || f.nombre = 'Hewlett-Packard' || f.nombre = 'Seagate';
-- 27. Devuelve un listado con el nombre de producto, precio y nombre de fabricante, de todos los productos que tengan un precio mayor o igual a 180€. Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre.
    SELECT p.nombre as nombre_producto , p.precio , f.nombre as nombre_Fabricante FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo WHERE p.precio >= 180 ORDER BY p.precio DESC , p.nombre DESC;
-- 28. Devuelve un listado de los nombres fabricantes que existen en la base de datos, junto con los nombres productos que tiene cada uno de ellos. El listado deberá mostrar también aquellos fabricantes que no tienen productos asociados.
    SELECT f.nombre as nombre_Fabricante , p.nombre as nombre_producto FROM fabricante f LEFT JOIN producto p ON p.codigo_fabricante = f.codigo;
-- 29. Devuelve un listado donde sólo aparezcan aquellos fabricantes que no tienen ningún producto asociado.
    SELECT f.nombre as nombre_Fabricante FROM fabricante f LEFT JOIN producto p ON f.codigo = p.codigo_fabricante WHERE p.codigo_fabricante IS NULL;
-- 30. Calcula el número total de productos que hay en la tabla productos. Utiliza la api de stream.
    SELECT COUNT(*) AS total_prodcutos FROM producto;
-- 31. Calcula el número de fabricantes con productos, utilizando un stream de Productos.
    SELECT COUNT(DISTINCT f.codigo) AS fabricante_con_productos FROM fabricante f JOIN producto p ON f.codigo = p.codigo_Fabricante;
-- 32. Calcula la media del precio de todos los productos
    SELECT AVG(precio) as media_precios_productos FROM producto p;
-- 33. Calcula el precio más barato de todos los productos. No se puede utilizar ordenación de stream.
    SELECT nombre ,precio FROM producto p WHERE precio = (SELECT MIN(precio));
    -- Si solo queremos saber el precio mas barato :
        SELECT MIN(precio) AS precio_mas_bajo FROM producto;
-- 34. Calcula la suma de los precios de todos los productos.
    SELECT SUM(precio) as suma_Precios FROM producto;
-- 35. Calcula el número de productos que tiene el fabricante Asus.
    SELECT COUNT(p.codigo) As numero_productos FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo WHERE f.nombre = 'Asus';
-- 36. Calcula la media del precio de todos los productos del fabricante Asus.
    SELECT AVG(p.precio) AS media_precio_Asus FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo WHERE f.nombre = 'Asus';
-- 37. Muestra el precio máximo, precio mínimo, precio medio y el número total de productos que tiene el fabricante Crucial. Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".
    SELECT MAX(p.precio) as precio_Maximo,MIN(p.precio) as precio_Minimo, AVG(p.precio) as precio_Medio, COUNT(p.codigo_fabricante) as numero_Total_Productos FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo WHERE f.nombre = 'Crucial';
-- 38. Muestra el número total de productos que tiene cada uno de los fabricantes. El listado también debe incluir los fabricantes que no tienen ningún producto. El resultado mostrará dos columnas, una con el nombre del fabricante y otra con el número de productos que tiene. Ordene el resultado descendentemente por el número de productos.
    SELECT f.nombre as nombre_Fabricante , COUNT(ALL p.codigo_fabricante) as num_productos FROM fabricante f LEFT JOIN producto p ON f.codigo = p.codigo_fabricante GROUP BY nombre_Fabricante , f.codigo ORDER BY num_productos DESC;
-- 39. Muestra el precio máximo, precio mínimo y precio medio de los productos de cada uno de los fabricantes. El resultado mostrará el nombre del fabricante junto con los datos que se solicitan. Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".
    SELECT MAX(p.precio) as precio_Maximo,MIN(p.precio) as precio_Minimo, AVG(p.precio) as precio_Medio, f.nombre as nombre_Fabricante FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo GROUP BY nombre_Fabricante;
-- 40. Muestra el precio máximo, precio mínimo, precio medio y el número total de productos de los fabricantes que tienen un precio medio superior a 200€. No es necesario mostrar el nombre del fabricante, con el código del fabricante es suficiente.
    SELECT MAX(p.precio) as precio_Maximo,MIN(p.precio) as precio_Minimo, AVG(p.precio) as precio_Medio,COUNT(p.codigo) as total_Productos, f.codigo as codigo_Fabricante FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo  GROUP BY f.codigo HAVING AVG(p.precio) > 200;
-- 41. Devuelve un listado con los nombres de los fabricantes que tienen 2 o más productos.
    SELECT f.nombre as nombre_Fabricante , COUNT(p.codigo) as total_Productos FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo GROUP BY f.nombre HAVING total_Productos >= 2;
-- 42. Devuelve un listado con los nombres de los fabricantes y el número de productos que tiene cada uno con un precio superior o igual a 220 €. Ordenado de mayor a menor número de productos.
    SELECT f.nombre as nombre_Fabricante , COUNT(p.codigo) as cantidad_Producto FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo WHERE p.precio >= 220 GROUP BY nombre_Fabricante  ORDER BY cantidad_Producto DESC;
-- 43. Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €
    SELECT f.nombre as nombre_Fabricante , SUM(p.precio) as suma_Precios FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo GROUP BY nombre_Fabricante HAVING (suma_Precios > 1000);
-- 44. Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €. Ordenado de menor a mayor por cuantía de precio de los productos.
-- 45. Devuelve un listado con el nombre del producto más caro que tiene cada fabricante. El resultado debe tener tres columnas: nombre del producto, precio y nombre del fabricante. El resultado tiene que estar ordenado alfabéticamente de menor a mayor por el nombre del fabricante.
-- 46. Devuelve un listado de todos los productos que tienen un precio mayor o igual a la media de todos los productos de su mismo fabricante. Se ordenará por fabricante en orden alfabético ascendente y los productos de cada fabricante tendrán que estar ordenados por precio descendente.