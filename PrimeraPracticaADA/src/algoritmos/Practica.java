package algoritmos;

import java.io.*;

/**
 * @author sanlosa
 * @author sergsan
 * @author juatorr
 *
 */

public class Practica {

	static double REPETICIONES = 20; //Número de veces que se repetirá cada algoritmo.
	
	public static void main(String[] args) {
		
		 try {
            BufferedWriter ficheroSalida = new BufferedWriter( new FileWriter(new File("Datos.txt")));
	 
            for(int i=1; i<=10; i++) {
            	int[] v = new int[i*10000];
            	double[] datos1 = llama(v,1);
            	double[] datos2 = llama(v,2);
            	double[] datos3 = llama(v,3);
            	//Obtenemos los datos en un fichero
			
	            ficheroSalida.write("DATOS VECTOR TAM: " + i*10000 );
	            
	            ficheroSalida.newLine();
	            ficheroSalida.write("Algoritmo 1:");
	            ficheroSalida.newLine();
	            ficheroSalida.write("	Comparaciones: " +String.format("%f",datos1[0]));
	            ficheroSalida.newLine();
	            ficheroSalida.write("	Asignaciones: " + String.format("%f",datos1[1]));
	            ficheroSalida.newLine();
	            ficheroSalida.write("	Tiempo: " + String.format("%f",datos1[2]) + "seg");
	            
	            ficheroSalida.newLine();
	            ficheroSalida.write("Algoritmo 2:");
	            ficheroSalida.newLine();
	            ficheroSalida.write("	Comparaciones: " + String.format("%f",datos2[0]));
	            ficheroSalida.newLine();
	            ficheroSalida.write("	Asignaciones: " + String.format("%f",datos2[1]));
	            ficheroSalida.newLine();
	            ficheroSalida.write("	Tiempo: " +String.format("%f",datos2[2]) + "seg");
	            
	            ficheroSalida.newLine();
	            ficheroSalida.write("Algoritmo 3:");
	            ficheroSalida.newLine();
	            ficheroSalida.write("	Comparaciones: " + String.format("%f",datos3[0]));
	            ficheroSalida.newLine();
	            ficheroSalida.write("	Asignaciones: " + String.format("%f",datos3[1]));
	            ficheroSalida.newLine();
	            ficheroSalida.write("	Tiempo: " + String.format("%f",datos3[2]) + "seg");
	            ficheroSalida.newLine();
	            ficheroSalida.newLine();
	            
	       }
            ficheroSalida.write("FINAL");
           ficheroSalida.close();
       	}
	    catch (IOException errorDeFichero){
            System.out.println("Ha habido problemas: " + errorDeFichero.getMessage() );
		}
		
		System.out.println("Final de programa.");
		
	}
	/* LLama al algoritmo ordenaX varias veces con vectores de contenido aleatorio.
	 * Entradas:
	 * 		vector donde realizar la ordenación 
	 * 		algoritmo que queremos utilizar
	 * Devuelve el tiempo de ejecución medio y el número de operaciones realizadas
	 * en forma de vector: [comp, asign, tiempo].
	 */
	static double[] llama(int v[], int algoritmo) {
		double tiempo = 0; //Tiempo medio que tarda en ejecutar el algoritmo (seg)
		double[] op = new double[2];
		double[] opTotal = {0,0};
		double[] resultado = new double[3];
		for(int i = 0; i<REPETICIONES; i++) { //Recoge los datos de cada repetición y los suma
			rellena(v);
			long inicio, fin;
			if(algoritmo == 1) {
				inicio = System.currentTimeMillis();
				op = ordena1(v, v.length);
				fin = System.currentTimeMillis();
			}else if(algoritmo == 2) {
				inicio = System.currentTimeMillis();
				op = ordena2(v, v.length);
				fin = System.currentTimeMillis();
			}else {
				inicio = System.currentTimeMillis();
				op = ordena3(v, v.length);
				fin = System.currentTimeMillis();
			}
			long t = fin - inicio;
			opTotal[0] += op[0];
			opTotal[1] += op[1];
			tiempo = tiempo + t;
		}
		tiempo = tiempo/1000; //Pasamos a segundos
		//Calculamos medias
		tiempo = tiempo / REPETICIONES;
		resultado[0] = opTotal[0] / REPETICIONES ;
		resultado[1] = opTotal[1] / REPETICIONES;
		resultado[2] = tiempo;
		return resultado;			
	}
	
	
	
	 // Rellena el vector dado con números aleatorios. 
	static void rellena(int v[]) {
		for(int i = 0; i < v.length; i++ ) {
			v[i] = (int) Math.floor(Math.random()*(100001)); //Número entero entre 0 y 100k
		}
	}
	
	/*Devuelve el número de operaciones (en forma de vector [comparaciones,asignaciones])
	 * que realiza el algoritmo 1.
	 */
	static double[] ordena1(int v[],int tam) {
		double asign = 0; //Número de asignaciones
		double comp = 0; //Número de comparaciones
		/* v con índices de 0 a tam-1 */
		int i,j,temp;
		i = 1;
		j = 2;
		while (i < tam) {
			comp++;
			if (v[i-1] <= v[i]) {
				i = j;
				j = j + 1;
			} else {
				temp=v[i-1];
				v[i-1]=v[i];
				v[i]=temp;
				asign = asign + 3;
				i = i - 1;
				if (i == 0) {
					i = 1;
				}
			}
		}
		double[] op = {comp, asign};
		return op;
	}
	
	/*Devuelve el número de operaciones (en forma de vector [comparaciones,asignaciones])
	 * que realiza el algoritmo 2.
	 */
	static double[] ordena2(int v[], int tam) {
		double asign = 0;
		double comp = 0;
		/* v con índices de 0 a tam-1 */
		int h, r, i, j, w;
		r = tam-1;
		h = 1;
		while (h <= r/9) {
			h = 3*h+1;
		}
		while ( h > 0 ) {
			for (i = h; i <= r; i++) {
				j = i;
				w = v[i];
				asign++;
				while ((j >= h) && (w<v[j-h])) {
					comp++;
					v[j] = v[j-h];
					asign++;
					j = j - h;
				}
				if(j>= h) {
					comp++;
				}
				v[j] = w;
				asign++;
			}
			h = h / 3;
		}
		double[] op = {comp, asign};
		return op;
	}
	
	/*Devuelve el número de operaciones (en forma de vector [comparaciones,asignaciones])
	 * que realiza el algoritmo 3.
	 */
	static double[] ordena3(int v[], int tam) {
		
		/* v con índices de 0 a tam-1 */
		double[] resultado = ordena3Rec(v, 0, tam-1);
		return resultado;
	}
	static double[] ordena3Rec(int v[], int l, int r) {
		double asign = 0;
		double comp = 0;
		int i,j,w,t;
		if (r > l) {
			i = l-1;
			j = r;
			w = v[r];
			asign++;
			while (true) {
				i = i + 1;
				while (v[i]< w) {
					comp++;
					i += 1;
				}
				comp++;
				j = j - 1;
				while (w<v[j]) {
					comp++;
					if (j == l) {
						break;
					}
					j = j - 1;
				}
				comp++;
				if (i >= j) {
					break;
				}
				t = v[i];
				v[i] = v[j];
				v[j] = t;
				asign += 3;
			}
			t = v[i];
			v[i] = v[r];
			v[r] = t;
			asign += 3;
			ordena3Rec(v, l, i-1);
			ordena3Rec(v, i+1, r);
		}
		double[] op = {comp, asign};
		return op;
	}
	
}
