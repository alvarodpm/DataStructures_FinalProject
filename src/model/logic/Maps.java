package model.logic;
import com.teamdev.jxmaps.*;
import com.teamdev.jxmaps.Polygon;
import com.teamdev.jxmaps.swing.MapView;

import model.data_structures.Edge;
import model.data_structures.ListaEncadenada;
import model.data_structures.UndirectedGraph;
import model.data_structures.Vertex;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class Maps<K extends Comparable<K>, I> extends MapView
{
	//-------------------------
	// Atributos
	//------------------------

	/**
	 * Mapa cargado de JxMaps
	 */
	private Map map;

	/**
	 * Tabla de Hash que contiene las universidades en llaves
	 * y las Latitudes y Longitudes en Valor
	 */
	private UndirectedGraph<Integer, Interseccion> grafo;

	/**
	 * Lista para graficar los caminos
	 */
	private ListaEncadenada<Vertex<Integer, Interseccion>> lista;

	//-----------------------
	// Constructores
	//------------------------

	/**
	 *
	 */
	public Maps(UndirectedGraph<Integer, Interseccion> grafo)
	{
		this.grafo = grafo;
		this.lista = null;

		setOnMapReadyHandler(new MapReadyHandler() {
			@Override
			public void onMapReady(MapStatus status)
			{
				if(status == MapStatus.MAP_STATUS_OK)
				{
					map = getMap();
					initMap(map);
					drawCircle(map);
					drawPolygon(map);
				}
			}
		});
	}

	/**
	 *
	 */
	public Maps(UndirectedGraph<Integer, Interseccion> grafo, ListaEncadenada<Vertex<Integer, Interseccion>> lista)
	{
		this.grafo = grafo;
		this.lista = lista;

		setOnMapReadyHandler(new MapReadyHandler() {
			@Override
			public void onMapReady(MapStatus status)
			{
				if(status == MapStatus.MAP_STATUS_OK)
				{
					map = getMap();
					initMap(map, lista.iterator());
					drawCircle(map, lista.iterator());
					drawPolygon(map, lista.iterator());
				}
			}
		});
	}

	/**
	 *
	 */
	public Maps(UndirectedGraph<Integer, Interseccion> grafo, ListaEncadenada<Vertex<Integer, Interseccion>> lista, double latOrigen, double longOrigen)
	{
		this.grafo = grafo;
		this.lista = lista;

		setOnMapReadyHandler(new MapReadyHandler() {
			@Override
			public void onMapReady(MapStatus status)
			{
				if(status == MapStatus.MAP_STATUS_OK)
				{
					map = getMap();
					initMap(map, latOrigen, longOrigen);
					drawCircle(map, lista.iterator(), latOrigen, longOrigen);
					drawPolygon(map, lista.iterator(), latOrigen, longOrigen);
				}
			}
		});
	}

	public Maps(UndirectedGraph<Integer, Interseccion> grafo, Iterable<Integer> cc, ListaEncadenada<Edge<Integer, Interseccion>> lista2) {
		this.grafo = grafo;

		setOnMapReadyHandler(new MapReadyHandler() {
			@Override
			public void onMapReady(MapStatus status)
			{
				if(status == MapStatus.MAP_STATUS_OK)
				{
					map = getMap();
					initMap(map, cc);
					drawCircle(map, cc);
					drawPolygon(map, cc, lista2.iterator());
				}
			}
		});
	}

	/**
	 * Metodo que se encarga de dibujar una linea con
	 * el comportamiento de un poligono
	 * @param map Lienzo donde se colocarán las lineas
	 */
	public void drawPolygon(Map map)
	{
		PolygonOptions po = new PolygonOptions();
		po.setFillColor("#C63B3B");
		po.setFillOpacity(0.35);

		LatLng[] path = new LatLng[2];

		Iterator<Integer> keys = grafo.keys();

		while(keys.hasNext())
		{ 
			Vertex<Integer, Interseccion> vertexActual = grafo.getVertex(keys.next());

			Iterator<Edge<Integer, Interseccion>> arcos = vertexActual.edges().iterator();

			while(arcos.hasNext())
			{
				Edge<Integer, Interseccion> arcoActual = arcos.next();

				Interseccion interseccion1 = arcoActual.v1.info;
				Interseccion interseccion2 = arcoActual.v2.info;

				path[0] = new LatLng(interseccion1.latitud, interseccion1.longitud);
				path[1] = new LatLng(interseccion2.latitud, interseccion2.longitud);

				Polygon polygon = new Polygon(map);
				polygon.setPath(path);
				polygon.setOptions(po);
			}
		}
	}

	/**
	 * Metodo que se encarga de dibujar una linea con
	 * el comportamiento de un poligono
	 * @param map Lienzo donde se colocarán las lineas
	 */
	public void drawPolygon(Map map, Iterator<Vertex<Integer,Interseccion>> iterador)
	{
		PolygonOptions po = new PolygonOptions();
		po.setFillColor("#C63B3B");
		po.setFillOpacity(0.35);

		LatLng[] path = new LatLng[2];

		Vertex<Integer, Interseccion> vertex1 = iterador.next();

		while(iterador.hasNext())
		{ 
			Vertex<Integer,Interseccion> vertex2 = iterador.next();

			Interseccion interseccion1 = vertex1.info;
			Interseccion interseccion2 =vertex2.info;

			path[0] = new LatLng(interseccion1.latitud, interseccion1.longitud);
			path[1] = new LatLng(interseccion2.latitud, interseccion2.longitud);

			Polygon polygon = new Polygon(map);
			polygon.setPath(path);
			polygon.setOptions(po);

			vertex1 = vertex2;

		}
	}
	
	/**
	 * Metodo que se encarga de dibujar una linea con
	 * el comportamiento de un poligono
	 * @param map Lienzo donde se colocarán las lineas
	 */
	public void drawPolygon(Map map,Iterable<Integer> cc, Iterator<Edge<Integer,Interseccion>> iterador)
	{
		PolygonOptions po = new PolygonOptions();
		po.setFillColor("#C63B3B");
		po.setFillOpacity(0.35);

		LatLng[] path = new LatLng[2];

		while(iterador.hasNext())
		{ 
			Edge<Integer,Interseccion> edge = iterador.next();

			Interseccion interseccion1 = edge.v1.info;
			Interseccion interseccion2 = edge.v2.info;

			path[0] = new LatLng(interseccion1.latitud, interseccion1.longitud);
			path[1] = new LatLng(interseccion2.latitud, interseccion2.longitud);

			Polygon polygon = new Polygon(map);
			polygon.setPath(path);
			polygon.setOptions(po);
		}
	}
	
	

	/**
	 * Metodo que se encarga de dibujar una linea con
	 * el comportamiento de un poligono
	 * @param map Lienzo donde se colocarán las lineas
	 */
	public void drawPolygon(Map map, Iterator<Vertex<Integer,Interseccion>> iterador, double latOrigen, double longOrigen)
	{
		PolygonOptions po = new PolygonOptions();
		po.setFillColor("#C63B3B");
		po.setFillOpacity(0.35);

		LatLng[] path = new LatLng[2];

		while(iterador.hasNext())
		{ 
			Vertex<Integer,Interseccion> vertex = iterador.next();

			Interseccion interseccion = vertex.info;

			path[0] = new LatLng(interseccion.latitud, interseccion.longitud);
			path[1] = new LatLng(latOrigen, longOrigen);

			Polygon polygon = new Polygon(map);
			polygon.setPath(path);
			polygon.setOptions(po);
		}
	}

	/**
	 * Método que se encarga de dibujar los circulos
	 * @param map Lienzo donde se colocarán los circulos
	 */
	public void drawCircle(Map map)
	{

		CircleOptions co = new CircleOptions();
		co.setFillOpacity(0.35);
		co.setStrokeWeight(1);
		co.setStrokeOpacity(0.2);
		co.setStrokeColor("FFA07A");

		Iterator<Integer> key = grafo.keys();
		while(key.hasNext())
		{ 
			int id = key.next();

			Interseccion actual = grafo.getInfoVertex(id);

			double longitud = actual.longitud;
			double latitud = actual.latitud;

			if(longitud >= -74.094723 && longitud <= -74.062707 && latitud >= 4.597714 && latitud <=  4.621360)
			{
				Circle circle = new Circle(map);
				circle.setCenter(new LatLng(latitud, longitud));
				circle.setOptions(co);
				circle.setRadius(8);
			}
		}
	}

	/**
	 * Método que se encarga de dibujar los circulos
	 * @param map Lienzo donde se colocarán los circulos
	 * @param iterador Contiene los vértices que se deben dibujar
	 */
	public void drawCircle(Map map, Iterator<Vertex<Integer,Interseccion>> iterador)
	{

		CircleOptions co = new CircleOptions();
		co.setFillOpacity(0.35);
		co.setStrokeWeight(1);
		co.setStrokeOpacity(0.2);
		co.setStrokeColor("FFA07A");

		while(iterador.hasNext())
		{ 
			int id = iterador.next().key;

			Interseccion actual = grafo.getInfoVertex(id);

			double longitud = actual.longitud;
			double latitud = actual.latitud;

			if(longitud >= -74.094723 && longitud <= -74.062707 && latitud >= 4.597714 && latitud <=  4.621360)
			{
				Circle circle = new Circle(map);
				circle.setCenter(new LatLng(latitud, longitud));
				circle.setOptions(co);
				circle.setRadius(8);
			}
		}
	}

	/**
	 * Método que se encarga de dibujar los circulos
	 * @param map Lienzo donde se colocarán los circulos
	 * @param iterador Contiene los vértices que se deben dibujar
	 */
	public void drawCircle(Map map, Iterable<Integer> cc)
	{

		CircleOptions co = new CircleOptions();
		co.setFillOpacity(0.35);
		co.setStrokeWeight(1);
		co.setStrokeOpacity(0.2);
		co.setStrokeColor("FFA07A");

		for(Integer kActual : cc)
		{
			Interseccion actual = grafo.getInfoVertex(kActual);

			double longitud = actual.longitud;
			double latitud = actual.latitud;

			if(longitud >= -74.094723 && longitud <= -74.062707 && latitud >= 4.597714 && latitud <=  4.621360)
			{
				Circle circle = new Circle(map);
				circle.setCenter(new LatLng(latitud, longitud));
				circle.setOptions(co);
				circle.setRadius(8);
			}
		}
	}

	/**
	 * Método que se encarga de dibujar los circulos
	 * @param map Lienzo donde se colocarán los circulos
	 * @param iterador Contiene los vértices que se deben dibujar
	 */
	public void drawCircle(Map map, Iterator<Vertex<Integer,Interseccion>> iterador, double latOrigen, double longOrigen)
	{

		CircleOptions co1 = new CircleOptions();
		co1.setFillOpacity(1);
		co1.setStrokeWeight(1);
		co1.setStrokeOpacity(0.2);
		co1.setStrokeColor("66FF33");

		if(longOrigen >= -74.094723 && longOrigen <= -74.062707 && latOrigen >= 4.597714 && latOrigen <=  4.621360)
		{
			Circle circle = new Circle(map);
			circle.setCenter(new LatLng(latOrigen, longOrigen));
			circle.setOptions(co1);
			circle.setRadius(2000);
		}

		CircleOptions co = new CircleOptions();
		co.setFillOpacity(1);
		co.setStrokeWeight(1);
		co.setStrokeOpacity(0.2);
		co.setStrokeColor("FFA07A");

		while(iterador.hasNext())
		{ 
			int id = iterador.next().key;

			Interseccion actual = grafo.getInfoVertex(id);

			double longitud = actual.longitud;
			double latitud = actual.latitud;

			if(longitud >= -74.094723 && longitud <= -74.062707 && latitud >= 4.597714 && latitud <=  4.621360)
			{
				Circle circle = new Circle(map);
				circle.setCenter(new LatLng(latitud, longitud));
				circle.setOptions(co);
				circle.setRadius(2000);
			}
		}
	}

	/**
	 * Inicialia el mapa de JxMaps
	 * @param map El mapa que será inicializado
	 */
	public void initMap(Map map)
	{

		MapOptions mapOptions = new MapOptions();
		MapTypeControlOptions controlOptions = new MapTypeControlOptions();
		mapOptions.setMapTypeControlOptions(controlOptions);

		map.setCenter(new LatLng(4.6012, -74.0657));
		map.setZoom(11.0);
	}

	/**
	 * Inicialia el mapa de JxMaps
	 * @param map El mapa que será inicializado
	 */
	public void initMap(Map map, Iterator<Vertex<Integer,Interseccion>> iterator)
	{

		MapOptions mapOptions = new MapOptions();
		MapTypeControlOptions controlOptions = new MapTypeControlOptions();
		mapOptions.setMapTypeControlOptions(controlOptions);

		Vertex<Integer,Interseccion> elVertex = iterator.next();

		map.setCenter(new LatLng(elVertex.info.latitud, elVertex.info.longitud));
		map.setZoom(15.0);
	}

	/**
	 * Inicialia el mapa de JxMaps
	 * @param map El mapa que será inicializado
	 */
	public void initMap(Map map, Iterable<Integer> cc)
	{

		MapOptions mapOptions = new MapOptions();
		MapTypeControlOptions controlOptions = new MapTypeControlOptions();
		mapOptions.setMapTypeControlOptions(controlOptions);

		Vertex<Integer,Interseccion> elVertex = grafo.darUnVerticeDeLaCCMasGrande();

		map.setCenter(new LatLng(elVertex.info.latitud, elVertex.info.longitud));
		map.setZoom(15.0);
	}

	/**
	 * Inicialia el mapa de JxMaps
	 * @param map El mapa que será inicializado
	 */
	public void initMap(Map map, double latOrigen, double longOrigen)
	{

		MapOptions mapOptions = new MapOptions();
		MapTypeControlOptions controlOptions = new MapTypeControlOptions();
		mapOptions.setMapTypeControlOptions(controlOptions);

		map.setCenter(new LatLng(latOrigen, longOrigen));
		map.setZoom(15.0);
	}

	/**
	 * Inicializa el marco donde el mapa se va a cargar
	 * @param titulo El título del marco
	 */
	public void initFrame(String titulo)
	{
		JFrame frame = new JFrame(titulo);
		frame.setSize(700, 500);
		frame.setVisible(true);
		frame.add(this, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}