/* INF4230 - Intelligence artificielle
   TP3 - Réseaux bayésiens
   http://ericbeaudry.uqam.ca/INF4230/tp3/
*/

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
public class TP3 {
    public static void main(String args[]) throws Exception
    {
        StreamTokenizer in = new StreamTokenizer(new InputStreamReader(args.length==0 ? System.in : new FileInputStream(args[0])));
        ReseauBayesien reseaubayesien = ReseauBayesien.lireReseauBayesien(in);
        // Lecture des requêtes
        while(in.nextToken() != StreamTokenizer.TT_EOF){
            in.pushBack();
            Requete requete = Requete.lireRequete(in);
            double p = reseaubayesien.evaluerProbabilite(requete);
            System.out.println("" + p);
        }
    }
}
