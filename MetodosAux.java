import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map.Entry;

public class MetodosAux {
	
	/*
	 * Lee el siguiente caracter del fichero
	 * Si el buffer de caracteres que no debieron ser leidos esta lleno, lee de hay pues se introdujo un caracter delimitador, o perteneciente 
	 * al siguiente token , y  no al actual analizado
	 */
	
	protected static int leer() throws IOException{
		if(AnalizadorLexico.aux==0){
			return AnalizadorLexico.fr.read();
		} //from if
		int s = AnalizadorLexico.aux;
		AnalizadorLexico.aux =0;
		return s;
	} //from leer
	
	/*
	 * Selecciona un estado del AFD a partir del primer caracter que llega al automata para la generacion de un nuevo token
	 * si es delimitador no hago nada, genero string vacio para que el switch de estados no ejecute ninguna accion
	 */
	protected static String selectorEstado(char c) throws Exception{

		String s = ""+c;

		

		if( c==10 || c==13 || c==' '|| c=='\t' )
			return "";	
		
		if(c=='"')
			return "CAD";
		
		if(AnalizadorLexico.TablaOpAux.contains(s))
			return "opAux";

		if(AnalizadorLexico.TablaOpArit.contains(s)){
			return "Ar";
		}
		if(AnalizadorLexico.TablaOpRel.contains(s))
			return "Re";


		if(AnalizadorLexico.TablaOpLog.contains(s))
			return "Lo";


		if( Character.isDigit(c) )
			return "d";

		
		if(Character.isLetter(c))
			return "id";
		

		throw new Exception("Caracter recibido no valido: " + c);

	} //from selectorEstado
	

} //from MetodosAux (class)
