@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.server.files.tos_tos

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.util.pipeline.PipelineContext
import kotlinx.html.*

object AndroidTosHandler {

    private const val publisherName = "Team Mercan"

    suspend fun PipelineContext<Unit, ApplicationCall>.androidTermsOfUseGet(appName: String) {
        call.respondHtml {
            head {
                title { +"Terms of use « $appName »" }
            }
            body {
                h1("title") {
                    +"Terms of use « $appName »"
                }
                val sections = createBodyMap(appName)
                for (section in sections) {
                    h4("title") {
                        +section.title
                    }
                    for (paragraph in section.paragraphs) {
                        p {
                            +paragraph
                        }
                    }
                }
            }
        }
    }

    private fun createBodyMap(appName: String): List<Section> {
        val body = ArrayList<Section>()
        body.add(Section("Terms and conditions of use of « $appName » on Android", createIntro(appName)))
        body.add(Section("Acceptance of the Terms of Use", createAcceptance(appName)))
        body.add(Section("Intellectual property", createIntellectualProperty()))
        body.add(Section("In-App Purchases and Subscriptions", createInAppPurchasesAndSubscription(appName)))
        body.add(Section("CHANGES TO THIS PRIVACY POLICY", createChanges(appName)))
        body.add(Section("Last update", createLastUpdate()))
        return body
    }

    private fun createIntro(appName: String) = arrayListOf(

        "Welcome to $appName ®, a service offered by $publisherName (« $appName », « " +
            "$publisherName », « we », « us » « our ») and our Terms and Conditions of Use. " +
            "This affects your $appName mobile application and website usage " +
            "(« the service », or « mobile application »).",

        "These Conditions of Use, and our Privacy Policy constitute a legal agreement, " +
            "which is a contract (the « Agreement ») between you and $publisherName, " +
            "concerning $appName usage.",

        "« you » and « user » are the people that is using the app $appName and so, is " +
            "accepting this terms of services.",

        "Children who are less than 13 years old are not authorized to use the service " +
            "or try to sign up.",

        "IF YOU DON’T AGREE WITH THESE TERMS, THEN PLEASE DON’T USE THE SERVICE."
    )

    private fun createAcceptance(appName: String) = arrayListOf(

        "Thanks to read carefully the Conditions of Use and our confidentiality policy. IF YOU " +
            "DON’T AGREE WITH THESE CONDITIONS, THEN PLEASE DON’T USE THE $appName MOBILE " +
            "APPLICATION.",

        "By reaching and using $appName or by reaching to the content of the application, " +
            "you declare that you guarantee that you read and understood the Conditions of Use " +
            "as well as the confidentiality policy and the community rules, that you stand by " +
            "it, that you are legally an adult or that you have more than 13 years and your " +
            "parents or guardian give you the right to use the $appName mobile application.",

        "By downloading, browsing, accessing or using this $appName app, you agree to be " +
            "bound by these Terms and Conditions of Use. We reserve the right to amend these " +
            "terms and conditions at any time. If you disagree with any of these Terms and " +
            "Conditions of Use, you must immediately discontinue your access to the $appName and " +
            "your use of the services offered on the $appName. Continued use of $appName will " +
            "constitute acceptance of these Terms and Conditions of Use, as may be amended " +
            "from time to time."
    )

    private fun createIntellectualProperty() = arrayListOf(

        "The brands of the Company and its partners, and logos appearing on the website and / " +
            "or Software are registered brands. Total or partial reproduction of these " +
            "trademarks or logos without the consent of the Company or its partners is prohibited."
    )

    private fun createInAppPurchasesAndSubscription(appName: String) = arrayListOf(
        // https://termsfeed.com/blog/in-app-purchases-terms-conditions/
        "$appName may include in-app purchases and subscription that allow you things such as " +
            "feature, supporting the app developer etc. If it does, it will not be necessary to " +
            "make any In-App purchases or Subscription in order to use the app with limited " +
            "feature. You acknowledge and agree that you are fully responsible for managing your " +
            "In-App Purchases and / or Subscriptions and the amount you spend on In-App Purchases " +
            "or Subscriptions within the app.",

        "If you are under 18 then you must have your parent's or guardians' permission to " +
            "make any In-App Purchases or Subscription. By completing an In-App Purchase or a " +
            "Subscription or any transaction, you are confirming to us that you have any and " +
            "all permission that may be necessary in order to allow you to make that purchase.",

        "More information about how you may be able to switch off and/or manage In-App " +
            "Purchases and Subscription using your device may be set out in the Google Play Store " +
            "terms and conditions/EULA. To unsubscribe, all the process should be done on the " +
            "Google Play Store.",

        "You acknowledge and agree that all billing and transaction processes are handled by " +
            "the Google Play Store from the whose platform you downloaded the app $appName and " +
            "are governed by the Google Play Store's term and conditions/EULA. If you have any " +
            "payment related issues with In-App Purchases or Subscription then you need to " +
            "contact the Google Play Store directly."
    )

    private fun createChanges(appName: String) = arrayListOf(

        "$appName reserves the right to change the contents. If we use your personal information " +
            "otherwise specified in the legal information provided at the time of data collection, " +
            "users will be informed through advertisements published by $appName."
    )

    private fun createLastUpdate() = arrayListOf(

        "Sunday, March 3, 2019."
    )

    private data class Section(
        val title: String,
        val paragraphs: List<String>
    )
}
