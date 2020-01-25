package com.thiakil.intellij.names.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.thiakil.intellij.names.CustomNamesList

/**
 * Created by Thiakil on 11/01/2020.
 */
class ImportCsvNames : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        FileChooser.chooseFiles(
            FileChooserDescriptor(true, false, false, false, true, true),
            e.project,
            ActionUtil.getLastFile(SELECTED_FOLDER_KEY)
        ) { fileList ->
            ApplicationManager.getApplication().runReadAction {
                val customNamesList = CustomNamesList.instance
                fileList.forEach { file ->
                    file.inputStream.bufferedReader().useLines { lines ->
                        ActionUtil.importCsvLines(lines, customNamesList)
                    }
                }
            }
        }
    }

    companion object {
        const val SELECTED_FOLDER_KEY = "mcp_names_import_selected"
    }
}