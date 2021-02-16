package model.logic;

import model.data_structures.ListaEncadenada;
import model.data_structures.MaxHeapCP;
import model.data_structures.RedBlackBST;

public class Zona implements Comparable<Zona>
{

	int movement_id;
	
	String scanombre;
	
	double shape_leng;
	
	double shape_area;
	
	RedBlackBST<Double, Interseccion> intersecciones;

	public Zona(int movement_id, String scanombre, double shape_leng, double shape_area, RedBlackBST<Double, Interseccion> intersecciones) {
		this.movement_id = movement_id;
		this.scanombre = scanombre;
		this.shape_leng = shape_leng*100;
		this.shape_area = shape_area*10000;
		this.intersecciones = intersecciones;
	}

	public int getMovement_id() {
		return movement_id;
	}

	public void setMovement_id(int movement_id) {
		this.movement_id = movement_id;
	}

	public String getScanombre() {
		return scanombre;
	}

	public void setScanombre(String scanombre) {
		this.scanombre = scanombre;
	}

	public double getShape_leng() {
		return shape_leng;
	}

	public void setShape_leng(double shape_leng) {
		this.shape_leng = shape_leng*100;
	}

	public double getShape_area() {
		return shape_area;
	}

	public void setShape_area(double shape_area) {
		this.shape_area = shape_area*10000;
	}

	public RedBlackBST<Double, Interseccion> getIntersecciones() {
		return intersecciones;
	}

	public void setNodos(RedBlackBST<Double, Interseccion> intersecciones) {
		this.intersecciones = intersecciones;
	}
	
	public Interseccion darNodoMasAlNorte()
	{
		return intersecciones.maxValue();
	}
	
	public int compareTo(Zona zona)
	{
		return this.darNodoMasAlNorte().compareTo(zona.darNodoMasAlNorte());
	}
	
	public int darCantidadNodos()
	{
		return intersecciones.size();
	}
}

