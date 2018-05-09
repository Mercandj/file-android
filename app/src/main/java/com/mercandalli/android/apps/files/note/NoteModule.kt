package com.mercandalli.android.apps.files.note

import android.content.Context
import android.content.Intent

class NoteModule(
        private val context: Context
) {

    fun provideNoteManager(): NoteManager {
        val sharedPreferences = context.getSharedPreferences(
                NoteManagerSharedPreferences.PREFERENCE_NAME,
                Context.MODE_PRIVATE)
        val addOn: NoteManagerSharedPreferences.AddOn = object : NoteManagerSharedPreferences.AddOn {
            override fun shareText(text: String) {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, text)
                sendIntent.type = "text/plain"
                context.startActivity(sendIntent)
            }
        }
        return NoteManagerSharedPreferences(
                sharedPreferences,
                addOn
        )
    }
}