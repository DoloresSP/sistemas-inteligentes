package practica1;

import java.util.Hashtable;

//import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;


public class Grafo {
	
    Hashtable<Long,Nodo> tablaNodos;
    
    public Grafo() {    
            tablaNodos=new Hashtable <Long,Nodo>();
    }
    
    public void addEnlace(Way way, long nodoInicial, long nodoTerminal, double distancia){
   	 tablaNodos.get(nodoInicial).addEnlace(tablaNodos.get(nodoTerminal), distancia);
     tablaNodos.get(nodoTerminal).addEnlace(tablaNodos.get(nodoInicial), distancia);
   }

    
    public void addNodo(Nodo nodo) //Añadir a tabla hash el id del nodo y el objeto nodo leidos
    {
    	tablaNodos.put(nodo.getNode().getId(), nodo);
            
    }
    
    public void deleteNodosTabla(long id){
    	tablaNodos.remove(id);
    }
    
    
    public Nodo getNodo(long IdNodo){
    	
            return tablaNodos.get(IdNodo);
    }  
   
    public Hashtable<Long,Nodo> getNodosTabla()
    {
            return tablaNodos;
    }
    
    
    
    
    
    
    

}
