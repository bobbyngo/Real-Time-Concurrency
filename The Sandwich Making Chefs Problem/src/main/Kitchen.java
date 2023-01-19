package main;
/**
 * 
 * @author Ngo Huu Gia Bao
 *
 */
public class Kitchen {
	public static void main(String[] args) {
		Sandwich sandwich = new Sandwich();
		
		Thread agentP = new Thread(new Agent("Agent P", sandwich, "bread", "peanut butter", "jam"));
		Thread chef1 = new Thread(new Chef("Chef 1", sandwich, "bread"));
		Thread chef2 = new Thread(new Chef("Chef 2", sandwich, "peanut butter"));
		Thread chef3 = new Thread(new Chef("Chef 3", sandwich, "jam"));
		
		agentP.start();
		chef1.start();
		chef2.start();
		chef3.start();	
	}
}
