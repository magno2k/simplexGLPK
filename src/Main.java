/*
Modelo:
Problema de Designação

Um empresa de eletrodomésticos planeja veicular seus produtos em comerciais de TV durante a novela das 8 e os jogos da seleção na Copa.
Comerciais na novela são vistos por 7 milhões de mulheres e 2 milhões de homens e custam $50000.
Comerciais nos jogos são vistos por 2 milhões de mulheres e 12 milhões de homens, e custam $100000.
Qual a distribuição ideal de comerciais se a empresa deseja que eles sejam vistos por 28 milhões de mulheres e 24 milhões de homens a um menor
custo possível ?

 */

import simplex.Restricoes;
import simplex.FuncaoObjeto;

import org.gnu.glpk.*;
import org.gnu.glpk.GLPK;
import org.gnu.glpk.GLPKConstants;
import org.gnu.glpk.GlpkException;
import org.gnu.glpk.SWIGTYPE_p_double;
import org.gnu.glpk.SWIGTYPE_p_int;
import org.gnu.glpk.glp_prob;
import org.gnu.glpk.glp_smcp;

public class Main {
    public static void main(String[] args) throws Exception {

        FuncaoObjeto obj = new FuncaoObjeto("MAX 50x1 + 100x2");
        Restricoes[] rest = new Restricoes[3];
        rest[0] = new Restricoes("7x1 + 2x2 >= 28", 2);
        rest[1] = new Restricoes("2x1 + 12x2 >= 24", 2);

        glp_prob lp;
        SWIGTYPE_p_int ind;
        SWIGTYPE_p_double val;
        int ret;
        glp_smcp parm;

        try {
            // cria o problema
            lp = GLPK.glp_create_prob();
            //System.out.println("Problema criado");
            GLPK.glp_set_prob_name(lp, "simplex");

            // define as colunas
            GLPK.glp_add_cols(lp, 2);
            GLPK.glp_set_col_name(lp, 1, "x1");
            GLPK.glp_set_col_kind(lp, 1, GLPKConstants.GLP_IV);
            GLPK.glp_set_col_bnds(lp, 1, GLPKConstants.GLP_LO, 0, 0);
            GLPK.glp_set_col_name(lp, 2, "x2");
            GLPK.glp_set_col_kind(lp, 2, GLPKConstants.GLP_IV);
            GLPK.glp_set_col_bnds(lp, 2, GLPKConstants.GLP_LO, 0, 0);

            // define as restrições
            GLPK.glp_add_rows(lp, 3);
            GLPK.glp_set_row_name(lp, 1, "c1");
            GLPK.glp_set_row_bnds(lp, 1, GLPKConstants.GLP_UP, 0, rest[0].get_elemento_livre());

            ind = GLPK.new_intArray(3);
            val = GLPK.new_doubleArray(3);

            GLPK.intArray_setitem(ind, 1, 1);
            GLPK.intArray_setitem(ind, 2, 2);
            GLPK.intArray_setitem(ind, 3, 3);
            GLPK.doubleArray_setitem(val, 1, rest[0].getCoeficiente()[0]);
            GLPK.doubleArray_setitem(val, 2,  rest[0].getCoeficiente()[1]);
            GLPK.glp_set_mat_row(lp, 1, 2, ind, val);

            GLPK.glp_set_row_name(lp, 2, "c2");
            GLPK.glp_set_row_bnds(lp, 2, GLPKConstants.GLP_UP, 0, rest[1].get_elemento_livre());

            GLPK.intArray_setitem(ind, 1, 1);
            GLPK.intArray_setitem(ind, 2, 2);
            GLPK.intArray_setitem(ind, 3, 3);
            GLPK.doubleArray_setitem(val, 1, rest[1].getCoeficiente()[0]);
            GLPK.doubleArray_setitem(val, 2, rest[1].getCoeficiente()[1]);
            GLPK.glp_set_mat_row(lp, 2, 2, ind, val);


            GLPK.delete_doubleArray(val);
            GLPK.delete_intArray(ind);

            // Define a função objetivo
            GLPK.glp_set_obj_name(lp, "Z");
            GLPK.glp_set_obj_dir(lp, GLPKConstants.GLP_MIN);
            GLPK.glp_set_obj_coef(lp, 0, 0);
            GLPK.glp_set_obj_coef(lp, 1, obj.getCoeficientes()[0]);
            GLPK.glp_set_obj_coef(lp, 2, obj.getCoeficientes()[1]);

            // Resolver o modelo
            parm = new glp_smcp();

            GLPK.glp_init_smcp(parm);

            //Desabilita as mensagens do terminal
            parm.setMsg_lev(GLPK.GLP_MSG_OFF);

            ret = GLPK.glp_simplex(lp, parm);

            // Retrieve solution
            if (ret == 0) {
                simplex_solucao(lp);
            } else {
                System.out.println("O problema não pode ser resolvido");
            }


            // Libera a memoria
            GLPK.glp_delete_prob(lp);
        } catch (GlpkException ex) {
            ex.printStackTrace();
            ret = 1;
        }
        System.exit(ret);
    }

    /**
     *
     * Solução do Simplex
     */
    static void simplex_solucao(glp_prob lp) {
        int i;
        int n;
        String name;
        double val;

        name = GLPK.glp_get_obj_name(lp);
        val = GLPK.glp_get_obj_val(lp);

        System.out.println("Custo dos comerciais: " + "R$" + val);
        n = GLPK.glp_get_num_cols(lp);
        for (i = 1; i <= n; i++) {
            name = GLPK.glp_get_col_name(lp, i);
            val = GLPK.glp_get_col_prim(lp, i);
            switch(i)
            {
                case 1:
                    System.out.println("Comerciais no horario da novela: " + (int) Math.ceil(val));
                    break;
                case 2:
                    System.out.println("Comerciais no horario do futebol: " + (int) Math.ceil(val));
            }

        }
    }


}
