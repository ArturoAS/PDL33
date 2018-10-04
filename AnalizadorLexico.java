import java.io.FileReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnalizadorLexico {

	//Atributos y constantes
	private int TokenCount;
	private String fichero;

	private Token token;
	private char aux;


	private int caractCount;

	private ArrayList<Token> TablaSimbolos;
	private Map<Integer,String> TablaVar = new HashMap<Integer,String>();
	private ArrayList<String> TablaOpRel = new ArrayList<>();
	private ArrayList<String> TablaOpLog = new ArrayList<>();
	private ArrayList<String> TablaOpArit = new ArrayList<>();
	private Map<Integer,String> TablaPR = new HashMap<Integer,String>();

	final String[] operadoresA = {"+","-","*","/","%","++"};
	final String[] operadoresR = {"=","==","!=",">","<",">=","<="};
	final String [] operadoresL = {"&&","||","!"}; 
	final String[] tokensReservadas = {"do","while","print", "function" , 
			"var", "int", "string", "bool", "default", "break","prompt"};


	/*
	 * Inicializacion de Tablas
	 */

	public AnalizadorLexico(String Fichero) {

		this.fichero=Fichero;

		TablaSimbolos = new ArrayList<Token>();	

		caractCount=0;

		TokenCount=0;

		for(int i=0; i<operadoresA.length; i++)
			TablaOpArit.add(operadoresA[i]);

		for(int i=0; i<operadoresR.length; i++)
			TablaOpRel.add(operadoresA[i]);

		for(int i=0; i<operadoresL.length; i++)
			TablaOpLog.put(i, operadoresL[i]);

		for(int i=0; i<tokensReservadas.length; i++)
			TablaPR.put(i, tokensReservadas[i]);
	}
	private FileInputStream fr;

	@SuppressWarnings({ "resource", "unused" })

	private void read() throws Exception{
		//		FileReader fr;

		int caract=-2;

		fr = new FileInputStream(fichero);

		//BUCLE DE LECTURA DE CARACTERES

		while(caract != -1) {
			token = new Token(0);	//inicializa token
			//leo caracter
			caract = leer();
			caractCount++;
			char c = (char)caract;



			if(caract==-1)
				continue;
			String estado = selectorEstado(c);


			switch (estado) {

			/* 
			 * Si el primer caracter del nuevo token es un delimitador, no lo genero y paso al siguiente caracter
			 */

			case "del": 
				caractCount++;
				System.out.println("");
				break;



				/*
				 * Compruebo si el primer caracter esta en la tabla de operadores aritmeticos.
				 * Si esta tengo que comprobar, al tener todos los operadores tamanio 1, si se da el caso de = o == que es
				 * el unico que puede dar problemas.
				 * IF case  = genero token y no leo el siguiente puesto que el leido me es necesario para el nuevo token como primer caracter.  Inicializo
				 * siguiente token.
				 * IF case == concateno, genero token y ahora si es necesario leer el siguiente caracter para comenzar con el nuevo token
				 * 
				 * >>> FALTA ESTUDIAR CASO DE COMENTARIOS EN BLOQUE  /*......*/

			case "Ar":
				token.concatena(c);
				if(c == '+'){
					// fr.mark(0); // guardo copia del caracter

					caract =  leer();
					caractCount++; // avanzo el contador de caracteres leido
					c = (char)caract;

					if(c== '+'){
						token.concatena(c);
						//genero token (++)
						token.generaToken();


					}
					else{
						//genero token y no leo caracter (meto el caracter leido en mi buffer
						token.generaToken();
						aux=c;
						//fr.reset(); //desleo
						caractCount--; // como desleo decremento contador

					}
					token = new Token(0);	//inicializa token siguiente iteracion (RESET)
					//exit
				}
				else{
					//genero token
					token.generaToken();
					//Leo sig caracter
					//exit
				}
				break;


				/*
				 * Compruebo si el primer caracter esta en la tabla de operadores Relacionales.
				 * Si esta tengo que comprobar si es asignacion (=) o de comparacion simple o compuesto (< o <=) 
				 * Se lee el siguiente caracter
				 * CASE  =  concateno, genero token y es necesario leer el siguiente caracter para comenzar con el nuevo token.
				 * CASE DEFAULT genero token y no es necesario leer sig caracter
				 *  Inicializo siguiente token.
				 * El caso de != se analizara en el siguiente modulo por simplicidad
				 */

			case "Re":

				token.concatena(c);

				caract = leer();
				caractCount++;
				c = (char)caract;
				switch (c) {
				case '=':
					token.concatena(c);
					//genero token
					token.generaToken();
					break;

				default:
					//genero token
					token.generaToken();
					aux=c;
					caractCount--;
					break;
				}
				break;

			case "d": 

				//mientras que lleguen numeros que concatene
				while(caract != -1 && c < 58 && c > 47){
					token.concatenaEntero(Character.getNumericValue(c));

					caract = leer();
					c = (char)caract;
				}
				token.generaToken();
				aux=c;
				break;

			}
		}
	}


	private String selectorEstado(char c) throws Exception{

		if( c==' ' || c==';' || c=='\n' || c=='\t')
			return "del";

		if(TablaOpArit.contains(c)){
			return "Ar";
		}
		if(c=='=' || c == '<' || c == '>')
			return "Re";


		if(c == '&' || c == '|')
			return "Lo";

		if(c == '!')
			return "!";

		if( c < 58 && c > 47 )
			return "d";

		if((c >= 65 && c <= 90) || c >= 97 && c <= 122 )
			return "l";

		throw new Exception("Caracter recibido no valido: " + c);

	}


	private int leer() throws IOException{
		if(aux==0){
			return fr.read();
		}
		int s = aux;
		aux = 0;
		return s;
	}





	public static void main(String[]args){


		String doc = "C:\\Users\\Alejandro\\Desktop\\ECLIPSE\\PDL\\src\\prueba.txt";

		AnalizadorLexico an = new AnalizadorLexico(doc);

		try {
			an.read();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}




}
