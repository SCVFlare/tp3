/* INF4230 - Intelligence artificielle
   TP3 - Réseaux bayésiens
   http://ericbeaudry.uqam.ca/INF4230/tp3/
*/

import java.util.ArrayList;
import java.util.Arrays;


public class VariableBool implements Comparable<VariableBool>{
    protected boolean evalue;
    protected int profondeur;
    protected boolean value;
    
    


    /** Nom de la variable. */
    protected String                nom;
    
    /** Liste des dépendances. Attention, l'ordre est important */
    protected ArrayList<String>     dependances = new ArrayList<String>();
    
    /** Table de probabilités.
     *  Contient 2^n entrées où n=dependences.size();
     * 
     *  L'ordre d'apparition dans dependences est important.
     * 
     *  Si aucune dépendence  : [probabilité à priori] ;
     *  Si une dépendence (A) : P(A=F) P(A=V) ;
     *  Si 2 dépendences (A,B): P(A=F,B=F) P(A=F,B=V) P(A=V,B=F) P(A=V,B=V);
     *  etc.
     */
    protected double[]              tableProbabilites;

    
     
	@Override
	public String toString() {
		return "prof=" + profondeur + ", v=" + value + ", nom=" + nom
				+ ", dependances=" + dependances + ", tableProbabilites=" + Arrays.toString(tableProbabilites);
	}



	@Override
	public int compareTo(VariableBool o) {
        return (this.profondeur - o.profondeur);
    }

	
    
}
