package com.thiakil.intellij.names

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
        super.doOKAction()
    }

    override fun getPreferredFocusedComponent(): JComponent? = nameField
}