package com.haxwell.apps.questions.managers;

import java.util.Comparator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.EntityWithAnIntegerIDBehavior;

public class Manager {

	protected static EntityManagerFactory emf;
	
	//TODO: what are the consequences of having this be static? Would it be better to just create an instance when necessary?
	static {
		emf = Persistence.createEntityManagerFactory(Constants.QUIZKI_PERSISTENCE_UNIT);
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
	
}
