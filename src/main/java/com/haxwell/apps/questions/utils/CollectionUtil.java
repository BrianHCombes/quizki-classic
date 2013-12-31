package com.haxwell.apps.questions.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.EntityWithAnIntegerIDBehavior;

public class CollectionUtil {

	public static final boolean ADD_OPENING_CLOSING_CURLY_BRACES = true;
	public static final boolean DONT_ADD_OPENING_CLOSING_CURLY_BRACES = false;
	
	public static String getCSVofIDsFromListofEntities(Collection<? extends EntityWithAnIntegerIDBehavior> list)
	{
		StringBuffer sb = new StringBuffer();
		Iterator<? extends EntityWithAnIntegerIDBehavior> iterator = list.iterator();
		
		while (iterator.hasNext())
		{
			EntityWithAnIntegerIDBehavior entity = iterator.next();
			sb.append(entity.getId());
			
			if (iterator.hasNext())
				sb.append(",");
		}
		
		return sb.toString();
	}
	
	public static String getCSV(Collection coll) {
		StringBuffer sb = new StringBuffer();
		Iterator iterator = coll.iterator();
		
		while (iterator.hasNext()) {
			sb.append(iterator.next().toString());
			
			if (iterator.hasNext())
				sb.append(",");
		}
		
		return sb.toString();
	}
	
	public static List<Long> getListOfIds(Collection<? extends EntityWithAnIntegerIDBehavior> list)
	{
		List<Long> rtn = new ArrayList<Long>();
		Iterator<? extends EntityWithAnIntegerIDBehavior> iterator = list.iterator();

		while (iterator.hasNext())
			rtn.add(iterator.next().getId());
		
		return rtn;
	}
	
	public static boolean contains(Collection coll, Object o)
	{
		return coll != null && coll.contains(o);
	}

	public static String toJSON(Collection<? extends AbstractEntity> coll, boolean addOpenAndClosingCurlyBraces) {
		StringBuffer sb = new StringBuffer();
		
		Iterator<? extends AbstractEntity> iterator = coll.iterator();

		if (addOpenAndClosingCurlyBraces)
			sb.append("{ ");
		
		if (iterator.hasNext()) {
			AbstractEntity ae = coll.iterator().next();

			if (ae != null) {
				sb.append("\"" + ae.getEntityDescription() + "\": [");
				
				while (iterator.hasNext()) {
					sb.append(iterator.next().toJSON());
				
					if (iterator.hasNext())
						sb.append(", ");
				}
				
				sb.append("]");
			}
		}
		
		if (addOpenAndClosingCurlyBraces)
			sb.append("}");
		
		return sb.toString();
	}
	
	public static String toJSON(Collection<? extends AbstractEntity> coll) {
		return toJSON(coll, true);
	}
	
	public static String toJSON(Map<String, List<String>> map) {
		StringBuffer sb = new StringBuffer();
		
		Iterator<String> keyIterator = map.keySet().iterator();
		
		sb.append("{ ");
		
		while (keyIterator.hasNext()) {
			String key = keyIterator.next();
			sb.append("\"" + key + "\": [");
			
			Iterator<String> listIterator = map.get(key).iterator();
			
			while (listIterator.hasNext()) {
				sb.append("\"" + listIterator.next() + "\"");
				
				if (listIterator.hasNext())
					sb.append(", ");
			}
			
			sb.append("]");
			
			if (keyIterator.hasNext())
				sb.append(", ");
		}
		
		sb.append("} ");
		
		return sb.toString();
	}
	
	/**
	 * @deprecated No longer necessary.
	 * 
	 * @param list
	 * @param offset
	 * @param maxEntityCount
	 * @return
	 */
	public static List pareListDownToSize(List<? extends AbstractEntity> list, int offset, int maxEntityCount) {
		List paginatedList = new ArrayList();
		
		int itemCount = 0;
		for (int i = offset; i < list.size() && itemCount < maxEntityCount; i++) {
			paginatedList.add(list.get(i));
			itemCount++;
		}

		return paginatedList;
	}
}
