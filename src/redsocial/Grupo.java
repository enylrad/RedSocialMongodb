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
	/////////////////////////////////////////////////METODOS/////////////////////////////////////////////////
	/**
	 * Metodo para la creación de un grupo
	 * 
	 * @param nombre
	 * @param db
	 * @param u
	 */
	public void crearGrupo(String nombre, DB db, Usuario u) {

		this.collection = db.getCollection("grupo");
		
		Date now = new Date();

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
	 * Borrado de Grupo
	 * 
	 * @param db
	 */
	public void borrarGrupo(DB db) {
		
		this.collection = db.getCollection("grupo");

		BasicDBObject query = new BasicDBObject("_id", this.id);
		
		this.collection.remove(query);

	}

	/**
	 * Incremento de los comentarios escritos
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
	
	
	
	public void disminuirComentarios(DB db, int num){
		
		this.collection = db.getCollection("grupo");
		
		this.total_comentarios = num;

		BasicDBObject busqueda = new BasicDBObject("_id", this.id);

		DBObject updateQuery = new BasicDBObject("$set", new BasicDBObject(
				"total_comentarios", this.total_comentarios));

		this.collection.update(busqueda, updateQuery);
		
	}

	/**
	 * Incrementar usuarios totales
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
	 * Quitar usuario del grupo
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
		this.comprobarVacio(u, db);
		
	}
	
	/**
	 * Metodo para borrar los usuarios del grupo
	 * @param u
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
	 * Metodo para borrar comentarios y disminuir varios Comentarios
	 * @param db
	 * @param num
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
	 * Disminuir usuarios totales
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
	 * @param u
	 * @param db
	 */
	public void comprobarVacio(Usuario u, DB db){
		
		this.collection = db.getCollection("grupo");
		
		BasicDBObject busqueda = new BasicDBObject("_id", this.id);
		
		BasicDBObject updateQuery = new BasicDBObject("usuarios", new BasicDBObject("$size",
				0));

		DBCursor cursor = collection.find(updateQuery);

		boolean encontrado = false;

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
			System.out.println(updateQuery);
			this.collection.update(busqueda, updateQuery);
		}
		
	}

	
	/**
	 * Metodo para la union de un grupo
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
	 * Metodo para añdir un comentarío en el grupo indicado
	 * 
	 * @param grupo
	 * @param comentario
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
	 * Metodo para visualizar todos los comentarios del grupo
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

			for (int i = 0; i < comentarios.size(); i++) {

				texto = (String) comentarios.get(i).get("texto");
				id_usuario = (ObjectId) comentarios.get(i).get("usuario");
				fecha = (Date) comentarios.get(i).get("fecha");

				nombre = Usuario.averiguarNombre(id_usuario, db);

				System.out.println("Comentario: " + texto + " - Fecha: "
						+ f.format(fecha) + " - Usuario: " + nombre);

			}

		}

	}

	/**
	 * Metodo para visualizar los usuarios del grupo
	 * @param db
	 */
	public void visualizarUsuarios(DB db) {
		
		this.collection = db.getCollection("grupo");

		System.out.println("Usuarios del grupo " + this.nombre + ":");

		DateFormat f = new SimpleDateFormat("EEEE MMMM d HH:mm:ss z yyyy");
		ObjectId id_usuario = null;
		Date fecha = null;
		String nombre = "";

		BasicDBObject query = new BasicDBObject("_id", this.id);

		DBCursor cursor = collection.find(query);
		for (DBObject grupo : cursor) {
			// Idea de Nando!
			@SuppressWarnings("unchecked")
			ArrayList<DBObject> usuarios = (ArrayList<DBObject>) grupo.get("usuarios");

			for (int i = 0; i < usuarios.size(); i++) {

				id_usuario = (ObjectId) usuarios.get(i).get("usuario");
				fecha = (Date) usuarios.get(i).get("fecha_ingreso");

				nombre = Usuario.averiguarNombre(id_usuario, db);

				System.out.println("Usuario: " + nombre + " - Fecha: "
						+ f.format(fecha));

			}

		}

	}
	
	///////////////////////////////////////////////FIN METODOS///////////////////////////////////////////////////////

	///////////////////////////////////////////////METODOS STATIC////////////////////////////////////////////////////

	/**
	 * Mostrar grupos en los que el usuario es administrador
	 * 
	 * @param u
	 * @param db
	 * @return
	 */
	public static ArrayList<Grupo> mostrarGruposAdmin(Usuario u, DB db) {

		ArrayList<Grupo> grupos = new ArrayList<>();

		BasicDBObject query = new BasicDBObject();
		query.put("usuarios.usuario", u.getId());
		query.put("usuarios.admin", true);

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
	 * Metodo para buscar todos los grupos donde esta un usuario
	 * 
	 * @param u
	 * @param db
	 * @return
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
	 * Metodo que busca todos los usuarios donde no esta inscrito el usuario
	 * 
	 * @param u
	 * @param db
	 * @return
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