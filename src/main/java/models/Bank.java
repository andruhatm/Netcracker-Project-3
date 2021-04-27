package models;

import threads.ClientGenerator;
import threads.BankWorker;

import java.util.ArrayList;
import java.util.List;

/**
 * bank thread
 */
public class Bank {

	/**
	 * list of available workers
	 */
	private List<BankWorker> workers = new ArrayList<>();
	/**
	 * workers count
	 */
	private int workersCount;

	/**
	 * cashbox instance
	 */
	private static Cashbox cashbox;

	/**
	 * Bank constructor
	 * @param workersCount cash operators count
	 * @param cash initial available count
	 */
	public Bank(int workersCount, double cash) {
		this.workersCount = workersCount;
		cashbox = new Cashbox();
		cashbox.put(cash);
	}

	/**
	 * getter for cashbox instance
	 *
	 * @return class Cashbox
	 */
	public static synchronized Cashbox getCashbox() {
		if (cashbox == null) {
			cashbox = new Cashbox();
		}
		return cashbox;
	}

	/**
	 * starting workers and client generator
	 */
	public void start() {
		for (int i = 0; i < this.workersCount; i++) {
			BankWorker bankWorker = new BankWorker(i + 1);
			workers.add(bankWorker);
			new Thread(bankWorker).start();
		}
		Thread generator = new ClientGenerator(this);
		generator.start();
	}

	/**
	 * find free worker/ worker with minimal queue size
	 *
	 * @param client client to add in queue
	 */
	public void addClientToWorker(Client client) {
		BankWorker worker = workers.get(0);
		int minClientCount = worker.getQueueSize();
		for (BankWorker bankWorker : workers) {
			if (bankWorker.getQueueSize() < minClientCount) {
				minClientCount = bankWorker.getQueueSize();
				worker = bankWorker;
			}
		}
		worker.addClient(client);
	}
}
