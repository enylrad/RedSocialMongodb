package redsocial;

import java.util.ArrayList;
import java.util.Arrays;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

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
	
	public void crearUsuario(String nombre, String apellido, String correo, String contrasenya, String[] direccion, DB db){

		
		BasicDBObject doc = new BasicDBObject();
        doc.put("nombre", nombre);
        doc.put("apellido", apellido);
        doc.put("correo", correo);
        doc.put("contrasenya", contrasenya);
        doc.put("direccion", new BasicDBObject("calle", direccion[0])
        						.append("numero", direccion[1])
        						.append("localidad", direccion[2])
        						.append("codigo postal", direccion[3]));
        
        this.collection = db.getCollection("usuario");				
		collection.save(doc);
	
	}
	
	public boolean logear(String correo, String contrasenya, DB db){
		
		/* Con la clase BasicDBObject tambien creamos objetos con los que hacer consultas */
		BasicDBObject query = new BasicDBObject("contrasenya", contrasenya).append("correo", correo);
		this.collection = db.getCollection("usuario");	
		
		DBCursor cursor = collection.find(query);
		for (DBObject usuario: cursor) {
			
			this.id = (ObjectId) usuario.get("_id");
			this.usuario= usuario.get("nombre").toString();
			this.apellido= usuario.get("apellido").toString();
			this.correo= usuario.get("correo").toString();
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
	
	public void anyadirComentario(Grupo grupo, String comentario, DB db){
		
		BasicDBObject busqueda = new BasicDBObject("_id", this.id);

		DBObject updateQuery = new BasicDBObject("$push", new BasicDBObject("comentario", new BasicDBObject("texto", comentario)
								.append("grupo", grupo.getId())));
		
		collection.update(busqueda, updateQuery);
		
		grupo.incrementarComentario(db);
		
		
		
	}
	
	public void unirseGrupo(){
		
	}
	
	public void borrarGrupo(){
		
	}
	
	public void darBaja(DB db){
		
		BasicDBObject query = new BasicDBObject("_id", this.id);
		
		this.collection = db.getCollection("usuario");	
		System.out.println(query);
		
		this.collection.remove(query);
		
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
	
	
	
}
