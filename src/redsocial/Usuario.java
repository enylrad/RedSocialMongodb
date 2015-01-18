package redsocial;

import java.util.ArrayList;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class Usuario {

	private ObjectId id;
	private String usuario;
	private String apellido;
	private String correo;
	private String contrasenya;
	private String[] direccion = new String[4];

	private DBCollection collection;

	public Usuario() {

	}

	public Usuario(ObjectId id, String usuario, String apellido, String correo,
			String contrasenya, String[] direccion) {

		this.id = id;
		this.usuario = usuario;
		this.apellido = apellido;
		this.correo = correo;
		this.contrasenya = contrasenya;
		this.direccion[0] = direccion[0];
		this.direccion[1] = direccion[1];
		this.direccion[2] = direccion[2];
		this.direccion[3] = direccion[3];

	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getContrasenya() {
		return contrasenya;
	}

	public void setContrasenya(String contrasenya) {
		this.contrasenya = contrasenya;
	}

	public DBCollection getCollection() {
		return collection;
	}

	public void setCollection(DBCollection collection) {
		this.collection = collection;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String[] getDireccion() {
		return direccion;
	}

	public void setDireccion(String[] direccion) {
		this.direccion = direccion;
	}

	/**
	 * Creación de usuario en la base de datos
	 * @param nombre
	 * @param apellido
	 * @param correo
	 * @param contrasenya
	 * @param direccion
	 * @param db
	 */
	public void crearUsuario(String nombre, String apellido, String correo,
			String contrasenya, String[] direccion, DB db) {

		BasicDBObject doc = new BasicDBObject();
		doc.put("nombre", nombre);
		doc.put("apellido", apellido);
		doc.put("correo", correo);
		doc.put("contrasenya", contrasenya);
		doc.put("direccion",
				new BasicDBObject("calle", direccion[0])
						.append("numero", direccion[1])
						.append("localidad", direccion[2])
						.append("codigo postal", direccion[3]));

		this.collection = db.getCollection("usuario");
		collection.save(doc);

	}

	/**
	 * Metodo para verificar el login y recoger el usuario
	 * @param correo
	 * @param contrasenya
	 * @param db
	 * @return
	 */
	public boolean logear(String correo, String contrasenya, DB db) {

		/*
		 * Con la clase BasicDBObject tambien creamos objetos con los que hacer
		 * consultas
		 */
		BasicDBObject query = new BasicDBObject("contrasenya", contrasenya)
				.append("correo", correo);
		this.collection = db.getCollection("usuario");

		DBCursor cursor = collection.find(query);
		for (DBObject usuario : cursor) {

			this.id = (ObjectId) usuario.get("_id");
			this.usuario = usuario.get("nombre").toString();
			this.apellido = usuario.get("apellido").toString();
			this.correo = usuario.get("correo").toString();
			this.contrasenya = usuario.get("contrasenya").toString();
			DBObject direccion = (DBObject) usuario.get("direccion");

			this.direccion[0] = (String) direccion.get("calle");
			this.direccion[1] = (String) direccion.get("numero");
			this.direccion[2] = (String) direccion.get("localidad");
			this.direccion[3] = (String) direccion.get("codigo postal");

			return true;
		}

		return false;

	}

	/**
	 * Metodo para añdir un comentarío en el grupo indicado
	 * @param grupo
	 * @param comentario
	 * @param db
	 */
	public void anyadirComentario(Grupo grupo, String comentario, DB db) {

		BasicDBObject busqueda = new BasicDBObject("_id", this.id);

		DBObject updateQuery = new BasicDBObject("$push", new BasicDBObject(
				"comentario", new BasicDBObject("texto", comentario).append(
						"grupo", grupo.getId())));

		collection.update(busqueda, updateQuery);

		grupo.incrementarComentario(db);

	}

	/**
	 * Metodo para la union de un grupo 
	 */
	public void unirseGrupo() {

	}

	/**
	 * Metodo para dar de Baja a un usuario
	 * @param db
	 */
	public void darBaja(DB db) {

		BasicDBObject query = new BasicDBObject("_id", this.id);

		this.collection = db.getCollection("usuario");
		this.collection.remove(query);

	}

}
