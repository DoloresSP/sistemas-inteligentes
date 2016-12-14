package practica1;

public class Enlace {

	 private Nodo NodoDestino;//Nodo destino
     private double distancia;//distancia en metros

     public Enlace(Nodo desti, double dis)
     {
              
    	 	 NodoDestino = desti;
             distancia = dis;
     }

     

     public double getDistancia() {
		return distancia;
	}



	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}





	public Nodo getDestino()
     {
             return NodoDestino;
     }

   	

}
