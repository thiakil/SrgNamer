package com.thiakil.intellij.names

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.util.ModificationTracker

/**
 * Created by Thiakil on 11/01/2020.
 */
@State(name = "Custom MCP Names", storages = [Storage("mcpnames.xml")])
class CustomNamesList : PersistentStateComponent<NamesState>, ModificationTracker {
    companion object {
        val instance: CustomNamesList
            get() = ServiceManager.getService(CustomNamesList::class.java)
    }

    private var names: NamesState =
        NamesState()
    private var modificationCounter: Long = 0

    override fun getState(): NamesState = names

    override fun loadState(state: NamesState) {
        this.names = state
    }

    val methods: Map<String, McpEntry>
        get() = names.methods
    val fields: Map<String, McpEntry>
        get() = names.fields
    val params: Map<String, McpEntry>
        get() = names.params

    fun setMethod(srgName: String, mcpName: String, comment: String) {
        names.methods.set(srgName, mcpName, comment)
        modificationCounter++
    }

    fun setField(srgName: String, mcpName: String, comment: String) {
        names.fields.set(srgName, mcpName, comment)
        modificationCounter++
    }

    fun setParam(srgName: String, mcpName: String, comment: String) {
        names.params.set(srgName, mcpName, comment)
        modificationCounter++
    }

    fun getMethod(srgName: String): McpEntry? = names.methods[srgName]

    fun getField(srgName: String): McpEntry? = names.fields[srgName]

    fun getParam(srgName: String): McpEntry? = names.params[srgName]
    fun clearLists() {
        names.methods.clear()
        names.fields.clear()
        names.params.clear()
    }

    private fun MutableMap<String, McpEntry>.set(srgName: String, mcpName: String, comment: String) {
        computeIfAbsent(srgName) { McpEntry() }.let {
            it.mcpName = mcpName
            it.comment = comment
        }
    }

    override fun getModificationCount(): Long  = modificationCounter
}

data class NamesState(
    var methods: MutableMap<String, McpEntry> = mutableMapOf(),
    var fields: MutableMap<String, McpEntry> = mutableMapOf(),
    var params: MutableMap<String, McpEntry> = mutableMapOf()
)

