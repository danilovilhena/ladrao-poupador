package algoritmo;

import java.util.Random;

public class Ladrao extends ProgramaLadrao {
	// Códigos da visão
	int ladrao = 200, poupador = 110, pasta = 5, moeda = 4, banco = 3, parede = 1, vazia = 0, fora = -1, semvisao = -2;

	// Códigos de movimentação
	int norte = 1, sul = 2, leste = 3, oeste = 4;
	int nordeste = new Random().nextBoolean() ? norte : leste;
	int noroeste = new Random().nextBoolean() ? norte : oeste;
	int sudeste = new Random().nextBoolean() ? sul : leste;
	int sudoeste = new Random().nextBoolean() ? sul : oeste;

	// Códigos da visão
	int[] visaoNorte = { 2, 7 };
	int[] visaoSul = { 16, 21 };
	int[] visaoLeste = { 12, 13 };
	int[] visaoOeste = { 10, 11 };
	int[] visaoNordeste = { 3, 4, 8, 9 };
	int[] visaoNoroeste = { 0, 1, 5, 6 };
	int[] visaoSudeste = { 17, 18, 22, 23 };
	int[] visaoSudoeste = { 14, 15, 19, 20 };

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
		if (direcao == norte && (visao[visaoNorte[0]] != vazia || visao[visaoNorte[0]] != poupador)) {
			return Util.selecionarAleatorio(new int[] { sul, leste, oeste });
		} else if (direcao == sul && (visao[visaoSul[0]] != vazia || visao[visaoSul[0]] != poupador)) {
			return Util.selecionarAleatorio(new int[] { norte, leste, oeste });
		} else if (direcao == leste && (visao[visaoLeste[0]] != vazia || visao[visaoLeste[0]] != poupador)) {
			return Util.selecionarAleatorio(new int[] { norte, sul, oeste });
		} else if (direcao == oeste && (visao[visaoOeste[0]] != vazia || visao[visaoOeste[0]] != poupador)) {
			return Util.selecionarAleatorio(new int[] { norte, sul, leste });
		} else
			return direcao;
	}

	// Descobre a direção com base nos arrays de visão ou de olfato
	public int descobrirDirecao(String arr, int num) {
		if (arr == "visao") {
			if (Util.contains(visaoNorte, num)) {
				return norte;
			} else if (Util.contains(visaoSul, num)) {
				return sul;
			} else if (Util.contains(visaoLeste, num)) {
				return leste;
			} else if (Util.contains(visaoOeste, num)) {
				return oeste;
			} else if (Util.contains(visaoSudeste, num)) {
				return sudeste;
			} else if (Util.contains(visaoSudoeste, num)) {
				return sudoeste;
			} else if (Util.contains(visaoNordeste, num)) {
				return nordeste;
			} else if (Util.contains(visaoNoroeste, num)) {
				return noroeste;
			} else
				return 0;
		} else {
			if (olfatoNorte == num) {
				return norte;
			} else if (olfatoSul == num) {
				return sul;
			} else if (olfatoLeste == num) {
				return leste;
			} else if (olfatoOeste == num) {
				return oeste;
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
		int indicePoupador = Util.indexOf(visao, poupador);
		return (indicePoupador != -1) ? descobrirDirecao("visao", indicePoupador) : 0;
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