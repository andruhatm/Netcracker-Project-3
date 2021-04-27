package threads;

import enums.OperationType;
import models.Bank;
import models.Client;

import java.util.Random;

/**
 * Client's generator thread
 */
public class ClientGenerator extends Thread {

	/**
	 * average client's money when init
	 */
	private static final double AVERAGE_CASH_AMOUNT = 20000;

	/**
	 * average customer service time
	 */
	private static final long AVERAGE_SERVICE_TIME = 3500;

	/**
	 * count of client generated per minute
	 */
	private static final int CLIENTS_PER_MINUTE = 20;

	/**
	 * delay between clients generation
	 */
	private int generationDelay;

	/**
	 * bank instance
	 */
	private Bank bank;

	private Random random;

	/**
	 * Client's generator constructor
	 *
	 * @param bank bank to direct clients to
	 */
	public ClientGenerator(Bank bank) {
		this.generationDelay = 60 / CLIENTS_PER_MINUTE;
		this.random = new Random();
		this.bank = bank;
	}


	/**
	 * thread creates new clients with interval and adds him to bank worker queue
	 */
	@Override
	public void run() {
		double rand;
		while (true) {
			try {
				Thread.sleep(Math.abs((long) ((random.nextGaussian() * (generationDelay / 2) + generationDelay) * 1000)));
				rand = Math.random();
				long clientServiceTime = Math.abs(Math.round(random.nextGaussian() * (AVERAGE_SERVICE_TIME / 2) + AVERAGE_SERVICE_TIME));
				double clientCash = Math.abs(Math.round(random.nextGaussian() * (AVERAGE_CASH_AMOUNT / 2) + AVERAGE_CASH_AMOUNT));
				Client client;
				if (rand > 0.5)
					client = new Client(clientCash, clientServiceTime, OperationType.PUT);
				else
					client = new Client(clientCash, clientServiceTime, OperationType.WITHDRAWS);

				bank.addClientToWorker(client);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
