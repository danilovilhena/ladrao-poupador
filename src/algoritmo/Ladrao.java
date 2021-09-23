package algoritmo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Ladrao extends ProgramaLadrao {
	int NORTE = 1, SUL = 2, LESTE = 3, OESTE = 4;
	int timerGlobal = 0, moedas, roubos;
	int[] visao, olfato;
	int[][] visitados = new int[31][31];

	// Códigos da visão
	final int[] VISAO_NORTE = { 2, 7 }, VISAO_SUL = { 16, 21 }, VISAO_LESTE = { 12, 13 }, VISAO_OESTE = { 10, 11 };
	final int[] VISAO_NORDESTE = { 3, 4, 8, 9 }, VISAO_NOROESTE = { 0, 1, 5, 6 };
	final int[] VISAO_SUDESTE = { 17, 18, 22, 23 }, VISAO_SUDOESTE = { 14, 15, 19, 20 };

	// Relacionadas à função buscarPoupador
	public int moverParaDirecao(int direcao) {
		int refletida = refletirObstaculos(direcao);
		return (refletida != direcao) ? refletida : direcao;
	}

	public int refletirObstaculos(int direcao) {
		if (direcao == NORTE
				&& (visao[VISAO_NORTE[0]] != 0 || visao[VISAO_NORTE[0]] != 100 || visao[VISAO_NORTE[0]] != 110)) {
			return Util.selecionarAleatorio(new int[] { SUL, LESTE, OESTE });
		} else if (direcao == SUL
				&& (visao[VISAO_SUL[0]] != 0 || visao[VISAO_SUL[0]] != 100 || visao[VISAO_SUL[0]] != 110)) {
			return Util.selecionarAleatorio(new int[] { NORTE, LESTE, OESTE });
		} else if (direcao == LESTE
				&& (visao[VISAO_LESTE[0]] != 0 || visao[VISAO_LESTE[0]] != 100 || visao[VISAO_LESTE[0]] != 110)) {
			return Util.selecionarAleatorio(new int[] { NORTE, SUL, OESTE });
		} else if (direcao == OESTE
				&& (visao[VISAO_OESTE[0]] != 0 || visao[VISAO_OESTE[0]] != 100 || visao[VISAO_OESTE[0]] != 110)) {
			return Util.selecionarAleatorio(new int[] { NORTE, SUL, LESTE });
		} else
			return direcao;
	}

	public int descobrirDirecao(int num) {
		if (Util.contains(VISAO_NORTE, num)) {
			return NORTE;
		} else if (Util.contains(VISAO_SUL, num)) {
			return SUL;
		} else if (Util.contains(VISAO_LESTE, num)) {
			return LESTE;
		} else if (Util.contains(VISAO_OESTE, num)) {
			return OESTE;
		} else if (Util.contains(VISAO_SUDESTE, num)) {
			return new Random().nextBoolean() ? SUL : LESTE;
		} else if (Util.contains(VISAO_SUDOESTE, num)) {
			return new Random().nextBoolean() ? SUL : OESTE;
		} else if (Util.contains(VISAO_NORDESTE, num)) {
			return new Random().nextBoolean() ? NORTE : LESTE;
		} else if (Util.contains(VISAO_NOROESTE, num)) {
			return new Random().nextBoolean() ? NORTE : OESTE;
		} else
			return 0;
	}

	public int buscarPoupador() {
		int indicePoupador = Util.indexOf(visao, 100);
		if (indicePoupador == -1)
			indicePoupador = Util.indexOf(visao, 110);

		boolean vaiPerseguir = Util.selecionarProbabilidade(new double[] { 0.95, 0.05 }) == 0;

		return (indicePoupador != -1 && vaiPerseguir) ? descobrirDirecao(indicePoupador) : 0;
	}

	// Protótipo de baseado em utilidades
	public int analisarVisao() {
		// Valores
		HashMap<Integer, Integer> valores = new HashMap<Integer, Integer>();
		valores.put(230, 0); // Ladrão
		valores.put(220, 0); // Ladrão
		valores.put(210, 0); // Ladrão
		valores.put(200, 0); // Ladrão
		valores.put(110, 20); // Poupador
		valores.put(100, 20); // Poupador
		valores.put(5, -1); // Pastilha
		valores.put(4, 0); // Moeda
		valores.put(3, -1); // Banco
		valores.put(1, -2); // Parede
		valores.put(0, 2); // Célula vazia
		valores.put(-1, -2); // Fora do ambiente
		valores.put(-2, -1); // Sem visão

		// Direções
		int[] cima = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		int[] baixo = new int[] { 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 };
		int[] direita = new int[] { 3, 4, 8, 9, 12, 13, 17, 18, 22, 23 };
		int[] esquerda = new int[] { 0, 1, 5, 6, 10, 11, 14, 15, 19, 20 };
		int[][] direcoes = new int[][] { cima, baixo, direita, esquerda };

		// Pesos
		int[] maiorPeso = new int[] { 6, 7, 8, 11, 12, 15, 16, 17 };

		// Avaliações
		int[] resultados = new int[] { 0, 0, 0, 0 }; // Cima, Baixo, Direita, Esquerda

		// Analisar direções

		for (int i = 0; i < direcoes.length; i++) {
			int[] direcao = direcoes[i];

			for (int j = 0; j < direcao.length; j++) {
				int result = valores.get(visao[direcao[j]]);
				resultados[i] += Util.contains(maiorPeso, direcao[j]) ? result * 2 : result;
			}
		}

		// System.out.println(Arrays.toString(resultados) + "\n");
		double[] probabilidades = Util.transformarEmProbabilidade(resultados);
		int indice = Util.selecionarProbabilidade(probabilidades);

		return indice + 1;
	}

	// Função baseada em modelo
	public int moverComMigalhas() {
		int x = (int) sensor.getPosicao().getX();
		int y = (int) sensor.getPosicao().getY();
		visitados[x][y]++;

		int[] direcoes = new int[4];

		// Cima
		if (!(visao[7] == 0 || visao[7] == 100 || visao[7] == 110)) {
			if (y == 0)
				direcoes[0] = -1;
			else {
				visitados[x][y - 1] = -1;
				direcoes[0] = visitados[x][y - 1];
			}
		} else {
			if (visitados[x][y - 1] == -1) {
				visitados[x][y - 1] = 0;
			}
			direcoes[0] = visitados[x][y - 1];
		}

		// Baixo
		if (!(visao[16] == 0 || visao[16] == 100 || visao[16] == 110)) {
			visitados[x][y + 1] = -1;
			direcoes[1] = visitados[x][y + 1];
		} else {
			if (visitados[x][y + 1] == -1) {
				visitados[x][y + 1] = 0;
			}
			direcoes[1] = visitados[x][y + 1];
		}

		// Direita
		if (!(visao[12] == 0 || visao[12] == 100 || visao[12] == 110)) {
			visitados[x + 1][y] = -1;
			direcoes[2] = visitados[x + 1][y];
		} else {
			if (visitados[x + 1][y] == -1) {
				visitados[x + 1][y] = 0;
			}
			direcoes[2] = visitados[x + 1][y];
		}

		// Esquerda
		if (!(visao[11] == 0 || visao[11] == 100 || visao[11] == 110)) {
			if (x == 0)
				direcoes[3] = -1;
			else {
				visitados[x - 1][y] = -1;
				direcoes[3] = visitados[x - 1][y];
			}
		} else {
			if (visitados[x - 1][y] == -1) {
				visitados[x - 1][y] = 0;
			}
			direcoes[3] = visitados[x - 1][y];
		}

		int smallest = Integer.MAX_VALUE;
		for (int i = 0; i < direcoes.length; i++) {
			if (direcoes[i] < smallest && direcoes[i] != -1) {
				smallest = direcoes[i];
			}
		}

		ArrayList<Integer> indices = new ArrayList<Integer>();
		for (int i = 0; i < direcoes.length; i++) {
			if (direcoes[i] == smallest)
				indices.add(i);
		}

		int rnd = new Random().nextInt(indices.size());
		printVisitados();
		return indices.get(rnd) + 1;
	}

	// Função principal
	public int acao() {
		atualizarVariaveis();
		atualizarMigalhas();

		int poupador = buscarPoupador();
		return (poupador != 0) ? moverParaDirecao(poupador) : moverComMigalhas();
	}

	// Funções auxiliares
	public void atualizarVariaveis() {
		if (sensor.getNumeroDeMoedas() > moedas) {
			roubos++;
			moedas = sensor.getNumeroDeMoedas();
		}
		visao = sensor.getVisaoIdentificacao();
		olfato = sensor.getAmbienteOlfatoLadrao();
	}

	public void atualizarMigalhas() {
		int x = (int) sensor.getPosicao().getX();
		int y = (int) sensor.getPosicao().getY();

		int d = 0;
		for (int i = -2; i <= 2; i++) {
			for (int j = -2; j <= 2; j++) {
				try {
					if (!(i == 0 && j == 0) && d < 24) {
						if (!(visao[d] == 0 || visao[d] == 100 || visao[d] == 110)) {
							visitados[x + j][y + i] = -1;
						} else {
							if (visao[d] == 0 && visitados[x + j][y + i] == -1) {
								visitados[x + j][y + i] = 0;
							}
						}
					}
				} catch (Exception e) {
				}
				d++;
			}
		}
	}

	public void printVisitados() {
		for (int row = 0; row < visitados.length; row++) {
			for (int col = 0; col < visitados[row].length; col++) {
				System.out.printf("%3d", visitados[row][col]);
			}
			System.out.println(); // Faz uma nova fileira
		}
	}
}