package practica1;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Entity;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;
import org.openstreetmap.osmosis.core.task.v0_6.RunnableSource;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import org.openstreetmap.osmosis.xml.common.CompressionMethod;
import org.openstreetmap.osmosis.xml.v0_6.XmlReader;





public class Principal {
	
		
	static Grafo grafo= new Grafo();//Creamos el grafo;
	static Scanner sc = new Scanner(System.in);
	static int complejidadEspacial=0;
	static int estrategia=0;
	static int prof_max;
	static int ramificacion;
	
	static public void main(String[] args) throws IOException{
		File file= new File("map.osm"); // Para trabajar offile  
		System.out.println("Construyendo grafo...");
		
		long IdNodoInicial=0;
		long objetivo1=0;
		long objetivo2=0;
		long objetivo3=0;
		
		RandomAccessFile ficheroGPX = new RandomAccessFile("ficheroRuta.gpx", "rw");
		
		
		Sink sinkImplementation = new Sink() {
			public void initialize(Map<String, Object> metaData){};
			public void process (EntityContainer entityContainer){
			Entity entity = entityContainer.getEntity();
			if (entity instanceof Node ){
				
				Nodo nodo1= new Nodo((Node) entity);
				//System.out.println(nodo1.getNode().getId());
				grafo.addNodo(nodo1);//Añadir el objeto nodo a la tabla hash que hay en el grafo
				
				
			} else if (entity instanceof Way){

				
				Collection<Tag> tags = entity.getTags();//Sacar los tags de los nodos que hay en la via
				
				if (tags.iterator().hasNext()){
					Tag tagHg = tags.iterator().next();//Mirar el primer tag para saber si es residencial, peatonal o trunck
					String value = tagHg.getValue();
					if (value.equals("residential")  || value.equals("pedestrian") || value.equals("trunk")){
						List<WayNode> wayNodes =  ((Way) entity).getWayNodes(); //Guardamos en una lista solo los nodos de la via que nos interesan
				
						
						for( int i = 0; i<(wayNodes.size()-1);i++) { //enlazamos los nodos que hemos encontrado de esa calle
							
							Nodo nodoOrigen = grafo.getNodo(wayNodes.get(i).getNodeId()); //cogemos el nodo que hay guardado en la tabla hash pasandole el id
							Nodo nodoDestino = grafo.getNodo(wayNodes.get(i+1).getNodeId());
							
							grafo.addEnlace((Way)entity, nodoOrigen.getNode().getId(), nodoDestino.getNode().getId(), distancia_metros(nodoOrigen.getNode(), nodoDestino.getNode()));
						}
					}
				}
			
			}
			
			}
			
			public void release(){}
			public void complete(){}
			
		};
		CompressionMethod compression = CompressionMethod.None;
		RunnableSource reader= new XmlReader(file, false, compression);
		// Para usar el fichero XML descargado.

		// Para acceder directamente desde la BD de OSM online
		// Utilizar XmlDownloader
		
		reader.setSink(sinkImplementation);

		Thread readerThread = new Thread(reader);
		readerThread.start();
		
		while (readerThread.isAlive()) {
		    try {
		        readerThread.join();
		    } catch (InterruptedException e) {
		        /* No hacer nada */
		    }
		}
		
		Enumeration <Long> enumeration = grafo.getNodosTabla().keys();
		while (enumeration.hasMoreElements()){
			long id=enumeration.nextElement();
			if(grafo.getNodo(id).getNumEnlaces()==0){
				grafo.deleteNodosTabla(id);
			}

		}
		
		int eleccion=0;
		
		do{
			
			System.out.println("Elige uno de los siguientes problemas: \n1.Complejidad pequeña (1 OBJETIVO) \n2.Complejidad mediana (2 OBJETIVOS) \n3.Complejidad grande (3 OBJETIVOS)");
			eleccion=excepcion_numerica();
			
		}while(eleccion<1 || eleccion>3);
		
		Nodo nodoInicial=null;
		ArrayList <Long> objetivos = new ArrayList<Long>();
		
		switch(eleccion){
		case 1:
			IdNodoInicial=368287523;
			nodoInicial=grafo.getNodo(IdNodoInicial);
			objetivo1=804689378;
			objetivos.add(objetivo1);
			break;
		
		case 2:
			IdNodoInicial=522198143;
			nodoInicial=grafo.getNodo(IdNodoInicial);
			objetivo1=765309500;
			objetivo2=803292576;
			
			objetivos.add(objetivo1);
			objetivos.add(objetivo2);
			
			
			break;
			
		case 3:
			
			IdNodoInicial=522198143;
		      nodoInicial=grafo.getNodo(IdNodoInicial);
		      objetivo1=765309500;
		      objetivo2=803292576;
		      objetivo3=814770952;
		      objetivos.add(objetivo1);
		      objetivos.add(objetivo2);
		      objetivos.add(objetivo3);
			
			/*IdNodoInicial=804689126;
			nodoInicial=grafo.getNodo(IdNodoInicial);
			objetivo1=368287515;
			objetivo2=804689060;
			objetivo3=803292813;
			objetivos.add(objetivo1);
			objetivos.add(objetivo2);
			objetivos.add(objetivo3);*/
			
			break;
		
		}
	
		
		
		Estado estadoInicial= new Estado(IdNodoInicial,nodoInicial.getNode().getLatitude(), nodoInicial.getNode().getLongitude(), objetivos);

		
		
		
		Problema prob = new Problema (IdNodoInicial, estadoInicial);
		
		ArrayList <NodoArbol> solucion= new ArrayList<NodoArbol>();
		
		long time_start, time_end;
		time_start = System.currentTimeMillis();
		solucion = elegirEstrategia(prob);
		time_end = System.currentTimeMillis();
		double tiempoEjecucion= time_end - time_start;

		
		
		if(solucion!=null){
			System.out.println("DATOS DE LA SOLUCIÓN: \nProfundidad: "+ solucion.get(solucion.size()-1).getProfundidad()+ " Valor: "+ solucion.get(solucion.size()-1).getValor()+ " Costo: "+solucion.get(solucion.size()-1).getCosto());
			System.out.println("Complejidad espacial: "+complejidadEspacial);
			System.out.println("Complejidad temporal: "+ ( tiempoEjecucion ) +" milisegundos");
			System.out.println("Ramificiacion: "+ramificacion);

			ficherogpx(estadoInicial, solucion,ficheroGPX,nodoInicial.getNode().getId(),objetivos, estrategia,prof_max, tiempoEjecucion);

		}else{
			System.out.println("No tiene solucion");
		}
		
		
			
		
	}
	
	
	static ArrayList <NodoArbol> Busqueda (Grafo grafo, Problema problema, int estrategia, int prof_Max, int inc_Prof, int realizarPoda){
		  int prof_Actual=inc_Prof;
		  ArrayList <NodoArbol> solucion= new ArrayList<NodoArbol>();
		  solucion=null;
		  while (solucion == null && prof_Actual <= prof_Max){
		     solucion= BusquedaArbol(grafo, problema,estrategia,prof_Actual, realizarPoda);
		     prof_Actual=prof_Actual+inc_Prof;
		  }
		  return solucion;
		
		}
	
	static ArrayList <NodoArbol> BusquedaArbol (Grafo grafo, Problema prob,int estrategia, int prof_max, int realizarPoda){
		  //Proceso de inicialización
		  Frontera frontera = new Frontera();
		  
		  long accion[]=null;
		  NodoArbol padre=null;
		  NodoArbol nodo_inicial= new NodoArbol(padre, prob.getEstado(),0,accion,0,0);
		  
		  frontera.insertar(nodo_inicial);
	
		  complejidadEspacial++;
		  
		  boolean solucion=false;
		  NodoArbol nodo_actual=null;
		  
		  
		  //Bucle de búsqueda
		  while( solucion==false && frontera.esVacia()==false ){
			  ArrayList <Sucesor> ls= new ArrayList<Sucesor>();
			  ArrayList <NodoArbol> ln= new ArrayList<NodoArbol>();
			  
			  nodo_actual=frontera.getPrimero();

		    if (prob.estado_meta(nodo_actual.getEstado())){
		       solucion=true;
		    }else{
		       ls=prob.Sucesores(grafo, nodo_actual.getEstado());
		    
		       ln=nodo_actual.CreaListaNodosArbol(frontera, prob, ls,nodo_actual,prof_max,estrategia, grafo, realizarPoda);
		       
		       int ramificacionAct=ln.size();
		       
		       if(ramificacionAct> ramificacion){
		    	   ramificacion=ramificacionAct;
		       }
		       
		       complejidadEspacial=complejidadEspacial + ln.size();
		 
		       frontera.insertarLista(ln);
		    }
		  }
		  //Resultado 
		  if (solucion==true){
			  
			 return creaSolucion(nodo_actual);
		  }else{
			  return null;
		  }
		       
		
		}
	
	static ArrayList <NodoArbol> creaSolucion(NodoArbol nodo_actual){
		ArrayList <NodoArbol> solucion=new ArrayList <NodoArbol>();
		Stack <NodoArbol> pila=new Stack <NodoArbol>();
		
		while (nodo_actual!= null){
			pila.push(nodo_actual);
			nodo_actual=nodo_actual.getPadre();
		}
		
		while(!pila.isEmpty()){
			solucion.add(pila.pop());
		}
		
		
		return solucion;
	}
	
	
	
	static ArrayList <NodoArbol> elegirEstrategia (Problema prob){
		int prof_max;
		int inc_prof;
		int estrategia;
		int realizarPoda;
		ArrayList <NodoArbol> sol= new ArrayList<NodoArbol>();
		do{
			System.out.println("Eliga la estrategia de búsqueda deseada: (Introduce un número del 1 al 5)\n1.Anchura \n2.Profundidad \n3.Costo uniforme \n4.Voraz \n5.A*");
			estrategia=excepcion_numerica();
				
		}while(estrategia<1 || estrategia>5);
		
		System.out.println("Elige la profundidad máxima deseada: ");
		prof_max=excepcion_numerica();
		
		do{
			System.out.println("¿Desea realizar poda? (Usted debe introducir un 1 o un 2) \n1.Si \n2.No");
			realizarPoda=excepcion_numerica();
		}while(realizarPoda!=1 && realizarPoda!=2);
		
		
		
		inc_prof=prof_max;
		
		
		sol= Busqueda(grafo, prob, estrategia, prof_max, inc_prof, realizarPoda);
		
		
		
		
		return sol;
	}
	
	
	static double distancia_metros(Node origen, Node destino){
		double lat1= origen.getLatitude();
		double long1 = origen.getLongitude();
		double lat2 = destino.getLatitude();
		double long2 = destino.getLongitude();
		double x1=merc_x(long1);
		double x2=merc_x(long2);
		double y1=merc_y(lat1);
		double y2=merc_y(lat2);
		return (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)));

		
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
	
	public static int excepcion_numerica(){
		boolean continuar=true;
		int x=1;
		do{
			try{
				x=sc.nextInt();
				continuar=false;
				
			}
			catch(InputMismatchException e){
				System.out.println("Usted debe introducir un dato numérico");
				sc.nextLine();
				
				
			}
				
		}while(continuar);
		
		return x;
		
	}
	
	public static void ficherogpx(Estado estadoInicial, ArrayList <NodoArbol> solucion, RandomAccessFile ficheroGPX,long idPadre, ArrayList <Long> objetivos, int estrategia, int prof_max, double tiempoEjecucion) throws IOException{
		NodoArbol aux=null;
		
		ficheroGPX.writeBytes("<?xml version=\""+1.0+"\" encoding=\"UTF-8\"?>");
		ficheroGPX.writeBytes("\n<gpx>");
		ficheroGPX.writeBytes("\n<wpt lat=\""+estadoInicial.getLatitud()+"\" lon=\""+estadoInicial.getLongitud()+"\">");
		ficheroGPX.writeBytes("\n\t<name>"+estadoInicial.getIdNodo()+"</name>");
		ficheroGPX.writeBytes("\n</wpt>");
		for(int i=0; i<objetivos.size(); i++){
			ficheroGPX.writeBytes("\n<wpt lat=\""+ grafo.getNodo(objetivos.get(i)).getNode().getLatitude()+"\" lon=\""+grafo.getNodo(objetivos.get(i)).getNode().getLongitude()+"\">");
			ficheroGPX.writeBytes("\n\t<name>"+objetivos.get(i)+"</name>");
			ficheroGPX.writeBytes("\n</wpt>");
		}
		
		ficheroGPX.writeBytes("\n<trk>");
		ficheroGPX.writeBytes("\n\t<name>Route</name>");
		ficheroGPX.writeBytes("\n\t<trkseg>");
		
		
		for(int i=0; i<solucion.size(); i++){
			aux=solucion.get(i);
			

			ficheroGPX.writeBytes("\n\t\t<trkpt lat=\""+aux.getEstado().getLatitud()+"\" lon=\""+aux.getEstado().getLongitud()+"\">");
			ficheroGPX.writeBytes("\n\t\t\t<ele>0</ele>");
			ficheroGPX.writeBytes("\n\t\t\t<name>"+aux.getEstado().getIdNodo()+"</name>");
			ficheroGPX.writeBytes("\n\t\t\t<cost>"+aux.getCosto()+"</cost>");
			ficheroGPX.writeBytes("\n\t\t\t<deep>"+aux.getProfundidad()+"</deep>");
			ficheroGPX.writeBytes("\n\t\t</trkpt>");
			
		}
		
		ficheroGPX.writeBytes("\n\t</trkseg>");
		ficheroGPX.writeBytes("\n</trk>");
		ficheroGPX.writeBytes("\n</gpx>");
		ficheroGPX.close();
	}
	
}