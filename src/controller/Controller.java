package controller;

import java.util.Iterator;
import java.util.Scanner;

import model.data_structures.Edge;
import model.data_structures.ListaEncadenada;
import model.data_structures.UndirectedGraph;
import model.data_structures.Vertex;
import model.logic.Interseccion;
import model.logic.MVCModelo;
import model.logic.Viaje;
import model.logic.Zona;
import view.MVCView;

public class Controller {

	/* Instancia del Modelo*/
	private MVCModelo modelo;

	/* Instancia de la Vista*/
	private MVCView view;

	/**
	 * Crear la vista y el modelo del proyecto
	 * @param capacidad tamaNo inicial del arreglo
	 */
	public Controller ()
	{
		view = new MVCView();
		modelo = new MVCModelo();
	}

	public void run() 
	{
		Scanner lector = new Scanner(System.in);
		boolean fin = false;
		String respuesta = "";

		while( !fin ){
			view.printMenu();

			int option = lector.nextInt();
			switch(option){
			case 1:
				System.out.println("--------- \nCargando malla vial...");
				try {
					modelo.cargarMallaVial();

					view.reportarCargaDeUnArchivo(modelo.cantidadVertices(), modelo.cantidadArcos(), modelo.cantidadComponentesConectadas(), modelo.darCantidadVerticesDeLasNCCMasGrandes(5));
					//modelo.graficarMapa();
					
					System.out.println("\n---------");
				} 
				catch (Exception e) 
				{
					System.out.println(e.getMessage());
				}						
				break;

			case 2: 
				System.out.println("\nCreando un archivo .json con la información del grafo...");
				
				modelo.crearArchivoJSON();
				
				System.out.println("\nEl archivo fue creado exitosamente");
				break;
			case 3: 
				try {
					modelo.cargarArchivoJSON();
					
					Iterator<Integer> iterador = modelo.darCantidadVerticesDeLasNCCMasGrandes(5);
					view.reportarCargaDeUnArchivo(modelo.cantidadVertices(), modelo.cantidadArcos(), modelo.cantidadComponentesConectadas(), iterador);
					modelo.graficarMapa();
					
					System.out.println("\n---------");
				} 
				catch (Exception e) 
				{
					System.out.println(e.getMessage());
				}	
				break;
				
			case 4: 
				System.out.println("\nIngrese la latitud a buscar:");
				double latitud = lector.nextDouble();
				System.out.println("\nIngrese la longitud a buscar:");
				double longitud = lector.nextDouble();
				
				view.reportarIdVerticeMasCercano(modelo.darIndiceVertexMasCercano(latitud, longitud));
				break;
				
			case 5: 
				break;
				
			case 6: 
				break;
				
			case 7: 
				break;
				
			case 8: 
				System.out.println("\nIngrese la latitud origen:");
				double latitudOrigen = lector.nextDouble();
				System.out.println("\nIngrese la longitud origen:");
				double longitudOrigen = lector.nextDouble();
				
				System.out.println("\nIngrese la latitud destino:");
				double latitudDestino = lector.nextDouble();
				System.out.println("\nIngrese la longitud destino:");
				double longitudDestino = lector.nextDouble();
				
				ListaEncadenada<Vertex<Integer, Interseccion>> lista;
				
				try {
					lista = modelo.darCaminoCostoMinimoDistancia(latitudOrigen, longitudOrigen, latitudDestino, longitudDestino);
					
					double tiempoEstimado = modelo.darTiempoEstimadoDeCamino(lista.iterator());
					
					double distanciaEstimada = modelo.darDistanciaEstimadaDeCamino(lista.iterator());
					
					view.reportarCaminoCostoMinimoDistancia(lista, tiempoEstimado, distanciaEstimada);
					
					modelo.graficarMapa(lista);
					
				} catch (Exception e) {
					view.printMessage(e.getMessage());
				}
				break;
				
			case 9: 
				System.out.println("\nIngrese la latitud origen:");
				double latitudOrigen1 = lector.nextDouble();
				System.out.println("\nIngrese la longitud origen:");
				double longitudOrigen1 = lector.nextDouble();
				System.out.println("\nIngrese el tiempo:");
				double tiempo = lector.nextDouble();
				
				ListaEncadenada<Vertex<Integer, Interseccion>> lista1;
				
				lista1 = modelo.darVerticesAlcanzablesSegunTiempo(latitudOrigen1, longitudOrigen1, tiempo);
				
				view.reportarVerticesAlcanzablesSegunTiempo(lista1);
				
				Interseccion interseccion = modelo.darInterseccionMasCercana(latitudOrigen1, longitudOrigen1);
				
				modelo.graficarMapa(lista1, interseccion.getLatitud(), interseccion.getLongitud());
				
				break;
				
			case 10: 
				try {
					double inicioTiempo = System.currentTimeMillis();
					
					ListaEncadenada<Edge<Integer, Interseccion>> lista2 = modelo.darMSTSegunDistanciaDeLaCCMasGrandeUsandoKruskal();
					
					double finTiempo = System.currentTimeMillis();
					
					view.reportarMSTSegunDistanciaDeLaCCMasGrandeUsandoKruskal(lista2, (finTiempo - inicioTiempo), modelo.darCantidadVerticesCCMasGrande(), modelo.calcularCostoDistancia(lista2));
					
					modelo.graficarMapa(modelo.darCCMasGrande(), lista2);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
				
			case 11: 
				UndirectedGraph<Integer, Interseccion> nuevoGrafo = modelo.construirGrafoSimplificado();
				
				view.reportarConstruccionGrafoSimplificado(nuevoGrafo.E(), nuevoGrafo.V());
				
				//modelo.graficarMapa(nuevoGrafo);
				break;
				
			case 12: 
				System.out.println("\nIngrese la zona origen:");
				int zonaOrigen = lector.nextInt();
				
				System.out.println("\nIngrese la zona destino:");
				int zonaDestino = lector.nextInt();
				
				ListaEncadenada<Vertex<Integer, Interseccion>> lista3;
				
				try {
					
					double inicioTiempo = System.currentTimeMillis();
					lista3 = modelo.darCaminoCostoMinimoTiempoEntreDosZonas(zonaOrigen, zonaDestino);
					double finTiempo = System.currentTimeMillis();
					double tiempoEstimado = modelo.darTiempoEstimadoDeCamino(lista3.iterator());
					
					
					view.reportarCaminoCostoMinimoDistancia(lista3, tiempoEstimado, (finTiempo-inicioTiempo));
					
					modelo.graficarMapa(lista3);
					
				} catch (Exception e) {
					view.printMessage(e.getMessage());
				}
				break;
				
			case 13: 
				break;

			case 14: 
				System.out.println("--------- \n Hasta pronto !! \n---------"); 
				lector.close();
				fin = true;
				break;

			default: 
				System.out.println("--------- \n Opcion Invalida !! \n---------");
				break;
			}
		}

	}	
}
