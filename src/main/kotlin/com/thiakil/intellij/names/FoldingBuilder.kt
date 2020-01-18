package com.thiakil.intellij.names

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.FoldingGroup
import com.intellij.openapi.util.Key
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil


/**
 * Created by Thiakil on 11/01/2020.
 */
class McpFoldingBuilder: FoldingBuilderEx() {
    companion object {
        val typeKey = Key<MemberType>("custom_mcp_name_type")
    }
    override fun getPlaceholderText(node: ASTNode): String? {
        return when (node.psi.getUserData(typeKey)) {
            MemberType.METHOD -> CustomNamesList.instance.getMethod(node.psi.text)
            MemberType.FIELD -> CustomNamesList.instance.getField(node.psi.text)
            MemberType.PARAM -> CustomNamesList.instance.getParam(node.psi.text)
            else -> null
        }?.mcpName ?: node.psi.text
    }

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        return mutableListOf<FoldingDescriptor>().also { regions ->
            val group = FoldingGroup.newGroup("custom mcp names")
            val customNamesList = CustomNamesList.instance
            addRegionsForType(root, regions, group, "func", MemberType.METHOD, PsiMethod::class.java, customNamesList)
            //addRegionsForType(root, regions, group, "field", MemberType.FIELD, PsiField::class.java, customNamesList)
            //addRegionsForType(root, regions, group, "p", MemberType.PARAM, PsiParameter::class.java, customNamesList)
            PsiTreeUtil.findChildrenOfType(root, PsiIdentifier::class.java).forEach { identifier ->
                when (identifier.text?.substringBefore("_")) {
                    "method" -> MemberType.METHOD
                    "field" -> MemberType.FIELD
                    "p" -> MemberType.PARAM
                    else -> null
                }?.let { memberType ->
                    regions.add(FoldingDescriptor(identifier.node, identifier.textRange, group, setOf(customNamesList), true, null, true))
                    identifier.putUserData(typeKey, memberType)
                }
            }
            PsiTreeUtil.findChildrenOfType(root, PsiMethodCallExpression::class.java).forEach { callExpression ->
                callExpression.methodExpression.referenceNameElement?.let { nameEl ->
                    if (nameEl.text.startsWith("func_")) {
                        regions.add(
                            FoldingDescriptor(
                                nameEl.node,
                                nameEl.textRange,
                                group,
                                setOf(customNamesList),
                                true,
                                null,
                                true
                            )
                        )
                        nameEl.putUserData(typeKey, MemberType.METHOD)
                    }
                }
            }
            PsiTreeUtil.findChildrenOfType(root, PsiMethodReferenceExpression::class.java).forEach { referenceExpression ->
                referenceExpression.referenceNameElement?.let { nameEl ->
                    if (nameEl.text.startsWith("func_")) {
                        regions.add(
                            FoldingDescriptor(
                                nameEl.node,
                                nameEl.textRange,
                                group,
                                setOf(customNamesList),
                                true,
                                null,
                                true
                            )
                        )
                        nameEl.putUserData(typeKey, MemberType.METHOD)
                    }
                }
            }
        }.toTypedArray()
    }

    private fun addRegionsForType(
        root: PsiElement,
        regions: MutableList<FoldingDescriptor>,
        group: FoldingGroup,
        prefix: String,
        memberType: MemberType,
        elementClass: Class<out PsiNameIdentifierOwner>,
        customNamesList: CustomNamesList
    ) {
        PsiTreeUtil.findChildrenOfType(root, elementClass).forEach { method ->
            if (method.name?.startsWith("${prefix}_") == true) {
                method.nameIdentifier?.let { identifier ->
                    regions.add(FoldingDescriptor(identifier.node, identifier.textRange, group, setOf(customNamesList), true, null, true))
                    identifier.putUserData(typeKey, memberType)
                }
            }
        }
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = true

}
