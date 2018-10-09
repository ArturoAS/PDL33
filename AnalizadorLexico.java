import java.io.FileReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class AnalizadorLexico {

	//Atributos y constantes
	protected static int TokenCount;
	private String fichero;

	private Token token;
	protected static char aux;




	protected static ArrayList<String> TablaOpRel = new ArrayList<>();
	protected static ArrayList<String> TablaOpLog = new ArrayList<>();
	protected static ArrayList<String> TablaOpArit = new ArrayList<>();
	protected static ArrayList<String> TablaOpAux = new ArrayList<>();
	protected static Map<Integer,String> TablaSimbolos = new HashMap<>();

	final String[] operadoresAux = {"{","}","[","]","(",")",";",",",":"};
	final String[] operadoresA = {"+","-","*","/","%","++"};
	final String[] operadoresR = {"=",">","<", };
	final String [] operadoresL = {"&","|","!"}; 
	final String[] tokensReservadas = {"do","while","print", "function" , 
			"var", "int", "String", "bool", "default", "break","prompt","return", "default","switch", "case", "if", "else"};


	// Inicializacion de Tablas

	public AnalizadorLexico(String Fichero) {

		this.fichero=Fichero;

		TablaSimbolos = new HashMap<Integer,String>();

		TokenCount=0;

		for(int i=0; i<operadoresAux.length; i++)
			TablaOpAux.add(operadoresAux[i]);

		for(int i=0; i<operadoresA.length; i++)
			TablaOpArit.add(operadoresA[i]);

		for(int i=0; i<operadoresR.length; i++)
			TablaOpRel.add(operadoresR[i]);

		for(int i=0; i<operadoresL.length; i++)
			TablaOpLog.add(operadoresL[i]);

		for(int i=0; i<tokensReservadas.length; i++)
			TablaSimbolos.put(TablaSimbolos.size(), tokensReservadas[i]);
	} //from AnalizadorLexico (Constructor)

	protected static FileInputStream fr;


	@SuppressWarnings({ "resource", "unused" })

	private void read() throws Exception{


		int caract=-2;

		fr = new FileInputStream(fichero);

		//BUCLE DE LECTURA DE CARACTERES

		while(caract != -1) {
			token = new Token("");	//inicializa token
			//leo caracter
			caract = MetodosAux.leer();

			char c = (char)caract;



			if(caract==-1)  // si caract == -1 continue para salir del bucle (Fin del fichero)
				continue;

			String estado = MetodosAux.selectorEstado(c);  // seleccion de Estado para identificar el tipo y generar el token determinado


			switch (estado) {

			// Si el primer caracter del nuevo token es un delimitador,no hago nada

			/*
			 * Operadores Auxiliares
			 * Concateno el caracter y genero el token
			 */
			case "opAux":
				token.concatena(c);
				token.generaToken();
				break;

				/*
				 * Operadores Aritmeticos
				 * Si detecta /*.......... , se trata de un comentario y se debe leer 
				 * hasta sin hacer nada hasta que encuentre ....  * / fin de comentario
				 * Si el primer caracter es un +  conpruebo si el siguiente es otro + . casos ( + o ++)
				 * En caso de que el caracter sea distinto, genero el token
				 * 
				 */

			case "Ar":
				token.concatena(c);

				if(c == '/'){
					caract = MetodosAux.leer();
					c = (char)caract;
					if(c=='*'){
						boolean exit = false; // flag de condicion de salida de bucle
						while(!exit){
							caract = MetodosAux.leer();
							c = (char)caract;
							if(c=='*'){
								caract = MetodosAux.leer();
								c = (char)caract;
								if(c=='/')
									exit = true;  // si detecta */ flag a true para salir del bucle 
							}	
						}
					}
				}


				else if(c == '+'){

					caract = MetodosAux.leer();
					c = (char)caract;

					if( c== '+'){
						token.concatena(c);
						token.generaToken();
					}
					else{
						//genero token y no leo caracter (meto el caracter leido en mi buffer)
						token.generaToken();
						aux=c;
					} //exit
				}
				else{
					//genero token
					token.generaToken();
				}

				break; // exit


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

				caract = MetodosAux.leer();
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
					break;
				}  // from switch
				break;


				/*
				 * Numeros
				 * mientras lleguen digitos concatenamos enteros
				 * En el momento que llegue un no digito dejamos de concatenar, desleemos aux=c y genero token
				 */
			case "d": 
				token = new Token.Entero();
				//
				while(Character.isDigit(c)){

					token.concatenaEntero(c);

					caract = MetodosAux.leer();
					c = (char)caract;
				} //from while

				token.generaToken();

				aux=c;
				break;


				/*
				 * Operadores logicos
				 * Si recibo el caracter ! debo comprobar el siguiente caracter para saber si tengo que generar el token ! o !=
				 * En caso contrario las posibles recepciones solo pueden ser & y |
				 * Ambos caracteres deben ir seguidos del mismo caracter (&& y ||)
				 * En caso contrario es un error Lexico y salta una excepcion
				 */
			case "Lo": 

				token.concatena(c);
				switch (c) {
				case '!':
					caract = MetodosAux.leer();
					c=(char)caract;
					if(c=='='){
						token.concatena(c);
						token.generaToken();
					} //from if
					else{
						//meto caracter leido en el buffer pq debo volver a leerlo ya que es delimitador
						aux=c;
						token.generaToken();
					} //from else
					break;


				case '&': 
					caract = MetodosAux.leer();
					c=(char)caract;
					if(c=='&'){
						token.concatena(c);
						token.generaToken();
						break;
					} // from if
					else{
						//meto caracter leido en el buffer pq debo volver a leerlo ya que es delimitador
						aux=c;
						throw new Exception("Token no valido >" + token.valor);
					} // from else


				case '|':
					caract = MetodosAux.leer();
					c=(char)caract;
					if(c=='|'){
						token.concatena(c);
						token.generaToken();
					}  //from if
					else{
						//meto caracter leido en el buffer pq debo volver a leerlo ya que es delimitador
						aux=c;
						throw new Exception("Token no valido" + token.valor);
					} //from else
					break;
				} //from switch
				break;
				/*
				 * Si primer caracter " entonces sabemos que es una cadena
				 * concatenamos caracteres hasta que lleguen otras comillas
				 * En caso de recibir el caracter \ no salimos del bucle y concatenamos pues esta permitido 
				 * que lleguen comillas tras esto (\" > para escribir comillas en una cadena) 
				 */

			case "CAD":

				caract = MetodosAux.leer();
				c= (char)caract;
				if(c!='"'){
					token.concatena(c);
					caract = MetodosAux.leer();
					c= (char)caract;
				} // from if
				while(c!='"'){

					if(c=='\\') {
						caract = MetodosAux.leer();
						c= (char)caract;
						token.concatena(c);

					} //from if
					else
						token.concatena(c);

					caract = MetodosAux.leer();
					c= (char)caract;
				} //from while
				token = new Token.Cadena(token.valor);
				token.generaToken();
				break;

				/*
				 * Identificadores
				 * En este caso tengo que generar un token del tipo Identificador, que hereda de token
				 * Un identificador comienza por letra, pudiendo ser el resto de caracteres digitos, letras o subrayado _
				 * Concateno en caso de que lleguen dichos caracteres
				 * En cuanto llegue un caracter distinto, desleo (aux=c)
				 * Compruebo si esta en la tabla de simbolos
				 * genero el token con parametros valor del id , pos tabla simbolos ,num de token  
				 */
			case  "id":
				token = new Token.Identificador(-1,""); // inicializo token tipo identificador vacio
				token.concatena(c);
				caract = MetodosAux.leer();
				c = (char)caract;

				while(Character.isJavaIdentifierPart(c) && c!='$' || c=='_'){
					token.concatena(c);
					caract = MetodosAux.leer();
					c = (char)caract;
				} //from while

				boolean found=false;
				if(TablaSimbolos.containsValue(token.valor)){
					for ( int i=0;!found && i< TablaSimbolos.size(); i++ ) {
						if(TablaSimbolos.get(i).equals(token.valor)){

							token = new Token.Identificador(i,token.valor);
							found=true;

						} //from if
					} //from for
				} //from if


				else{
					TablaSimbolos.put(TablaSimbolos.size(), token.valor);
					token=new Token.Identificador(TablaSimbolos.size()-1 ,token.valor );
				} //from else


				token.generaToken();
				aux=c;
				break;

			} //from switch (de Estados)

		} //from while (bucle de lectura de caracteres)

	} //from read()
	/*
	 * Imprime cada entrada de la Tabla de simbolos
	 */

	protected static void printTs(){
		//AnalizadorLexico.TablaSimbolos
		System.out.printf("\n%-5s %6s %10s" ,"N.SIMBOLO", "|", "SIMBOLO \n");
		System.out.println("_______________|________________");
		for(int i = 1 ; i<= AnalizadorLexico.TablaSimbolos.size();i++ ){
			System.out.printf("\n%-5d %10s %10s ",i , "|" , AnalizadorLexico.TablaSimbolos.get(i-1));
		}
		System.out.println("\n");
	}


	public static void main(String[]args) throws Exception{


		int opcion = 0;

		
		String doc = "C:\\Users\\Alejandro\\Desktop\\ECLIPSE\\PDL\\src\\prueba.txt";

		AnalizadorLexico an = new AnalizadorLexico(doc);

		do {
			System.out.println("Bienvenido \n");
			System.out.println("Selecciona una opcion:\n");
			System.out
			.println("\t1. Analizador Lexico.");
			System.out.println("\t2. Mostrar Contenido Tabla de Simbolos.");
			System.out.println("\t3. Salir.");


			try {
				System.out.print("> ");
				Scanner entrada=new Scanner(System.in);
				opcion= entrada.nextInt();
			} catch (Exception e) {
				throw new Exception("Not number");
			}

			switch (opcion) {
			case 1:
				System.out.println("Comenzando Generacion de Tokens... \n");
				System.out.println("__________________________________________________________");
				System.out.println("N.Token |\t TOKENS");


				try {
					an.read();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("\n ...Tokens Generados Correctamente...\n");
				break;

			case 2: 
				printTs();
				break;
			case 3: 
				System.out.println("Saliendo del Sistema...");
				break;
			default:
				System.err.println("Opcion No Valida");
				break;


			} // from switch
		}while(opcion != 3); // do While
		
		System.out.println("Hasta pronto");
	} // from main
} //from class AnalizadorLexico
