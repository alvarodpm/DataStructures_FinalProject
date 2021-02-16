package model.logic;

public class Interseccion implements Comparable<Interseccion>{
	
	int id;
	
	double longitud;
	
	double latitud;
	
	int MOVEMENT_ID;

	public Interseccion(int id, double longitud, double latitud, int MOVEMENT_ID) {
		this.id = id;
		this.longitud = longitud;
		this.latitud = latitud;
		this.MOVEMENT_ID = MOVEMENT_ID;
	}

	public int getMOVEMENT_ID() {
		return MOVEMENT_ID;
	}

	public void setMOVEMENT_ID(int mOVEMENT_ID) {
		MOVEMENT_ID = mOVEMENT_ID;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	
	@Override
	public int compareTo(Interseccion interseccion) {
		if(this.latitud > interseccion.latitud)
			return 1;
		else if(this.latitud < interseccion.latitud)
			return -1;
		else
			return 0;
	}
}
