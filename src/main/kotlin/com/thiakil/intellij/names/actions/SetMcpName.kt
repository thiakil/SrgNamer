package com.thiakil.intellij.names.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameter
import com.thiakil.intellij.names.MemberType
import com.thiakil.intellij.names.SetNameDialog

/**
 * Created by Thiakil on 11/01/2020.
 */
class SetMcpName : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        when (val el = e.psiElement) {
            is PsiMethod -> SetNameDialog(
                MemberType.METHOD,
                el.name
            ).show()
            is PsiField -> SetNameDialog(
                MemberType.FIELD,
                el.name
            ).show()
            is PsiParameter -> SetNameDialog(
                MemberType.PARAM,
                el.name
            ).show()
        }
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isVisible = when (val el = e.psiElement) {
            is PsiMethod -> when {
                el.name.startsWith("func_") -> true
                else -> false
            }
            is PsiField -> when {
                el.name.startsWith("field_") -> true
                else -> false
            }
            is PsiParameter -> when {
                el.name.startsWith("p_") -> true
                else -> false
            }
            else -> false
        }
    }
}