import java.util.HashSet;
import java.util.Scanner;

/**
 * @author ------------------ FERNANDO SÁNCHEZ MARTÍNEZ    DAMP_2  -----------------------
 * @date 20/11/2015
 */

//----------------------------------------------------------------------------------------------------//

/**
 * Clase que sirve para crear cada uno de los hilos de los jugadores
 */
class Jugador extends Thread {
	final int TOTAL_CARTON = 5; // Cantidad de números por cartón
	final int TOTAL_BOMBO = 10; // Números posibles del bombo
	int idJugador; // Identificador del jugador
	HashSet<Integer> carton; // Para almacenar los números pendientes de acertar
	Bombo b;

	/**
	 * @param identificador
	 *            del jugador
	 */
	Jugador(int idJugador, Bombo b) {
		this.b = b;
		this.idJugador = idJugador;
		carton = new HashSet<Integer>();
		while (carton.size() < TOTAL_CARTON)
			carton.add((int) Math.floor(Math.random() * TOTAL_BOMBO) + 1);
		System.out.println("CARTON JUGADOR: " + idJugador + carton);
	}

	/**
	 * Muestra el cartón por pantalla con los números pendientes
	 */
	void imprimeCarton() {
		System.out.print("Pendientes jugador " + idJugador + ": ");
		for (Integer integer : carton)
			System.out.print(integer + " ");
		System.out.println();
	}

	/**
	 * Tacha el número del cartón en caso de que exista
	 * 
	 * @param numero
	 *            a tachar
	 */
	void tacharNum(Integer numero) {
		carton.remove(numero);
	}

	/**
	 * Método encargado de las acciones del jugador.
	 */
	public void run() {
		while (carton.size() > 0) {// mientras que el cartón no tenga tachados
									// todos los números

			// imprimeCarton();
			b.consultar();// se consulta
			System.out.println("El jugador  " + idJugador + "  va ha jugar ");
			tacharNum(b.ultNumero);// se tacha el ultimo numero que ha salido
			for (Integer integer : b.bombo) {// se comprueba que hayamos tachado
												// todos los números que han
												// salido, en su defecto se
												// tachan.
				carton.remove(integer);
			}

			System.out.println("El jugador  " + idJugador + "  ha jugado ");
			imprimeCarton();

		}
		System.out.println("el jugador" + idJugador + "ha hecho BINGO");

	}

}

// ----------------------------------------------------------------------------------------------------//

/**
 * Clase que sirve para el hilo del presentador
 */
class Presentador extends Thread {
	Bombo c;
	// Bingo bing;
	int aux;

	Presentador(Bombo c) {
		this.c = c;

	}

	public void run() {
		try {

			for (int i = 0; i < c.TOTAL_BOMBO; i++) {
				Thread.sleep(2000);
				c.sacarNum();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		/*
		 * if (c.numvecJugado >= Bingo.numjugadores) { c.sacarNum();
		 * c.numvecJugado = 0; }else if (c.numvecJugado == 0){ c.sacarNum(); }
		 */

		// c.sacarNum();
		/*
		 * while (c.numvecJugado >= 5) { for (int i = 0; i < c.TOTAL_BOMBO; i++)
		 * { c.sacarNum(); c.numvecJugado = 0; } }
		 */
	}

}

// ----------------------------------------------------------------------------------------------------//

/**
 * Clase que se utiliza para crear el objeto compartido entre todos los hilos
 * del programa
 */
class Bombo {
	int numvecJugado = 0;
	int hayBola = 0;
	Jugador jug;
	final int TOTAL_BOMBO = 10; // Números posibles del bombo
	HashSet<Integer> bombo; // Para almacenar los valores que van saliendo
	Integer ultNumero; // Último número del bombo

	int aux = Bingo.numjugadores;

	/**
	 * Inicializa vacío el bombo
	 */
	Bombo() {

		bombo = new HashSet<Integer>();

	}

	/**
	 * @return El número que sale del bombo
	 */
	synchronized void sacarNum() {

		while (hayBola != 0 /* && numvecJugado!= aux */) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		sacarNum2();
		imprimirBombo();
		hayBola++;
		// hayBola=0;
		notify();

	}

	Integer sacarNum2() {
		Integer bolita = 0;
		int cantidadBolas = bombo.size();
		if (cantidadBolas < TOTAL_BOMBO) {
			do {
				ultNumero = (int) Math.floor(Math.random() * TOTAL_BOMBO) + 1;
				bombo.add(ultNumero);
				bolita = ultNumero;
			} while (cantidadBolas == bombo.size());
			System.out.println("Ha salido el número: " + ultNumero);
		} else
			System.out.println("Ya han salido todas las bolas");
		return bolita;
	}

	synchronized void consultar() {
		while (hayBola == 0 /* && numvecJugado>= aux */) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		numvecJugado++;
		hayBola--;
		notify();

	}

	/**
	 * Muestra todas las bolas que han salido hasta el momento
	 */
	void imprimirBombo() {
		System.out.print("Bolas sacadas hasta el momento: ");
		for (Integer integer : bombo)
			System.out.print(integer + " ");
		System.out.println();
	}

}

// ----------------------------------------------------------------------------------------------------//
// ----------------------------------------------------------------------------------------------------//

/**
 * Clase principal desde la que se inicializa el juego del Bingo
 */
public class Bingo {
	static int numjugadores;
	static Scanner in = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.println("SE ABRE EL BINGO");
		System.out.println("¿CUANTOS JUGADORES VAN A JUGAR?");
		numjugadores = in.nextInt();

		Bombo bomb = new Bombo();
		Thread jugador;

		for (int i = 1; i <= numjugadores; i++) {
			jugador = new Jugador(i, bomb);
			System.out.println("el jugador" + i + "esta preparado");
			jugador.start();
		}

		Thread present = new Presentador(bomb);
		present.start();

	}
}