package redsocial;

import java.util.ArrayList;
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

	public Grupo() {

	}

	public Grupo(ObjectId id, String nombre, int total_usuarios,
			int total_comentarios) {

		this.id = id;
		this.nombre = nombre;
		this.total_usuarios = total_usuarios;
		this.total_comentarios = total_comentarios;

	}

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

	/**
	 * Metodo para la creación de un grupo
	 * @param nombre
	 * @param db
	 * @param u
	 */
	public void crearGrupo(String nombre, DB db, Usuario u) {

		Date now = new Date();

		BasicDBObject doc = new BasicDBObject();
		doc.put("nombre", nombre);
		doc.put("usuarios",
				new BasicDBObject(new BasicDBObject("usuario", u.getId())
						.append("fecha_ingreso", now).append("admin", true)));
		doc.put("total_usuarios", 1);
		doc.put("total_comentarios", 0);

		this.collection = db.getCollection("grupo");
		collection.save(doc);
	}

	/**
	 * Borrado de Grupo
	 * @param db
	 */
	public void borrarGrupo(DB db) {

		BasicDBObject query = new BasicDBObject("_id", this.id);

		this.collection = db.getCollection("grupo");

		this.collection.remove(query);

	}

	/**
	 * Incremento de los comentarios escritos
	 * @param db
	 */
	public void incrementarComentario(DB db) {

		this.total_comentarios++;

		BasicDBObject busqueda = new BasicDBObject("_id", this.id);

		DBObject updateQuery = new BasicDBObject("$set", new BasicDBObject(
				"total_comentarios", this.total_comentarios));
		this.collection = db.getCollection("grupo");
		this.collection.update(busqueda, updateQuery);

	}

	// /////////////////////////////////////////////METODOS STATIC///////////////////////////////////////////////////

	/**
	 * Mostrar grupos en los que el usuario es administrador
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

}