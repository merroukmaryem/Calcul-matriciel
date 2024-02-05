package alglin;

public class Helder extends SysLin{
		private Matrice L,D,R;
	public Helder(Matrice A, Vecteur b) throws IrregularSysLinException {
		super(A, b);
		L=new Matrice(A.nbLigne(),A.nbColonne());
		D=new Matrice(A.nbLigne(),A.nbColonne());
		R=new Matrice(A.nbLigne(),A.nbColonne());
	}
	
	  public void factorLDR(){

	        Matrice A = getMatriceSystem();
	        int n = A.nbLigne();
	        double somme;

	        // Diagonaliser les Matrice L et R
	        for (int i = 0; i < n; i++) {
	            L.remplacecoef(i,i, 1.0);
	            R.remplacecoef(i,i, 1.0);
	        }

	        // Debut D'algorithme
	        for (int i = 0; i < n; i++) {
	            // L
	            // pour i >j  : L
	            for (int j = 0; j < i; j++) {
	                somme = 0;
	                for (int k = 0; k < j; k++) {
	                    somme += L.getCoef(i,k) * D.getCoef(k,k) * R.getCoef(k,j);
	                }
	                L.remplacecoef(i,j, (A.getCoef(i,j) - somme) / D.getCoef(j,j));
	            }

	            // D
	            somme = 0;
	            for (int k = 0; k < i + 1; k++) {
	                somme += L.getCoef(i, k) * D.getCoef(k, k) * R.getCoef(k, i);
	            }
	            D.remplacecoef(i, i, A.getCoef(i,i) - somme);  // pour  i=j


	            // R j>i
	            for (int j = i; j < n; j++) {
	                somme = 0;
	                for (int k = 0; k < i; k++) {
	                    somme += L.getCoef(i,k) * D.getCoef(k,k) * R.getCoef(k,j);
	                }
	                R.remplacecoef(i,j, (A.getCoef(i,j) - somme) / D.getCoef(i,i));
	            }
	        }
	        System.out.println("Matrice L :");
	        System.out.println(L);
	        System.out.println("Matrice D :");
	        System.out.println(D);
	        System.out.println("Matrice R :");
	        System.out.println(R);
	        
	    }
	  @Override
		public Vecteur resolution() throws IrregularSysLinException {
			this.factorLDR(); // factorisation de la matrice du systeme
			SysTriangulaireInfUnite Sys_LYB=new SysTriangulaireInfUnite(L,secondMembre);
			Vecteur y=Sys_LYB.resolution(); //resolution du systeme Ly=b
			SysDiagonal Sys_DZY=new SysDiagonal(D,y);
			Vecteur z=Sys_DZY.resolution();//resolution du systeme Dz=y
			SysTriangulaireSupUnite Sys_RXZ=new SysTriangulaireSupUnite(R,z);
			Vecteur x=Sys_RXZ.resolution(); //resolution du systeme Rx=z
			return x;
		}
	  public Vecteur resolutionPartielle() throws IrregularSysLinException {
		  SysTriangulaireInfUnite Sys_LYB=new SysTriangulaireInfUnite(this.L,secondMembre);
			Vecteur y=Sys_LYB.resolution(); //resolution du systeme Ly=b
			SysDiagonal Sys_DZY=new SysDiagonal(this.D,y);
			Vecteur z=Sys_DZY.resolution();//resolution du systeme Dz=y
			SysTriangulaireSupUnite Sys_RXZ=new SysTriangulaireSupUnite(this.R,z);
			Vecteur x=Sys_RXZ.resolution(); //resolution du systeme Rx=z
			return x;
	  }

	  
	  public void setSecondMembre(Vecteur newSecondMembre) {
		  this.secondMembre=newSecondMembre;
	  }
	    public static void main(String[] args) throws Exception {
	        System.out.println("\n***************Resolution en utilisant resolution***********************\n");

	        // Créer la matrice et le vecteur du système
	        double matrice[][] = {{1, 1, -2}, {4, -2, 1}, {3, -1, 3}};
	        Matrice a = new Matrice(matrice);
	        double vecteur[] = {3, 5, 8};
	        Vecteur b = new Vecteur(vecteur);

	        // Créer le système linéaire
	        Helder systeme = new 	Helder(a, b);
	        Vecteur x=systeme.resolution();

	        // Appliquer la méthode factorLDR
	        System.out.println("la solution du systeme ax=b est :\n"+"matrice A :\n"+a+"\nVecteur b :\n"+b+"\nSolution :\n"+x);
	    
	    // Calculer Ax - b
        Vecteur ax = systeme.produit(a, x);
        int n = systeme.getOrdre();
        Vecteur difference = new Vecteur(n);
       

        for (int i = 0; i < b.taille(); i++) {
            difference.remplaceCoef(i, ax.getCoeffecient(i) - b.getCoeffecient(i));
        }

        // Calculer la norme de la différence
        double normeL2 = difference.normeL2();
        double normeL1 = difference.normeL1();
        double normeLinfini = difference.normeLinfini();
        // Vérifier si la norme est négligeable
        if (normeL2 < Matrice.EPSILON && normeL1 < Matrice.EPSILON && normeLinfini < Matrice.EPSILON) {
            System.out.println("La solution est précise avec une norme inférieure à epsilon .");
        } else {
            System.out.println("La solution n'est pas suffisamment précise.");
        }
        System.out.println("\n***************Resolution en utilisant resolutionPartielle***********************\n");
        double matrice2[][] = {{1, 1, -2}, {4, -2, 1}, {3, -1, 3}};
        Matrice a2 = new Matrice(matrice);
        double vecteur2[] = {3, 5, 8};
        Vecteur b2 = new Vecteur(vecteur2);

        // Créer le système linéaire
        Helder systeme2 = new 	Helder(a2, b2);
        systeme2.factorLDR();
        Vecteur x2=systeme2.resolutionPartielle();

        // Appliquer la méthode factorLDR
        System.out.println("la solution du systeme ax=b est :\n"+"matrice A :\n"+a2+"\nVecteur b :\n"+b2+"\nSolution :\n"+x2);
    
    // Calculer Ax - b
    Vecteur ax2 = systeme.produit(a2, x2);
    int n2 = systeme2.getOrdre();
    Vecteur difference2 = new Vecteur(n2);
   

    for (int i = 0; i < b.taille(); i++) {
        difference2.remplaceCoef(i, ax2.getCoeffecient(i) - b2.getCoeffecient(i));
    }

    // Calculer la norme de la différence
    double normeL22 = difference.normeL2();
    double normeL12 = difference.normeL1();
    double normeLinfini2 = difference.normeLinfini();
    // Vérifier si la norme est négligeable
    if (normeL22 < Matrice.EPSILON && normeL12 < Matrice.EPSILON && normeLinfini2 < Matrice.EPSILON) {
        System.out.println("La solution est précise avec une norme inférieure à epsilon .");
    } else {
        System.out.println("La solution n'est pas suffisamment précise.");
    }
	
	    }
}
