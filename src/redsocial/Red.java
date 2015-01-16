package redsocial;

import java.net.UnknownHostException;
import java.util.Scanner;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class Red {

	private static Mongo mongoClient;
	private static DB db;
	
	private static String dir = "127.0.0.1";
	private static int socket = 27017;
	
	private static Scanner registro = new Scanner(System.in);
	
	public static void main(String[] args) throws UnknownHostException, MongoException {
		
		 mongoClient = new Mongo( dir , socket );
		 db = mongoClient.getDB( "redsocial" );
		 
		 menuPrincipal(db);
		 
	}
	
	public static void menuPrincipal(DB db) throws InterruptedException{
		
		int opcion = -1;
		 
		 do{
			 
			 System.out.println("Bienvenido a la Red Social MongoMola,");
			 System.out.println("\t1 - Iniciar sesión");
			 System.out.println("\t2 - Crear cuenta");
			 System.out.println("\t0 - Salir");
			 System.out.print("Introduzca una opción: ");
			 
			 try{
				 
				 opcion = Integer.parseInt(registro.nextLine());
				 
				 switch (opcion) {
					case 1:
						System.out.println("Accediendo al login...");
						Thread.sleep(1000);
						Logear();					
						break;
						
					case 2:
						System.out.println("Accediendo a la creación de cuenta...");
						Thread.sleep(1000);
						Logear();				
						nuevoUsuario();
						
						break;
						
					case 0:
						System.out.println("Salidendo del pragra...");
						Thread.sleep(1000);
						System.out.println("Ha salido del programa.");
						break;
	
					default:
						
						System.out.println("Opción no valida, introduzcala de nuevo.");
						opcion = -1;
		
						break;
					}
				 
			 }catch(NumberFormatException e){
				 
				 System.out.println("La opción debe ser un número.");
				 opcion = -1;
				 
			 }
			 
		 }while(opcion != 0);
		
	}
	
	
	public static void nuevoUsuario(){
		
		String nombre, apellido, correo, contrasenya, repcontrasenya, direccion;
		
		System.out.println("Se va a proceder a crear un nuevo usuario,");
		System.out.print("Nombre: ");
		nombre = registro.nextLine();
		System.out.print("Apellidos: ");
		apellido = registro.nextLine();
		System.out.print("Correo: ");
		correo = registro.nextLine();
		
		do{
			
			System.out.print("Contraseña: ");
			contrasenya = registro.nextLine();
			System.out.print("Repita la contraseña: ");
			repcontrasenya = registro.nextLine();
			
			if(!contrasenya.equals(repcontrasenya)){
				System.out.println("La contraseña no coincide, repitala por favor.");
			}
			
		}while(!contrasenya.equals(repcontrasenya));
		
		System.out.print("Dirección: ");
		direccion= registro.nextLine();
		
		Usuario u = new Usuario();
		u.crearUsuario(nombre, apellido, correo, contrasenya, direccion, db);
		System.out.println("Ya puedes logerate en la Red Social...");
		
	}
	
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
	
	public static void menuRedSocial(Usuario u){
	
	
		int opcion = -1;
		 
		 do{
			 
			 System.out.println("Bienvenido " + u.getUsuario());
			 System.out.println("¿Que desea hacer?,");
			 System.out.println("\t1 - Gestión Grupo");
			 System.out.println("\t2 - Darse de baja");
			 System.out.println("\t0 - Cerrar Sesión");
			 System.out.print("Introduzca una opción: ");
			 
			 try{
				 
				 opcion = Integer.parseInt(registro.nextLine());
				 
				 switch (opcion) {
					case 1:
				
						menuGrupo(u);
									
						break;
						
					case 2:
						
						darseBaja(u);
						
						break;
						
					case 0:
						
						System.out.println("Sesión cerrada correctamente");
						
						break;
	
					default:
						
						System.out.println("Opción no valida, introduzcala de nuevo.");
						opcion = -1;
		
						break;
					}
				 
			 }catch(NumberFormatException e){
				 
				 System.out.println("La opción debe ser un número.");
				 opcion = -1;
				 
			 }
			 
		 }while(opcion != 0);
		
		
	}
	
	public static void menuGrupo(Usuario u){
		
		int opcion = -1;
		 
		 do{
			 
			 System.out.println("Bienvenido " + u.getUsuario());
			 System.out.println("¿Que desea hacer?,");
			 System.out.println("\t1 - Crear Grupo");
			 System.out.println("\t2 - Borrar Grupo");
			 System.out.println("\t3 - Comentar en Grupo");
			 System.out.println("\t4 - Visualizar Comentarios");
			 System.out.println("\t5 - Listar usuarios de localidad de un grupo");
			 System.out.println("\t6 - Salir Grupo");
			 System.out.println("\t0 - Volver");
			 System.out.print("Introduzca una opción: ");
			 
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
						
						System.out.println("Opción no valida, introduzcala de nuevo.");
						opcion = -1;
		
						break;
					}
				 
			 }catch(NumberFormatException e){
				 
				 System.out.println("La opción debe ser un número.");
				 opcion = -1;
				 
			 }
			 
		 }while(opcion != 0);
		
	}
	
	public static void crearGrupo(Usuario u){
		
	}
	
	public static void borrarGrupo(Usuario u){
		
		
	}
	
	public static void unirseGrupo(Usuario u){
		
	}
	
	public static void salirGrupo(Usuario u){
		
		
	}
	
	public static void comentarGrupo(Usuario u){
		
		
	}
	
	public static void listarGrupo(){
		
		
	}
	
	public static void listarComentarios(){
		
	}
	
	public static void darseBaja(Usuario u){
		
		
	}
	
}
