import java.util.HashSet;
import java.util.Set;

/**
 * @author
 * @date 20/11/2015
 */

/**
 * Clase que sirve para crear cada uno de los hilos de los jugadores
 */
class Jugador {
	final int TOTAL_CARTON = 5; // Cantidad de números por cartón
	final int TOTAL_BOMBO = 10; // Números posibles del bombo
	int idJugador; // Identificador del jugador
	Set<Integer> carton; // Para almacenar los números pendientes de acertar

	/**
	 * @param identificador
	 *            del jugador
	 */
	Jugador(int idJugador) {
		this.idJugador = idJugador;
		carton = new HashSet<Integer>();
		while (carton.size() < TOTAL_CARTON)
			carton.add((int) Math.floor(Math.random() * TOTAL_BOMBO) + 1);
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
	 * @param numero a tachar
	 */
	void tacharNum(Integer numero) {
		carton.remove(numero);
	}

}

/**
 * Clase que sirve para el hilo del presentador
 */
class Presentador {

}

/**
 * Clase que se utiliza para crear el objeto compartido entre todos los hilos
 * del programa
 */
class Bombo {
	final int TOTAL_BOMBO = 10; // Números posibles del bombo
	Set<Integer> bombo; // Para almacenar los valores que van saliendo
	Integer ultNumero; // Último número del bombo

	/**
	 * Inicializa vacío el bombo
	 */
	Bombo() {
		bombo = new HashSet<Integer>();
	}

	/**
	 * @return El número que sale del bombo
	 */
	Integer sacarNum() {
		Integer bolita=0;
		int cantidadBolas = bombo.size();
		if (cantidadBolas < TOTAL_BOMBO) {
			do {
				ultNumero = (int) Math.floor(Math.random() * TOTAL_BOMBO) + 1;
				bombo.add(ultNumero);
				bolita=ultNumero;
			} while (cantidadBolas == bombo.size());
			System.out.println("Ha salido el número: " + ultNumero);
		} else
			System.out.println("Ya han salido todas las bolas");
		return bolita;
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

/**
 * Clase principal desde la que se inicializa el juego del Bingo
 */
public class Bingo {
	public static void main(String[] args) {

	}
}