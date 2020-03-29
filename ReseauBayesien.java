/* INF4230 - Intelligence artificielle
   TP3 - Réseaux bayésiens
   http://ericbeaudry.uqam.ca/INF4230/tp3/
*/
import java.io.StreamTokenizer;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class ReseauBayesien {
    
    public ReseauBayesien(){}
    
    public double evaluerProbabilite(Requete requete)
    {
        // À compléter.
        System.err.println(requete);
        return 0;
    }
    
    protected Map<String, VariableBool>  variables = new TreeMap();
    
    protected boolean verifierVariablesNonDefinies(){
        for(VariableBool v : variables.values())
            for(String dep : v.dependances)
                if(!variables.containsKey(dep)) {
                    System.err.println("Erreur: la variable " + dep + " n'a pas été définie!");
                    return false;
                }
        return true;
    }
    
   
    
    
    public static ReseauBayesien lireReseauBayesien(StreamTokenizer in) throws Exception
    {
        ReseauBayesien rb = new ReseauBayesien();
        
        if(in.nextToken()!=StreamTokenizer.TT_WORD) throw new Exception("Erreur de format de fichier à la ligne " + in.lineno());
        if(in.sval.compareTo("ReseauBayesien")!=0) throw new Exception("Erreur de format de fichier à la ligne " + in.lineno());
        
        if(in.nextToken()!='{') throw new Exception("Erreur de format de fichier à la ligne " + in.lineno());
        
        while(in.nextToken()!='}')
        {
            VariableBool variable = new VariableBool();
            // Lire une variable
            if(in.ttype!=StreamTokenizer.TT_WORD) throw new Exception("Erreur de format de fichier à la ligne " + in.lineno());
            String nom = in.sval;
            variable.nom = nom;
            
            // Lire les dépendences
            TreeSet<String> dependences = new TreeSet<String>();
            if(in.nextToken()!='(') throw new Exception("Erreur de format de fichier à la ligne " + in.lineno());
            in.nextToken();
            while(in.ttype!=')'){
                if(in.ttype!=StreamTokenizer.TT_WORD) throw new Exception("Erreur de format de fichier à la ligne " + in.lineno());
                String dependence = in.sval;
                variable.dependances.add(dependence);
                if(!dependences.add(dependence)) throw new Exception("Erreur ligne " + in.lineno() + " : la dépendance à " + dependence + " a déjà été définie.");
                in.nextToken();
                if(in.ttype!=',' && in.ttype!=')') throw new Exception("Erreur de format de fichier à la ligne " + in.lineno());
                if(in.ttype==',') in.nextToken();
            }
            
            if(in.nextToken()!=':') throw new Exception("Erreur de format de fichier à la ligne " + in.lineno());
            
            // Lire la table de probabilités
            int nbValeurs = 1;
            nbValeurs <<= variable.dependances.size(); // nbvaleurs = 2^variable.dependences.size()
            variable.tableProbabilites = new double[nbValeurs];
            for(int i=0;i<nbValeurs;i++){
                if(in.nextToken()!=StreamTokenizer.TT_NUMBER) throw new Exception("Erreur de format de fichier à la ligne " + in.lineno());
                variable.tableProbabilites[i] = in.nval;
            }
            
            if(in.nextToken()!=';') throw new Exception("Erreur de format de fichier à la ligne " + in.lineno());
            
            rb.variables.put(variable.nom, variable);
        }
        
        if(!rb.verifierVariablesNonDefinies())
            throw new Exception("Erreur de structuration du réseau bayésien!");
        
        return rb;
    }
    
}
