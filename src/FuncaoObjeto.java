package simplex;


public class FuncaoObjeto {

    private String tipo_func_obj;
    private double[] coeficientes;
    private double elemento_livre;
    private Tipo tipo;

    public enum Tipo {
        MINIMIZACAO, MAXIMIZACAO;
    }


    public FuncaoObjeto(String t) {
        this.tipo_func_obj = t;
        try {
            setTipo();
            setCoeficiente_funcOBJ();
            setExpressaoSinal();
            FuncOBj_simplex();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    /**
     *
     * Medo para verificar de que tipo a expressão é
     *
     */
    private void setTipo() throws Exception {
        // Verifica se é max ou min
        String[] f_tipo = tipo_func_obj.split(" ");
        if (f_tipo[0].toLowerCase().equals("max")) {

            this.tipo = Tipo.MAXIMIZACAO;
            tipo_func_obj = tipo_func_obj.toLowerCase().replace("max ", "");

        } else if (f_tipo[0].toLowerCase().equals("min")) {

            this.tipo = Tipo.MINIMIZACAO;
            tipo_func_obj = tipo_func_obj.toLowerCase().replace("min ", "");

        } else {

            throw new Exception("TIPO DA FUNCAO (MAX/MIN) NÃO EXCONTRADA");
        }

    }

    /**
     *
     * Metodo que extrai os coeficientes e o elemento livre
     *
     */
    private void setCoeficiente_funcOBJ() throws Exception {
        // Remove os espaços
        String function = tipo_func_obj.replace(" ", "");

        // extrai os coeficientes
        String[] todos_coeficientes = function.split("(\\-)|(\\+)");
        coeficientes = new double[todos_coeficientes.length];
        elemento_livre = 0;
        for (int i = 0; i < todos_coeficientes.length; i++) {
            String[] partes = todos_coeficientes[i].split("(x)|(X)");
            if (partes.length > 1) {
                if (todos_coeficientes.length == 1) {
                    throw new Exception("EXPRESSÃO COM FORMATO ERRADO");
                } else if (partes[0].equals("")) {
                    // Se o lado esquerdo  de X está vazio, o coeficiente é 1
                    coeficientes[i] = 1;
                } else {

                    coeficientes[i] = Double.parseDouble(partes[0]);
                }
            } else {
                try {
                    elemento_livre = Double.parseDouble(partes[0]);
                } catch (NumberFormatException e) {
                    throw new Exception("EXPRESSÃO COM FORMATO ERRADO");
                }
            }
        }
    }

    /**
     *
     * Metodo que trata os sinais das expressoes
     *
     */
    private void setExpressaoSinal() {
        int partes_c = 0;

        String function = tipo_func_obj.replaceAll("\\s{2,}", " ").trim();
        String[] partes = function.split(" ");

        for (int i = 0; i < partes.length; i++) {
            if (partes[i].equals("+")) {
                partes_c++;
            } else if (partes[i].equals("-")) {
                coeficientes[partes_c] *= -1;
                partes_c++;
            }
        }

    }

    /**
     *
     * Metodo que trata os sinais da inequação para o simplex
     */

    private void FuncOBj_simplex() {
        if (tipo == Tipo.MINIMIZACAO) {
            for (int i = 0; i < coeficientes.length; i++) {
                coeficientes[i] *= -1;
            }
        }
    }

    public String getTypedObjectiveFunction() {
        return tipo_func_obj;
    }

    public void setTypedObjectiveFunction(String t) {
        this.tipo_func_obj = t;
    }

    public double[] getCoeficientes() {
        return coeficientes;
    }

    public void setCoeficientes(double[] c) {
        this.coeficientes = c;
    }

    public double get_elemento_livre() {
        return elemento_livre;
    }

    public void set_elemento_livre(double f) {
        this.elemento_livre = f;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setType(Tipo t) {
        this.tipo = t;
    }


}

