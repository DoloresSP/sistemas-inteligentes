package practica1;




import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;



public class NodoArbol {
	
	private NodoArbol padre;
	private Estado estado;
	private double costo;
	private long accion[];
	private int profundidad;
	private double valor;
	
	
	public NodoArbol(NodoArbol padre, Estado estado, double costo, long [] accion,
			int profundidad, double valor) {
		super();
		this.padre = padre;
		this.estado = estado;
		this.costo = costo;
		this.accion = accion;
		this.profundidad = profundidad;
		this.valor = valor;
	}
	
	
	public ArrayList<NodoArbol> CreaListaNodosArbol (Frontera frontera, Problema problema, ArrayList<Sucesor> ls,NodoArbol nodo_actual,int prof_max,int estrategia, Grafo g, int realizarPoda){
		
		ArrayList<NodoArbol> listanodos = new ArrayList<NodoArbol>();
		
		for(int i=0; i<ls.size(); i++){
			
			Sucesor sucesor=ls.get(i);
			long [] accion =  new long [2];
			accion=sucesor.getAccion();
			double valor=0;
			NodoArbol nodoSucesor= null;
			if(estrategia==1){
				
		    	valor=(nodo_actual.getProfundidad()+1);
		    	 
		    }else if(estrategia==2){
		    	
		    	
		    	valor= prof_max - (nodo_actual.getProfundidad()+1);
		    	
		    }else if(estrategia==3){
		    	
		    	valor= sucesor.getCosteAccion()+nodo_actual.getCosto();
		    	
		    }else if(estrategia==4){
		    	
		    	valor= distanciaMaxima(sucesor, g);
		    	
		    }else if(estrategia==5){
		    	
		    	valor= distanciaMaxima(sucesor, g)+sucesor.getCosteAccion()+nodo_actual.getCosto();
		    	
		    }
			nodoSucesor= new NodoArbol(nodo_actual, sucesor.getEstadoSucesor(), sucesor.getCosteAccion()+nodo_actual.getCosto(), accion, nodo_actual.getProfundidad()+1, valor);

			
			
			if(realizarPoda==1){//ralizamos la poda
				if(nodoSucesor.getProfundidad() <= prof_max  && !problema.poda(nodoSucesor, estrategia)){
					
					listanodos.add(nodoSucesor);
				}
			}else{//no realizamos la poda
				if(nodoSucesor.getProfundidad() <= prof_max){
					listanodos.add(nodoSucesor);
				}
			}
			
		}
		
		
		return listanodos;
		
	}
	
	
	
	
	static double distanciaMaxima(Sucesor sucesor, Grafo g){
		double distanciaActual=0;
		
		double lat1= sucesor.getEstadoSucesor().getLatitud();
		double long1 = sucesor.getEstadoSucesor().getLongitud();

		for( int j=0; j<sucesor.getEstadoSucesor().getObjetivos().size(); j++){
			Nodo n= g.getNodo(sucesor.getEstadoSucesor().getObjetivos().get(j));
			
			double lat2 = n.getNode().getLatitude();
			double long2 = n.getNode().getLongitude();
			
			double x1=merc_x(long1);
			double x2=merc_x(long2);
			double y1=merc_y(lat1);
			double y2=merc_y(lat2);
			double distancia= (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)));
			if(distanciaActual < distancia){
				distanciaActual=distancia;
			}
			
		}
		return distanciaActual;
		

	}//fin coordenadas a metros

	static double merc_x(double lon){
		  double r_major=6378137.000;
		  return r_major*Math.toRadians(lon);
	} 
	static double merc_y(double lat){
		  if (lat>89.5){
			  lat=89.5;
		  }
		  if (lat<-89.5){
			  lat=-89.5;
		  }
		  double r_major=6378137.000;
		  double r_minor=6356752.3142;
		  double temp=r_minor/r_major;
		  double eccent=Math.sqrt(Math.pow(1-temp, 2));
		  double phi=Math.toRadians(lat);
		  double sinphi=Math.sin(phi);
		  double con=eccent*sinphi;
		  double com=eccent/2;
		  con=Math.pow(((1.0-con)/(1.0+con)), com);
		  double ts=Math.tan((Math.PI/2-phi)/2)/con;
		  double y=0-r_major*Math.log(ts);
		  return y;

	}
	
	
	
	public NodoArbol getPadre() {
		return padre;
	}
	public void setPadre(NodoArbol padre) {
		this.padre = padre;
	}
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	public double getCosto() {
		return costo;
	}
	public void setCosto(int costo) {
		this.costo = costo;
	}
	public long[] getAccion() {
		return accion;
	}
	public void setAccion(long[] accion) {
		this.accion = accion;
	}
	public int getProfundidad() {
		return profundidad;
	}
	public void setProfundidad(int profundidad) {
		this.profundidad = profundidad;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	
	
	
	
	

}
