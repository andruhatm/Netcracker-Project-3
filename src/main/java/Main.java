import models.Bank;

public class Main {
	public static void main(String[] args) {
		Bank bank = new Bank(4, 30000);
		bank.start();
	}
}
