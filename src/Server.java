
import java.io.*;
import java.lang.module.Configuration;
import java.net.InetSocketAddress;
import java.util.*;

import dataMapped.ArticlesEntity;
import dataMapped.ClientsEntity;
import dataMapped.OrdersEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.json.*;
import java.sql.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * INSY Webshop Server
 */
public class Server {

    private static SessionFactory factory;
    /**
     * Port to bind to for HTTP service
     */
    private int port = 8000;

    /**
     * Connect to the database
     * @throws IOException
     */
    Session setupDB()  {

        /*  String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        Properties dbProps = new Properties();
        try {
            dbProps.load(new FileInputStream(rootPath + "db.properties"));

            Properties user = new Properties();
            user.setProperty("user","postgres");
            user.setProperty("password","Pass2020!");
            Connection conn = DriverManager.getConnection(dbProps.getProperty("url"), user);
            return conn;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        */
        Session ses = factory.openSession();

        return ses;
    }

    /**
     * Startup the Webserver
     * @throws IOException
     */
    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/articles", new ArticlesHandler());
        server.createContext("/clients", new ClientsHandler());
        server.createContext("/placeOrder", new PlaceOrderHandler());
        server.createContext("/orders", new OrdersHandler());
        server.createContext("/", new IndexHandler());

        server.start();
    }


    public static void main(String[] args) throws Throwable {
        Server webshop = new Server();
        Configuration config = Configuration.empty();
        config.findModule("dataMapped/hibernate.cfg.xml");
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("dataMapped/hibernate.cfg.xml").build();
        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();

        factory = meta.getSessionFactoryBuilder().build();

        webshop.start();
        System.out.println("Webshop running at http://127.0.0.1:" + webshop.port);
    }


    /**
     * Handler for listing all articles
     */
    class ArticlesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            /*Connection conn = setupDB();

            JSONArray res = new JSONArray();
            try {
				ResultSet rs = conn.createStatement().executeQuery("Select * From articles");
				
				 while(!rs.isLast()) {
		            	rs.next();
		            	JSONObject art = new JSONObject();
		            	art.put("id", rs.getInt(1));
		            	art.put("description", rs.getString(2));
		            	art.put("price", rs.getInt(3)/100.0);
		            	art.put("amount", rs.getInt(4));
		            	res.put(art);
		            }
				 
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}*/
            Session ses = setupDB();
            JSONArray res = new JSONArray();

            for (int i = 1; ses.get(ArticlesEntity.class,i) != null ; i++) {
                JSONObject art = new JSONObject();
               ArticlesEntity a = ses.get(ArticlesEntity.class,i);
                art.put("id",a.getId());
                art.put("description",a.getDescription());
                art.put("price",a.getPrice());
                art.put("amount",a.getAmount()/100.0);
                res.put(art);
            }
          /*  
            JSONObject art1 = new JSONObject();
            art1.put("id", 1);
            art1.put("description", "Bleistift");
            art1.put("price", 0.70);
            art1.put("amount", 2);
            res.put(art1);
            JSONObject art2 = new JSONObject();
            art2.put("id", 2);
            art2.put("description", "Papier");
            art2.put("price", 2);
            art2.put("amount", 100);
            res.put(art2);
          */

           answerRequest(t,res.toString());
        }

    }

    /**
     * Handler for listing all clients
     */
    class ClientsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {

            //TODO read all clients and add them to res
            Session ses = setupDB();
            JSONArray res = new JSONArray();
            for (int i = 1; ses.get(ArticlesEntity.class,i) != null ; i++) {
                JSONObject client = new JSONObject();
                ClientsEntity a = ses.get(ClientsEntity.class,i);
                client.put("id",a.getId());
                client.put("name",a.getName());
                client.put("address",a.getAddress());
                res.put(client);
            }

            answerRequest(t,res.toString());
          /*  Connection conn = setupDB();

            JSONArray res = new JSONArray();
            
            try {
            	
				ResultSet rs = conn.createStatement().executeQuery("Select * From clients");
				
				 while(!rs.isLast()) {
		            	rs.next();
		            	JSONObject client = new JSONObject();
		            	client.put("id", rs.getInt(1));
		            	client.put("name", rs.getString(2));
		            	client.put("address", rs.getString(3));
		            	res.put(client);
		            }
				 
			} catch (SQLException e) {

				e.printStackTrace();
			}
            
            
	    JSONObject cli = new JSONObject();
            cli.put("id", 1);
            cli.put("name", "Brein");
            cli.put("address", "TGM, 1220 Wien");
            res.put(cli);

           */
        }

    }


    /**
     * Handler for listing all orders
     */
    class OrdersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {

            //TODO
            // Join orders with clients, order lines, and articles
            // Get the order id, client name, number of lines, and total prize of each order and add them to res



            // get resultList ohne parameter erstelt mir eine liste mit Object Arrays
            // liste ist zeile object,  array ist spalte index anfang mitt 0 !!
            //

            Session ses = setupDB();
            JSONArray res = new JSONArray();


          List<Object[]> objectArray =  ses.createQuery("SELECT o.id, cl.name, COUNT(o.id), SUM(al.price) FROM OrdersEntity o INNER JOIN OrderLinesEntity ol on o.id = ol.orderId INNER JOIN ClientsEntity cl on o.clientId=cl.id INNER JOIN ArticlesEntity al ON al.id=ol.articleId GROUP BY o.id, cl.name",Object[].class).getResultList();
            for (Object[] obj: objectArray) {
                for(int i =0; i < obj.length; i++){
                JSONObject order = new JSONObject();
                   System.out.println(obj);
                }

          }

           // mit getSingleResult kann man von einer Agregats Funktion das ergebnis getten
            // brauch dann wenn ich die Maximale Id brauche
            // ses.createQuery("select AVG(a.id) from ArticlesEntity a",Integer.class).getSingleResult();


           /* Connection conn = setupDB();

            JSONArray res = new JSONArray();
            
            
           // statt orders.id -> o.id bissl wie java erst bei from declarieren als OrdersEntity o
           // statt On With
            
           
             
            try {
            	 //					 Join orders with clients, order_lines, and articles
        			ResultSet rs = conn.createStatement().executeQuery("SELECT o.id, cl.name, COUNT(o.id), SUM(al.price) FROM OrdersEntity o INNER JOIN order_lines ol ON o.id = ol.order_id INNER JOIN clients cl ON o.client_id=cl.id INNER JOIN articles al ON al.id=ol.article_id GROUP BY o.id, cl.name");
        			// Get the order id, client name, number of lines, and total prize of each order and add them to res
        			  while(!rs.isLast()) {
        				  rs.next();
        				  JSONObject order = new JSONObject();
        				  order.put("id",rs.getInt(1));
        				  order.put("client",rs.getString(5));
        				  order.put("lines",rs.getInt(11));
        				  order.put("price",rs.getInt(15)/100.0);
        				  res.put(order);	  
        			  }
        				
        				 
        			} catch (SQLException e) {

        				e.printStackTrace();
        			}
            
            
          //TODO  group by  /number of lines and total price  für jede order eine gruppe erstellen 
            
            
            
            JSONObject ord = new JSONObject();
            ord.put("id", 1);
            ord.put("client", "Brein");
            ord.put("lines", 2);
            ord.put("price", 3.5);
            res.put(ord);

            
            */
           // answerRequest(t, res.toString());
        }
    }

   
    /**
     * Handler class to place an order
     */
    class PlaceOrderHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
/*

            Connection conn = setupDB();
            Map <String,String> params  = queryToMap(t.getRequestURI().getQuery());

            int client_id = Integer.parseInt(params.get("client_id"));

            String response = "";
            int order_id = 1;
            try {

                //TODO Get the next free order id

                //TODO Create a new order with this id for client client_id


            ResultSet groesteID =	conn.createStatement().executeQuery("SELECT MAX(id) FROM orders");
            groesteID.next();
            int freeID =  groesteID.getInt(1)+1; 
                //TODO Get the available amount for article article_id

            PreparedStatement ps = conn.prepareStatement("INSERT INTO orders VALUES(?,default,?)");
            ps.setInt(1, freeID);
            ps.setInt(2, client_id);
            ps.execute();

                for (int i = 1; i <= (params.size()-1) / 2; ++i ){
                    int article_id = Integer.parseInt(params.get("article_id_"+i));
                    int amount = Integer.parseInt(params.get("amount_"+i));


                    ps = conn.prepareStatement("SELECT amount FROM articles WHERE id=?");
                    ps.setInt(1,article_id);
                    groesteID = ps.executeQuery();
                    groesteID.next();
                    int available = groesteID.getInt(1);

                 //TODO Decrease the available amount for article article_id by amount

		         //TODO Insert new order line

                    if (available < amount)
                        throw new IllegalArgumentException(String.format("Not enough items of article #%d available", article_id));

		    //TODO Decrease the available amount for article article_id by amount
                    available = available - amount;
                    ps = conn.prepareStatement("UPDATE articles SET amount = ? WHERE id=?");
                    ps.setInt(1, available);
                    ps.setInt(2, article_id);
                    ps.execute();
		    //TODO Insert new order line
                    ps = conn.prepareStatement("INSERT INTO order_lines VALUES(default,?,?,?)");
                    ps.setInt(1,article_id);
                    ps.setInt(2,order_id);
                    ps.setInt(3, amount);
                }

                response = String.format("{\"order_id\": %d}", order_id);
            } catch (IllegalArgumentException iae) {
                response = String.format("{\"error\":\"%s\"}", iae.getMessage());
            } catch (SQLException e) {
				// TODO Auto-generated catch block
            	throw new RuntimeException(e);
			}

            answerRequest(t, response);
*/


        }
    }

    /**
     * Handler for listing static index page
     */
    class IndexHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "<!doctype html>\n" +
                    "<html><head><title>INSY Webshop</title><link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/water.css@2/out/water.css\"></head>" +
                    "<body><h1>INSY Pseudo-Webshop</h1>" +
                    "<h2>Verf&uuml;gbare Endpoints:</h2><dl>"+
                    "<dt>Alle Artikel anzeigen:</dt><dd><a href=\"http://127.0.0.1:"+port+"/articles\">http://127.0.0.1:"+port+"/articles</a></dd>"+
                    "<dt>Alle Bestellungen anzeigen:</dt><dd><a href=\"http://127.0.0.1:"+port+"/orders\">http://127.0.0.1:"+port+"/orders</a></dd>"+
                    "<dt>Alle Kunden anzeigen:</dt><dd><a href=\"http://127.0.0.1:"+port+"/clients\">http://127.0.0.1:"+port+"/clients</a></dd>"+
                    "<dt>Bestellung abschicken:</dt><dd><a href=\"http://127.0.0.1:"+port+"/placeOrder?client_id=<client_id>&article_id_1=<article_id_1>&amount_1=<amount_1&article_id_2=<article_id_2>&amount_2=<amount_2>\">http://127.0.0.1:"+port+"/placeOrder?client_id=&lt;client_id>&article_id_1=&lt;article_id_1>&amount_1=&lt;amount_1>&article_id_2=&lt;article_id_2>&amount_2=&lt;amount_2></a></dd>"+
                    "</dl></body></html>";

            answerRequest(t, response);
        }

    }


    /**
     * Helper function to send an answer given as a String back to the browser
     * @param t HttpExchange of the request
     * @param response Answer to send
     * @throws IOException
     */
    private void answerRequest(HttpExchange t, String response) throws IOException {
        byte[] payload = response.getBytes();
        t.sendResponseHeaders(200, payload.length);
        OutputStream os = t.getResponseBody();
        os.write(payload);
        os.close();
    }

    /**
     * Helper method to parse query paramaters
     * @param query
     * @return
     */
    public static Map<String, String> queryToMap(String query){
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length>1) {
                result.put(pair[0], pair[1]);
            }else{
                result.put(pair[0], "");
            }
        }
        return result;
    }

  
}
