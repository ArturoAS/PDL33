import java.io.IOException;

public class MetodosAux {
	
	/*
	 * Lee el siguiente caracter del fichero
	 * Si el buffer de caracteres que no debieron ser leidos esta lleno, lee de hay pues se introdujo un caracter delimitador, o perteneciente 
	 * al siguiente token , y  no al actual analizado
	 */
	
	protected static int leer() throws IOException{
		if(AnalizadorLexico.aux==0){
			return AnalizadorLexico.fr.read();
		}
		int s = AnalizadorLexico.aux;
		AnalizadorLexico.aux =0;
		return s;
	}
	
	/*
	 * Selecciona un estado del AFD a partir del primer caracter que llega al automata para la generacion de un nuevo token
	 */
	protected static String selectorEstado(char c) throws Exception{

		String s = ""+c;



		if( c==' ' || c==';' || c=='\n' || c=='\t' )
			return "del";

		if(AnalizadorLexico.TablaOpArit.contains(s)){
			return "Ar";
		}
		if(AnalizadorLexico.TablaOpRel.contains(s))
			return "Re";


		if(AnalizadorLexico.TablaOpLog.contains(s))
			return "Lo";


		if( Character.isDigit(c) )
			return "d";

		if(Character.isJavaIdentifierStart(c) )
			return "l";

		throw new Exception("Caracter recibido no valido: " + c);

	}

}
