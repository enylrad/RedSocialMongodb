package redsocial;

import java.util.ArrayList;
import java.util.Scanner;

import com.mongodb.DB;

public class Menu {

	private static DB db; // Base de datos

	private static Scanner registro = new Scanner(System.in);

	// ////////////////////////////MENUS////////////////////////////////////

	/**
	 * Menu principal de inicio de la aplicacion
	 * 
	 * @param db
	 * @throws InterruptedException
	 */
	public void menuPrincipal(DB db) throws InterruptedException {

		Menu.db = db;

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
	 * @throws InterruptedException
	 */
	public static void menuRedSocial(Usuario u) throws InterruptedException {

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
	 * @throws InterruptedException
	 */
	public static void menuGrupo(Usuario u) throws InterruptedException {

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

					listarComentarios(u);

					break;

				case 6:

					listarUsuariosGrupo(u);

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

	// //////////////////////////////////////FIN
	// MENUS///////////////////////////////////////////////////////////

	// //////////////////////////////////////GESTIONES///////////////////////////////////////////////////////////

	/**
	 * Creación del nuevo usuario ATENCIÓN: En esta practica no se encripta la
	 * contraseña, tener en cuenta
	 */
	public static void nuevoUsuario() {

		String nombre, apellido, correo, contrasenya, repcontrasenya;
		String[] direccion = new String[4];

		System.out.println("Se va a proceder a crear un nuevo usuario,");
		System.out.print("Nombre: ");
		nombre = registro.nextLine();
		System.out.print("Apellidos: ");
		apellido = registro.nextLine();

		boolean duplicado = false;
		boolean correcto = true;

		do {

			if (duplicado) {
				System.out
						.println("El correo electronico ya ha sido registrado. \nIntentelo de nuevo");
				duplicado = false;
			}

			if (!correcto) {
				System.out
						.println("El correo electronico introducido no es valido. \nIntentelo de nuevo.");
				correcto = true;
			}

			System.out.print("Correo: ");
			correo = registro.nextLine();

			// Comprobaciones
			duplicado = Usuario.comprobarDuplicados(correo, db);
			correcto = Usuario.comprobarCorreo(correo);

		} while (duplicado || !correcto);

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
		System.out.print("\tCalle: ");
		direccion[0] = registro.nextLine();
		System.out.print("\tNº: ");
		direccion[1] = registro.nextLine();
		System.out.print("\tLocalidad: ");
		direccion[2] = registro.nextLine();
		System.out.print("\tC.P: ");
		direccion[3] = registro.nextLine();

		Usuario u = new Usuario();
		u.crearUsuario(nombre, apellido, correo, contrasenya, direccion, db);
		System.out.println("Ya puedes logerate en la Red Social...");

	}

	/**
	 * Gestión del inicio de sesión
	 * 
	 * @throws InterruptedException
	 */
	public static void Logear() throws InterruptedException {

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

		ArrayList<Grupo> grupos = Grupo.mostrarGruposAdmin(u, db);

		if (grupos.size() != 0) {

			System.out.println("¿Que grupo desea borrar?,");

			int opcion = -1;

			do {

				try {

					for (int i = 0; i < grupos.size(); i++) {

						System.out.println((i + 1) + " - "
								+ grupos.get(i).getNombre());

					}
					System.out.println("0 - Volver");
					System.out.print("Elige una opción: ");
					opcion = Integer.parseInt(registro.nextLine());

					if (opcion == 0) {

						System.out.println("Volviendo al menu...");
						Thread.sleep(1000);

					} else {

						if (opcion > grupos.size() || opcion < 1) {

							System.out.println("La opción no es válida");
							opcion = -1;

						} else {

							String nombre = grupos.get(opcion - 1).getNombre();

							System.out
									.println("Se va a proceder a borar el grupo "
											+ nombre + "...");
							Thread.sleep(1000);
							System.out.println("...");
							grupos.get(opcion - 1).borrarGrupo(db);
							Thread.sleep(1000);
							System.out.println("Se ha borrado el grupo "
									+ nombre);
						}
					}

				} catch (NumberFormatException e) {
					
					System.out.println("La opción debe ser un número");
					opcion = -1;
					
				} catch (InterruptedException e) {
					
					e.printStackTrace();
					opcion = -1;
					
				}

			} while (opcion == -1);

		} else {

			System.out
					.println("No es administrador de ningun grupo, por lo que no puede borrar ninguno.");
		}

	}

	/**
	 * Union de grupos
	 * 
	 * @param u
	 * @throws InterruptedException
	 */
	public static void unirseGrupo(Usuario u) throws InterruptedException {

		ArrayList<Grupo> grupos = Grupo.mostrarGruposLibres(u, db);

		if (grupos.size() != 0) {

			System.out.println("¿Que grupo desea unirse?,");

			int opcion = -1;

			do {

				try {

					for (int i = 0; i < grupos.size(); i++) {

						System.out.println((i + 1) + " - "
								+ grupos.get(i).getNombre());

					}

					System.out.println("0 - Volver");
					System.out.print("Elige una opción: ");
					opcion = Integer.parseInt(registro.nextLine());

					if (opcion == 0) {

						System.out.println("Volviendo al menu...");
						Thread.sleep(1000);

					} else {

						if (opcion > grupos.size() || opcion < 1) {

							System.out.println("La opción no es válida");
							opcion = -1;

						} else {

							System.out.println("Uniendose a grupo "
									+ grupos.get(opcion - 1).getNombre()
									+ "...");
							Thread.sleep(1000);
							System.out.println("...");
							grupos.get(opcion - 1).unirseGrupo(u, db);
							System.out.println("Se ha unido al grupo");

						}

					}

				} catch (NumberFormatException e) {
					
					System.out.println("La opción debe ser un número");
					opcion = -1;
				}

			} while (opcion == -1);

		} else {

			System.out.println("Pertenece a todos los grupos existentes.");
		}

	}

	/**
	 * Salida de grupos
	 * 
	 * @param u
	 * @throws InterruptedException
	 */
	public static void salirGrupo(Usuario u) throws InterruptedException {

		System.out.println("¿En que grupo desea darse de baja?: ");

		ArrayList<Grupo> grupos = Grupo.mostrarGrupos(u, db);

		if (grupos.size() != 0) {

			int opcion = -1;

			do {

				try {

					for (int i = 0; i < grupos.size(); i++) {

						System.out.println((i + 1) + " - "
								+ grupos.get(i).getNombre());

					}

					System.out.println("0 - Volver");
					System.out.print("Elige una opción: ");
					opcion = Integer.parseInt(registro.nextLine());

					if (opcion == 0) {

						System.out.println("Volviendo al menu...");
						Thread.sleep(1000);

					} else {

						if (opcion > grupos.size() || opcion < 1) {

							System.out.println("La opción no es válida");
							opcion = -1;

						} else {

							System.out.println("Dandose de baja del grupo "
									+ grupos.get(opcion - 1).getNombre()
									+ "...");
							Thread.sleep(1000);
							System.out.println("...");
							grupos.get(opcion - 1).salirGrupo(u, db);
							System.out.println("Ha salido del grupo");

						}
					}

				} catch (NumberFormatException e) {
					
					System.out.println("La opción debe ser un número");
					opcion = -1;
				}

			} while (opcion == -1);

		} else {

			System.out.println("No perteneces a ningun grupos.");
		}

	}

	/**
	 * Comentar en un grupo
	 * 
	 * @param u
	 * @throws InterruptedException
	 */
	public static void comentarGrupo(Usuario u) throws InterruptedException {

		System.out.println("¿En que grupo quieres comentar?: ");

		ArrayList<Grupo> grupos = Grupo.mostrarGrupos(u, db);

		if (grupos.size() != 0) {

			int opcion = -1;

			do {

				try {

					for (int i = 0; i < grupos.size(); i++) {

						System.out.println((i + 1) + " - "
								+ grupos.get(i).getNombre());

					}

					System.out.println("0 - Volver");
					System.out.print("Elige una opción: ");
					opcion = Integer.parseInt(registro.nextLine());

					if (opcion == 0) {

						System.out.println("Volviendo al menu...");
						Thread.sleep(1000);

					} else {

						if (opcion > grupos.size() || opcion < 1) {

							System.out.println("La opción no es válida");
							opcion = -1;

						} else {

							System.out.print("Comentario: ");

							String comentario = registro.nextLine();

							grupos.get(opcion - 1).anyadirComentario(u,
									comentario, db);

						}
					}

				} catch (NumberFormatException e) {
					
					System.out.println("La opción debe ser un número");
					opcion = -1;
				}

			} while (opcion == -1);

		} else {

			System.out
					.println("No perteneces a ningun grupos, no puedes comentar.");
		}

	}

	/**
	 * Listar usuarios de un grupo
	 * 
	 * @throws InterruptedException
	 */
	public static void listarUsuariosGrupo(Usuario u)
			throws InterruptedException {

		ArrayList<Grupo> grupos = Grupo.mostrarGrupos(u, db);

		if (grupos.size() != 0) {

			System.out.println("¿De que grupo desea ver los usuarios?,");

			int opcion = -1;

			do {

				try {

					for (int i = 0; i < grupos.size(); i++) {

						System.out.println((i + 1) + " - "
								+ grupos.get(i).getNombre());

					}

					System.out.println("0 - Volver");
					System.out.print("Elige una opción: ");
					opcion = Integer.parseInt(registro.nextLine());

					if (opcion == 0) {

						System.out.println("Volviendo al menu...");
						Thread.sleep(1000);

					} else {

						if (opcion > grupos.size() || opcion < 1) {

							System.out.println("La opción no es válida");
							opcion = -1;

						} else {

							grupos.get(opcion - 1).visualizarUsuariosLocalidad(
									u, db);

						}
					}

				} catch (NumberFormatException e) {
					
					System.out.println("La opción debe ser un número");
					opcion = -1;
				}

			} while (opcion == -1);

		} else {

			System.out.println("No pertenece a ningun grupo.");
		}

	}

	/**
	 * Listar comentarios de un grupo
	 * 
	 * @throws InterruptedException
	 */
	public static void listarComentarios(Usuario u) throws InterruptedException {

		ArrayList<Grupo> grupos = Grupo.mostrarGrupos(u, db);

		if (grupos.size() != 0) {

			System.out.println("¿De que grupo desea ver los comentarios?,");

			int opcion = -1;

			do {

				try {

					for (int i = 0; i < grupos.size(); i++) {

						System.out.println((i + 1) + " - "
								+ grupos.get(i).getNombre());

					}

					System.out.println("0 - Volver");
					System.out.print("Elige una opción: ");
					opcion = Integer.parseInt(registro.nextLine());

					if (opcion == 0) {

						System.out.println("Volviendo al menu...");
						Thread.sleep(1000);

					} else {

						if (opcion > grupos.size() || opcion < 1) {

							System.out.println("La opción no es válida");
							opcion = -1;

						} else {

							grupos.get(opcion - 1).visualizarComentarios(db);

						}
					}

				} catch (NumberFormatException e) {
					
					System.out.println("La opción debe ser un número");
					opcion = -1;
				}

			} while (opcion == -1);

		} else {

			System.out.println("No pertenece a ningun grupo.");
		}

	}

	/**
	 * Gestion de baja de la red social
	 * 
	 * @param u
	 */
	public static boolean darseBaja(Usuario u) {

		String respuesta = "No";

		while (true) {

			ArrayList<Grupo> grupos = Grupo.mostrarGruposAdmin(u, db);

			if (grupos != null) {

				if (grupos.size() > 0) {

					System.out
							.println("Es administrador de los siguientes grupos: ");

					for (int i = 0; i < grupos.size(); i++) {

						System.out.println(grupos.get(i).getNombre());

					}

					System.out
							.println("Se le dará el derecho de administración al siguiente usuario en la lista.");

				}

				System.out
						.println("Se va a procederá a darle de baja del servicio, ¿Esta seguro?: ");
				respuesta = registro.nextLine();

				if (Character.toString(respuesta.charAt(0)).equalsIgnoreCase(
						"S")) {

					u.darBaja(db);

					if (grupos.size() > 0) {

						for (int i = 0; i < grupos.size(); i++) {

							grupos.get(i).salirGrupo(u, db);

						}

					}

					System.out.println("Se ha dado de baja correctamente.");
					return true;

				} else if (Character.toString(respuesta.charAt(0))
						.equalsIgnoreCase("N")) {

					System.out
							.println("Gracias por seguir con nosotros :), se le devolvera al menu");
					return false;
				}

				System.out.println("La respuesta no es correcta.");

			} else {
				System.out.println("No es administrador de ningun grupo.");
			}
		}

	}

	// /////////////////////////////////////////////FIN
	// GESTIONES//////////////////////////////////////////////////

}
