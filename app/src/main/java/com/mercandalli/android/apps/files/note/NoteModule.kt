package com.mercandalli.android.apps.files.note

import android.app.Activity
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
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, text)
                intent.type = "text/plain"
                if (context !is Activity) {
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                context.startActivity(intent)
            }
        }
        return NoteManagerSharedPreferences(
                sharedPreferences,
                addOn
        )
    }
}