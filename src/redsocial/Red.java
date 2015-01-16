package redsocial;

import java.net.UnknownHostException;
import java.util.Scanner;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class Red {

	
	private static Mongo mongoClient; 	//Conexi�n
	private static DB db;				//Base de datos
	
	private static String dir = "127.0.0.1";
	private static int socket = 27017;
	
	private static Scanner registro = new Scanner(System.in);

	
	public static void main(String[] args) throws UnknownHostException, MongoException, InterruptedException {
		
		 mongoClient = new Mongo( dir , socket );
		 db = mongoClient.getDB( "redsocial" );
		 
		 menuPrincipal(db);
		 
	}
	
	//////////////////////////////MENUS////////////////////////////////////
	
	/**
	 * Menu principal de inicio de la aplicacion
	 * @param db
	 * @throws InterruptedException
	 */
	public static void menuPrincipal(DB db) throws InterruptedException{
		
		int opcion = -1;
		 
		 do{
			 
			 System.out.println("Bienvenido a la Red Social MongoMola,");
			 System.out.println("\t1 - Iniciar sesi�n");
			 System.out.println("\t2 - Crear cuenta");
			 System.out.println("\t0 - Salir");
			 System.out.print("Introduzca una opci�n: ");
			 
			 try{
				 
				 opcion = Integer.parseInt(registro.nextLine());
				 
				 switch (opcion) {
					case 1:
				
						Logear();					
						break;
						
					case 2:
						
						nuevoUsuario();
						
						break;
						
					case 0:
						System.out.println("Salidendo del programa...");
						Thread.sleep(1000);
						System.out.println("Ha salido del programa.");
						break;
	
					default:
						
						System.out.println("Opci�n no valida, introduzcala de nuevo.");
						opcion = -1;
		
						break;
					}
				 
			 }catch(NumberFormatException e){
				 
				 System.out.println("La opci�n debe ser un n�mero.");
				 opcion = -1;
				 
			 }
			 
		 }while(opcion != 0);
		
	}
	

	/**
	 * Menu de la red social una vez iniciada la sesion
	 * @param u
	 */
	public static void menuRedSocial(Usuario u){
	
	
		int opcion = -1;
		 
		 do{
			 
			 System.out.println("Bienvenido " + u.getUsuario());
			 System.out.println("�Que desea hacer?,");
			 System.out.println("\t1 - Gesti�n Grupo");
			 System.out.println("\t2 - Darse de baja");
			 System.out.println("\t0 - Cerrar Sesi�n");
			 System.out.print("Introduzca una opci�n: ");
			 
			 try{
				 
				 opcion = Integer.parseInt(registro.nextLine());
				 
				 switch (opcion) {
					case 1:
				
						menuGrupo(u);
									
						break;
						
					case 2:
						
						if(darseBaja(u)){
							opcion = 0;
						}else{
							opcion = -1;
						}
						
						break;
						
					case 0:
						
						System.out.println("Sesi�n cerrada correctamente");
						
						break;
	
					default:
						
						System.out.println("Opci�n no valida, introduzcala de nuevo.");
						opcion = -1;
		
						break;
					}
				 
			 }catch(NumberFormatException e){
				 
				 System.out.println("La opci�n debe ser un n�mero.");
				 opcion = -1;
				 
			 }
			 
		 }while(opcion != 0);
		
		
	}
	
	/**
	 * Menu de gestion de grupos
	 * @param u
	 */
	public static void menuGrupo(Usuario u){
		
		int opcion = -1;
		 
		 do{
			 
			 System.out.println("Bienvenido " + u.getUsuario());
			 System.out.println("�Que desea hacer?,");
			 System.out.println("\t1 - Crear Grupo");
			 System.out.println("\t2 - Borrar Grupo");
			 System.out.println("\t3 - Comentar en Grupo");
			 System.out.println("\t4 - Visualizar Comentarios");
			 System.out.println("\t5 - Listar usuarios de localidad de un grupo");
			 System.out.println("\t6 - Salir Grupo");
			 System.out.println("\t0 - Volver");
			 System.out.print("Introduzca una opci�n: ");
			 
			 try{
				 
				 opcion = Integer.parseInt(registro.nextLine());
				 
				 switch (opcion) {
				 case 1:
						
						crearGrupo(u);
									
						break;
						
					case 2:
						
						borrarGrupo(u);
						
						break;
					
					case 3:
							
						comentarGrupo(u);
										
						break;
							
					case 4:
							
						listarComentarios();
							
						break;
						
					case 5:
								
						listarGrupo();
											
						break;
								
					case 6:
								
						salirGrupo(u);
								
						break;
						
					case 0:
						System.out.println("Volviendo al menu principal");
						break;
	
					default:
						
						System.out.println("Opci�n no valida, introduzcala de nuevo.");
						opcion = -1;
		
						break;
					}
				 
			 }catch(NumberFormatException e){
				 
				 System.out.println("La opci�n debe ser un n�mero.");
				 opcion = -1;
				 
			 }
			 
		 }while(opcion != 0);
		
	}
	
	////////////////////////////////////////FIN MENUS///////////////////////////////////////////////////////////
	
	////////////////////////////////////////GESTIONES///////////////////////////////////////////////////////////
	
	/**
	 * Creaci�n del nuevo usuario
	 */
	public static void nuevoUsuario(){
		
		String nombre, apellido, correo, contrasenya, repcontrasenya;
		String[] direccion = new String[4];
 		
		System.out.println("Se va a proceder a crear un nuevo usuario,");
		System.out.print("Nombre: ");
		nombre = registro.nextLine();
		System.out.print("Apellidos: ");
		apellido = registro.nextLine();
		System.out.print("Correo: ");
		correo = registro.nextLine();
		
		do{
			
			System.out.print("Contrase�a: ");
			contrasenya = registro.nextLine();
			System.out.print("Repita la contrase�a: ");
			repcontrasenya = registro.nextLine();
			
			if(!contrasenya.equals(repcontrasenya)){
				System.out.println("La contrase�a no coincide, repitala por favor.");
			}
			
		}while(!contrasenya.equals(repcontrasenya));
		
		System.out.println("Direcci�n,");
		System.out.print("Calle: ");
		direccion[0] = registro.nextLine();
		System.out.print("N�: ");
		direccion[1] = registro.nextLine();
		System.out.print("Localidad: ");
		direccion[2] = registro.nextLine();
		System.out.print("C.P: ");
		direccion[3] = registro.nextLine();
		
		Usuario u = new Usuario();
		u.crearUsuario(nombre, apellido, correo, contrasenya, direccion, db);
		System.out.println("Ya puedes logerate en la Red Social...");
		
	}
	
	/**
	 * Gesti�n del inicio de sesi�n
	 */
	public static void Logear(){
		
		String correo = "", pass = "";
		
		System.out.print("Introduce correo: ");
		correo=registro.nextLine();
		System.out.print("Introduce password: ");
		pass=registro.nextLine();
		
		Usuario u = new Usuario();
		
		if(u.logear(correo, pass, db)){
			
			menuRedSocial(u);
			
		}else{
			
			System.out.println("Error al logear");
		}
	}
	
	
	/**
	 * Creacion de grupos
	 * @param u
	 */
	public static void crearGrupo(Usuario u){
		
		String nombre = "";
		Grupo g = new Grupo();
		
		System.out.print("�Que nombre tendr� su grupo?: ");
		nombre = registro.nextLine();
		
		g.crearGrupo(nombre, db, u);
		
	}
	
	/**
	 * Eliminacion de grupos
	 * @param u
	 */
	public static void borrarGrupo(Usuario u){
		
		
	}
	
	/**
	 * Union de grupos
	 * @param u
	 */
	public static void unirseGrupo(Usuario u){
		
	}
	
	/**
	 * Salidad de grupos
	 * @param u
	 */
	public static void salirGrupo(Usuario u){
		
		
	}
	
	/**
	 * Comentar en un grupo	
	 * @param u
	 */
	public static void comentarGrupo(Usuario u){
		
		
	}
	
	/**
	 * Listar usuarios de un grupo
	 */
	public static void listarGrupo(){
		
		
	}
	
	/**
	 * Listar comentarios de un grupo
	 */
	public static void listarComentarios(){
		
	}
	
	/**
	 * Gestion de baja de la red social
	 * @param u
	 */
	public static boolean darseBaja(Usuario u){
		
		String respuesta = "No";
			
		while(true){
			
			System.out.println("Se va a procedr a darle de baja del servicio, �Esta seguro?: ");
			respuesta = registro.nextLine();
			
			if(Character.toString(respuesta.charAt(0)).equalsIgnoreCase("S")){				
				u.darBaja(db);
				System.out.println("Se ha dado de baja correctamente.");
				return true;
			}else if(Character.toString(respuesta.charAt(0)).equalsIgnoreCase("N")){
				System.out.println("Gracias por seguir con nosotros :), se le devolvera al menu");
				return false;
			}
		
			System.out.println("La respuesta no es correcta.");
		}
			
	}
		
	
	///////////////////////////////////////////////FIN GESTIONES//////////////////////////////////////////////////
	
}
