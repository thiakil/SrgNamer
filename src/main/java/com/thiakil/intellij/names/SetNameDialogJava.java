package com.thiakil.intellij.names;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Java class for IDEA to bind form to (kotlin wasn't supported last I checked)
 */
public abstract class SetNameDialogJava extends DialogWrapper {
    protected JPanel rootPanel;
    protected JTextField nameField;
    protected JTextArea commentField;

    protected SetNameDialogJava() {
        super(true);
    }

}
