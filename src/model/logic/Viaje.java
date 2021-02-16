package model.logic;

public class Viaje implements Comparable<Viaje>{

	//Atributos
	
	private int sourceid;
	private int dstid;
	private int	time;
	private double mean_travel_time;
	private double standard_deviation_travel_time;
	private double geometric_mean_travel_time;
	private double geometric_standard_deviation_travel_time;
	
	//Constructor
	
	public Viaje(int sourceid, int dstid, int time, double mean_travel_time, double standard_deviation_travel_time,
			double geometric_mean_travel_time, double geometric_standard_deviation_travel_time) {
		super();
		this.sourceid = sourceid;
		this.dstid = dstid;
		this.time = time;
		this.mean_travel_time = mean_travel_time;
		this.standard_deviation_travel_time = standard_deviation_travel_time;
		this.geometric_mean_travel_time = geometric_mean_travel_time;
		this.geometric_standard_deviation_travel_time = geometric_standard_deviation_travel_time;
	}
	
	//Métodos

	public int getSourceid() {
		return sourceid;
	}

	public void setSourceid(int sourceid) {
		this.sourceid = sourceid;
	}

	public int getDstid() {
		return dstid;
	}

	public void setDstid(int dstid) {
		this.dstid = dstid;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public double getMean_travel_time() {
		return mean_travel_time;
	}

	public void setMean_travel_time(double mean_travel_time) {
		this.mean_travel_time = mean_travel_time;
	}

	public double getStandard_deviation_travel_time() {
		return standard_deviation_travel_time;
	}

	public void setStandard_deviation_travel_time(double standard_deviation_travel_time) {
		this.standard_deviation_travel_time = standard_deviation_travel_time;
	}

	public double getGeometric_mean_travel_time() {
		return geometric_mean_travel_time;
	}

	public void setGeometric_mean_travel_time(double geometric_mean_travel_time) {
		this.geometric_mean_travel_time = geometric_mean_travel_time;
	}

	public double getGeometric_standard_deviation_travel_time() {
		return geometric_standard_deviation_travel_time;
	}

	public void setGeometric_standard_deviation_travel_time(double geometric_standard_deviation_travel_time) {
		this.geometric_standard_deviation_travel_time = geometric_standard_deviation_travel_time;
	}
	
	//El toString debe cambiarse
	@Override
	public String toString()
	{
		return "Zona de origen: " + sourceid + ", Zona de destino: " + dstid + ", Día de la semana: " + time + ", Tiempo promedio(seg): " + mean_travel_time;
	}

	@Override
	public int compareTo(Viaje o) {
		
		if(time < o.getTime())
			return 1;
		else if(time > o.getTime())
			return -1;
		else
			return 0;
	}
}
