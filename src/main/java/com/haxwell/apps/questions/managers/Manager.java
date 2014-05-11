package com.haxwell.apps.questions.managers;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.EntityWithASequenceNumberBehavior;
import com.haxwell.apps.questions.entities.EntityWithAnIntegerIDBehavior;

public class Manager {

	protected static EntityManagerFactory emf;

	public static final int ADDL_INFO_USER_HAS_CREATED_NO_ENTITIES = 0;
	public static final int ADDL_INFO_NO_ENTITIES_MATCHING_GIVEN_FILTER = 1;
	public static final int ADDL_INFO_NO_SELECTED_ITEMS = 2;
	
	public static Logger log = Logger.getLogger(Manager.class.getName());
	
	//TODO: what are the consequences of having this be static? Would it be better to just create an instance when necessary?
	static {
		emf = Persistence.createEntityManagerFactory(Constants.QUIZKI_PERSISTENCE_UNIT, getEntityManagerFactoryEnvironmentalOverrides());
	}
	
	private static Map<String, Object> getEntityManagerFactoryEnvironmentalOverrides() {
		Map<String, Object> overrides = new HashMap<>();

		String jdbc_url = System.getProperty("QUIZKI_JDBC_URL");

		if (jdbc_url == null) {
			Exception e = new Exception("Could not find a value for QUIZKI_JDBC_URL in System.getProperty(). ERROR! Will not be able to connect to a database!!");
			e.printStackTrace();
		}
		else {
			overrides.put("javax.persistence.jdbc.url", jdbc_url);
		}
		
		return overrides;
	}
	
	public static Manager getInstance() {
		return null;
	}
	
	public AbstractEntity getEntity(String entityId) {
		return null;
	}
	
	protected static void flush(EntityManager em)
	{
		if (em.getTransaction().isActive() == false)
			em.getTransaction().begin();
		
		em.flush();
		
		em.getTransaction().commit();
	}
	
	public static final Comparator<EntityWithAnIntegerIDBehavior> ID_COMPARATOR = new Comparator<EntityWithAnIntegerIDBehavior>() {

		@Override
		public int compare(EntityWithAnIntegerIDBehavior o1, EntityWithAnIntegerIDBehavior o2) { 
				
			if (((EntityWithAnIntegerIDBehavior)o1).getId() > ((EntityWithAnIntegerIDBehavior)o2).getId())
				return -1;
			else if (((EntityWithAnIntegerIDBehavior)o1).getId() < ((EntityWithAnIntegerIDBehavior)o2).getId())
				return 1;
			
			return 0;
		}
	};
	
	public static final Comparator<EntityWithASequenceNumberBehavior> SEQUENCE_NUMBER_COMPARATOR = new Comparator<EntityWithASequenceNumberBehavior>() {

		@Override
		public int compare(EntityWithASequenceNumberBehavior o1, EntityWithASequenceNumberBehavior o2) { 

			if (((EntityWithASequenceNumberBehavior)o1).getSequence() > ((EntityWithASequenceNumberBehavior)o2).getSequence())
				return 1;
			else if (((EntityWithASequenceNumberBehavior)o1).getSequence() < ((EntityWithASequenceNumberBehavior)o2).getSequence())
				return -1;
			
			return 0;
		}
	};

}
