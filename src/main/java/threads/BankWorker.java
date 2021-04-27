package threads;

import enums.OperationType;
import lombok.NoArgsConstructor;
import models.Bank;
import models.Cashbox;
import models.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * bank worker entity
 */
@NoArgsConstructor
public class BankWorker implements Runnable {
	/**
	 * logger instanse
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BankWorker.class);

	/**
	 * worker id
	 */
	private int id;

	/**
	 * clients queue for operator
	 */
	private BlockingQueue<Client> clients = new LinkedBlockingQueue<>();

	public BankWorker(int id) {
		this.id = id;
	}

	/**
	 * add money to cash machine
	 *
	 * @param sum    amount to add
	 * @param client current client
	 */
	private void putMoney(double sum, Client client) {
		Bank.getCashbox().put(sum);
		LOGGER.info("Worker adds : " + sum + " for " + client);
	}

	/**
	 * Withdraw money from the cashBox
	 *
	 * @param sum    amount to gave
	 * @param client current client
	 */
	private void withdrawMoney(double sum, Client client) {
		if (Bank.getCashbox().withdraw(sum)) {
			LOGGER.info("Worker gave out: " + sum + " for " + client);
		} else {
			LOGGER.warn("Worker cant gave out: " + sum + " for " + client);
		}
	}

	/**
	 * add client to queue
	 *
	 * @param newClient client to add
	 */
	public void addClient(Client newClient) {
		synchronized (clients) {
			clients.add(newClient);
			clients.notify();
			LOGGER.info("Client " + newClient + " was added to queue of worker with id: " + this.id);
		}
	}

	/**
	 * getter for clients queue
	 * @return clients count
	 */
	public int getQueueSize() {
		return clients.size();
	}

	/**
	 * Bank worker's work process
	 * Gets first client from the queue and sleep until end, else waiting for new clients
	 */
	@Override
	public void run() {
		Client client;
		while (true) {
			try {
				synchronized (clients) {
					if (clients.isEmpty()) {
						clients.wait();
					}
				}
				LOGGER.info("Clients in queue: " + clients.size());
				client = clients.poll();
				Thread.sleep(client.getServiceTime());
				if (client.getOperationType() == OperationType.PUT) {
					this.putMoney(client.getMoney(), client);
				} else {
					this.withdrawMoney(client.getMoney(), client);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
