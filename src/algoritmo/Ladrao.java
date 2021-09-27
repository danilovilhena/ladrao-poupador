package algoritmo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Ladrao extends ProgramaLadrao {
	int NORTE = 1, SUL = 2, LESTE = 3, OESTE = 4;
	int moedas, roubos, x, y;
	int[] visao, olfato;
	int[][] visitados = new int[31][31];
	ArrayList<Integer> visitadosLinear = new ArrayList<Integer>();

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

	// Relacionada à função moverComFelicidade
	public boolean podeMover(int pos) {
		return (visao[pos] == 0 || visao[pos] == 100 || visao[pos] == 110);
	}

	// Função baseada em utilidade
	public int moverComFelicidade() {
		// Pesos
		HashMap<String, Integer> pesos = new HashMap<String, Integer>();
		pesos.put("poupador", 200);
		pesos.put("vazio", 20);
		pesos.put("visitado", -5);

		// Direções
		int[] cima = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		int[] baixo = new int[] { 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 };
		int[] direita = new int[] { 3, 4, 8, 9, 12, 13, 17, 18, 22, 23 };
		int[] esquerda = new int[] { 0, 1, 5, 6, 10, 11, 14, 15, 19, 20 };
		int[][] direcoes = new int[][] { cima, baixo, direita, esquerda };

		// Cima, Baixo, Direita, Esquerda
		int[] resultados = new int[] { 0, 0, 0, 0 };

		// Eliminar direções bloqueadas
		if (!podeMover(7))
			resultados[0] = -1;
		if (!podeMover(16))
			resultados[1] = -1;
		if (!podeMover(11))
			resultados[2] = -1;
		if (!podeMover(12))
			resultados[3] = -1;

		// Analisar direções
		for (int i = 0; i < direcoes.length; i++) {
			if (resultados[i] != -1) {
				int[] direcao = direcoes[i];

				for (int j = 0; j < direcao.length; j++) {
					if (visao[direcao[j]] == 0) {
						if (visitadosLinear.get(direcao[j]) == 0) {
							resultados[i] += pesos.get("vazio");
						} else {
							resultados[i] += pesos.get("visitado") * visitadosLinear.get(direcao[j]);
						}
					} else if (visao[direcao[j]] == 100 || visao[direcao[j]] == 110) {
						resultados[i] += pesos.get("poupador");
					}
				}

			}
		}

		// Escolher com probabilidades
		double[] probabilidades = Util.transformarEmProbabilidade(resultados);
		int direcao = Util.selecionarProbabilidade(probabilidades);
		return probabilidades[direcao] > 0.5 ? (direcao + 1) : 0;
	}

	// Função baseada em modelo
	public int moverComMigalhas() {
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
		return indices.get(rnd) + 1;
	}

	// Função principal
	public int acao() {
		atualizarVariaveis();
		atualizarMigalhas();

		int felicidade = moverComFelicidade();
		if (felicidade != 0) {
			return felicidade;
		}

		int poupador = buscarPoupador();
		if (poupador != 0) {
			return moverParaDirecao(poupador);
		} else {
			return moverComMigalhas();
		}
	}

	// Funções auxiliares
	public void atualizarVariaveis() {
		if (sensor.getNumeroDeMoedas() > moedas) {
			roubos++;
			moedas = sensor.getNumeroDeMoedas();
		}
		visao = sensor.getVisaoIdentificacao();
		olfato = sensor.getAmbienteOlfatoLadrao();
		x = (int) sensor.getPosicao().getX();
		y = (int) sensor.getPosicao().getY();
	}

	public void atualizarMigalhas() {
		int d = 0;
		visitadosLinear.clear();
		visitados[x][y]++;
		for (int i = -2; i <= 2; i++) {
			for (int j = -2; j <= 2; j++) {
				try {
					if (!(i == 0 && j == 0) && d <= 24) {
						if (!(visao[d] == 0 || visao[d] == 100 || visao[d] == 110)) {
							visitados[x + j][y + i] = -1;
						} else {
							if (visao[d] == 0 && visitados[x + j][y + i] == -1) {
								visitados[x + j][y + i] = 0;
							}
						}
						visitadosLinear.add(visitados[x + j][y + i]);
					}
				} catch (Exception e) {
					visitadosLinear.add(-100);
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
		System.out.println("------------");
	}
}