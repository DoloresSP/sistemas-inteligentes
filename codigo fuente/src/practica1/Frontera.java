package practica1;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;




public class Frontera {

	private PriorityQueue<NodoArbol> colaprioridad;
	
	
	
	public Frontera(){
		Comparator<NodoArbol> comp = new NodoBusquedaComparator();
		colaprioridad = new PriorityQueue<NodoArbol>(1000000, comp);
	}
	
	public void insertarLista(ArrayList<NodoArbol> listaNodos) {
		for(int i=0; i<listaNodos.size(); i++)
			insertar(listaNodos.get(i));		
	}
	public void insertar(NodoArbol nodo){
			
			colaprioridad.add(nodo);
			
			
		}
	public NodoArbol getPrimero(){
		NodoArbol nodo = null;
		
				nodo = colaprioridad.poll();
				
		
		return nodo;
	}
	
	public void eliminarNodo(NodoArbol nodo){
		colaprioridad.remove(nodo);
	}
	
	public boolean esVacia(){
		int size=0;
		size = colaprioridad.size();
		boolean vacia=false;
		if(size==0){
			vacia=true;	
		}
		return vacia;		
	}
	
	
	
	
	
}
