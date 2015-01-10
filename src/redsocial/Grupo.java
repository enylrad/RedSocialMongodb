package redsocial;

import java.util.ArrayList;
import java.util.Arrays;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class Grupo {
	
	private Long id;
	private String nombre;
	private ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
	private ArrayList<String> comentarios = new ArrayList<String>();
	
	private DBCollection collection;
	
	public Grupo(){
		
		
	}
	
	public Grupo(String nombre, Usuario u, DB db){
	
		BasicDBObject doc = new BasicDBObject();
        doc.put("nombre", nombre);
        doc.put("usuarios", Arrays.asList(u.getId()));
        doc.put("comentarios", null);        
        
        this.collection = db.getCollection("grupo");				
		collection.save(doc);
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public ArrayList<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(ArrayList<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public ArrayList<String> getComentarios() {
		return comentarios;
	}

	public void setComentarios(ArrayList<String> comentarios) {
		this.comentarios = comentarios;
	}
	
	

}
