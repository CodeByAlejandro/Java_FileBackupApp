package org.codebyalejandro.FileBackupApp.configuration;

import org.codebyalejandro.FileBackupApp.configuration.validation.ValidationMessage;

import java.util.List;

public abstract class ConfigTreeNode {
	private String name;
	private List<ValidationMessage> validationMessages;
	private List<ConfigTreeNode> children;

	// ----------- CONSTRUCTORS
	public ConfigTreeNode(String name) {
		setName(name);
		setChildren(null);
		setValidationMessages(null);
	}

	public ConfigTreeNode(String name, List<ValidationMessage> validationMessages) {
		this(name);
		setValidationMessages(validationMessages);
	}

	public ConfigTreeNode(String name, List<ValidationMessage> validationMessages, List<ConfigTreeNode> children) {
		this(name, validationMessages);
		setChildren(children);
	}

	// ----------- GETTERS & SETTERS
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ConfigTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<ConfigTreeNode> children) {
		this.children = children;
	}

	public List<ValidationMessage> getValidationMessages() {
		return validationMessages;
	}

	public void setValidationMessages(List<ValidationMessage> validationMessages) {
		this.validationMessages = validationMessages;
	}

	// ----------- OTHER METHODS
	public void addValidationMessage(ValidationMessage validationMessage) {
		getValidationMessages().add(validationMessage);
	}

	public void addAllValidationMessages(List<ValidationMessage> validationMessages) {
		getValidationMessages().addAll(validationMessages);
	}

	public void addChild(ConfigTreeNode child) {
		getChildren().add(child);
	}

	public void addAllChildren(List<ConfigTreeNode> children) {
		getChildren().addAll(children);
	}

	public String getCompositeValidationMessage() {
		StringBuilder strBuilder = new StringBuilder();
		getCompositeValidationMessage(strBuilder, 0);
		// Remove extra newline at end
		strBuilder.setLength(strBuilder.length() - 1);
		return strBuilder.toString();
	}

	private boolean getCompositeValidationMessage(StringBuilder strBuilder, int depth) {
		boolean appendedStrBuilder;
		if (getChildren() == null) {
			appendedStrBuilder = getCompositePropertyValidationMessage(strBuilder, depth);
		} else {
			appendedStrBuilder = getCompositeSectionValidationMessage(strBuilder, depth);
		}
		return appendedStrBuilder;
	}

	private boolean getCompositeSectionValidationMessage(StringBuilder strBuilder, int depth) {
		boolean appendedStrBuilder = false;
		strBuilder.append("\t".repeat(depth)).append(getName()).append(":").append(System.lineSeparator());
		depth += 1;
		for (ConfigTreeNode child : getChildren()) {
			boolean childAppendedStrBuilder = child.getCompositeValidationMessage(strBuilder, depth);
			if (childAppendedStrBuilder) {
				appendedStrBuilder = true;
			}
		}
		if (getValidationMessages() != null) {
			for (ValidationMessage validationMessage : getValidationMessages()) {
				strBuilder.append("\t".repeat(depth)).append(validationMessage.getMessage()).append(System.lineSeparator());
			}
			appendedStrBuilder = true;
		}
		if (!appendedStrBuilder) {
			int sectionHeaderLineLength = (depth - 1) + getName().length() + 1;
			strBuilder.setLength(strBuilder.length() - sectionHeaderLineLength);
		}
		return appendedStrBuilder;
	}

	private boolean getCompositePropertyValidationMessage(StringBuilder strBuilder, int depth) {
		boolean appendedStrBuilder = false;
		if (getValidationMessages() != null) {
			if (getValidationMessages().size() > 1) {
				strBuilder.append("\t".repeat(depth)).append(getName()).append(":").append(System.lineSeparator());
				depth += 1;
				for (ValidationMessage validationMessage : getValidationMessages()) {
					strBuilder.append("\t".repeat(depth)).append(validationMessage.getMessage()).append(System.lineSeparator());
				}
			} else {
				strBuilder.append("\t".repeat(depth)).append(getName()).append(": ")
						.append(getValidationMessages().getFirst().getMessage()).append(System.lineSeparator());
			}
			appendedStrBuilder = true;
		}
		return appendedStrBuilder;
	}
}
