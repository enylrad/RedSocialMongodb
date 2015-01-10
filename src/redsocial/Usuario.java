package redsocial;

import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class Usuario {
	
	private long id;
	private String usuario;
	private String apellido;
	private String correo;
	private String contrasenya;
	private String direccion;
	private ArrayList<Grupo> grupos;
	
	private DBCollection collection;

	public Usuario() {
		
	}
	
	public void crearUsuario(String nombre, String apellido, String correo, String contrasenya, String direccion, DB db){
	
		BasicDBObject doc = new BasicDBObject();
        doc.put("nombre", nombre);
        doc.put("apellido", apellido);
        doc.put("correo", correo);
        doc.put("contrasenya", contrasenya);
        doc.put("direccion", direccion);
        
        this.collection = db.getCollection("usuario");				
		collection.save(doc);

		
	}
	
	public boolean logear(String correo, String contrasenya, DB db){
		
		/* Con la clase BasicDBObject tambien creamos objetos con los que hacer consultas */
		BasicDBObject query = new BasicDBObject("contrasenya", contrasenya).append("correo", correo);
		this.collection = db.getCollection("usuario");	
		
		DBCursor cursor = collection.find(query);
		for (DBObject usuario: cursor) {
			
			this.id = (Long) usuario.get("_id");
			this.usuario= usuario.get("nombre").toString();
			this.apellido= usuario.get("apellido").toString();
			this.correo= usuario.get("correo").toString();
			this.contrasenya = usuario.get("contrasenya").toString();
			this.direccion = usuario.get("direccion").toString();
			
			return true;
		}
		
		return false;
		
		
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

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public DBCollection getCollection() {
		return collection;
	}

	public void setCollection(DBCollection collection) {
		this.collection = collection;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
	
}
