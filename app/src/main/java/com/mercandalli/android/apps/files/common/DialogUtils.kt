package com.mercandalli.android.apps.files.common

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import android.text.Spanned
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView

import java.lang.ref.WeakReference

object DialogUtils {

    fun seekBarDialog(
            context: Context?,
            title: String,
            initValue: Int,
            maxValue: Int,
            positive: String?,
            onDialogUtilsSeekBarListenerParam: OnDialogUtilsSeekBarListener?) {
        if (context == null) {
            return
        }
        val onDialogUtilsSeekBarListenerReference = WeakReference<OnDialogUtilsSeekBarListener>(
                onDialogUtilsSeekBarListenerParam
        )
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(6, 6, 6, 6)

        val valueText = TextView(context)
        valueText.gravity = Gravity.CENTER_HORIZONTAL
        valueText.textSize = 32f
        layout.addView(valueText, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT))

        val seekBar = SeekBar(context)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val onDialogUtilsSeekBarListener = onDialogUtilsSeekBarListenerReference.get()
                if (onDialogUtilsSeekBarListener == null) {
                    valueText.setText(progress)
                } else {
                    valueText.text = onDialogUtilsSeekBarListener.onDialogUtilsSeekBarChanged(
                            progress
                    )
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        seekBar.max = maxValue
        seekBar.progress = initValue
        layout.addView(seekBar, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT))
        if (onDialogUtilsSeekBarListenerParam == null) {
            valueText.setText(initValue)
        } else {
            valueText.text = onDialogUtilsSeekBarListenerParam.onDialogUtilsSeekBarChanged(initValue)
        }
        builder.setView(layout)
        builder.setPositiveButton(positive) { dialog, _ ->
            val onDialogUtilsSeekBarListener = onDialogUtilsSeekBarListenerReference.get()
            onDialogUtilsSeekBarListener?.onDialogUtilsSeekBarCalledBack(
                    seekBar.progress)
            dialog.dismiss()
        }
        builder.create().show()
    }

    fun listDialog(
            context: Context?,
            title: String,
            items: List<String>,
            listener: DialogInterface.OnClickListener?) {
        if (context == null) {
            return
        }
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        if (listener == null) {
            builder.setItems(items.toTypedArray()) { _, _ ->
            }
        } else {
            builder.setItems(items.toTypedArray(), listener)
        }
        builder.create().show()
    }

    fun alert(
            context: Context,
            title: String,
            message: String,
            positive: String?,
            positiveListenerParam: OnDialogUtilsListener?,
            negative: String?,
            negativeListenerParam: OnDialogUtilsListener?) {
        val positiveListenerReference = WeakReference<OnDialogUtilsListener>(positiveListenerParam)
        val negativeListenerReference = WeakReference<OnDialogUtilsListener>(negativeListenerParam)
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        if (positive != null) {
            builder.setPositiveButton(positive) { dialog, _ ->
                val positiveListener = positiveListenerReference.get()
                positiveListener?.onDialogUtilsCalledBack()
                dialog.dismiss()
            }
        }
        if (negative != null) {
            builder.setNegativeButton(negative) { dialog, _ ->
                val negativeReference = negativeListenerReference.get()
                negativeReference?.onDialogUtilsCalledBack()
                dialog.dismiss()
            }
        }
        builder.create().show()
    }

    fun alert(
            context: Context,
            title: String,
            message: Spanned,
            positive: String?,
            positiveListenerParam: OnDialogUtilsListener,
            negative: String?,
            negativeListenerParam: OnDialogUtilsListener) {

        val positiveListenerReference = WeakReference(positiveListenerParam)
        val negativeListenerReference = WeakReference(negativeListenerParam)
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        if (positive != null) {
            builder.setPositiveButton(positive) { dialog, _ ->
                val positiveListener = positiveListenerReference.get()
                positiveListener?.onDialogUtilsCalledBack()
                dialog.dismiss()
            }
        }
        if (negative != null) {
            builder.setNegativeButton(negative) { dialog, _ ->
                val negativeListener = negativeListenerReference.get()
                negativeListener?.onDialogUtilsCalledBack()
                dialog.dismiss()
            }
        }
        val alert = builder.create()
        alert.show()
    }

    fun prompt(
            context: Context,
            title: String,
            message: String,
            positive: String,
            positiveListener: OnDialogUtilsStringListener,
            negative: String,
            negativeListener: OnDialogUtilsListener,
            dismissListener: OnDialogUtilsListener?) {
        prompt(
                context,
                title,
                message,
                positive,
                positiveListener,
                negative,
                negativeListener, null,
                dismissListener)
    }

    @JvmOverloads
    fun prompt(
            context: Context,
            title: String,
            message: String,
            positive: String,
            positiveListener: OnDialogUtilsStringListener,
            negative: String,
            negativeListener: OnDialogUtilsListener?,
            preTex: String? = null,
            dismissListener: OnDialogUtilsListener? = null) {
        prompt(
                context,
                title,
                message,
                positive,
                positiveListener,
                negative,
                negativeListener,
                preTex, null,
                dismissListener)
    }

    fun prompt(
            context: Context,
            title: String,
            message: String?,
            positive: String,
            positiveListener: OnDialogUtilsStringListener?,
            negative: String,
            negativeListener: OnDialogUtilsListener?,
            preText: String?,
            hint: String?,
            dismissListener: OnDialogUtilsListener?) {
        val alert = AlertDialog.Builder(context)

        alert.setTitle(title)
        if (message != null) {
            alert.setMessage(message)
        }

        // Set an EditText view to get user input
        val input = EditText(context)
        input.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI

        if (preText != null) {
            input.setText(preText)
        }
        if (hint != null) {
            input.hint = hint
        }

        alert.setPositiveButton(positive) { _, _ ->
            positiveListener?.onDialogUtilsStringCalledBack(input.text.toString())
        }
        alert.setNegativeButton(negative) { _, _ ->
            negativeListener?.onDialogUtilsCalledBack()
        }
        alert.setOnDismissListener {
            dismissListener?.onDialogUtilsCalledBack()
        }

        //alert.show();
        val alertDialog = alert.create()
        alertDialog.setView(
                input,
                38,
                20,
                38,
                0
        )
        alertDialog.show()
    }

    interface OnDialogUtilsListener {
        fun onDialogUtilsCalledBack()
    }

    interface OnDialogUtilsStringListener {
        fun onDialogUtilsStringCalledBack(text: String)
    }

    interface OnDialogUtilsSeekBarListener {
        fun onDialogUtilsSeekBarCalledBack(value: Int)

        fun onDialogUtilsSeekBarChanged(value: Int): String
    }
}
