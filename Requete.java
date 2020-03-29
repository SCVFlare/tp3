/* INF4230 - Intelligence artificielle
   TP3 - Réseaux bayésiens
   http://ericbeaudry.uqam.ca/INF4230/tp3/
*/
import java.io.StreamTokenizer;
import java.util.ArrayList;


public class Requete {
    protected ArrayList<Expression>   interrogation = new ArrayList<Expression>();
    protected ArrayList<Expression>   connu = new ArrayList<Expression>();
    
    public static Requete lireRequete(StreamTokenizer in) throws Exception
    {
        Requete requete = new Requete();
        
        if(in.nextToken()!=StreamTokenizer.TT_WORD) throw new Exception("Erreur dans le format de la requête à la ligne " + in.lineno());
        if(!in.sval.equalsIgnoreCase("P")) throw new Exception("Erreur dans le format de la requête à la ligne " + in.lineno());        
        if(in.nextToken()!='(') throw new Exception("Erreur dans le format de la requête à la ligne " + in.lineno());

        do{
            Expression expinterogation = Expression.lireExpression(in);
            requete.interrogation.add(expinterogation);
        }while(in.nextToken()==',');
        if(in.ttype=='|')
            do{
                Expression exconnu = Expression.lireExpression(in);
                requete.connu.add(exconnu);
            }while(in.nextToken()==',');
            
        if(in.ttype!=')') throw new Exception("Erreur dans le format de la requête à la ligne " + in.lineno());
        if(in.nextToken()!=';') throw new Exception("Erreur dans le format de la requête à la ligne " + in.lineno());
        return requete;
    }

	@Override
	public String toString() {
		return "P("+interrogation + " | " + connu+")";
	}
    
    
}
