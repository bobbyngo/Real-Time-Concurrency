package main;

/**
 * Consumer class
 *
 */

public class Chef implements Runnable{
	private String name;
	private Sandwich sandwich;
	private String ingredient;
	
	public Chef(String name, Sandwich sandwich, String ingredient) {
		this.name = name;
		this.sandwich = sandwich;
		this.ingredient = ingredient;
		
	}

	@Override
	public void run() {
		for (int i = 0; i < Sandwich.ITERATION_SIZE; i ++) {
			this.makeSandwich();
		}
	}
	
	private void makeSandwich() {	
		sandwich.removeIngredients(name, ingredient);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {	
		}
	}
}
