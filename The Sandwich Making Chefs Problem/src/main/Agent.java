package main;

import java.util.Random;

/**
 * Producer class
 */
public class Agent implements Runnable{
	private String name;
	private Sandwich sandwich;
	private String ingredient1;
	private String ingredient2;
	private String ingredient3;
	private Random random = new Random();

	/**
	 * @param name
	 * @param sandwich
	 * @param ingredient1
	 * @param ingredient2
	 */
	public Agent(String name, Sandwich sandwich, String ingredient1, String ingredient2, String ingredient3) {
		this.name = name;
		this.sandwich = sandwich;
		this.ingredient1 = ingredient1;
		this.ingredient2 = ingredient2;
		this.ingredient3 = ingredient3;
	}
	
	@Override
	public void run() {
		for(int i = 0; i < Sandwich.ITERATION_SIZE; i++) {
			this.chooseIngredients();
		}
	}
	

	private void chooseIngredients() {	
		System.out.println(name + " is choosing random 2 ingredients");
		int rand = random.nextInt(3);
		switch (rand) {
		case 0:
			sandwich.addIngredient(name, ingredient1, ingredient2);
			break;
		case 1:
			sandwich.addIngredient(name, ingredient2, ingredient3);
			break;
		case 2:
			sandwich.addIngredient(name, ingredient1, ingredient3);
			break;
		}
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {}
	}
}
