package contenedores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Algoritmo {
	public static void main(String[] args) {
		ArrayList<Container> containers = obtenerContenedores();
		int pos = Algoritmo1(containers); // Obtener tabla programacion dinamica y asi obtenemos el primero
		ArrayList<Container> apilados = Algoritmo2(containers, pos); //Algoritmo que elimina pesos sabiendo el primero
		System.out.println("Numero de contenedores: " + apilados.size());
		for (int i = 0; i < apilados.size(); i++) {
			System.out.println("Contenedor " + apilados.get(i).getSerialNum());
		}
	}

	public static ArrayList<Container> obtenerContenedores() {
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		ArrayList<Container> containers = new ArrayList<>();
		try {
			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).
			archivo = new File("entrada.txt");
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);

			// Lectura del fichero
			int numContainer = Integer.parseInt(br.readLine());
			String linea;
			// Recorremos el fichero y creamos la lista de containers.
			for (int i = 0; i < numContainer; i++) {
				linea = br.readLine();
				String datContainer[] = linea.split(" ");
				Container container = new Container(Integer.parseInt(datContainer[0]),
						Integer.parseInt(datContainer[1]), i + 1);
				containers.add(container);
			}
			fr.close();
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return containers;
	}
	
	/**
	 * Primer algoritmo, basado en programacion dinamica, con ello obtenemos el contenedor mas optimo para colocar abajo
	 * de la pila.
	 * De aqui tambien podemos obtener de manera correcta el numero exacto de contenedores que se pueden apilar
	 * @param containers
	 * @return
	 */
	public static int Algoritmo1(ArrayList<Container> containers) {
		int n = containers.size();
		int w = 10000; // Como pesos maximos son 5000 y cargas maximas son 5000, la suma
		int[][] tabla = new int[n][w];
		//Construccion de la tabla
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < w; j++) {
				if (i == 0 && j < containers.get(i).getWeight()) {
					tabla[i][j] = 0;
				} else if (i == 0) {
					tabla[i][j] = containers.get(i).getMaxLoad();
				} else if (j < containers.get(i).getWeight()) {
					tabla[i][j] = tabla[i - 1][j];
				} else {
					tabla[i][j] = Math.max(tabla[i - 1][j],
							tabla[i - 1][j - containers.get(i).getWeight()] + containers.get(i).getMaxLoad());
				}
			}
		}

		//A partir de aqui recorremos la tabla de final al principio y encontramos el primer contenedor
		int[] sol = new int[n];

		for (int i = 0; i < n; i++) {
			sol[i] = 0;
		}
		int i = n - 1;
		int j = containerMaxLoad(containers).getMaxLoad() + containerMaxWeigth(containers).getWeight();
		while (i >= 0) {
			if ((i > 0 && (tabla[i][j] == tabla[i - 1][j])) || (i == 0 && (tabla[i][j] == 0))) {
				i = i - 1;
			} else {
				sol[i] = 1;
				j = j - containers.get(i).getWeight();
				i = i - 1;
			}
		}
		int flag = 0;
		int pos = 0;
		for (int k = 0; k < n; k++) {
			if (flag == 0 && sol[k] == 1) {
				flag = 1;
				pos = k;
			}
		}
		return pos;
	}
	/**
	 * Segundo algoritmo basado en un algoritmo voraz, se encarga de buscar y eliminar contenedores con 
	 * pesos maximos por encima del contenedor (i) y lo hace para cada contenedor
	 * @param contenedoresDes Array de contenedores desapilados
	 * @param pos Primer contenedor a colocar abajo
	 * @return Array de contenedores apilados
	 */
	public static ArrayList<Container> Algoritmo2(ArrayList<Container> contenedoresDes, int pos) {
		Container contenedorMax = contenedoresDes.get(pos); // Obtiene el contenedor que soporta mas peso
		ArrayList<Container> apilados = new ArrayList<>(); // Crea arraylist donde va apilando la solucion
		apilados.add(contenedorMax); // Añadimos abajo del todo el contenedor que mas soporta
		for (int i = 0; i < contenedorMax.getSerialNum(); i++) {
			contenedoresDes.remove(0);
		}
		// En el for, elimina del arraylist que se pasa todos los contenedores que estan
		// por debajo del encontrado.
		borrarPesosMax(contenedoresDes, contenedorMax.getMaxLoad()); 
		
		// Bucle hasta que el arraylist que se pasa, que vamos borrando de ese y
		// añadiendo
		// al de la solucion, quede vacio
		while (!contenedoresDes.isEmpty()) { 
			apilados.add(contenedoresDes.get(0)); // Añadimos el primer contenedor del arraydesapilado al arraysolucion
			borrarPesosMax(contenedoresDes, contenedoresDes.get(0).getMaxLoad()); // Lllamamos para borrar los pesos
																					// maximos
			contenedoresDes.remove(0); // Eliminamos el añadido del arraydesapilado para ir vaciandolo

		}

		return apilados;
	}

	/**
	 * Esta funcion lo que hace es contar el peso total que tienen todos los
	 * siguientes contenedores en el arraylist, si este peso es mayor del que
	 * soporta el que estamos mirando entonces va eliminando el contenedor que mas
	 * pese cada vez, hasta que el peso sea menor o igual que el que se soporta
	 * 
	 * @param contenedores
	 * @param pesoActAbajo
	 */
	public static void borrarPesosMax(ArrayList<Container> contenedores, int pesoActAbajo) {
		int pesoTotal = 0;
		Container contenedorBorrar;
		for (int i = 1; i < contenedores.size(); i++) {
			pesoTotal += contenedores.get(i).getWeight();
		}
		while (pesoActAbajo < pesoTotal) {
			contenedorBorrar = containerMaxWeigth(contenedores);
			contenedores.remove(contenedorBorrar);
			pesoTotal -= contenedorBorrar.getWeight();
		}
	}
	
	/**
	 * Esta funcion busca el contenedor que mas peso soporta y es el que devuelve
	 * @param contenedores array de contenedores
	 * @return contenedorMax Contenedor que mas peso aguanta
	 */
	public static Container containerMaxLoad(ArrayList<Container> contenedores) {
		int maxLoad = 0;
		Container contenedorMax = contenedores.get(0);
		for(int i = 0; i<contenedores.size();i++) {
			if(contenedores.get(i).getMaxLoad() > maxLoad) {
				maxLoad= contenedores.get(i).getMaxLoad();
				contenedorMax = contenedores.get(i);
			}
		}
		return contenedorMax;	
	}

	/**
	 * Esta funcion busca el contenedor que mas pesa dentro del array
	 * 
	 * @param contenedores array de contenedores
	 * @return contenedor que mas pesa
	 */
	public static Container containerMaxWeigth(ArrayList<Container> contenedores) {
		int maxWeigth = 0;
		Container contenedorMaxW = contenedores.get(0);
		for (int i = 0; i < contenedores.size(); i++) {
			if (contenedores.get(i).getWeight() > maxWeigth) {
				maxWeigth = contenedores.get(i).getWeight();
				contenedorMaxW = contenedores.get(i);
			}
		}
		return contenedorMaxW;
	}
}
