package practica1;



public class Sucesor {

	private long [] accion= new long[2];
	private Estado estadoSucesor;
	private double costeAccion;	
	
	public Sucesor(long[] accion, Estado estadoSiguiente,
			double costeAccion) {
		this.accion = accion;
		this.estadoSucesor = estadoSiguiente;
		this.costeAccion = costeAccion;
	}
	
	
	public long[] getAccion() {
		return accion;
	}
	
	public void setAccion(long[] accion) {
		this.accion = accion;
	}
	
	public Estado getEstadoSucesor() {
		return estadoSucesor;
	}
	
	public void setEstadoSucesor(Estado estadoSiguiente) {
		this.estadoSucesor = estadoSiguiente;
	}
	
	public double getCosteAccion() {
		return costeAccion;
	}
	
	public void setCosteAccion(double costeAccion) {
		this.costeAccion = costeAccion;
	}


	public String toString() {
		return "Sucesor [accion=" + accion[0]+" -> "+ accion[1]
				+ ", estadoSiguiente=" + estadoSucesor.toString() + ", costeAccion="
				+ costeAccion + "]\n";
	}
	
	
	
	
	
}
