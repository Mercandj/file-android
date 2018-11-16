@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.server.files.android_tos

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.util.pipeline.PipelineContext
import kotlinx.html.*

object AndroidTosHandler {

    private const val publisherName = "Team Mercan"

    suspend fun PipelineContext<Unit, ApplicationCall>.androidTermsOfUseGet(appName: String) {
        val intro = createIntro(appName)
        val acceptance = createAcceptance(appName)
        val intellectualProperty = createIntellectualProperty()
        val changes = createChanges(appName)
        call.respondHtml {
            head {
                title { +"Terms of use $appName" }
            }
            body {
                h1("title") {
                    +"Terms of use $appName"
                }
                a {
                    +intro
                }
                h4("title") {
                    +"Acceptance of the Terms of Use"
                }
                a {
                    +acceptance
                }
                h4("title") {
                    +"Intellectual property"
                }
                a {
                    +intellectualProperty
                }
                h4("title") {
                    +"CHANGES TO THIS PRIVACY POLICY"
                }
                a {
                    +changes
                }
            }
        }
    }

    private fun createIntro(appName: String) = "Welcome to $appName ®, a service offered by $publisherName (« $appName », « $publisherName », « we », « us » « our ») and our Terms and Conditions of Use. This affects your $appName mobile application and website usage (« the service », or « mobile application »).\n" +
        "\n" +
        "These Conditions of Use, and our Privacy Policy constitute a legal agreement, which is a contract (the « Agreement ») between you and $publisherName, concerning $appName usage.\n" +
        "\n" +
        "Children who are less than 13 years old are not authorized to use the service or try to sign up.\n" +
        "\n" +
        "IF YOU DON’T AGREE WITH THESE TERMS, THEN PLEASE DON’T USE THE SERVICE."

    private fun createAcceptance(appName: String) = "Thanks to read carefully the Conditions of Use and our confidentiality policy. IF YOU DON’T AGREE WITH THESE CONDITIONS, THEN PLEASE DON’T USE THE $appName MOBILE APPLICATION.\n" +
        "\n" +
        "By reaching and using $appName or by reaching to the content of the application, you declare that you guarantee that you read and understood the Conditions of Use as well as the confidentiality policy and the community rules, that you stand by it, that you are legally an adult or that you have more than 13 years and your parents or guardian give you the right to use the $appName mobile application."

    private fun createIntellectualProperty() = "The brands of the Company and its partners, and logos appearing on the website and / or Software are registered brands. Total or partial reproduction of these trademarks or logos without the consent of the Company or its partners is prohibited."

    private fun createChanges(appName: String) = "$appName reserves the right to change the contents. If we use your personal information otherwise specified in the legal information provided at the time of data collection, users will be informed through advertisements published by $appName."
}
