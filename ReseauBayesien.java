/* INF4230 - Intelligence artificielle
   TP3 - Réseaux bayésiens
   http://ericbeaudry.uqam.ca/INF4230/tp3/
*/
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

public class ReseauBayesien {
    
    public ReseauBayesien(){}
    
    public double evaluerProbabilite(Requete requete)
    {
    	System.out.println(requete);
    	for(Expression e:requete.connu) {
    		variables.get(e.variable).evalue=e.valeurverite;
    	}
    	int nbMatchConnu=0;
    	int nbMatchInterrogation=0;
        for(int i=0;i<100000;i++) {
        	List<VariableBool> values = new ArrayList<VariableBool>(variables.values());
        	//Collection<VariableBool> values= ;
        	Collections.sort(values);
        	for(VariableBool v: values) {
        		setEvalue(v,false);
        	}
        	for(VariableBool v: values) {
        		evaluerVariableBool(v);
        	}
        	boolean cs=connuSatisfaite(requete);
        	boolean is=interrogationSatisfaite(requete);
        	if(cs) {
        		nbMatchConnu++;
        		if(is) {
        			nbMatchInterrogation++;
        		}
        	}	
        }
        for(VariableBool v: variables.values()) {
    		setValue(v,false);
    	}
        return (double)nbMatchInterrogation / (double)nbMatchConnu;
    }
    private void evaluerVariableBool(VariableBool v) {
    	if(!v.evalue) {
    		int i=0;
    		for(String s:v.dependances) {
    			try {
    				VariableBool p = this.variables.get(s);
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
    		setValue(v,randomValue<prob);
    		setEvalue(v,true);
    	}
    }
    
    public boolean connuSatisfaite(Requete r) {
    	boolean satisfait=true;
    	for(Expression e:r.connu) {
    		try {
    			VariableBool v= this.variables.get(e.variable);
    			if(v.value!=e.valeurverite) {
    				satisfait=false;
    			}
    		}
    		catch(Exception exc) {
    			System.out.println("variable not found");
    		}
    	}
    	return satisfait;
    }
    
    public boolean interrogationSatisfaite(Requete r) {
    	boolean satisfait=true;
    	for(Expression e:r.interrogation) {
    		try {
    			VariableBool v= this.variables.get(e.variable);
    			if(v.value!=e.valeurverite) {
    				satisfait=false;
    			}
    		}
    		catch(Exception exc) {
    			System.out.println("variable not found");
    		}
    	}
    	return satisfait;
    }
    
    protected Map<String, VariableBool>  variables = new TreeMap();
    protected int res;
    
    
    protected void getProfondeur() {
    	/*for(VariableBool v: variables.values()) {
    		profondeurMax(v,0);
    	}
    	int m=0;
    	for(VariableBool v: variables.values()) {
    		m=Math.max(m, v.profondeur);
    	}*/
    	for(VariableBool v: variables.values()) {
    		this.res=-1;
    		profondeurRec(v,0);
    		v.profondeur=res;
    	}
    	
    }
    
    /*public void profondeurMax(VariableBool v,int max) {
    	if(v.dependances.isEmpty()) {
    		v.profondeur=Math.max(v.profondeur, max);
    	}
    	else {
    		for(String s:v.dependances) {
    			VariableBool v1=variables.get(s);
    			profondeurMax(v1,1+max);
    		}
    	}
    }*/
    
    public void profondeurRec(VariableBool v,int prof) {
    	if(v.dependances.isEmpty()) {
    		this.res=Math.max(this.res, prof);
    	}
    	else {
    		for(String s:v.dependances) {
    			VariableBool v1=variables.get(s);
    			profondeurRec(v1,prof+1);
    		}
    	}
    }
    
    
	public void setEvalue(VariableBool v, boolean evalue) {
		try {
			this.variables.get(v.nom).evalue=evalue;
		}
		catch(Exception e){
			System.out.println("variable not found");
		}
	}

	public void setValue(VariableBool v, boolean value) {
		try {
			this.variables.get(v.nom).value=value;
		}
		catch(Exception e){
			System.out.println("variable not found");
		}
	}

	
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
