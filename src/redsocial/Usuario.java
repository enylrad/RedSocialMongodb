package redsocial;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class Usuario {
	
	private String usuario;
	private String apellido;
	private String correo;
	private String contrasenya;
	private String direccion;
	
	public Usuario() {
		
	}
	
	public void crearUsuario(String nombre, String apellido, String correo, String contrasenya, String direccion, DB db){
	
		BasicDBObject doc = new BasicDBObject();
        doc.put("nombre", nombre);
        doc.put("apellido", apellido);
        doc.put("correo", correo);
        doc.put("contrasenya", contrasenya);
        doc.put("direccion", direccion);
        
        DBCollection collection = db.getCollection("usuario");				
		collection.save(doc);
		
		this.usuario=nombre;
		this.apellido=apellido;
		this.correo= correo;
		this.contrasenya = contrasenya;
		this.direccion = direccion;
		
	}
	
	public void logear(String correo, String contrasenya){
		
		
		
	}
	
}
