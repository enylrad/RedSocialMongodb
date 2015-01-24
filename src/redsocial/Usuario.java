package redsocial;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	/////////////////////////CONTRUCTORES//////////////////////////
	
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
	
	/////////////////////////FIN CONTRUCTORES//////////////////////////
	/////////////////////////GET Y SET/////////////////////////////////

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

	/////////////////////////FIN GET Y SET//////////////////////////
	/////////////////////////METODOS////////////////////////////////
	
	/**
	 * Creación de usuario en la base de datos
	 * ATENCION: No se encripta la contraseña
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
		this.collection.save(doc);

	}

	/**
	 * Metodo para verificar el login y recoger el usuario
	 * 
	 * @param correo
	 * @param contrasenya
	 * @param db
	 * @return
	 */
	public boolean logear(String correo, String contrasenya, DB db) {

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
	 * Metodo para averiguar el usuario apartir del ID
	 * @param id_usuario
	 * @param db
	 */
	public void averiguarUsuario(ObjectId id_usuario, DB db){
		
		BasicDBObject query = new BasicDBObject("_id", id_usuario);
		DBCollection collection = db.getCollection("usuario");

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

		}
		
	}
	

	/**
	 * Metodo para dar de Baja a un usuario
	 * 
	 * @param db
	 */
	public void darBaja(DB db) {

		BasicDBObject query = new BasicDBObject("_id", this.id);

		this.collection = db.getCollection("usuario");
		this.collection.remove(query);

	}
	/////////////////////////////////////////FIN METODOS////////////////////////////////////////
	/////////////////////////////////////////METODOS STATICOS///////////////////////////////////
	
	/**
	 * Metodo para comprobar si hay duplicados de correo electronico en la base de datos
	 * @param correo
	 * @param db
	 * @return
	 */
	public static boolean comprobarDuplicados(String correo, DB db){
		
		BasicDBObject query = new BasicDBObject("correo", correo);
		DBCollection collection = db.getCollection("usuario");

		DBCursor cursor = collection.find(query);
		for (DBObject usuario : cursor) {

			return true;

		}
		
		return false;
	}
	
	/**
	 * Metodo para comprobar que el correo electronico es valido
	 * @param correo
	 * @return
	 */
	public static boolean comprobarCorreo(String correo){
		
		String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
	            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);

        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();
		
	}
	

	/////////////////////////////////////////FIN METODOS STATICOS///////////////////////////////////
	
}
