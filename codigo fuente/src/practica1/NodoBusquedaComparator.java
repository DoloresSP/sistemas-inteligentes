package practica1;
import java.util.Comparator;


public class NodoBusquedaComparator implements Comparator<NodoArbol> {


	public int compare(NodoArbol o1, NodoArbol o2) {
		int r;
        if (o1.getValor() < o2.getValor())
        	r = -1;
        else if (o1.getValor() > o2.getValor())
            r = 1;
        else r = 0;
        
        return r;
	}

}