package br.edu.cs.poo.ac.seguro.mediators;

public class StringUtils {
	private StringUtils() {}

	public static boolean ehNuloOuBranco(String str) {
		if (str == null || str.isBlank()) {
			return true;
		}
		return false;
	}

	public static boolean temSomenteNumeros(String input) {
		if (input == null || input.isEmpty()) {
			return false;
		}

		for (int i = 0; i < input.length(); i++) {
			if (!Character.isDigit(input.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}