package redsocial;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class Grupo {

	private ObjectId id;
	private String nombre;
	private int total_usuarios;
	private int total_comentarios;

	private DBCollection collection;

	/////////////////////////CONTRUCTORES//////////////////////////
	
	public Grupo() {

	}

	public Grupo(ObjectId id, String nombre, int total_usuarios,
			int total_comentarios) {

		this.id = id;
		this.nombre = nombre;
		this.total_usuarios = total_usuarios;
		this.total_comentarios = total_comentarios;

	}
	
	/////////////////////////FIN CONTRUCTORES//////////////////////////
	
	/////////////////////////GET Y SET//////////////////////////

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getTotal_usuarios() {
		return total_usuarios;
	}

	public void setTotal_usuarios(int total_usuarios) {
		this.total_usuarios = total_usuarios;
	}

	public int getTotal_comentarios() {
		return total_comentarios;
	}

	public void setTotal_comentarios(int total_comentarios) {
		this.total_comentarios = total_comentarios;
	}

	/////////////////////////FIN GET Y SET//////////////////////////
	/////////////////////////METODOS////////////////////////////////
	/**
	 * Metodo para la creación de un grupo nuevo
	 * 
	 * @param nombre nombre que tendrá el grupo
	 * @param db 
	 * @param u Usuario que desea crear el grupo
	 */
	public void crearGrupo(String nombre, DB db, Usuario u) {

		this.collection = db.getCollection("grupo");

		Date now = new Date(); //Fecha actual

		BasicDBObject doc = new BasicDBObject();
		doc.put("nombre", nombre);
		doc.put("usuarios",
				Arrays.asList(new BasicDBObject("usuario", u.getId()).append(
						"fecha_ingreso", now).append("admin", true)));
		doc.put("total_usuarios", 1);
		doc.put("total_comentarios", 0);

		this.collection.save(doc);
		
	}

	/**
	 * Borrado de el grupo
	 * 
	 * @param db
	 */
	public void borrarGrupo(DB db) {
		
		this.collection = db.getCollection("grupo");

		BasicDBObject query = new BasicDBObject("_id", this.id);
		
		this.collection.remove(query);

	}

	/**
	 * Incremento de los comentarios escritos en el grupo actual
	 * 
	 * @param db
	 */
	public void incrementarComentario(DB db) {

		this.collection = db.getCollection("grupo");
		this.total_comentarios++;

		BasicDBObject busqueda = new BasicDBObject("_id", this.id);

		DBObject updateQuery = new BasicDBObject("$set", new BasicDBObject(
				"total_comentarios", this.total_comentarios));
		
		this.collection.update(busqueda, updateQuery);

	}
	
	
	/**
	 * Disminuir comentarios escritos en el grupo actual
	 * @param db
	 * @param num número de comentarios que existen actualmente en el grupo
	 */
	public void disminuirComentarios(DB db, int num){
		
		this.collection = db.getCollection("grupo");
		this.total_comentarios = num;

		BasicDBObject busqueda = new BasicDBObject("_id", this.id);

		DBObject updateQuery = new BasicDBObject("$set", new BasicDBObject(
				"total_comentarios", this.total_comentarios));

		this.collection.update(busqueda, updateQuery);
		
	}

	/**
	 * Incrementar usuarios totales en el grupo actual
	 * 
	 * @param db
	 */
	public void incrementarUsuarios(DB db) {
		
		this.collection = db.getCollection("grupo");

		this.total_usuarios++;

		BasicDBObject busqueda = new BasicDBObject("_id", this.id);

		DBObject updateQuery = new BasicDBObject("$set", new BasicDBObject(
				"total_usuarios", this.total_usuarios));
		
		this.collection.update(busqueda, updateQuery);

	}
	
	/**
	 * Quitar usuario del grupo actual
	 *
	 * @param u Usuario que deseamos que salga del grupo
	 * @param db
	 */
	public void salirGrupo(Usuario u, DB db) {
		
		this.collection = db.getCollection("grupo");

		// Borramos el Usuario del grupo
		this.borrarUsuarioGrupo(u, db);

		// Borramos los comentarios del usuario y disminuimos total_comentarios		
		this.borrarComentarios(u, db);

		//Bajamos en uno el usuario del grupo
		this.disminuirUsuarios(db);
		

		//Miramos a ver si los usuarios del grupo es igual a 0
		this.comprobarVacio(db);
		
	}
	
	/**
	 * Borrar los usuario del grupo
	 * @param u Usuario que se desea sacar del grupo
	 * @param db
	 */
	public void borrarUsuarioGrupo(Usuario u, DB db){
		
		this.collection = db.getCollection("grupo");
		
		BasicDBObject busqueda = new BasicDBObject("_id", this.id);

		DBObject updateQuery = new BasicDBObject("$pull", new BasicDBObject(
				"usuarios", new BasicDBObject("usuario", u.getId())));

		this.collection.update(busqueda, updateQuery);

		this.disminuirUsuarios(db);
		
	}
	
	/**
	 * 	/**
	 * Borrado de comentarios y disminuir varios Comentarios

	 * @param u Usuario del que se desea borrar los comentarios
	 * @param db
	 */
	public void borrarComentarios(Usuario u, DB db){
		
		this.collection = db.getCollection("grupo");

		BasicDBObject busqueda = new BasicDBObject("_id", this.id);
		
		BasicDBObject updateQuery = new BasicDBObject("$pull", new BasicDBObject(
				"comentario", new BasicDBObject("usuario", u.getId())));
		
		this.collection.update(busqueda, updateQuery);
		
		DBCursor cursor = this.collection.find(busqueda);
		for (DBObject grupo : cursor) {
			
				@SuppressWarnings("unchecked")
				ArrayList<DBObject> comentarios = (ArrayList<DBObject>) grupo.get("comentario");
			
			if(comentarios !=null){
				
				disminuirComentarios(db, comentarios.size());
				
			}else{
				
				disminuirComentarios(db, 0);
				
			}

		}
		
	}
	
	/**
	 * Disminuir usuarios totales del grupo actual
	 * 
	 * @param db
	 */
	public void disminuirUsuarios(DB db) {
		
		this.collection = db.getCollection("grupo");
		
		BasicDBObject busqueda = new BasicDBObject("_id", this.id);
		
		BasicDBObject updateQuery = new BasicDBObject("$set", new BasicDBObject(
				"total_usuarios", this.total_usuarios));
		
		this.collection.update(busqueda, updateQuery);

		this.total_usuarios--;

		updateQuery = new BasicDBObject("$set", new BasicDBObject(
				"total_usuarios", this.total_usuarios));

		this.collection.update(busqueda, updateQuery);

	}
	
	/**
	 * Metodo para comprobar si esta vacio el grupo(se borra) y si existe usuario pasar el admin
	 * @param db
	 */
	public void comprobarVacio(DB db){
		
		this.collection = db.getCollection("grupo");
		
		BasicDBObject busqueda = new BasicDBObject("_id", this.id);
		
		BasicDBObject updateQuery = new BasicDBObject("usuarios", new BasicDBObject("$size",
				0));

		DBCursor cursor = collection.find(updateQuery);

		boolean encontrado = false; //Variable para comprobar que se ha encontrado usuarios

		for (@SuppressWarnings("unused") DBObject usuario : cursor) {

			encontrado = true;

		}

		// Si lo encontramos borramos, sino damos el admin al siguiente(falta comprobar)
		if (encontrado) {

			this.collection.remove(updateQuery);
			System.out.println("Se a eliminado el grupo " + this.getNombre()
					+ " ya que no queda ningun usuario");

		} else {
			// Falta
			updateQuery = new BasicDBObject("$set", new BasicDBObject(
					"usuarios.0.admin", true));
			
			this.collection.update(busqueda, updateQuery);
		}
		
	}

	
	/**
	 * Union de un usuario al grupo actual
	 */
	public void unirseGrupo(Usuario u, DB db) {
		
		this.collection = db.getCollection("grupo");

		Date now = new Date();

		BasicDBObject busqueda = new BasicDBObject("_id", this.id);

		DBObject updateQuery = new BasicDBObject("$push", new BasicDBObject(
				"usuarios", new BasicDBObject("usuario", u.getId()).append(
						"fecha_ingreso", now).append("admin", false)));

		this.collection.update(busqueda, updateQuery);

		this.incrementarUsuarios(db);

	}

	/**
	 * Añdir un comentarío en el grupo actual
	 * @param usuario Usuario que añade el comentario
	 * @param comentario Comentario que añadimos al grupo
	 * @param db
	 */
	public void anyadirComentario(Usuario usuario, String comentario, DB db) {
		
		this.collection = db.getCollection("grupo");

		BasicDBObject busqueda = new BasicDBObject("_id", this.id);

		Date now = new Date();

		DBObject updateQuery = new BasicDBObject("$push", new BasicDBObject(
				"comentario", new BasicDBObject("texto", comentario).append(
						"usuario", usuario.getId()).append("fecha", now)));

		this.collection.update(busqueda, updateQuery);

		this.incrementarComentario(db);

	}

	/**
	 * Visualizacion de todos los comentarios del grupo
	 * @param db
	 */
	public void visualizarComentarios(DB db) {
		
		this.collection = db.getCollection("grupo");

		System.out.println("Comentarios del grupo " + this.nombre + ":");

		DateFormat f = new SimpleDateFormat("EEEE MMMM d HH:mm:ss z yyyy");
		ObjectId id_usuario = null;
		String texto = "";
		Date fecha = null;
		String nombre = "";

		BasicDBObject query = new BasicDBObject("_id", this.id);

		DBCursor cursor = collection.find(query);
		for (DBObject grupo : cursor) {
			// Idea de Nando!
			@SuppressWarnings("unchecked")
			ArrayList<DBObject> comentarios = (ArrayList<DBObject>) grupo.get("comentario");
			
			if(comentarios != null){

				for (int i = 0; i < comentarios.size(); i++) {
	
					texto = (String) comentarios.get(i).get("texto");
					id_usuario = (ObjectId) comentarios.get(i).get("usuario");
					fecha = (Date) comentarios.get(i).get("fecha");
	
					Usuario usuario = new Usuario();
					usuario.averiguarUsuario(id_usuario, db);
					
					nombre = usuario.getUsuario();
	
					System.out.println("Comentario: " + texto + " - Fecha: "
							+ f.format(fecha) + " - Usuario: " + nombre);
	
				}
				
			}else{
				
				System.out.println("No hay comentarios en este grupo.");
				
			}

		}

	}

	/**
	 * Visualizar los usuarios del grupo y que pertenecen a la misma localidad
	 * @param db
	 */
	public void visualizarUsuariosLocalidad(Usuario u, DB db) {
		
		this.collection = db.getCollection("grupo");
		
		String localidad = u.getDireccion()[2];

		System.out.println("Usuarios del grupo " + this.nombre + " y localidad " + localidad + ":");

		DateFormat f = new SimpleDateFormat("EEEE MMMM d HH:mm:ss z yyyy");
		ObjectId id_usuario = null;
		Date fecha = null;
		String nombre = "";
		
		BasicDBObject query = new BasicDBObject("_id", this.id);

		DBCursor cursor = collection.find(query);
		for (DBObject grupo : cursor) {
			
			@SuppressWarnings("unchecked")
			ArrayList<DBObject> usuarios = (ArrayList<DBObject>) grupo.get("usuarios");

			for (int i = 0; i < usuarios.size(); i++) {

				id_usuario = (ObjectId) usuarios.get(i).get("usuario");
				fecha = (Date) usuarios.get(i).get("fecha_ingreso");

				Usuario usuario = new Usuario();
				usuario.averiguarUsuario(id_usuario, db);
				
				//Comrpobamos que la localidades coinciden
				if(u.getDireccion()[2].equals(usuario.getDireccion()[2])){
				
					nombre = usuario.getUsuario();
	
					System.out.println("Usuario: " + nombre + " - Fecha: "
							+ f.format(fecha));
					
				}

			}

		}

	}
	
	///////////////////////////////////////////////FIN METODOS///////////////////////////////////////////////////////

	///////////////////////////////////////////////METODOS STATIC////////////////////////////////////////////////////

	/**
	 * Mostrar grupos en los que el usuario es administrador
	 * 
	 * @param u Usuario que se desea consultar
	 * @param db
	 * @return Grupos que cumplen la condición
	 */
	public static ArrayList<Grupo> mostrarGruposAdmin(Usuario u, DB db) {

		ArrayList<Grupo> grupos = new ArrayList<>();

		BasicDBObject query = new BasicDBObject();
		query.put("usuarios.usuario", u.getId());
		query.put("usuarios.admin", true);

		DBCollection col = db.getCollection("grupo");
		
		boolean encontrado = false; //Variable para ver si ha sido encontrado el admin

		DBCursor cursor = col.find(query);
		for (DBObject grupo : cursor) {
			
			encontrado = false;

			ObjectId id = (ObjectId) grupo.get("_id");
			String nombre = (String) grupo.get("nombre");
			int total_usuarios = (int) grupo.get("total_usuarios");
			int total_comentarios = (int) grupo.get("total_comentarios");
				
			//Recogemos los usuarios
			@SuppressWarnings("unchecked")
			ArrayList<DBObject> usuarios = (ArrayList<DBObject>) grupo.get("usuarios");
			
			for(int i=0; i<usuarios.size(); i++){
				
				ObjectId id_encontrada = (ObjectId) usuarios.get(i).get("usuario");
				boolean admin = (Boolean) usuarios.get(i).get("admin");
				
				/* Si los IDs del usuario del array coinciden con el usuario a consultar y son administradores
				 * se asigna true para que se muestre el grupo como que es administrador
				 */
				if((u.getId().equals(id_encontrada)) && admin){
					
					encontrado = true;
					
				}
				
			}
			
			if(encontrado){
				System.out.println("añadido grupo");
				grupos.add(new Grupo(id, nombre, total_usuarios, total_comentarios));
			}

		}

		return grupos;

	}

	/**
	 * Buscar todos los grupos donde esta un usuario
	 * 
	 * @param u Usuario que se desea consultar
	 * @param db
	 * @return Grupos que cumplen la condición
	 */
	public static ArrayList<Grupo> mostrarGrupos(Usuario u, DB db) {

		ArrayList<Grupo> grupos = new ArrayList<>();

		BasicDBObject query = new BasicDBObject();
		query.put("usuarios.usuario", u.getId());

		DBCollection col = db.getCollection("grupo");

		DBCursor cursor = col.find(query);
		for (DBObject grupo : cursor) {

			ObjectId id = (ObjectId) grupo.get("_id");
			String nombre = (String) grupo.get("nombre");
			int total_usuarios = (int) grupo.get("total_usuarios");
			int total_comentarios = (int) grupo.get("total_comentarios");

			grupos.add(new Grupo(id, nombre, total_usuarios, total_comentarios));

		}

		return grupos;

	}

	/**
	 * Busca todos los usuarios donde no esta inscrito el usuario
	 * 
	 * @param u Usuario que se desea consultar
	 * @param db
	 * @return Grupos que cumplen la condición
	 */
	public static ArrayList<Grupo> mostrarGruposLibres(Usuario u, DB db) {

		ArrayList<Grupo> grupos = new ArrayList<>();

		BasicDBObject query = new BasicDBObject(new BasicDBObject("usuarios",
				new BasicDBObject("$not", new BasicDBObject("$elemMatch",
						new BasicDBObject("usuario", u.getId())))));

		DBCollection col = db.getCollection("grupo");
		DBCursor cursor = col.find(query);
		for (DBObject grupo : cursor) {

			ObjectId id = (ObjectId) grupo.get("_id");
			String nombre = (String) grupo.get("nombre");
			int total_usuarios = (int) grupo.get("total_usuarios");
			int total_comentarios = (int) grupo.get("total_comentarios");

			grupos.add(new Grupo(id, nombre, total_usuarios, total_comentarios));

		}

		return grupos;

	}
	
///////////////////////////////////////////////FIN METODOS STATIC////////////////////////////////////////////////////

}