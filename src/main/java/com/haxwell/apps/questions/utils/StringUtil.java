package com.haxwell.apps.questions.utils;

/**
 * Copyright 2013,2014 Johnathan E. James - haxwell.org - jj-ccs.com - quizki.com
 *
 * This file is part of Quizki.
 *
 * Quizki is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Quizki is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Quizki. If not, see http://www.gnu.org/licenses.
 */

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.haxwell.apps.questions.entities.EntityWithAnIntegerIDBehavior;

public class StringUtil {

	public static String getField(int index, String beginningFieldMarker, String endFieldMarker, String text) {
		Iterator<String> iterator = new StringUtil.FieldIterator(text, beginningFieldMarker, endFieldMarker);
		
		int count = 0;
		String rtn = null;
		
		while (count++ < index && iterator.hasNext())
			rtn = iterator.next();
		
		return rtn;
	}

	public static int getCountOfDynamicFields(String text) {
		int bindex = -1;
		int eindex = -1;
		int count = 0;
		
		do {
			bindex = text.indexOf("[[", bindex+1);
			
			if (bindex != -1) {
				eindex = text.indexOf("]]", bindex+1);
				
				if (eindex != -1) {
					bindex = eindex;
					count++;
				}
			}
		} while (bindex != -1);
		
		return count;
	}
	
	public static Iterator<String> getDynamicFields(String text) {
		return new FieldIterator(text, "[[", "]]");
	}
	
	public static Iterator<String> getFields(String delimiter, String text) {
		return new FieldIterator(text, delimiter);
	}
	
	public static class FieldIterator implements Iterator<String> {

		private String beginningFieldMarker = null;
		private String endingFieldMarker = null;
		private String source = null;
		private int beginIndex = -1;
		private int endIndex = -1;
		private boolean beginAndEndIndexesHaveBeenSet = false;
		
		public FieldIterator(String source, String endingFieldMarker) {	
			this.source = source;
			this.beginningFieldMarker = null;
			this.endingFieldMarker = endingFieldMarker;
		}
		
		public FieldIterator(String source, String beginningFieldMarker, String endingFieldMarker) {	
			this.source = source;
			this.beginningFieldMarker = beginningFieldMarker;
			this.endingFieldMarker = endingFieldMarker;
			
			if (StringUtil.equals(beginningFieldMarker, endingFieldMarker)) {
				this.beginningFieldMarker = null;
			}
		}
		
		private void setBeginAndEndIndex() {
			if (beginningFieldMarker == null) {

				int prevEndIndex = endIndex;
				boolean beginIndexHasMoved = false;
				
				endIndex = source.indexOf(endingFieldMarker, endIndex + 1);
				
				beginIndex = prevEndIndex; // + endingFieldMarker.length();
				
				if (endIndex > -1 && beginIndex == -1) {
					beginIndex = 0;
					beginIndexHasMoved = true;
				}
				
				if (endIndex == -1 && beginIndex > -1 && remainingPortionOfSourceStringIsWorthyOfProcessing(prevEndIndex)) {
					endIndex = source.length();
				}
				
				if (endIndex == -1 && beginIndex == -1) { // the endingFieldMarker is not in SOURCE
					beginIndex = 0;
					endIndex = source.length();
				}
				else {
					if (!beginIndexHasMoved)
						beginIndex += endingFieldMarker.length();
				}
			}
			else {
				// if this is a dual field marker, ie: [[ and ]], then the begin index is at the beginning 
				//  of the first field marker
				beginIndex = source.indexOf(beginningFieldMarker, endIndex + 1);

				if (beginIndex == -1)
					endIndex = -1;
				
				if (beginIndex > -1) {
					beginIndex += beginningFieldMarker.length();
					endIndex = source.indexOf(endingFieldMarker, beginIndex + 1);
				}
			}
			
			beginAndEndIndexesHaveBeenSet = true;
		}
		
		private boolean remainingPortionOfSourceStringIsWorthyOfProcessing(int index) {
			boolean rtn = true;
			
			if (index > -1) {
				String remainingPartOfSourceString = source.substring(index);
				
				rtn = (!StringUtil.isNullOrEmpty(remainingPartOfSourceString) && !StringUtil.equals(remainingPartOfSourceString, endingFieldMarker));
			}
			
			return rtn;
		}
		
		private boolean thereAreFieldsInTheSourceString() {
			return (source != null && source.contains(endingFieldMarker));
		}
		
		@Override
		public boolean hasNext() {
			if (thereAreFieldsInTheSourceString()) {
				if (!beginAndEndIndexesHaveBeenSet) setBeginAndEndIndex();
				return endIndex != -1;
			}
			else {
				return source != null;
			}
		}

		@Override
		public String next() {
			String rtn = null;
			
			if (!thereAreFieldsInTheSourceString()) {
				rtn = source;
				source = null;
				return rtn;
			} 
			else if (endIndex > -1) {
			
				if (!beginAndEndIndexesHaveBeenSet) 
					setBeginAndEndIndex();
				
				rtn = source.substring(beginIndex, endIndex);
				
				beginAndEndIndexesHaveBeenSet = false;
			}

			return rtn;
		}

		@Override
		public void remove() { /* do nothing */ }
	}
	
	public static int getNumberOfOccurrances(String pattern, String str) {
		int index = str.indexOf(pattern);
		int count = 0;
		
		while (index > -1) {
			count++;
			index = str.indexOf(pattern, index+1);
		}
		
		return count;
	}
	
	public static boolean equals(Object str1, Object str2) {
		if (str1 == null && str2 == null) return true;
		
		if (str1 == null && str2 != null) return false;
		if (str1 != null && str2 == null) return false;
		
		return str1.toString().equals(str2.toString());
	}
	
	public static boolean equalsCaseInsensitive(Object str1, Object str2) {
		if (str1 == null && str2 == null) return true;
		
		if (str1 == null && str2 != null) return false;
		if (str1 != null && str2 == null) return false;
		
		String str1lowercase = str1.toString().toLowerCase();
		String str2lowercase = str2.toString().toLowerCase();
		
		return str1lowercase.equals(str2lowercase);
	}
	
	public static boolean isNullOrEmpty(String str)
	{
		return str == null ? true : (str.length() <= 0);
	}
	
	public static String startJavascriptArray()
	{
		return "[";
	}

	public static void addToJavascriptArray(StringBuffer sb, String val)
	{
		addToJavascriptArray(sb, val, true);
	}
	
	public static void addToJavascriptArray(StringBuffer sb, String val, boolean wrapInQuotes)
	{
		if (sb.length() > 0)
		{
			char lastChar = sb.charAt(sb.length() - 1); 
			
			if (lastChar != ',' && lastChar != '[')
				sb.append(',');
		}
		
		if (wrapInQuotes)
			sb.append("\"" + val + "\"");
		else
			sb.append(val);
		
	}

	public static void closeJavascriptArray(StringBuffer sb) {
		sb.append("]");
	}
	
	public static String getToStringOfEach(Collection<?> coll)
	{
		if (coll == null) return "";
		
		Iterator<?> iterator = coll.iterator();
		StringBuffer rtn = new StringBuffer();
		
		while (iterator.hasNext())
		{
			rtn.append(iterator.next().toString() + " -\\\n");
		}
		
		return rtn.toString();
	}

	public static String getCSVString(List list) {
		if (list == null) return "";
		
		StringBuffer sb = new StringBuffer();
		Iterator iterator = list.iterator();

		while (iterator.hasNext())
		{
			sb.append(iterator.next());
			
			if (iterator.hasNext())
				sb.append(",");
		}
		
		return sb.toString();
	}
	
	/**
	 * Returns a CSV of the IDs from a collection of entities with Integer IDs.
	 * 
	 * @param coll
	 * @return
	 */
	public static String getCSVFromCollection(Collection<? extends EntityWithAnIntegerIDBehavior> coll)
	{
		if (coll == null) return "";
		
		Iterator<? extends EntityWithAnIntegerIDBehavior> iterator = coll.iterator();
		StringBuffer sb = new StringBuffer();
		
		while (iterator.hasNext())
		{
			EntityWithAnIntegerIDBehavior entity = iterator.next();

			sb.append(entity.getId());
			
			if (iterator.hasNext())
				sb.append(",");
		}
		
		return sb.toString();
	}

	public static String getShortURL(String fullUrl) {
		int index = fullUrl.indexOf("/", "http://".length());
		return fullUrl.substring(index + 1);
	}
	
	public static boolean isEmptyJSON(String str) {
		String s = str.replaceAll(" ", "");
		
		return s.equals("{}");
	}
	
	public static String toJSON(String key, List<String> values) {

		// "errors":[{"val":"fdafdsafdsa,fdsafdsafdsa,3v3v"}]
		
		String rtn = " \"" + key + "\":[";

		String csv = getCSVString(values);
		String str = "";
		
		str = "{";
		
		str += "\"val\":";
		str += "\"" + csv + "\"";
		str += "}]";

		rtn += str;
		
		return rtn;
	}
	
	public static String toJSON(Map<String, List<String>> map) {
		Set<String> set = map.keySet();
		
		// { "errors":[{"val":"fdafdsafdsa,fdsafdsafdsa,3v3v"}],"successes":[{"val":"dffads"}] }
		
		String rtn = "{";
		Iterator<String> keyIterator = set.iterator();
		
		while (keyIterator.hasNext()) {
			String key = keyIterator.next();
			
			rtn += toJSON(key, map.get(key));
			
			if (keyIterator.hasNext())
				rtn += ",";
		}
		
		rtn += " }";
		
		return rtn;
	}
	
	public static String removeQuotes(String str)
	{
		return str.replaceAll("\"", "");
	}
	
	public static String getStringWithEllipsis(String str, int length) {
		if (str.length() <= length)
			return str;
		
		String ellipsis = "...";
		
		String shortened = str.substring(0, length - ellipsis.length());
		
		return shortened + ellipsis;
	}
	
	public static String capitalize(String str) {
		StringBuilder sb = new StringBuilder();
		char c = (char) 'a';
		
		sb.insert(0, str.subSequence(1, str.length()));
		c = str.charAt(0);
		if (Character.isUpperCase(c)) {
			sb.insert(0, c);
		} else {
			sb.insert(0, Character.toUpperCase(c));
		}
		return sb.toString();
	}
}
