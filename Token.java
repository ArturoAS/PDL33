
public class Token {

	private String valor;
	private int valorI;

	private int idToken;
	public Token(int idToken){
		this.valor="";
		this.idToken=idToken;
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
		if(valorI == 0){
			System.out.println(valor);
		}
		else
			System.out.println(valorI);
	}

}
