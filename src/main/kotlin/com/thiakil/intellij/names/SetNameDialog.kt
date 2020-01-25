package com.thiakil.intellij.names

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import javax.swing.JComponent

/**
 * Created by Thiakil on 11/01/2020.
 */
class SetNameDialog(private val memberType: MemberType, private val srgName: String): SetNameDialogJava() {
    init {
        title = "Set Name for $srgName"
        init()
    }
    override fun createCenterPanel(): JComponent? {
        when (memberType) {
            MemberType.METHOD -> CustomNamesList.instance.getMethod(srgName)
            MemberType.FIELD -> CustomNamesList.instance.getField(srgName)
            MemberType.PARAM -> CustomNamesList.instance.getParam(srgName)
        }?.let {
            nameField.text = it.mcpName
            commentField.text = it.comment
        }
        return rootPanel
    }

    override fun doOKAction() {
        when (memberType) {
            MemberType.METHOD -> CustomNamesList.instance.setMethod(srgName, nameField.text, commentField.text)
            MemberType.FIELD -> CustomNamesList.instance.setField(srgName, nameField.text, commentField.text)
            MemberType.PARAM -> CustomNamesList.instance.setParam(srgName, nameField.text, commentField.text)
        }
        if (copyBotCommand.isSelected) {
            val textToCopy = when(memberType) {
                MemberType.METHOD -> "sm "
                MemberType.FIELD -> "sf "
                MemberType.PARAM -> "sp "
            } + srgName + " " + nameField.text + if (commentField.text.isNotBlank()) " "+commentField.text else ""
            Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(textToCopy), null)
        }
        super.doOKAction()
    }

    override fun getPreferredFocusedComponent(): JComponent? = nameField
}