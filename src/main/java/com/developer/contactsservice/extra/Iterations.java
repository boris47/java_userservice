package com.developer.contactsservice.extra;

import java.util.*;

public class Iterations
{
	private static final ArrayList<Integer> Coll1 = new ArrayList<>(50);
	private static final LinkedList<Integer> Coll2 = new LinkedList<>();
	private static final HashMap<Integer, String> Coll3 = new HashMap<>(50);
	private static final HashSet<Integer> Coll4 = new HashSet<>(20);
	
	private static void Iterations()
	{
		Iterator<Integer> iter;
		
		// ArrayList
		for (Integer item : Coll1) {}
		for (int i = 0, length = Coll1.size(); i < length; ++i) { Integer ii = Coll1.get(i); }
		iter = Coll1.iterator(); while (iter.hasNext()) {/* iter.next() */}
		Coll1.forEach(System.out::println);
		
		// LinkedList
		for (Integer item : Coll2) {}
		for (int i = 0, length = Coll2.size(); i < length; ++i) { Integer ii = Coll2.get(i); }
		iter = Coll2.iterator(); while (iter.hasNext()) {/* iter.next() */}
		Coll2.forEach(System.out::println);
		
		// HashMap
		Coll3.forEach((k, v) -> {});
		Coll3.keySet().forEach(k -> {}); Coll3.values().forEach(v -> {});
		Coll3.entrySet().forEach(keyValue -> {});
		for (Map.Entry<Integer, String> entry : Coll3.entrySet()) {}
		
		// HashSet
		for (Integer item : Coll4) {}
		iter = Coll4.iterator();
		while (iter.hasNext()) {/* iter.next() */}
		Coll4.forEach(System.out::println);
	}
}
