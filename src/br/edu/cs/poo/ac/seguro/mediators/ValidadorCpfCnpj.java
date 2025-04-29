package br.edu.cs.poo.ac.seguro.mediators;

public class ValidadorCpfCnpj {

    public static boolean ehCnpjValido(String cnpj) {
        if (cnpj == null || cnpj.isEmpty()) {
            return false;
        }

        cnpj = cnpj.replaceAll("[^0-9]", "");

        if (cnpj.length() != 14) {
            return false;
        }

        boolean digitosIguais = true;
        for (int i = 1; i < cnpj.length(); i++) {
            if (cnpj.charAt(i) != cnpj.charAt(0)) {
                digitosIguais = false;
                break;
            }
        }
        if (digitosIguais) {
            return false;
        }

        int soma = 0;
        int peso = 5;
        for (int i = 0; i < 12; i++) {
            int num = cnpj.charAt(i) - '0';
            soma += num * peso;
            peso = (peso == 2) ? 9 : peso - 1;
        }

        int resto = soma % 11;
        int dv1 = (resto < 2) ? 0 : 11 - resto;
        if (dv1 != (cnpj.charAt(12) - '0')) {
            return false;
        }

        soma = 0;
        peso = 6;
        for (int i = 0; i < 13; i++) {
            int num = cnpj.charAt(i) - '0';
            soma += num * peso;
            peso = (peso == 2) ? 9 : peso - 1;
        }

        resto = soma % 11;
        int dv2 = (resto < 2) ? 0 : 11 - resto;
        return dv2 == (cnpj.charAt(13) - '0');
    }

    public static boolean ehCpfValido(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            return false;
        }

        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) {
            return false;
        }

        boolean digitosIguais = true;
        for (int i = 1; i < cpf.length(); i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) {
                digitosIguais = false;
                break;
            }
        }
        if (digitosIguais) {
            return false;
        }

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            int num = cpf.charAt(i) - '0';
            soma += num * (10 - i);
        }

        int resto = soma % 11;
        int dv1 = (resto < 2) ? 0 : 11 - resto;
        if (dv1 != (cpf.charAt(9) - '0')) {
            return false;
        }

        soma = 0;
        for (int i = 0; i < 10; i++) {
            int num = cpf.charAt(i) - '0';
            soma += num * (11 - i);
        }

        resto = soma % 11;
        int dv2 = (resto < 2) ? 0 : 11 - resto;
        return dv2 == (cpf.charAt(10) - '0');
    }

}
