package com.thiakil.intellij.names.actions

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.thiakil.intellij.names.CustomNamesList

/**
 * Created by Thiakil on 11/01/2020.
 */
val AnActionEvent.psiElement get() = getData(PlatformDataKeys.PSI_ELEMENT)

object ActionUtil {
    fun getLastFile(key: String): VirtualFile? {
        return PropertiesComponent.getInstance().getValue(key)
            ?.let { VirtualFileManager.getInstance().findFileByUrl(it) }
    }

    fun setLastFile(key: String, file: VirtualFile) {
        PropertiesComponent.getInstance().setValue(key, file.url)
    }

    fun importCsvLines(lines: Sequence<String>, customNamesList: CustomNamesList) {
        lines.forEachIndexed { index, line ->
            if (index > 0){
                val parts = line.split(Regex(","), 4)
                if (parts.size < 2)
                    return@forEachIndexed
                val searge = parts[0]
                val name = parts[1]
                val desc = if (parts.size == 4) parts[3] else ""
                if (searge.isNotEmpty()){
                    when (searge.substringBefore("_")){
                        "func" -> customNamesList.setMethod(searge, name, desc)
                        "field" -> customNamesList.setField(searge, name, desc)
                        "p" -> customNamesList.setParam(searge, name, desc)
                    }
                }
            }
        }
    }
}