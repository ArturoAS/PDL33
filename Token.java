
public class Token {

	protected String valor; //atributo


	//Constructor
	
	protected Token(String valor){
		this.valor=valor;
	}

//-----------------------------------------------------------SUBCLASES (TIPOS DE TOKENS)---------------------------------------------------------------

	/*
	 * Clase Identificador
	 * Inicializa el valor del token al String s y la posicion en la tabla de simbolos a pos
	 * proveniendo pos de un metodo buscar implementado antes de generar el token en la clase analizador lexico
	 */
	protected static class Identificador extends Token{
		private int posTs;
		protected Identificador( int pos, String s){
			super(s);
			this.posTs=pos;
		} //from Identificador (constructor)
		protected void generaToken(){
			AnalizadorLexico.TokenCount++;
			System.out.println(AnalizadorLexico.TokenCount + "\t|   < ID , " + valor + " , "+ posTs + " >");
		} //from generaToken
	} //from Identificador (subclass)


	/*
	 * Clase Cadena
	 * Inicializa el valor del token a s, siendo s un string pasado como parametro en el constructor que contiene la cadena entera
	 */

	protected static class Cadena extends Token{
		protected Cadena(String s){
			super(s);
		} //from Cadena (constructor)

		protected void generaToken(){
			AnalizadorLexico.TokenCount++;
			System.out.println(AnalizadorLexico.TokenCount + "\t|   < Cadena , " +valor + " >");
		} //from generaToken
	} //from Cadena (subclass)


	/*
	 * Clase Entero
	 * inicializa token a valorEntero = e y Valor="" a cadena vacia
	 */

	protected static class Entero extends Token{
		protected int valorEntero;
		protected Entero(){
			super("");
			valorEntero=-1;
		} //from Entero (constructor)

		protected void generaToken(){
			AnalizadorLexico.TokenCount++;
			System.out.println(AnalizadorLexico.TokenCount + "\t|   < Entero , " +valorEntero + " >");
		} //from generaToken
		protected void concatenaEntero(int e){

			if(valorEntero==-1)
				valorEntero = Character.getNumericValue(e);

			else
				this.valorEntero=this.valorEntero*10 + Character.getNumericValue(e);
		} //from concatenaEntero
	} //from Entero (subclass)

	/*
	 * Genera Token para Operadores
	 * FORMARO: <Operador , caracterOperador>
	 * <Operador , } >
	 */

	protected void generaToken(){
		AnalizadorLexico.TokenCount++;

		System.out.println(AnalizadorLexico.TokenCount + "\t|   < Operador , "+valor+" >");
	} //from generaToken



	//----------------------------------------------------------METODOS AUXILIARES TOKEN----------------------------------------------------------------

	protected void concatena(char c){
		this.valor=valor+c;
	} //from concatena

	//Metodo padre implementado en la clase Entero
	// INTERFAZ
	protected void concatenaEntero(int e){
	} 

}
