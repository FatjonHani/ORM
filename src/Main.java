import java.lang.module.Configuration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.*;
import org.hibernate.boot.registry.*;

import dataMapped.ArticlesEntity;


/**
 * @author Fatjon Hani 
 *
 */
public class Main {

	/**
	 *
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Configuration config = Configuration.empty();
		config.findModule("dataMapped/hibernate.cfg.xml");
		StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("dataMapped/hibernate.cfg.xml").build();
        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
        
        SessionFactory factory = meta.getSessionFactoryBuilder().build();
        Session ses = factory.openSession();
        Transaction trans = ses.beginTransaction();
        
        ArticlesEntity a = ses.load(ArticlesEntity.class, 1); 
        System.out.println(a.getDescription());
		throw new IllegalArgumentException();
	}

}
