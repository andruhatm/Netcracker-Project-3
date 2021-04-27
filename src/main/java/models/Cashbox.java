package models;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor
public class Cashbox {

	/**
	 * logger instanse
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Cashbox.class);

	/**
	 * cashbox current money count
	 */
	private volatile double moneyCount = 0;

	/**
	 * initialize cashbox with starting money count
	 *
	 * @param sum value
	 */
	public synchronized void put(double sum) {
		LOGGER.info("Current money count: " + this.moneyCount);
		this.moneyCount += sum;
		LOGGER.info("New money count: " + this.moneyCount);
	}

	/**
	 * withdraw money operation
	 *
	 * @param sum amount to withdraw
	 * @return true if success
	 */
	public synchronized boolean withdraw(double sum) {
		LOGGER.info("Available money count: " + this.moneyCount);
		if (this.moneyCount - sum >= 0) {
			this.moneyCount -= sum;
			LOGGER.info("New money count: " + this.moneyCount);
			return true;
		}
		return false;
	}
}
