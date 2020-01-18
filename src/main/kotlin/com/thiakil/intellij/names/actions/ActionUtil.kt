package com.thiakil.intellij.names.actions

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager

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
}