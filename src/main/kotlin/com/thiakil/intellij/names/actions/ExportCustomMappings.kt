package com.thiakil.intellij.names.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.vfs.VirtualFile
import com.thiakil.intellij.names.CustomNamesList
import com.thiakil.intellij.names.McpEntry

/**
 * Created by Thiakil on 11/01/2020.
 */
class ExportCustomMappings : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        FileChooser.chooseFile(
            FileChooserDescriptor(false, true, false, false, false, false),
            e.project,
            null,
            ActionUtil.getLastFile(SELECTED_FOLDER_KEY)
        ) { vFile ->
            ActionUtil.setLastFile(
                SELECTED_FOLDER_KEY,
                vFile
            )
            val customNamesList = CustomNamesList.instance
            ApplicationManager.getApplication().runWriteAction {
                writeNames(vFile, "methods.csv", customNamesList.methods)
                writeNames(vFile, "fields.csv", customNamesList.fields)
                writeNames(vFile, "params.csv", customNamesList.params)
            }
        }
    }

    private fun writeNames(
        directory: VirtualFile,
        csvName: String,
        nameMap: Map<String, McpEntry>
    ) {
        directory.findOrCreateChildData(this, csvName).getOutputStream(this).use { os ->
            os.bufferedWriter().use { writer ->
                val srgColName = when (csvName) {
                    "params.csv" -> "param"
                    else -> "searge"
                }
                writer.write("$srgColName,name,side,desc\r\n")
                for (entry in nameMap) {
                    if (entry.value.mcpName.isNotBlank()) {
                        writer.write("${entry.key},${entry.value.mcpName.csvIse()},0,${entry.value.comment.csvIse()}\r\n")
                    }
                }
            }
        }
    }

    private fun String.csvIse(): String {
        var str = this
        if (str.contains("\n")){
            str = str.replace("\r\n", "\n").replace("\n", "\\n")
        }
        if (str.contains(",")) {
            return '"'+str.replace("\"", "\"\"")+'"'
        }
        return str
    }

    companion object {
        const val SELECTED_FOLDER_KEY = "mcp_names_export_folder"
    }
}