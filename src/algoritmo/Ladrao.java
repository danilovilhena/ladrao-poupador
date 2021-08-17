package algoritmo;

import java.util.Arrays;

public class Ladrao extends ProgramaLadrao {

	// Converte array linear para bidimensional
	public int[][] converterArray(int[] original) {
		// Passo 1 - adicionar posição do ladrão
		int[] larger = new int[original.length + 1];
		for (int i = 0; i < original.length + 1; i++) {
			if (i < original.length / 2)
				larger[i] = original[i];
			else if (i == original.length / 2)
				larger[i] = -3;
			else
				larger[i] = original[i - 1];
		}
		original = larger;

		// Passo 2 - converter para array bidimensional
		int dimension = (int) Math.sqrt(original.length);
		int[][] bidimensional = new int[dimension][dimension];
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				bidimensional[i][j] = original[i * dimension + j];
			}
		}

		return bidimensional;
	}

	// Função principal
	public int acao() {
		int[][] visao = converterArray(sensor.getVisaoIdentificacao());
		int[][] olfato = converterArray(sensor.getAmbienteOlfatoLadrao());

		System.out.println("Visão: " + Arrays.deepToString(visao));
		System.out.println("Olfato: " + Arrays.deepToString(olfato));

		return (int) (Math.random() * 5);
	}

}