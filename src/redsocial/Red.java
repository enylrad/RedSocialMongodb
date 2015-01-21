package redsocial;

import java.net.UnknownHostException;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class Red {

	private static String dir = "127.0.0.1";
	private static int socket = 27017;
	private static String BBDD = "redsocial";

	/**
	 * Conexi�n e inicio del programa
	 * 
	 * @param args
	 * @throws UnknownHostException
	 * @throws MongoException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws UnknownHostException,
			MongoException, InterruptedException {

		Mongo mongoClient = new Mongo(dir, socket); // Conexi�n
		DB db = mongoClient.getDB(BBDD); // Base de datos

		Menu m = new Menu();

		m.menuPrincipal(mongoClient, db);

	}

}
