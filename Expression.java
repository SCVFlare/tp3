/* INF4230 - Intelligence artificielle
   TP3 - Réseaux bayésiens
   http://ericbeaudry.uqam.ca/INF4230/tp3/
*/
import java.io.StreamTokenizer;

public class Expression {
    
    protected String variable;
    protected boolean valeurverite;
        
    public static Expression lireExpression(StreamTokenizer in) throws Exception
    {
        Expression expression = new Expression();
        if(in.nextToken()!=StreamTokenizer.TT_WORD) throw new Exception("Erreur dans le format de la requête à la ligne " + in.lineno());
        expression.variable = in.sval;
        if(in.nextToken()!='=') throw new Exception("Erreur dans le format de la requête à la ligne " + in.lineno());
        if(in.nextToken()!=StreamTokenizer.TT_WORD) throw new Exception("Erreur dans le format de la requête à la ligne " + in.lineno());
        expression.valeurverite = in.sval.equalsIgnoreCase("true") || in.sval.equalsIgnoreCase("vrai") || in.sval.equalsIgnoreCase("T") || in.sval.equalsIgnoreCase("V");
        if(!expression.valeurverite && !(in.sval.equalsIgnoreCase("false") || in.sval.equalsIgnoreCase("faux") || in.sval.equalsIgnoreCase("F")))throw new Exception("Erreur dans le format de la requête à la ligne " + in.lineno());
        return expression;
    }

	@Override
	public String toString() {
		return variable+"="+valeurverite;
	}
    
}
