package com.thiakil.intellij.names.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.MessageDialogBuilder
import com.intellij.openapi.ui.Messages
import com.thiakil.intellij.names.CustomNamesList

/**
 * Created by Thiakil on 11/01/2020.
 */
class ClearCustomNames : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        if (MessageDialogBuilder.yesNo("Clear List", "Are you sure you want to clear the custom names lists? This cannot be undone.").show() == Messages.YES) {
            CustomNamesList.instance.clearLists()
        }
    }
}