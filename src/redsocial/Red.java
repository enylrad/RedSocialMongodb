package redsocial;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class Red {

	private static Mongo mongoClient; // Conexión
	private static DB db; // Base de datos

	private static String dir = "192.168.1.6";
	private static int socket = 27017;

	private static Scanner registro = new Scanner(System.in);

	public static void main(String[] args) throws UnknownHostException,
			MongoException, InterruptedException {

		mongoClient = new Mongo(dir, socket);
		db = mongoClient.getDB("redsocial_david");

		menuPrincipal(db);

	}

	// ////////////////////////////MENUS////////////////////////////////////

	/**
	 * Menu principal de inicio de la aplicacion
	 * 
	 * @param db
	 * @throws InterruptedException
	 */
	public static void menuPrincipal(DB db) throws InterruptedException {

		int opcion = -1;

		do {

			System.out.println("Bienvenido a la Red Social MongoMola,");
			System.out.println("\t1 - Iniciar sesión");
			System.out.println("\t2 - Crear cuenta");
			System.out.println("\t0 - Salir");
			System.out.print("Introduzca una opción: ");

			try {

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

					System.out
							.println("Opción no valida, introduzcala de nuevo.");
					opcion = -1;

					break;
				}

			} catch (NumberFormatException e) {

				System.out.println("La opción debe ser un número.");
				opcion = -1;

			}

		} while (opcion != 0);

	}

	/**
	 * Menu de la red social una vez iniciada la sesion
	 * 
	 * @param u
	 */
	public static void menuRedSocial(Usuario u) {

		int opcion = -1;

		do {

			System.out.println("Bienvenido " + u.getUsuario());
			System.out.println("¿Que desea hacer?,");
			System.out.println("\t1 - Gestión Grupo");
			System.out.println("\t2 - Darse de baja");
			System.out.println("\t0 - Cerrar Sesión");
			System.out.print("Introduzca una opción: ");

			try {

				opcion = Integer.parseInt(registro.nextLine());

				switch (opcion) {
				case 1:

					menuGrupo(u);

					break;

				case 2:

					if (darseBaja(u)) {
						opcion = 0;
					} else {
						opcion = -1;
					}

					break;

				case 0:

					System.out.println("Sesión cerrada correctamente");

					break;

				default:

					System.out
							.println("Opción no valida, introduzcala de nuevo.");
					opcion = -1;

					break;
				}

			} catch (NumberFormatException e) {

				System.out.println("La opción debe ser un número.");
				opcion = -1;

			}

		} while (opcion != 0);

	}

	/**
	 * Menu de gestion de grupos
	 * 
	 * @param u
	 */
	public static void menuGrupo(Usuario u) {

		int opcion = -1;

		do {

			System.out.println("Bienvenido " + u.getUsuario());
			System.out.println("¿Que desea hacer?,");
			System.out.println("\t1 - Crear Grupo");
			System.out.println("\t2 - Borrar Grupo");
			System.out.println("\t3 - Unirse Grupo");
			System.out.println("\t4 - Comentar en Grupo");
			System.out.println("\t5 - Visualizar Comentarios");
			System.out
					.println("\t6 - Listar usuarios de localidad de un grupo");
			System.out.println("\t7 - Salir Grupo");
			System.out.println("\t0 - Volver");
			System.out.print("Introduzca una opción: ");

			try {

				opcion = Integer.parseInt(registro.nextLine());

				switch (opcion) {
				case 1:

					crearGrupo(u);

					break;

				case 2:

					borrarGrupo(u);

					break;

				case 3:

					unirseGrupo(u);

					break;

				case 4:

					comentarGrupo(u);

					break;

				case 5:

					listarComentarios();

					break;

				case 6:

					listarGrupo();

					break;

				case 7:

					salirGrupo(u);

					break;

				case 0:
					System.out.println("Volviendo al menu principal");
					break;

				default:

					System.out
							.println("Opción no valida, introduzcala de nuevo.");
					opcion = -1;

					break;
				}

			} catch (NumberFormatException e) {

				System.out.println("La opción debe ser un número.");
				opcion = -1;

			}

		} while (opcion != 0);

	}

	////////////////////////////////////////FIN MENUS///////////////////////////////////////////////////////////

	////////////////////////////////////////GESTIONES///////////////////////////////////////////////////////////

	/**
	 * Creación del nuevo usuario
	 */
	public static void nuevoUsuario() {

		String nombre, apellido, correo, contrasenya, repcontrasenya;
		String[] direccion = new String[4];

		System.out.println("Se va a proceder a crear un nuevo usuario,");
		System.out.print("Nombre: ");
		nombre = registro.nextLine();
		System.out.print("Apellidos: ");
		apellido = registro.nextLine();
		System.out.print("Correo: ");
		correo = registro.nextLine();

		do {

			System.out.print("Contraseña: ");
			contrasenya = registro.nextLine();
			System.out.print("Repita la contraseña: ");
			repcontrasenya = registro.nextLine();

			if (!contrasenya.equals(repcontrasenya)) {
				System.out
						.println("La contraseña no coincide, repitala por favor.");
			}

		} while (!contrasenya.equals(repcontrasenya));

		System.out.println("Dirección,");
		System.out.print("Calle: ");
		direccion[0] = registro.nextLine();
		System.out.print("Nº: ");
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
	 * Gestión del inicio de sesión
	 */
	public static void Logear() {

		String correo = "", pass = "";

		System.out.print("Introduce correo: ");
		correo = registro.nextLine();
		System.out.print("Introduce password: ");
		pass = registro.nextLine();

		Usuario u = new Usuario();

		if (u.logear(correo, pass, db)) {

			menuRedSocial(u);

		} else {

			System.out.println("Error al logear");

		}
	}

	/**
	 * Creacion de grupos
	 * 
	 * @param u
	 */
	public static void crearGrupo(Usuario u) {

		String nombre = "";
		Grupo g = new Grupo();

		System.out.print("¿Que nombre tendrá su grupo?: ");
		nombre = registro.nextLine();

		g.crearGrupo(nombre, db, u);

	}

	/**
	 * Eliminacion de grupos
	 * 
	 * @param u
	 */
	public static void borrarGrupo(Usuario u) {

		System.out.println("¿Que grupo desea borrar?,");

		ArrayList<Grupo> grupos = Grupo.mostrarGruposAdmin(u, db);

		int opcion = -1;

		do {

			try {

				for (int i = 0; i < grupos.size(); i++) {

					System.out.println((i + 1) + " - "
							+ grupos.get(i).getNombre());

				}

				System.out.print("Elige una opción: ");
				opcion = Integer.parseInt(registro.nextLine());

				if (opcion > grupos.size() || opcion < 1) {

					System.out.println("La opción no es válida");
					opcion = -1;

				} else {

					String nombre = grupos.get(opcion - 1).getNombre();

					System.out.println("Se va a proceder a borar el grupo "
							+ nombre + "...");
					Thread.sleep(1000);
					System.out.println("...");
					grupos.get(opcion - 1).borrarGrupo(db);
					Thread.sleep(1000);
					System.out.println("Se ha borrado el grupo " + nombre);
				}

			} catch (NumberFormatException e) {
				// TODO: handle exception
				System.out.println("La opción debe ser un número");
				opcion = -1;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} while (opcion == -1);

	}

	/**
	 * Union de grupos
	 * 
	 * @param u
	 */
	public static void unirseGrupo(Usuario u) {

	}

	/**
	 * Salida de grupos
	 * 
	 * @param u
	 */
	public static void salirGrupo(Usuario u) {

	}

	/**
	 * Comentar en un grupo
	 * 
	 * @param u
	 */
	public static void comentarGrupo(Usuario u) {

		System.out.println("¿En que grupo quieres comentar?: ");

		ArrayList<Grupo> grupos = Grupo.mostrarGrupos(u, db);

		int opcion = -1;

		do {

			try {

				for (int i = 0; i < grupos.size(); i++) {

					System.out.println((i + 1) + " - "
							+ grupos.get(i).getNombre());

				}

				System.out.print("Elige una opción: ");
				opcion = Integer.parseInt(registro.nextLine());

				if (opcion > grupos.size() || opcion < 1) {

					System.out.println("La opción no es válida");
					opcion = -1;

				} else {

					System.out.print("Comentario: ");

					String comentario = registro.nextLine();

					u.anyadirComentario(grupos.get(opcion - 1), comentario, db);

				}

			} catch (NumberFormatException e) {
				// TODO: handle exception
				System.out.println("La opción debe ser un número");
				opcion = -1;
			}

		} while (opcion == -1);
	}

	/**
	 * Listar usuarios de un grupo
	 */
	public static void listarGrupo() {

	}

	/**
	 * Listar comentarios de un grupo
	 */
	public static void listarComentarios() {

	}

	/**
	 * Gestion de baja de la red social
	 * 
	 * @param u
	 */
	public static boolean darseBaja(Usuario u) {

		String respuesta = "No";

		while (true) {

			System.out
					.println("Se va a procedr a darle de baja del servicio, ¿Esta seguro?: ");
			respuesta = registro.nextLine();

			if (Character.toString(respuesta.charAt(0)).equalsIgnoreCase("S")) {
				u.darBaja(db);
				System.out.println("Se ha dado de baja correctamente.");
				return true;
			} else if (Character.toString(respuesta.charAt(0))
					.equalsIgnoreCase("N")) {
				System.out
						.println("Gracias por seguir con nosotros :), se le devolvera al menu");
				return false;
			}

			System.out.println("La respuesta no es correcta.");
		}

	}

	///////////////////////////////////////////////FIN GESTIONES//////////////////////////////////////////////////

}
