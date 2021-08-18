package algoritmo;

import java.util.Random;

public class Ladrao extends ProgramaLadrao {
	int timerGlobal = 0;
	// Códigos da visão
	final int LADRAO = 200, POUPADOR = 110, PASTA = 5, MOEDA = 4, BANCO = 3, PAREDE = 1, VAZIA = 0, FORA = -1,
			SEMVISAO = -2;

	// Códigos de movimentação
	final int NORTE = 1, SUL = 2, LESTE = 3, OESTE = 4;
	int nordeste = new Random().nextBoolean() ? NORTE : LESTE;
	int noroeste = new Random().nextBoolean() ? NORTE : OESTE;
	int sudeste = new Random().nextBoolean() ? SUL : LESTE;
	int sudoeste = new Random().nextBoolean() ? SUL : OESTE;

	// Códigos da visão
	final int[] VISAO_NORTE = { 2, 7 };
	final int[] VISAO_SUL = { 16, 21 };
	final int[] VISAO_LESTE = { 12, 13 };
	final int[] VISAO_OESTE = { 10, 11 };
	final int[] VISAO_NORDESTE = { 3, 4, 8, 9 };
	final int[] VISAO_NOROESTE = { 0, 1, 5, 6 };
	final int[] VISAO_SUDESTE = { 17, 18, 22, 23 };
	final int[] VISAO_SUDOESTE = { 14, 15, 19, 20 };

	// Códigos do olfato
	int olfatoNorte = 1;
	int olfatoSul = 6;
	int olfatoLeste = 4;
	int olfatoOeste = 3;
	int olfatoNordeste = 2;
	int olfatoNoroeste = 0;
	int olfatoSudeste = 7;
	int olfatoSudoeste = 5;

	int[] visao;
	int[] olfato;
	int moedas;
	int roubos;

	public void atualizarVariaveis() {
		if (sensor.getNumeroDeMoedas() > moedas) {
			roubos++;
			moedas = sensor.getNumeroDeMoedas();
		}
		visao = sensor.getVisaoIdentificacao();
		olfato = sensor.getAmbienteOlfatoLadrao();
	}

	public int moverParaDirecao(int direcao) {
		int refletida = refletirObstaculos(direcao);
		return (refletida != direcao) ? refletida : direcao;
	}

	// Verifica se a direção que o usuário quer se mover não é vazia ou um poupador.
	// Caso não seja, retorna outra direção aleatória
	public int refletirObstaculos(int direcao) {
		if (direcao == NORTE && (visao[VISAO_NORTE[0]] != VAZIA || visao[VISAO_NORTE[0]] != POUPADOR)) {
			return Util.selecionarAleatorio(new int[] { SUL, LESTE, OESTE });
		} else if (direcao == SUL && (visao[VISAO_SUL[0]] != VAZIA || visao[VISAO_SUL[0]] != POUPADOR)) {
			return Util.selecionarAleatorio(new int[] { NORTE, LESTE, OESTE });
		} else if (direcao == LESTE && (visao[VISAO_LESTE[0]] != VAZIA || visao[VISAO_LESTE[0]] != POUPADOR)) {
			return Util.selecionarAleatorio(new int[] { NORTE, SUL, OESTE });
		} else if (direcao == OESTE && (visao[VISAO_OESTE[0]] != VAZIA || visao[VISAO_OESTE[0]] != POUPADOR)) {
			return Util.selecionarAleatorio(new int[] { NORTE, SUL, LESTE });
		} else
			return direcao;
	}

	// Descobre a direção com base nos arrays de visão ou de olfato
	public int descobrirDirecao(String arr, int num) {
		if (arr == "visao") {
			if (Util.contains(VISAO_NORTE, num)) {
				return NORTE;
			} else if (Util.contains(VISAO_SUL, num)) {
				return SUL;
			} else if (Util.contains(VISAO_LESTE, num)) {
				return LESTE;
			} else if (Util.contains(VISAO_OESTE, num)) {
				return OESTE;
			} else if (Util.contains(VISAO_SUDESTE, num)) {
				return sudeste;
			} else if (Util.contains(VISAO_SUDOESTE, num)) {
				return sudoeste;
			} else if (Util.contains(VISAO_NORDESTE, num)) {
				return nordeste;
			} else if (Util.contains(VISAO_NOROESTE, num)) {
				return noroeste;
			} else
				return 0;
		} else {
			if (olfatoNorte == num) {
				return NORTE;
			} else if (olfatoSul == num) {
				return SUL;
			} else if (olfatoLeste == num) {
				return LESTE;
			} else if (olfatoOeste == num) {
				return OESTE;
			} else if (olfatoSudeste == num) {
				return sudeste;
			} else if (olfatoSudoeste == num) {
				return sudoeste;
			} else if (olfatoNordeste == num) {
				return nordeste;
			} else if (olfatoNoroeste == num) {
				return noroeste;
			} else
				return 0;
		}
	}

	// Verifica se tem algum poupador. Se tiver, vai atrás dele
	public int buscarPoupador() {
		int indicePoupador = Util.indexOf(visao, POUPADOR);
		return (indicePoupador != -1) ? descobrirDirecao("visao", indicePoupador) : 0;
	}

	public void printVisaoAgente() {
		int counter = 0;
		int mainCounter = 0;
		System.out.println(sensor.getPosicao().getX() + " " + sensor.getPosicao().getY() + "\n");
		for (int i : sensor.getVisaoIdentificacao()) {
			if (mainCounter == 12) {
				System.out.print("x ");
				counter++;
			}
			if (counter == 5) {
				System.out.println();
				counter = 0;
			}
			System.out.print(i + " ");
			mainCounter++;
			counter++;

		}
		System.out.println("\n");

	}

	// Função principal
	public int acao() {
		atualizarVariaveis();
		int poupador = buscarPoupador();
		if (poupador != 0) {
			return moverParaDirecao(poupador);
		} else {
			return moverParaDirecao((int) (Math.random() * 5));
		}

	}

}