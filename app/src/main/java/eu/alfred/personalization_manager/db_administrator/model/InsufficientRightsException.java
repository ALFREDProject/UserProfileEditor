package eu.alfred.personalization_manager.db_administrator.model;

public class InsufficientRightsException extends Exception {

	private static final long serialVersionUID = 3158093551163604810L;

	public InsufficientRightsException(String operation) {
		super("The user has insuffiecient rights to perform operation: " + operation);
	}
	

}
