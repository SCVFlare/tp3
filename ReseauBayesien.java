/* INF4230 - Intelligence artificielle
   TP3 - Réseaux bayésiens
   http://ericbeaudry.uqam.ca/INF4230/tp3/
*/
import java.io.StreamTokenizer;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

public class ReseauBayesien {
    
    public ReseauBayesien(){}
    
    public double evaluerProbabilite(Requete requete)
    {
    	System.out.println(requete);
    	int nbMatchConnu=0;
    	int nbMatchInterrogation=0;
        for(int i=0;i<100;i++) {
        	for(VariableBool v: variables.values()) {
        		v.clear();
        	}
        	for(VariableBool v: variables.values()) {
        		evaluerVariableBool(v);
        	}
        	if(connuSatisfaite(requete)) {
        		nbMatchConnu++;
        		if(interrogationSatisfaite(requete)) {
        			nbMatchInterrogation++;
        		}
        	}	
        }
        if(nbMatchConnu==0) {
        	return 0.00;
        }
        return nbMatchInterrogation / nbMatchConnu;
    }
    private void evaluerVariableBool(VariableBool v) {
    	if(!v.evalue) {
    		int i=0;
    		for(String s:v.dependances) {
    			try {
    				VariableBool p = variables.get(s);
    				i=i*2;
    				evaluerVariableBool(p);
    				if(p.value) {
    					i++;
    				}
    			}
    			catch(Exception e){
    				System.out.println("variable not found");
    			}
    		
    		}
    		double prob=v.tableProbabilites[i];
    		Random r = new Random();
    		double randomValue = r.nextDouble();
    		v.value=randomValue<prob;
    		v.evalue=true;
    	}
    }
    
    public boolean connuSatisfaite(Requete r) {
    	boolean satisfait=true;
    	for(Expression e:r.connu) {
    		try {
    			VariableBool v= variables.get(e.variable);
    			if(v.value!=e.valeurverite) {
    				satisfait=false;
    			}
    		}
    		catch(Exception exc) {
    			System.out.println("variable not found");
    		}
    	}
    	return false;
    }
    
    public boolean interrogationSatisfaite(Requete r) {
    	boolean satisfait=true;
    	for(Expression e:r.interrogation) {
    		try {
    			VariableBool v= variables.get(e.variable);
    			if(v.value!=e.valeurverite) {
    				satisfait=false;
    			}
    		}
    		catch(Exception exc) {
    			System.out.println("variable not found");
    		}
    	}
    	return false;
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
