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
package org.infosmid.example.kfsm

import org.infosmid.kfsm.viz.Visualization.plantUml
import org.infosmid.kfsm.viz.Visualization.mermaidDiagram
import org.infosmid.kfsm.viz.Parser.parseStateMachine
import org.infosmid.kfsm.viz.Visualization.asciiDoc

import org.junit.Before
import org.junit.Test
import java.io.File

class VisualizeParserTest {
    @Before
    fun setup() {
        val generated = File("generated-parsed")
        if (generated.exists() && !generated.isDirectory) {
            error("Expected generated to be a directory")
        } else if (!generated.exists()) {
            generated.mkdirs()
        }
    }

    @Test
    fun produceVisualizationSimpleTurnstileFSM() {
        println("== Simple TurnStile")
        val visualisation = parseStateMachine("SimpleTurnstileFSM", File("src/test/resources/SimpleTurnstile.kt"))
        println(visualisation)
        File("generated-parsed", "simple-turnstile.plantuml").writeText(plantUml(visualisation))
        File("generated-parsed", "simple-turnstile.mmd").writeText(mermaidDiagram(visualisation))
        File("generated-parsed", "simple-turnstile-simple.plantuml").writeText(plantUml(visualisation, false))
        File("generated-parsed", "simple-turnstile-simple.mmd").writeText(mermaidDiagram(visualisation, false))
        File("generated-parsed", "simple-turnstile.adoc").writeText(asciiDoc(visualisation))
    }

    @Test
    fun produceVisualizationTurnstileFSM() {
        println("== TurnStile")
        val visualisation = parseStateMachine("TurnstileFSM", File("src/test/resources/Turnstile.kt"))
        println(visualisation)
        File("generated-parsed", "turnstile.plantuml").writeText(plantUml(visualisation))
        File("generated-parsed", "turnstile.mmd").writeText(mermaidDiagram(visualisation))
        File("generated-parsed", "turnstile-simple.plantuml").writeText(plantUml(visualisation, false))
        File("generated-parsed", "turnstile-simple.mmd").writeText(mermaidDiagram(visualisation, false))
        File("generated-parsed", "turnstile.adoc").writeText(asciiDoc(visualisation))
    }

    @Test
    fun produceVisualizationPayingTurnstile() {
        println("== PayingTurnstile")
        val visualisation = parseStateMachine("PayingTurnstileFSM", File("src/test/resources/PayingTurnstile.kt"))
        println(visualisation)
        File("generated-parsed", "paying-turnstile.plantuml").writeText(plantUml(visualisation))
        File("generated-parsed", "paying-turnstile.mmd").writeText(mermaidDiagram(visualisation))
        File("generated-parsed", "paying-turnstile-simple.plantuml").writeText(plantUml(visualisation, false))
        File("generated-parsed", "paying-turnstile-simple.mmd").writeText(mermaidDiagram(visualisation, false))
        File("generated-parsed", "paying-turnstile.adoc").writeText(asciiDoc(visualisation))
    }

    @Test
    fun produceVisualizationSecureTurnstile() {
        println("== SecureTurnstile")
        val visualisation = parseStateMachine("SecureTurnstileFSM", File("src/test/resources/SecureTurnstile.kt"))
        println(visualisation)
        File("generated-parsed", "secure-turnstile.plantuml").writeText(plantUml(visualisation))
        File("generated-parsed", "secure-turnstile.mmd").writeText(mermaidDiagram(visualisation))
        File("generated-parsed", "secure-turnstile-simple.plantuml").writeText(plantUml(visualisation, false))
        File("generated-parsed", "secure-turnstile-simple.mmd").writeText(mermaidDiagram(visualisation, false))
        File("generated-parsed", "secure-turnstile.adoc").writeText(asciiDoc(visualisation))
    }

    @Test
    fun produceVisualizationPacketReader() {
        println("== PacketReader")
        val visualisation = parseStateMachine("PacketReaderFSM", File("src/test/resources/PacketReader.kt"))
        println(visualisation)
        File("generated-parsed", "packet-reader.plantuml").writeText(plantUml(visualisation))
        File("generated-parsed", "packet-reader.mmd").writeText(mermaidDiagram(visualisation))
        File("generated-parsed", "packet-reader-simple.plantuml").writeText(plantUml(visualisation, false))
        File("generated-parsed", "packet-reader-simple.mmd").writeText(mermaidDiagram(visualisation, false))
        File("generated-parsed", "packet-reader.adoc").writeText(asciiDoc(visualisation))
    }

    @Test
    fun produceVisualizationTimeoutSecureTurnstile() {
        println("== TimeoutSecureTurnstile")
        val visualisation =
            parseStateMachine("TimerSecureTurnstileFSM", File("src/test/resources/TimeoutSecureTurnstile.kt"))
        println(visualisation)
        File("generated-parsed", "timeout-secure-turnstile.plantuml").writeText(plantUml(visualisation))
        File("generated-parsed", "timeout-secure-turnstile.mmd").writeText(mermaidDiagram(visualisation))
        File("generated-parsed", "timeout-secure-turnstile-simple.plantuml").writeText(plantUml(visualisation, false))
        File("generated-parsed", "timeout-secure-turnstile-simple.mmd").writeText(mermaidDiagram(visualisation, false))
        File("generated-parsed", "timeout-secure-turnstile.adoc").writeText(asciiDoc(visualisation))
    }

}
