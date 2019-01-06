package chav1961.merc.api.interfaces.back;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.UUID;

public interface InfrastructureDescriptor {
	public interface TaskDescription {
		UUID getId();
		ExecutionTaskType getType();
		String getCaptionId();
		String getDescriptionId();
		boolean restrictedByPeriod();
		Timestamp validFrom();
		Timestamp validTo();
		String getPreTask();
		String getPostTask();
	}
	
	public interface ExecutionTask {
		TaskDescription getCargoAssociated();
		Timestamp placedTimestamp();
		Timestamp startedTimestamp();
		Timestamp terminatedTimestamp();
		ExecutionTaskState getState();
		String getTerminationMessage();
		Throwable getTerminationException();
		InputStream getLog();
	}

	AccountDescriptor getAccount(UUID accountId);
	AccountDescriptor getAccount(String accountName);
	Iterable<AccountDescriptor> getAccounts(AccountState... states);
	
	Iterable<TaskDescription> getAvailableCargos(int minlevel, ExecutionTaskType type);
	
	UUID scheduleExecution(UUID account, TaskDescription cargo, String programName);
	Iterable<ExecutionTask> scheduledTasks();
	ExecutionTask getTask(UUID taskId);

	void sendNotification(NotificationType type, UUID accountId, Object... parameters);

	InputStream buildNewspaper(UUID account);
}
