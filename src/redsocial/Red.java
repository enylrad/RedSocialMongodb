package redsocial;

import java.net.UnknownHostException;
import java.util.Scanner;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class Red {

	private static Mongo mongoClient;
	private static String dir = "192.168.1.10";
	private static int socket = 27017;
	private static DB db;
	
	private static Scanner registro = new Scanner(System.in);
	
	public static void main(String[] args) throws UnknownHostException, MongoException {
		
		 mongoClient = new Mongo( dir , socket );
		 db = mongoClient.getDB( "redsocial" );
		 
		 int opcion = -1;
		 
		 do{
			 
			 System.out.println("Bienvenido a la Red Social MongoMola,");
			 System.out.println("¿Quiere iniciar sesión o es un nuevo usuario?,");
			 System.out.println("\t1 - Iniciar sesión");
			 System.out.println("\t2 - Crear cuenta");
			 System.out.println("\t0 - Salir");
			 System.out.print("Introduzca una opción: ");
			 
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
		
	}
	
	public static void Logear(){
		
		
	}
	
	public static void redSocial(){
	
	
		
		
	}
	
	
}
