/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.infosmid.kfsm.viz


import org.infosmid.kfsm.viz.TransitionType.*

import java.io.PrintWriter
import java.io.StringWriter

/**
 * @author Corneil du Plessis
 * @soundtrack Wolfgang Amadeus Mozart
 */
object Visualization {
    fun String.mermaidEscape() = this
        .replace(";", "#59;")
        .replace("#", "#35;")
        .replace(Regex("""\s+->\s+"""), "#160;#8594;#160;")
        .replace("->", "#8594;")
        .replace(":", "#58;")
        .replace("{", "#123;").replace("}", "#125;")
        .replace("<", "#60;").replace(">", "#62;")
        .replace("&", "#38;")
        .replace("\r\n", "\n").replace("\n", "<br/>")
        .replace(" ", "#160;")


    private fun printMermaidTransition(transition: VisualTransition, output: PrintWriter, includeDetail: Boolean) {
        val startName = when {
            transition.type == DEFAULT -> transition.startMap
            "<<start>>" == transition.start || "START" == transition.start -> "[*]"
            else -> transition.start
        } ?: "<<unknown>>"
        val endName = when {
            "<<end>>" == transition.target || "END" == transition.target -> "[*]"
            else -> transition.target
        } ?: transition.start ?: "<<unknown>>"
        val event = if (transition.automatic) "<<automatic>>" else transition.event
        output.print("  ${startName.mermaidEscape()} --> ${endName.mermaidEscape()}")
        if (event != null) {
            output.print(" : ${event.mermaidEscape()}")
        }
        if (transition.guard != null && includeDetail) {
            val guard = transition.guard!!

            val guardExp = if (guard.startsWith("{")) {
                guard.substring(1, guard.length - 1).trim()
            } else {
                guard.trim()
            }
            if (event == null) {
                output.print(" :")
            }
            output.print(" [".mermaidEscape())
            output.print(guardExp.mermaidEscape())
            output.print("]".mermaidEscape())
        }

        if (includeDetail && transition.action != null && transition.action?.trim() != "{}") {
            val action = transition.action!!
            if (event == null && transition.guard == null) {
                output.print(" : ")
            }
            output.print("#160;#8594;#160;")
            if (action.endsWith("  }")) {
                output.print(action.substring(0, action.length - 3).mermaidEscape())
                output.print("}".mermaidEscape())
            } else {
                output.print(action.mermaidEscape())
            }
        }
        output.println()
    }

    @JvmStatic
    fun mermaidDiagram(statemachine: VisualStateMachineDefinion, includeDetail: Boolean = true): String {
        val sw = StringWriter()
        val output = PrintWriter(sw)
        output.println("---")
        output.println("title: ${statemachine.name}")
        output.println("""config:
  themeCSS: |
    .edgeLabel p:has(br) { text-align: left; }""")
        output.println("---")
        output.println("stateDiagram-v2")
        statemachine.stateMaps.filter { it.value.name == statemachine.name }.forEach { stateMap ->
            output.println("state ${stateMap.value.name} {")
            stateMap.value.transitions.filter { it.start == "<<start>>" || it.start == "START" }.forEach {
                printMermaidTransition(it, output, includeDetail)
            }
            stateMap.value.transitions.filter { it.start != "<<start>>" && it.start != "START" }.forEach {
                printMermaidTransition(it, output, includeDetail)
            }
        }
        statemachine.stateMaps.filter { it.value.name != statemachine.name }.forEach { stateMap ->
            output.print("  ")
            output.println("state ${stateMap.value.name} {")
            stateMap.value.transitions.filter { it.start == "<<start>>" || it.start == "START" }.forEach {
                output.print("  ")
                printMermaidTransition(it, output, includeDetail)
            }
            stateMap.value.transitions.filter { it.start != "<<start>>" && it.start != "START" }.forEach {
                output.print("  ")
                printMermaidTransition(it, output, includeDetail)
            }
            output.println("  }")
        }
        output.println("}")
        if (includeDetail && statemachine.invariants.isNotEmpty()) {
            output.println("note left of ${statemachine.name}")
            statemachine.invariants.forEach {
                output.println("<<invariant>> $it".mermaidEscape())
            }
            output.println("end note")
        }
        output.println()
        output.flush()
        sw.flush()
        return sw.toString()
    }
    private fun printPlantUmlTransition(transition: VisualTransition, output: PrintWriter, includeDetail: Boolean) {
        val startName = if (transition.type == DEFAULT) transition.startMap else
            if ("<<start>>" == transition.start || "START" == transition.start) "[*]" else transition.start
                ?: "<<unknown>>"
        val endName = if ("<<end>>" == transition.target || "END" == transition.target) "[*]" else transition.target
            ?: transition.start ?: "<<unkown>>"
        val event = if (transition.automatic) "<<automatic>>" else transition.event
        output.print("$startName --> $endName")
        if (event != null) {
            output.print(" : $event")
        }
        if (transition.guard != null && includeDetail) {
            val guard = transition.guard!!.replace("\n", "\\l  ").replace("\r", "")

            val guardExp = if (guard.startsWith("{")) {
                guard.substring(1, guard.length - 1).trim()
            } else {
                guard.trim()
            }
            output.print(" [")
            output.print(guardExp)
            output.print("]")
        }

        if (includeDetail && transition.action != null && transition.action?.trim() != "{}") {
            val action = transition.action!!
                .replace("\n", "\\l  ")
                .replace("\r", "")
            output.print(" -> ")
            if (action.endsWith("  }")) {
                output.print(action.substring(0, action.length - 3))
                output.print("}")
            } else {
                output.print(action)
            }
        }
        output.println()
    }

    @JvmStatic
    fun plantUml(statemachine: VisualStateMachineDefinion, includeDetail: Boolean = true): String {
        val sw = StringWriter()
        val output = PrintWriter(sw)
        output.println("@startuml")
        output.println("skinparam monochrome true")
        output.println("skinparam StateFontName Helvetica")
        output.println("skinparam defaultFontName Monospaced")
        output.println("skinparam defaultFontStyle Bold")
        output.println(
            """
            skinparam state {
                FontStyle Bold
            }
        """.trimIndent()
        )
        statemachine.stateMaps.filter { it.value.name != statemachine.name }.forEach { stateMap ->
            output.println("state ${stateMap.value.name} {")
            stateMap.value.transitions.filter { it.start == "<<start>>" || it.start == "START" }.forEach {
                printPlantUmlTransition(it, output, includeDetail)
            }
            stateMap.value.transitions.filter { it.start != "<<start>>" && it.start != "START" }.forEach {
                printPlantUmlTransition(it, output, includeDetail)
            }
            output.println("}")
        }
        statemachine.stateMaps.filter { it.value.name == statemachine.name }.forEach { stateMap ->
            output.println("state ${stateMap.value.name} {")
            stateMap.value.transitions.filter { it.start == "<<start>>" || it.start == "START" }.forEach {
                printPlantUmlTransition(it, output, includeDetail)
            }
            stateMap.value.transitions.filter { it.start != "<<start>>" && it.start != "START" }.forEach {
                printPlantUmlTransition(it, output, includeDetail)
            }
            output.println("}")
        }
        if (includeDetail && statemachine.invariants.isNotEmpty()) {
            output.println("note top of ${statemachine.name}")
            statemachine.invariants.forEach {
                output.println("<<invariant>> $it")
            }
            output.println("end note")
        }
        output.println("@enduml")
        output.flush()
        sw.flush()
        return sw.toString()
    }

    @JvmStatic
    public fun asciiDoc(statemachine: VisualStateMachineDefinion): String {
        val sw = StringWriter()
        val output = PrintWriter(sw)
        output.println("== ${statemachine.name} State Chart")
        output.println()
        statemachine.stateMaps.filter { it.value.name == statemachine.name }.forEach { stateMap ->
            output.println("=== ${statemachine.name} State Map")
            output.println()
            output.println(
                """
        |===
        | Start | Event[Guard] | Target | Action
        """.trimIndent()
            )
            stateMap.value.transitions.filter { it.start == "<<start>>" || it.start == "START" }.forEach {
                printAsciiDocTransition(it, output)
            }
            stateMap.value.transitions.filter { it.start != "<<start>>" && it.start != "START" }.forEach {
                printAsciiDocTransition(it, output)
            }
            output.println("|===")
            output.println()
        }
        statemachine.stateMaps.filter { it.value.name != statemachine.name }.forEach { stateMap ->
            output.println("=== State Map ${stateMap.value.name}")
            output.println()
            output.println(
                """
        |===
        | Start | Event[Guard] | Target | Action
        """.trimIndent()
            )
            stateMap.value.transitions.forEach {
                printAsciiDocTransition(it, output)
            }
            output.println("|===")
            output.println()
        }
        output.flush()
        sw.flush()
        return sw.toString()
    }

    fun escapeCharacters(input: String, escape: String): String {
        val builder = StringBuilder()
        input.forEach { c ->
            if (escape.contains(c)) {
                builder.append('\\')
            }
            builder.append(c)
        }
        return builder.toString()
    }

    fun printAsciiDocTransition(transition: VisualTransition, output: PrintWriter) {
        output.println()
        val startName = when {
			transition.type == DEFAULT -> transition.startMap
			"<<start>>" == transition.start || "START" == transition.start -> "<<start>>"
			else -> transition.start
		} ?: "<<unknown>>"
        val endName = when {
			"<<end>>" == transition.target || "END" == transition.target -> "<<end>>"
			else -> transition.target
		} ?: transition.start ?: "<<unknown>>"
		val endText = endName.replace("<<", "\\<<")
        val event = if (transition.automatic) "<<automatic>>" else transition.event ?: ""
		val eventName = startName.replace("<<", "\\<<")
		val eventText = event.replace("<<", "\\<<")
        output.println("| $eventName")
        output.print("| $eventText")

        if (transition.guard != null) {
            val guard = transition.guard?.replace("\n", " ")
				?.replace("\r", "")
            output.print(escapeCharacters(" `[$guard]`", "|"))
        }
        output.println()
        output.println("| $endText")
        output.print("a| ")
        if (transition.action != null && transition.action?.trim() != "{}") {
            val action = transition.action?.replace("\r", "")
            output.print(
                escapeCharacters(
                    """[source,kotlin]
----
$action
----""", "|"
                )
            )
        }
        output.println()
    }
}

