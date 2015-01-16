package redsocial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class Grupo {
	
	private ObjectId id;
	private String nombre;
	private ArrayList<ObjectId> usuarios;
	private int total_usuarios;
	private int total_comentarios;
	
	private DBCollection collection;
	
	public Grupo(){
		
		
	}
	
	public void crearGrupo(String nombre, DB db, Usuario u){
		
		BasicDBObject doc = new BasicDBObject();
        doc.put("nombre", nombre);
        doc.put("usuarios", Arrays.asList(u.getId()));
        doc.put("total_usuarios", 1);
        doc.put("total_comentarios", 0);

        
        this.collection = db.getCollection("grupo");				
		collection.save(doc);
	}
	
	
	
}