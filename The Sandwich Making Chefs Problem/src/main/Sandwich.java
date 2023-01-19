package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sandwich {
	
	static final int ITERATION_SIZE = 20;
	private List<Object> sandwich = Collections.synchronizedList( new ArrayList<>());
	private int count = 0;

	public synchronized void addIngredient(String name, Object item1, Object item2) {
		
		while (sandwich.size() != 0) {
			try {
				System.out.println(name + " waits for chef to consume");
				wait();
				System.out.println(name +" wakes up");
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}
		sandwich.add(item1);
		sandwich.add(item2);
		System.out.println(name + " chooses " + sandwich.get(0) + " and " + sandwich.get(1));
		notifyAll();
		System.out.println("agent notifies all");
	}
	
	public synchronized void removeIngredients(String name, Object ingredient) {
		
		while (sandwich.size() < 2 || sandwich.contains(ingredient)) {
			try {
				System.out.println(name + " waits for new ingredients");
				wait();
				System.out.println(name + " wakes up");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		sandwich.clear();
		System.out.println(name + " makes and eat the sandwich with their " + ingredient);
		count ++;
		System.out.println("Total sandwich " + count);
		notifyAll();
	}

	public List<Object> getSandwich() {
		return sandwich;
	}
}
