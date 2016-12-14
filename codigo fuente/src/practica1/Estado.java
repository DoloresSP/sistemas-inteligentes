package practica1;


import java.util.ArrayList;


public class Estado {
	
	private long idNodo;
	private double latitud;
	private double longitud;
	private ArrayList <Long> objetivos;
	
	
	
	public Estado(long idNodo, double latitud, double longitud, ArrayList<Long> objetivos) {
		super();
		this.idNodo = idNodo;
		this.objetivos = objetivos;
		this.latitud=latitud;
		this.longitud=longitud;
	}

	public double getLatitud() {
		return latitud;
	}


	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	
	
	public ArrayList <Sucesor> sucesores (Estado e, Grafo g){
		
		Double distancia;
		Nodo nodoInicial= g.getNodo(e.getIdNodo());
		ArrayList <Enlace> adyacentes = nodoInicial.getEnlaces();
		ArrayList <Long> objetivos= new ArrayList<Long>();
		
		ArrayList <Sucesor> listaSucesores= new ArrayList <Sucesor>();
		objetivos=e.getObjetivos();
		
		
		
		
		for(int i=0; i<adyacentes.size(); i++){
			long [] accion = new long [2];
			accion[0]=e.getIdNodo();
			Nodo nodoSucesor=adyacentes.get(i).getDestino();
			accion[1]=nodoSucesor.getNode().getId();
			ArrayList <Long> objetivosNuevos= new ArrayList<Long>();
			
			for(int j=0; j<objetivos.size(); j++){
				objetivosNuevos.add(objetivos.get(j));
				if(accion[1]==objetivos.get(j)){
					objetivosNuevos.remove(j);
				}
			}
		
			
			distancia= adyacentes.get(i).getDistancia();
			
			Estado estadoNuevo =new Estado(accion[1], nodoSucesor.getNode().getLatitude(), nodoSucesor.getNode().getLongitude(), objetivosNuevos);
			Sucesor suc1= new Sucesor(accion, estadoNuevo , distancia);
			
			listaSucesores.add(i, suc1);
			
		}
		
	
		
		return listaSucesores;
		
		
		
	}






	public boolean EsValido(Estado e, Grafo g){
		boolean valido = true;
		for(int i=0; i<e.getObjetivos().size(); i++){
			if(g.getNodosTabla().containsKey(e.getObjetivos().get(i))== false){
				valido=false;
			}
		}
		return valido;
	}

	public boolean EsObjetivo(Estado e){
		boolean objetivo=false;
		
		if(e.getObjetivos().size()==0){
			objetivo=true;
		}
		return objetivo;
	}
	

	public long getIdNodo() {
		return idNodo;
	}

	public void setIdNodo(long idNodo) {
		this.idNodo = idNodo;
	}

	public ArrayList<Long> getObjetivos() {
		return objetivos;
	}

	public void setObjetivos(ArrayList<Long> objetivos) {
		this.objetivos = objetivos;
	}



	
	public String toString() {

		String id= "["+idNodo + ", [";
		for(int i=0; i<objetivos.size(); i++){
			id= id + objetivos.get(i)+ " ";
		}
			id= id + "]";
			
		return id;
		
	}
	
	

}
