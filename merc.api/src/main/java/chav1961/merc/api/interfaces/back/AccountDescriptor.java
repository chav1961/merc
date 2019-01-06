package chav1961.merc.api.interfaces.back;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.purelib.fsys.interfaces.FileSystemInterface;

public interface AccountDescriptor {
	public interface Notification {
		Timestamp getTimestamp();
		String getCaption();
		String getMessage();
		void markAsRead();
	}	
	
	public interface ActionInterface {
		int getLevel();
		BigDecimal getCache(MoneyType type);
		FileSystemInterface getFileSystemRoot();
		Iterable<Notification> getNotifications();
	}
	
	public interface ClientHistory {
		Timestamp getTransactionTimestamp();
		MoneyType getMoneyType();
		BigDecimal getSum();
		PaymentType getPaymentType();
		String getDescription();
	}
	
	public interface ClientInterface {
		BigDecimal getCache(MoneyType type);
		BigDecimal calculateConvert(BigDecimal sum, MoneyType from, MoneyType to);
		void convert(BigDecimal sum, MoneyType from, MoneyType to);
		void recache(BigDecimal sum, MoneyType type);
		BigDecimal calculateRecache(BigDecimal sum, MoneyType type);
		Timestamp getFirstTransactionTimestamp();
		Timestamp getLastTransactionTimestamp();
		Iterable<ClientHistory> getHistory(final Timestamp from, final Timestamp to);
	}
	
	UUID getAccountId();
	String getAccountName();
	String getNickName();
	String getMailAddress();
	String getPhoneNumber();
	String getPasswordHash();
	AccountState getCurrentState();
	Date getValidTo();

	void setCurrentState(AccountState state) throws MercEnvironmentException;

	ActionInterface getActionInterface();
	ClientInterface getClientInterface();
}
