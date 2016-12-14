package practica1;
import java.util.ArrayList;



import org.openstreetmap.osmosis.core.domain.v0_6.Node;


public class Nodo {

	private Node node; 
    private ArrayList<Enlace> enlaces; //cada nodo va a tener una lista de sus enlaces a los nodos adyacentes, donde cada enlace tiene el id del nodo destino y el peso del enlace 
    private int numEnlaces;
    
    public Nodo(Node nod) {
    		setNode(nod);
    		numEnlaces=0;
            enlaces = new ArrayList<Enlace>();
    }
    
	public void addEnlace(Nodo nodoDestino, double distancia){
           
	    int validarEnlace= existeEnlace(nodoDestino.getNode().getId());
	    if (validarEnlace == -1) {
	            enlaces.add(new Enlace(nodoDestino, distancia));
	            numEnlaces++;      
	    }
           
    }

    public int existeEnlace(long idNodoDest)
 
    {
            for (int i = 0; i < enlaces.size(); i++)
            {
                    
                    Enlace miEnlace = enlaces.get(i);
                    if ( miEnlace.getDestino().getNode().getId() == idNodoDest )
                    	return i;
            }
            return -1;//No hay enlace
    }

    public int getNumEnlaces() {
		return numEnlaces;
	}

	public void setNumEnlaces(int numEnlaces) {
		this.numEnlaces = numEnlaces;
	}

	public Node getNode() {
		return node;
		
	}

	public void setNode(Node node) {
		this.node = node;
	}
	  public ArrayList<Enlace> getEnlaces()
	    {
	            return enlaces;
	    }
	
   
	
}
