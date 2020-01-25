package com.thiakil.intellij.names.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.util.io.HttpRequests
import com.thiakil.intellij.names.CustomNamesList

/**
 * Created by Thiakil on 24/01/2020.
 */
class ImportMcpBotNames: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        DownloadCsvsTask(e.project).queue()
    }
}

class DownloadCsvsTask(project: Project?): Task.Backgroundable(project, "Downloading Test CSVs") {
    override fun run(indicator: ProgressIndicator) {
        //indicator.isIndeterminate = false;
        indicator.text = "Loading methods"
        val methods = HttpRequests.request("http://export.mcpbot.bspk.rs/methods.csv").readString(indicator).split("\r\n")
        indicator.text = "Loading fields"
        val fields = HttpRequests.request("http://export.mcpbot.bspk.rs/fields.csv").readString(indicator).split("\r\n")
        indicator.text = "Loading params"
        val params = HttpRequests.request("http://export.mcpbot.bspk.rs/params.csv").readString(indicator).split("\r\n")
        indicator.text = "Importing names"
        indicator.isIndeterminate = true
        val customNamesList = CustomNamesList.instance
        customNamesList.clearLists()
        ActionUtil.importCsvLines(methods.asSequence(), customNamesList)
        ActionUtil.importCsvLines(fields.asSequence(), customNamesList)
        ActionUtil.importCsvLines(params.asSequence(), customNamesList)
    }

}