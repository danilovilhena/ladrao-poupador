package algoritmo;

import java.util.Random;

public class Ladrao extends ProgramaLadrao {
	// Códigos da visão
	int ladrao = 200, poupador = 100, pasta = 5, moeda = 4, banco = 3, parede = 1, vazia = 0, fora = -1, semvisao = -2;

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

		if (refletida != direcao) {
			System.out.println("Refletiu");
			return refletida;
		} else
			return direcao;
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

	// Função principal
	public int acao() {
		atualizarVariaveis();

		return moverParaDirecao((int) (Math.random() * 5));
	}

}