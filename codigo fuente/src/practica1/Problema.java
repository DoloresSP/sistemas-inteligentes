package practica1;
import java.util.ArrayList;
import java.util.Enumeration;
//import java.util.Hashtable;
import java.util.Hashtable;


public class Problema {

	private long idEstadoInicial;
	private Estado estado;
	static Hashtable<String, Double> tablaPoda = new Hashtable<String, Double>();

	//private Hashtable<Long,Nodo> espacioEstados;
	
	public Problema(long idEstadoInicial, Estado estado) {
		super();
		this.idEstadoInicial = idEstadoInicial;
		this.estado = estado;
	}
	
	

	
	
	
	public boolean poda(NodoArbol nodo, int estrategia){

		String estado= nodo.getEstado().toString();
		Enumeration<String> e = tablaPoda.keys();
		boolean esta=false;
		boolean poda=false;

		while(e.hasMoreElements()){

			String estadoTabla=e.nextElement();
			
			if(estado.equals(estadoTabla)){
				
				esta=true; 
				if(estrategia==2){
					if(nodo.getValor()>tablaPoda.get(estadoTabla)){
						poda=false;
						tablaPoda.remove(estadoTabla);
						tablaPoda.put(estado, nodo.getValor());

					}else{
						poda=true;
					}
				}else{
					if(nodo.getValor()<=tablaPoda.get(estadoTabla)){
						poda=false;
						tablaPoda.remove(estadoTabla);
						tablaPoda.put(estado, nodo.getValor());

					}else{
						poda=true;
					}

				}
				
			}
		}
		if(esta==false){
			tablaPoda.put(estado, nodo.getValor());
			poda=false;
		}


		return poda;


	}

	
	
	
	
	
	
	
	
	
	
	
	public ArrayList <Sucesor> Sucesores (Grafo g, Estado e){
		return e.sucesores(e, g);
		
	}
	
	
	
	public boolean estado_meta(Estado e){
		boolean objetivo= e.EsObjetivo(e);
		return objetivo;
	}
	

	public long getIdEstadoInicial() {
		return idEstadoInicial;
	}

	public void setIdEstadoInicial(long idEstadoInicial) {
		this.idEstadoInicial = idEstadoInicial;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	
	
	
	
	
}
