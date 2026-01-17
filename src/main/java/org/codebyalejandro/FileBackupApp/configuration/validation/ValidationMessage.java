package org.codebyalejandro.FileBackupApp.configuration.validation;

public class ValidationMessage {
	private ValidationLevel validationLevel;
	private String message;

	// ----------- CONSTRUCTOR
	public ValidationMessage(ValidationLevel validationLevel, String message) {
		this.validationLevel = validationLevel;
		this.message = message;
	}

	// ----------- GETTERS & SETTERS
	public ValidationLevel getValidationLevel() {
		return validationLevel;
	}

	public void setValidationLevel(ValidationLevel validationLevel) {
		this.validationLevel = validationLevel;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
