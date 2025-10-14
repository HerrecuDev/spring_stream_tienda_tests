package org.iesvdm.tienda;


import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.ast.SqlTreeCreationException;
import org.iesvdm.tienda.modelo.Fabricante;
import org.iesvdm.tienda.modelo.Producto;
import org.iesvdm.tienda.repository.FabricanteRepository;
import org.iesvdm.tienda.repository.ProductoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.*;


@Slf4j
@SpringBootTest
class TiendaApplicationTests {

	@Autowired
	FabricanteRepository fabRepo;

	@Autowired
	ProductoRepository prodRepo;
    private HikariDataSource dataSource2;

    @Test
	void testAllFabricante() {
		var listFabs = fabRepo.findAll();

		listFabs.forEach(f -> {
			System.out.println(">>"+f+ ":");
			f.getProductos().forEach(System.out::println);
		});
	}

	@Test
	void testAllProducto() {
		var listProds = prodRepo.findAll();

		listProds.forEach( p -> {
			System.out.println(">>"+p+":"+"\nProductos mismo fabricante "+ p.getFabricante());
			p.getFabricante().getProductos().forEach(pF -> System.out.println(">>>>"+pF));
		});

	}


	/**
	 * 1. Lista los nombres y los precios de todos los productos de la tabla producto
	 */
	@Test
	void test1() {
		var listProds = prodRepo.findAll();

		var listNomPrec = listProds.stream()
				.map(s -> "nombre" + s.getNombre()+" Precio "+s.getPrecio())
				.toList();


		listNomPrec.forEach(
			x -> System.out.println(x)
		);

		Assertions.assertEquals(11, listNomPrec.size());
		Assertions.assertTrue(listNomPrec.contains("Nombre Disco duro"));
	}


	/**
	 * 2. Devuelve una lista de Producto completa con el precio de euros convertido a dólares .
	 */
	@Test
	void test2() {
		var listProds = prodRepo.findAll();
		//EL var lisPrecios = List<String> listPrecios.
			var listPrecios = listProds.stream()
									.map(p -> p.getNombre()
									+ "con precio : "
									+ BigDecimal.valueOf(p.getPrecio() * 1.08)
												.setScale(2, RoundingMode.HALF_UP)
									+ "$")
									.toList();
												//Imprimimos el Streams :
			listPrecios.forEach(s -> System.out.println(s));


            Assertions.assertEquals(11, listPrecios.size());
	}

	/**
	 * 3. Lista los nombres y los precios de todos los productos, convirtiendo los nombres a mayúscula.
	 */
	@Test
	void test3() {
		var listProds = prodRepo.findAll();

		var lista = listProds.stream() //Producto
				.map(p -> p.getNombre().toUpperCase() + " precio =  " + p.getPrecio()) //String
				.toList();


        lista.forEach(x -> System.out.println(x));
        Assertions.assertEquals(11 , listProds.size());

	}

	/**
	 * 4. Lista el nombre de todos los fabricantes y a continuación en mayúsculas los dos primeros caracteres del nombre del fabricante.
	 */
	@Test
	void test4() {
		var listFabs = fabRepo.findAll();

        var listanueva = listFabs.stream()
                .map(f -> f.getNombre()+ " " + f.getNombre().toUpperCase().substring(0,2))
                .toList();

        listanueva.forEach(System.out::println);

        Assertions.assertEquals(9 ,listFabs.size());

	}

	/**
	 * 5. Lista el código de los fabricantes que tienen productos.
	 */
	@Test
	void test5() {
		var listFabs = fabRepo.findAll();

           List<Integer> listCode =  listFabs.stream()
                    .filter(fabricante -> fabricante.getProductos() != null
                    && !fabricante.getProductos().isEmpty())
                    .map( c -> c.getCodigo())
                    .toList();

           listCode.forEach(c-> System.out.println(c));

           Assertions.assertEquals(7 , listCode.size());
	}

	/**
	 * 6. Lista los nombres de los fabricantes ordenados de forma descendente.
	 */
	@Test
	void test6() {
		var listFabs = fabRepo.findAll();

        var listOrdenadas = listFabs.stream()
                .sorted(comparing((Fabricante f) -> f.getNombre(), reverseOrder() ))
                .map(f -> f.getNombre())
                .toList();

        listOrdenadas.forEach(x -> System.out.println(x));


        Assertions.assertEquals(11 , listOrdenadas.size());
	}

	/**
	 * 7. Lista los nombres de los productos ordenados en primer lugar por el nombre de forma ascendente y en segundo lugar por el precio de forma descendente.
	 */
	@Test
	void test7() {
		var listProds = prodRepo.findAll();

        var listaAscendente = listProds.stream()
                .sorted(comparing((Producto p)-> p.getNombre())

                        .thenComparing((Producto p)->p.getPrecio(), reverseOrder()))
                        .map(p -> p.getNombre() + " " + p.getPrecio())
                        .toList();

        listaAscendente.forEach(x -> System.out.println(x));

        Assertions.assertEquals(11, listaAscendente.size());
	}

	/**
	 * 8. Devuelve una lista con los 5 primeros fabricantes.
	 */
	@Test
	void test8() {
		var listFabs = fabRepo.findAll();

        var lista5_primeros = listFabs.stream()

                .limit(5)
                .map(fabricante -> fabricante.getNombre())
                .toList();


        lista5_primeros.forEach(x -> System.out.println(x));
        Assertions.assertEquals(5, lista5_primeros.size());
	}

	/**
	 * 9.Devuelve una lista con 2 fabricantes a partir del cuarto fabricante. El cuarto fabricante también se debe incluir en la respuesta.
	 */
	@Test
	void test9() {
		var listFabs = fabRepo.findAll();

        var lista_2_Fabricantes = listFabs.stream()
                .skip(3)
                .limit(2)
                .map(f -> f.getNombre())
                .toList();

        lista_2_Fabricantes.forEach( x -> System.out.println(x));
        Assertions.assertEquals(2, lista_2_Fabricantes.size());

	}

	/**
	 * 10. Lista el nombre y el precio del producto más barato
	 */
	@Test
	void test10() {
		var listProds = prodRepo.findAll();

        var producto_Barato = listProds.stream()
                .sorted(comparing((Producto p)-> p.getPrecio()))
                .map(producto -> producto.getNombre() + " con precio : " + producto.getPrecio())
                .limit(1) // o usar el metodo findFirst().
                .toList();

        producto_Barato.forEach(x -> System.out.println(x));
        Assertions.assertEquals(1,producto_Barato.size());

	}

	/**
	 * 11. Lista el nombre y el precio del producto más caro
	 */
	@Test
	void test11() {
		var listProds = prodRepo.findAll();
    //Para usar el findFirst declramos el var como Optional<>;
       /* var producto_mas_Caro = listProds.stream()
                .sorted(comparing((Producto p)-> p.getPrecio() , reverseOrder()))
                .map(producto -> producto.getNombre() + " con precio :" + producto.getPrecio())
                .limit(1)// o usar el metodo findFirst().
                .toList();
*/

        //Forma del profesor :
        Optional<Producto> productoCaro = listProds.stream()
                        .sorted(
                                comparing(producto -> producto.getPrecio() ,reverseOrder())
                        ).findFirst();

        if (productoCaro.isPresent()){

            Producto prod = productoCaro.get();

            System.out.println(prod.getNombre() + " " + prod.getPrecio());
        }


        productoCaro.ifPresent(x -> System.out.println(x.getNombre() + " " + x.getPrecio()));

        productoCaro.ifPresent(producto -> System.out.println(producto.getNombre() + " " + producto.getPrecio()));


	}

	/**
	 * 12. Lista el nombre de todos los productos del fabricante cuyo código de fabricante es igual a 2.
	 *
	 */
	@Test
	void test12() {
		var listProds = prodRepo.findAll();

            var listaCodigo2 = listProds.stream()
                    .filter(p -> p.getFabricante().getCodigo() == 2)
                    .map(producto -> producto.getNombre() + " Tiene el codigo 2 de Fabricante")

                    .toList();


            listaCodigo2.forEach(x -> System.out.println(x));
	}

	/**
	 * 13. Lista el nombre de los productos que tienen un precio menor o igual a 120€.
	 */
	@Test
	void test13() {
		var listProds = prodRepo.findAll();

        var preciosmenor120 = listProds.stream()

                .filter(p -> p.getPrecio() <= 120 )
                .map(p -> p.getNombre() + " Precio = " + p.getPrecio())
                .toList();

        preciosmenor120.forEach(x -> System.out.println(x));

        Assertions.assertEquals(3 , preciosmenor120.size());

	}

	/**
	 * 14. Lista los productos que tienen un precio mayor o igual a 400€.
	 */
	@Test
	void test14() {
		var listProds = prodRepo.findAll();

        var listaproductos = listProds.stream()
                .filter(p -> p.getPrecio() >= 400)
                .map( p -> p.getNombre() + "Precio = " + p.getPrecio())
                .toList();

        listaproductos.forEach(x -> System.out.println(x));

        Assertions.assertEquals(3 , listaproductos.size());
	}

	/**
	 * 15. Lista todos los productos que tengan un precio entre 80€ y 300€.
	 */
	@Test
	void test15() {
		var listProds = prodRepo.findAll();

        var listaProductosentre = listProds.stream()
                .sorted(comparing((Producto p) -> p.getPrecio() , reverseOrder()))
                .filter(producto -> producto.getPrecio() >= 80 && producto.getPrecio() <= 300)
                .map(p -> p.getNombre() + " , su precio es = " + p.getPrecio())
                .toList();

        listaProductosentre.forEach(z -> System.out.println(z));

        Assertions.assertEquals(7 , listaProductosentre.size());
	}

	/**
	 * 16. Lista todos los productos que tengan un precio mayor que 200€ y que el código de fabricante sea igual a 6.
	 */
	@Test
	void test16() {
		var listProds = prodRepo.findAll();

        var listporcodigoFab = listProds.stream()
                .filter(producto -> producto.getPrecio() >= 200 && producto.getFabricante().getCodigo() == 6)
                .map(p -> p.getNombre() + " y el codigo del fabricante = " + p.getFabricante().getCodigo())
                .toList();

        listporcodigoFab.forEach(x -> System.out.println(x));
        Assertions.assertEquals(1 , listporcodigoFab.size());
	}

	/**
	 * 17. Lista todos los productos donde el código de fabricante sea 1, 3 o 5 utilizando un Set de codigos de fabricantes para filtrar.
	 */
	@Test
	void test17() {
		var listProds = prodRepo.findAll();
        Set<Integer> codigosAbuscar = Set.of(1,3,5);
        var listaporCodigos = listProds.stream()
                .filter(p -> p.getFabricante() != null && codigosAbuscar.contains(p.getFabricante().getCodigo()))
                .map(p -> p.getNombre() + " tiene asignado el codigo solicitado = " + p.getFabricante().getCodigo())
                .toList();

        listaporCodigos.forEach(x -> System.out.println(x));
        Assertions.assertEquals(5 , listaporCodigos.size());
	}

	/**
	 * 18. Lista el nombre y el precio de los productos en céntimos.
	 */
	@Test
	void test18() {
		var listProds = prodRepo.findAll();

        var listaProductosCent = listProds.stream()
                .map(p -> p.getNombre() + " su precio en centimos = " + (p.getPrecio()*100) + "€")
                .toList();

            listaProductosCent.forEach(x -> System.out.println(x));

            Assertions.assertEquals(11 , listaProductosCent.size());
	}


	/**
	 * 19. Lista los nombres de los fabricantes cuyo nombre empiece por la letra S
	 */
	@Test
	void test19() {
		var listFabs = fabRepo.findAll();

        var listaEmpiezaS = listFabs.stream()
                .filter(f -> f.getNombre().startsWith("S"))
                .map(f -> f.getNombre())
                .toList();

        listaEmpiezaS.forEach(x -> System.out.println(x));

        Assertions.assertEquals(2 , listaEmpiezaS.size());
	}

	/**
	 * 20. Devuelve una lista con los productos que contienen la cadena Portátil en el nombre.
	 */
	@Test
	void test20() {
		var listProds = prodRepo.findAll();

        var listaPortatiles = listProds.stream()
                .filter(p -> p.getNombre().contains("Portátil"))
                .map(p -> p.getNombre() + " - Contiene la palabra ")
                .toList();

        listaPortatiles.forEach(x -> System.out.println(x));
        Assertions.assertEquals(2, listaPortatiles.size());
	}

	/**
	 * 21. Devuelve una lista con el nombre de todos los productos que contienen la cadena Monitor en el nombre y tienen un precio inferior a 215 €.
	 */
	@Test
	void test21() {
		var listProds = prodRepo.findAll();

            var listaMonitores = listProds.stream()
                    .filter(p-> p.getNombre().contains("Monitor") && p.getPrecio() <= 215)
                    .map(p->p.getNombre() + " Contiene la palabra Monitor y su precio es INFERIOR a 215€ " + p.getPrecio())
                    .toList();

            listaMonitores.forEach(x -> System.out.println(x));

            Assertions.assertEquals(1, listaMonitores.size());

	}

	/**
	 * 22. Lista el nombre y el precio de todos los productos que tengan un precio mayor o igual a 180€.
	 * Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre (en orden ascendente).
	 */
    @Test
	void test22() {
		var listProds = prodRepo.findAll();

            var listoProductos = listProds.stream()
                    .sorted(comparing((Producto p)-> p.getPrecio() , reverseOrder()))
                    .sorted(comparing((Producto p)-> p.getNombre()))
                    .filter(p -> p.getPrecio() >= 180)
                    .map(p-> p.getNombre() + " tiene un precio de " + p.getPrecio())
                    .toList();

            listoProductos.forEach(x -> System.out.println(x));
            Assertions.assertEquals(7 , listoProductos.size());

	}

	/**
	 * 23. Devuelve una lista con el nombre del producto, precio y nombre de fabricante de todos los productos de la base de datos.
	 * Ordene el resultado por el nombre del fabricante, por orden alfabético.
	 */
	@Test
	void test23() {
		var listProds = prodRepo.findAll();

        var listaFabOrdenadaAlfabeticamente = listProds.stream()

                .sorted(Comparator.comparing((Producto p)-> p.getFabricante().getNombre() ,reverseOrder()))
                .map(p-> p.getNombre() + " con precio = " + p.getPrecio() + " y fabricante = " + p.getFabricante().getNombre())
                .toList();

        listaFabOrdenadaAlfabeticamente.forEach(x -> System.out.println(x));

        Assertions.assertEquals(11 , listaFabOrdenadaAlfabeticamente.size());

	}

	/**
	 * 24. Devuelve el nombre del producto, su precio y el nombre de su fabricante, del producto más caro.
	 */
	@Test
	void test24() {
		var listProds = prodRepo.findAll();

            var productoMascaro = listProds.stream()
                    .sorted(comparing((Producto p)-> p.getPrecio() ,reverseOrder()))
                    .limit(1)
                    .map(p->p.getNombre() + " con el precio mas caro = " +  p.getPrecio() + " y nombre de Fabricante = " + p.getFabricante().getNombre())
                    .toList();

            productoMascaro.forEach(z -> System.out.println(z));
            Assertions.assertEquals(1 , productoMascaro.size());
	}

	/**
	 * 25. Devuelve una lista de todos los productos del fabricante Crucial que tengan un precio mayor que 200€.
	 */
	@Test
	void test25() {
		var listProds = prodRepo.findAll();
		    var listaFabCrucial = listProds.stream()
                    .filter(p -> p.getFabricante().getNombre().equals("Crucial") && p.getPrecio() >= 200)
                    .map(p -> p.getNombre() + " tiene un precio de " + p.getPrecio() + "€" + " y su fabricante es " + p.getFabricante().getNombre())
                    .toList();


            listaFabCrucial.forEach(z-> System.out.println(z));

	}

	/**
	 * 26. Devuelve un listado con todos los productos de los fabricantes Asus, Hewlett-Packard y Seagate
	 */
	@Test
	void test26() {
		var listProds = prodRepo.findAll();

        var listaProdSeleccionados = listProds.stream()
                .filter(p -> p.getFabricante().getNombre().equals("Asus")
                        || p.getFabricante().getNombre().equals("Hewlett-Packard")
                        || p.getFabricante().getNombre().equals("Seagate")
                )
                .map(p -> p.getNombre() + " su fabricante es = " + p.getFabricante().getNombre())
                .toList();

        listaProdSeleccionados.forEach(z -> System.out.println(z));
        Assertions.assertEquals(5 , listaProdSeleccionados.size());
	}

	/**
	 * 27. Devuelve un listado con el nombre de producto, precio y nombre de fabricante, de todos los productos que tengan un precio mayor o igual a 180€.
	 * Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre.
	 * El listado debe mostrarse en formato tabla. Para ello, procesa las longitudes máximas de los diferentes campos a presentar y compensa mediante la inclusión de espacios en blanco.
	 * La salida debe quedar tabulada como sigue:

Producto                Precio             Fabricante
-----------------------------------------------------
GeForce GTX 1080 Xtreme|611.5500000000001 |Crucial
Portátil Yoga 520      |452.79            |Lenovo
Portátil Ideapd 320    |359.64000000000004|Lenovo
Monitor 27 LED Full HD |199.25190000000003|Asus

	 */
	@Test
	void test27() {
		var listProds = prodRepo.findAll();
		    var listaCompletaEntabla = listProds.stream()
                    .filter(p -> p.getPrecio() >= 180)
                    .sorted(Comparator
                            .comparing(Producto :: getPrecio).reversed()
                            .thenComparing(Producto::getNombre))

                    .toList();

            //Para calcular las longitudes maximas de cada columna:
            int maxNombre = listaCompletaEntabla.stream()
                    .mapToInt(p -> p.getNombre().length())
                    .max().orElse(10);

            int maxPrecio = listaCompletaEntabla.stream()
                    .mapToInt( p-> String .format(".2f", p.getPrecio()).length())
                    .max().orElse(6);

            int maxFabricante = listaCompletaEntabla.stream()
                    .mapToInt(p -> p.getFabricante().getNombre().length())
                    .max().orElse(10);

            //Encabezado :

            String header = String.format(
                    "%-" + maxNombre + "s | %" + maxPrecio + "s | %" + maxFabricante + "s" ,
                    "PRODUCTO" , "PRECIO (€)" , "FABRICANTE"
            );

        System.out.println(header);
        System.out.println("-".repeat(header.length()));


        //Ahora para realiar las Filas:
        listaCompletaEntabla.forEach( p-> {
            String fila =  String.format(
                    "%-" +maxNombre + "s | %" + maxPrecio + ".2f | %-" + maxFabricante + "s" ,
                    p.getNombre(),
                    p.getPrecio(),
                    p.getFabricante().getNombre()
            );
            System.out.println(fila);

        });


	}

	/**
	 * 28. Devuelve un listado de los nombres fabricantes que existen en la base de datos, junto con los nombres productos que tiene cada uno de ellos.
	 * El listado deberá mostrar también aquellos fabricantes que no tienen productos asociados.
	 * SÓLO SE PUEDEN UTILIZAR STREAM, NO PUEDE HABER BUCLES
	 * La salida debe queda como sigue:
Fabricante: Asus

            	Productos:
            	Monitor 27 LED Full HD
            	Monitor 24 LED Full HD

Fabricante: Lenovo

            	Productos:
            	Portátil Ideapd 320
            	Portátil Yoga 520

Fabricante: Hewlett-Packard

            	Productos:
            	Impresora HP Deskjet 3720
            	Impresora HP Laserjet Pro M26nw

Fabricante: Samsung

            	Productos:
            	Disco SSD 1 TB

Fabricante: Seagate

            	Productos:
            	Disco duro SATA3 1TB

Fabricante: Crucial

            	Productos:
            	GeForce GTX 1080 Xtreme
            	Memoria RAM DDR4 8GB

Fabricante: Gigabyte

            	Productos:
            	GeForce GTX 1050Ti

Fabricante: Huawei

            	Productos:


Fabricante: Xiaomi

            	Productos:

	 */
	@Test
	void test28() {
		var listFabs = fabRepo.findAll();
        var listaProds = prodRepo.findAll();
            listFabs.stream()
                    .forEach(fabricante ->{

                        System.out.println("Fabricante : " + fabricante.getNombre());
                        System.out.println("\n\t Productos :");

                        listaProds.stream()
                                .filter(p-> p.getFabricante().getCodigo() == fabricante.getCodigo())
                                .map(p-> "\t" + p.getNombre())
                                .forEach(System.out::println);
                        System.out.println();
                        System.out.println("-----------------------------------------------------------------------------");

                    });


            //Formato 2 de hacerlo :
        /*
        var formato = listFabs.stream()
                .map(fabricante -> {

                    var productosFabricante = listaProds.stream()
                            .filter(p -> p.getFabricante().getCodigo() == fabricante.getCodigo())
                            .map(p -> p.getNombre())
                            .toList();

                    return "Fabricante : " + fabricante.getNombre() + " Productos : " + productosFabricante;

                })
                .toList();

        formato.forEach(s -> System.out.println(s));
        Assertions.assertEquals(9 , formato.size());
            */
	}

	/**
	 * 29. Devuelve un listado donde sólo aparezcan aquellos fabricantes que no tienen ningún producto asociado.
	 */
	@Test
	void test29() {
		var listFabs = fabRepo.findAll();

        var listaFabricantesSinProductos = listFabs.stream()
                .filter(fabricante -> fabricante.getProductos().isEmpty())
                .map(f -> "Fabricante : " + f.getNombre())
                .toList();

        listaFabricantesSinProductos.forEach(s-> System.out.println(s));
        Assertions.assertEquals(2 , listaFabricantesSinProductos.size());
	}

	/**
	 * 30. Calcula el número total de productos que hay en la tabla productos. Utiliza la api de stream.
	 */
	@Test
	void test30() {
		var listProds = prodRepo.findAll();

        var totalProdcutos = listProds.stream()
                .map(p -> p.getNombre())
                .count();

        System.out.println("El numero total de productos que hay en la tabla productos es = " + totalProdcutos);
        Assertions.assertEquals(11, totalProdcutos);

	}


	/**
	 * 31. Calcula el número de fabricantes con productos, utilizando un stream de Productos.
	 */
	@Test
	void test31() {
		var listProds = prodRepo.findAll();

        var numeroFabricantesconProd = listProds.stream()

                .map(producto -> producto.getFabricante().getCodigo())
                .distinct()
                .count();

        System.out.println("El número de fabricantes con productos es " + numeroFabricantesconProd);

        Assertions.assertEquals(7 , numeroFabricantesconProd );


	}

	/**
	 * 32. Calcula la media del precio de todos los productos
	 */
	@Test
	void test32() {
		var listProds = prodRepo.findAll();

        var mediaPrecios = listProds.stream()
                .mapToDouble(p->p.getPrecio())
                .average()
                .orElse(0.0);

        System.out.println("La media del precio de todos los productos es : " + mediaPrecios);



	}

	/**
	 * 33. Calcula el precio más barato de todos los productos. No se puede utilizar ordenación de stream.
	 */
	@Test
	void test33() {
		var listProds = prodRepo.findAll();
		//TODO
	}

	/**
	 * 34. Calcula la suma de los precios de todos los productos.
	 */
	@Test
	void test34() {
		var listProds = prodRepo.findAll();
		//TODO
	}

	/**
	 * 35. Calcula el número de productos que tiene el fabricante Asus.
	 */
	@Test
	void test35() {
		var listProds = prodRepo.findAll();
		//TODO
	}

	/**
	 * 36. Calcula la media del precio de todos los productos del fabricante Asus.
	 */
	@Test
	void test36() {
		var listProds = prodRepo.findAll();
		//TODO
	}


	/**
	 * 37. Muestra el precio máximo, precio mínimo, precio medio y el número total de productos que tiene el fabricante Crucial.
	 *  Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".
	 */
	@Test
	void test37() {
		var listProds = prodRepo.findAll();

        var sumaryStadistics = listProds.stream()
                .filter(producto -> producto.getFabricante().getNombre().equals("Crucial"))
                .mapToDouble(p -> p.getPrecio())
                .summaryStatistics();

        System.out.println(sumaryStadistics);

        listProds.stream()
                .filter(producto -> producto.getFabricante().getNombre().equals("Crucial"))
                .map(producto -> new double[]{0, (double) 0,(double)0 , producto.getPrecio()})
                //Esta mal :
                .reduce(new double[]{0.0 /*min*/ ,0.0 /*max*/,0.0 /*sum*/,0.0/*con*/ }, (a , b) ->{
                    double minAct = 0.0;
                    double maxACt = 0.0;
                    double sumAct = 0.0;
                    double countAct = 0.0;

                    double minAnterior = a[0];
                    if ((Double) b[0] < minAnterior){
                        minAct = (Double)b[0];

                    }else{
                        minAct = minAnterior;
                    }

                    double maxAnterior = (Double)a[1];
                    if ((Double)b[1]> maxAnterior){

                        maxAnterior = (Double)b[1];

                    }




                    double sumAnterior = (Double) a[2];
                    sumAct = sumAnterior + b[2];
                    double countAnt = (Double)a[3];
                    countAct = countAnt + 1;

                    return new double[]{minAct,maxACt,sumAct,countAnt};



                });
	}

	/**
	 * 38. Muestra el número total de productos que tiene cada uno de los fabricantes.
	 * El listado también debe incluir los fabricantes que no tienen ningún producto.
	 * El resultado mostrará dos columnas, una con el nombre del fabricante y otra con el número de productos que tiene.
	 * Ordene el resultado descendentemente por el número de productos. Utiliza String.format para la alineación de los nombres y las cantidades.
	 * La salida debe queda como sigue:

     Fabricante     #Productos
-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
           Asus              2
         Lenovo              2
Hewlett-Packard              2
        Samsung              1
        Seagate              1
        Crucial              2
       Gigabyte              1
         Huawei              0
         Xiaomi              0

	 */
	@Test
	void test38() {
		var listFabs = fabRepo.findAll();
		//TODO
	}

	/**
	 * 39. Muestra el precio máximo, precio mínimo y precio medio de los productos de cada uno de los fabricantes.
	 * El resultado mostrará el nombre del fabricante junto con los datos que se solicitan. Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".
	 * Deben aparecer los fabricantes que no tienen productos.
	 */
	@Test
	void test39() {
		var listFabs = fabRepo.findAll();
		//TODO
	}

	/**
	 * 40. Muestra el precio máximo, precio mínimo, precio medio y el número total de productos de los fabricantes que tienen un precio medio superior a 200€.
	 * No es necesario mostrar el nombre del fabricante, con el código del fabricante es suficiente.
	 */
	@Test
	void test40() {
		var listFabs = fabRepo.findAll();
		//TODO
	}

	/**
	 * 41. Devuelve un listado con los nombres de los fabricantes que tienen 2 o más productos.
	 */
	@Test
	void test41() {
		var listFabs = fabRepo.findAll();
		//TODO
	}

	/**
	 * 42. Devuelve un listado con los nombres de los fabricantes y el número de productos que tiene cada uno con un precio superior o igual a 220 €.
	 * Ordenado de mayor a menor número de productos.
	 */
	@Test
	void test42() {
		var listFabs = fabRepo.findAll();

            var listaNombre = listFabs.stream()

                    .map( f-> new Object[]{
                            f.getNombre(),
                            f.getProductos()
                                    .stream()
                                    .filter(p -> p.getPrecio() >= 200)
                                    .count()



                    })
                    .sorted(comparing((a) ->(Long) a[1] , reverseOrder()))
                    .toList();

            listaNombre.forEach( s -> System.out.println("Fabricante : " + s[0] + " Cantidad Producto = " + s[1] ));

/*
            var listaNombre2 = listFabs.stream()
                    .flatMap(fabricante -> fabricante.getProductos().stream())
                    //.filter(producto -> producto.getPrecio() > 220)
                    .collect(groupingBy(producto -> producto.getFabricante().getProductos() ,
                            filtering(producto -> producto.getPrecio() > 220) ,
                            counting()))

                    .entrySet()
                    .stream()
                    .sorted(comparing( (Map.Entry<String, Long> stringLongEntry ) -> stringLongEntry.getValue() , reverseOrder() ))
                    .toList();

            listaNombre2.forEach(System.out::println);


            record Mivector(String nomFab , long contProds){}

            var listadonombre3 = listFabs.stream()
                    .map(f -> new NomFabConteoPRods(
                            f.getNombre(),
                            f.getProductos()
                                    .stream()
                                    .filter(p-> p.getPrecio()>200)
                                    .count())

                    )
                    .sorted(comparing()) */
    }

	/**
	 * 43.Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €
	 */
	@Test
	void test43() {
		var listFabs = fabRepo.findAll();
		//TODO
	}

	/**
	 * 44. Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €
	 * Ordenado de menor a mayor por cuantía de precio de los productos.
	 */
	@Test
	void test44() {
		var listFabs = fabRepo.findAll();
		//TODO
	}

	/**
	 * 45. Devuelve un listado con el nombre del producto más caro que tiene cada fabricante.
	 * El resultado debe tener tres columnas: nombre del producto, precio y nombre del fabricante.
	 * El resultado tiene que estar ordenado alfabéticamente de menor a mayor por el nombre del fabricante.
	 */
	@Test
	void test45() {
		var listFabs = fabRepo.findAll();
		//TODO
	}

	/**
	 * 46. Devuelve un listado de todos los productos que tienen un precio mayor o igual a la media de todos los productos de su mismo fabricante.
	 * Se ordenará por fabricante en orden alfabético ascendente y los productos de cada fabricante tendrán que estar ordenados por precio descendente.
	 */
	@Test
	void test46() {
		var listFabs = fabRepo.findAll();
		//TODO
	}



    @Test
    void testReduce(){
        int sumatotal = Stream.of(1,2,3,4)

                .reduce(0 , (a,b) -> a+b);

        System.out.println(sumatotal);

    }

    @Test
    void testReduceWithFor(){
        int sumatotal = IntStream.iterate(1 , i->i <100 ,  i -> i+2)

                .peek(value -> System.out.println(value))

                .reduce(0 , (a,b) -> a+b);

        System.out.println(sumatotal);

    }

    @Test
    void testJoining(){
        String hola = Stream.of("Hola" , "mundo")

                        .collect(joining("," , ">" , "!"));

        System.out.println(hola);



    }

}


