package simplex;

import java.util.Arrays;

public class Restricoes {


    public enum TIPO {
        MENOR_IGUAL, MAIOR_IGUAL;
    }

    private String t_inequacao;
    private String e_equacao;
    private double[] coeficientes;
    private double elemento_livre;
    private TIPO tipo;
    private int tam;

    public Restricoes(String t_inequacao, int t) throws Exception {
        this.t_inequacao = t_inequacao;
        this.tam = t;
        Extrair_coef_ineq();

        Sinal_expressao();
        Tratar_restri_simplex();
    }

    public String getT_inequacao() {
        return t_inequacao;
    }

    public void setT_inequacao(String t) {
        this.t_inequacao = t;
    }

    public String getE_equacao() {
        return e_equacao;
    }

    public void setEquivalentEquation(String e) {
        this.e_equacao = e;
    }

    public double[] getCoeficiente() {
        return coeficientes;
    }

    public void setCoeficiente(double[] c) {
        this.coeficientes = c;
    }

    public double get_elemento_livre() {
        return elemento_livre;
    }

    public void set_elemento_livre(double elemento_livre) {
        this.elemento_livre = elemento_livre;
    }

    public TIPO getTipo() {
        return tipo;
    }

    public void setTipo(TIPO t) {
        this.tipo = t;
    }

    /**
     * @throws Exception
     *
     * Metodo para extrair os coeficientes e o elemento livre da expressão
     *
     *
     */
    private void Extrair_coef_ineq() throws Exception {

        //Transforma a inequação em uma equação e adiciona ou subtrae uma constante
        if (t_inequacao.contains("<=")) {

            e_equacao = t_inequacao.replace("<=", "+ x =");
            tipo = TIPO.MENOR_IGUAL;
        }
        else if (t_inequacao.contains(">=")) {
            e_equacao = t_inequacao.replace(">=", "- x =");
            tipo = TIPO.MAIOR_IGUAL;
        }
        else {
            throw new Exception("EXPRESSÃO COM FORMATO ERRADO");
        }

        // remove os espaços
        String function = e_equacao.replace(" ", "");


        // Divide a expressão para extrair os coeficientes
        String[] t_coeficiente = function.split("(\\-)|(\\+)|(=)");
        coeficientes = new double[tam];
        elemento_livre = 0;

        Arrays.fill(coeficientes, 0);


        for (int i = 0; i < t_coeficiente.length; i++) {


            // Pega somente a parte numeral
            String[] portions = t_coeficiente[i].split("(x)|(X)");

            try {

                // tenta descobrir se é o elemento livre da expressão
                elemento_livre += Double.parseDouble(t_coeficiente[i]);

            } catch(NumberFormatException e) {


                // Se o elemento é "x" saia da interação, senão insira o elemento e transforma a inequação em uma equação
                if (t_coeficiente[i].toLowerCase().equals("x")) {
                    continue;
                }

                // verifica se a expressão está correta
                if (portions.length > 1) {
                    if (t_coeficiente.length == 1) {
                        throw new Exception("EXPRESSÃO COM FORMATO ERRADO");
                    }
                } else {
                    throw new Exception("EXPRESSÃO COM FORMATO ERRADO");
                }


                if (portions[0].equals("")) {

                    // Se o lada esquerdo de X está vazio, o coeficiente é 1
                    coeficientes[Integer.valueOf(portions[1])-1] += 1;
                } else {

                    // senão leia a constante
                    coeficientes[Integer.valueOf(portions[1])-1] += Double.parseDouble(portions[0]);
                }

            }


        }

    }


    /**
     *
     * Metodo para tratar os sinais nas expressões
     *
     */
    private void Sinal_expressao() {
        int p_count = 1;

        String function = e_equacao.replaceAll("\\s{2,}", " ").trim();
        String[] partes = function.split(" ");
        for (int i = 0; i < partes.length; i++) {
            if (coeficientes.length == p_count) {
                break;
            }

            if (partes[i].equals("+")) {
                p_count++;
            } else if (partes[i].equals("-")) {
                if (coeficientes[p_count] == 0) {
                    p_count++;
                    continue;
                }
                if (!partes[i+1].toLowerCase().equals("x")) {
                    coeficientes[p_count] *= -1;
                }
                p_count++;
            }
        }

    }

    /**
     *
     * Metodo para tratar os sinais das inequações para o calculo do simplex.
     *
     */
    private void Tratar_restri_simplex() {
        if (tipo == TIPO.MAIOR_IGUAL) {
            elemento_livre *= -1;
            for (int i = 0; i < coeficientes.length; i++) {
                coeficientes[i] *= -1;
            }
        }
    }

    /**
     *
     *
     * Metodo para resolver as equaçoes baseado nos valores das variaveis basicas
     *
     */
    public double Resol_eq_bvariaveis(double v[]) {

        double resultado = 0.0;
        resultado = elemento_livre;

        for (int i = 0; i < coeficientes.length; i++) {
            resultado = resultado - coeficientes[i] * v[i];
        }

        return resultado;
    }


}


