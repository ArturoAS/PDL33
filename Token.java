
public class Token {

	protected String valor;
	protected int valorI;

	private int idToken;
	public Token(int idToken){
		this.valor="";
		this.idToken=idToken;
		valorI=0;
	}
	public class Identificador{
		protected String id;
		protected int pos;
		protected Identificador(String id, int pos){
			this.id=id;
			this.pos=pos;
		}
	}



	protected void concatena(char c){
		this.valor=valor+c;
	}

	protected void concatenaEntero(int e){
		if(valorI==0)
			valorI = e;
		else
			this.valorI=this.valorI*10 + e;
	}
	
	public void generaToken(){
		AnalizadorLexico.TokenCount++;
		if(valorI == 0){
			System.out.println("< "+valor+" , "+AnalizadorLexico.TokenCount + " >");
		}
		else
			System.out.println("< " + valorI +" , "+AnalizadorLexico.TokenCount + " >");
	}

}
