package khe.banking.controllers;

import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import khe.banking.models.enums.FormMode;
import khe.banking.util.DialogUtil;

public abstract class BaseFormController<T> {

	// =========================
    // COMMON STATE
    // =========================
	protected FormMode mode;
	protected boolean saved = false;
	protected T entity;

	// =========================
    // MODE SETUP
    // =========================
	public void setMode(FormMode mode) {
		this.mode = mode;
		updateTitle(mode);
	}

	// =========================
    // ABSTRACT HOOKS (Template Pattern)
    // =========================

	// Each form decides how UI reacts to mode change
	// (e.g., setting title label, disabling fields, etc.)
	protected abstract void updateTitle(FormMode mode);

	// Each form implements how data is saved
	protected abstract void save();

	// =========================
    // VALIDATION HELPER
    // =========================
    protected boolean validate(Control... fields) {
        return DialogUtil.hasEmptyFields(fields);
    }

    // =========================
    // WINDOW CONTROL
    // =========================
    protected void closeWindow(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    // =========================
    // RESULT
    // =========================
    public boolean isSaved() {
        return saved;
    }
}
