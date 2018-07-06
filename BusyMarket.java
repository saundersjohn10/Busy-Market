
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class BusyMarket {
	
	private int fruitCount; 
	private int numCust;
	ReentrantLock lock = new ReentrantLock();
	
	//lanes of customers, use a LinkedList rather than queue to allow viewing of all elements
	private LinkedList<customer> lane1;
	private LinkedList<customer> lane2;
	private LinkedList<customer> lane3;
	
	public BusyMarket() {
		fruitCount = 72; //number of fruit initially
		numCust = 3; //start with a customer in each lane
		lane1 = new LinkedList<>();
		lane2 = new LinkedList<>();
		lane3 = new LinkedList<>();
	}
	
	//class of customer
	class customer {
		
		String letter;
		
		public customer() {
			
			//get a random letter to supply a visualization
			Random rand = new Random();
			int n = rand.nextInt(9);
			
			if(n == 0) 
				letter = "A";
			else if(n == 1) 
				letter = "X";
			else if(n == 2)
				letter = "O";
			else if(n == 3) 
				letter = "Z";
			else if(n == 4)
				letter = "R";
			else if(n == 5) 
				letter = "J";
			else if(n == 6)
				letter = "P";
			else if(n == 7) 
				letter = "C";
			else if(n == 8)
				letter = "K";
		}
		
		@Override
		public String toString() { //print the letter when it is printed
			return this.letter;
		}
	}
	
	public static void main(String[] args) {
		BusyMarket m = new BusyMarket();
		m.runMarket();
	}
	
	public void runMarket() {
		
		//start out with one customer per lane
		lane1.add(new customer());
		lane2.add(new customer());
		lane3.add(new customer());
		
		//Threads that run each lane
		Runnable addCustL1 = new Runnable() {
			public void run() { addCustomers(lane1);}
		};
		
		Runnable addCustL2 = new Runnable() {
			public void run() {addCustomers(lane2);}
		};
		
		Runnable addCustL3 = new Runnable() {
			public void run() {addCustomers(lane3);}
		};
		
		Runnable remFruitL1 = new Runnable() {
			public void run() {takeFruit(lane1);}
		};
		Runnable remFruitL2 = new Runnable() {
			public void run() {takeFruit(lane2);}
		};
		Runnable remFruitL3 = new Runnable() {
			public void run() {takeFruit(lane3);}
		};
		
		//run each lane while fruit is still available
		while(fruitCount > 0) {
			addCustL1.run();
			addCustL2.run();
			addCustL3.run();
			remFruitL1.run();
			remFruitL2.run();
			remFruitL3.run();
		}
		
	}
	
	private void addCustomers(LinkedList<customer> lane) {
		try {
			lock.lock(); //get the lock
			
			//add a customer
			if(fruitCount > numCust && lane.size() < 5) { //if there is enough fruit and enough lane space
				Random rand = new Random();
				int n = rand.nextInt(2); //randomly add one or two customers in a lane
				
				if(n == 0 && fruitCount > numCust + 1 && lane.size() < 4) {//add two customers, if able
					lane.add(new customer());
					lane.add(new customer());
					numCust += 2;
				}
				else if(n == 1) {
					lane.add(new customer()); 
					numCust++;
				}
			}
			else return;
			
			System.out.println(this.printMarket()); //print market
			
			try { //wait .25 seconds
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		finally {
			lock.unlock(); //remove lock always
		}
		
	}
	
	private void takeFruit(LinkedList<customer> lane) {
		try {
			lock.lock(); //get the lock
			
			//customer takes fruit
			if(lane.peek() != null) { 
				lane.removeFirst(); //remove person from lane
				fruitCount--; //take a fruit
				numCust--; 
			}
			else
				return; //if there is no customers in line, stop method
			
			System.out.println(this.printMarket()); //print market
			
			try { //wait .35 seconds
				Thread.sleep(350);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		finally {
			lock.unlock(); //remove lock always
		}
	}
	
	//prints current situation of market
	private String printMarket() {
		StringBuffer market = new StringBuffer();
		market.append("___fruit left:" + fruitCount + "___\n"); //top piece
		
		int fruitCopy = fruitCount; 
		
		if(fruitCount == 0) {
			market.append("|                  |\n|     Sold Out     |\n|                  |\n|                  |\n");
		}
		else {
			//print available fruit
			for(int spaces = 0; spaces < 80; spaces++) {
				
				if(spaces % 20 == 0) { //a new line
					market.append("|");
				}
				else if(spaces % 20 == 19) { //the end of a line
					market.append("|\n");
				}
				else if(fruitCopy - 18 >= 0) { //a whole row of fruit
					market.append("OOOOOOOOOOOOOOOOOO");
					spaces += 17;
					fruitCopy -= 18;
				}
				else if(fruitCopy > 0){ //last row of fruit
					market.append("O");
					fruitCopy--;
				}
				else { //nothing left but a space
					market.append(" ");
				}
			}
		}
		
		market.append("|__________________|\n"); //end of fruit
		
		//print lanes
		//search through lanes once, add them to an array
		customer[] one = new customer[5];
		customer[] two = new customer[5];
		customer[] three = new customer[5];
		
		int i = 0;
		for(customer c: lane1) {
			if(c == null) //if there are no more customers, break
				break;
			one[i] = c;
			i++;
		}
		
		i = 0;
		for(customer c: lane2) {
			if(c == null) 
				break;
			two[i] = c;
			i++;
		}
		
		i = 0;
		for(customer c: lane3) {
			if(c == null) 
				break;
			three[i] = c;
			i++;
		}
		
		//print lanes
		for(int depth = 0; depth < 5; depth++) { //loop through each layer or person position in line
			if(one[depth] != null) {
				market.append("|  " + one[depth] + "  ||  ");
			}
			else
				market.append("|     ||  ");
			
			if(two[depth] != null) {
				market.append(two[depth] + "  ||  ");
			}
			else
				market.append("   ||  ");
			
			if(three[depth] != null) {
				market.append(three[depth] + "  |");
			}
			else
				market.append("   |");
			
			market.append("\n");
		}
		
		return market.toString();
	}
	
}
