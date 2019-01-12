package com.mercandalli.android.apps.files.dialog

interface DialogContract {

    interface UserAction {

        fun onCreate(dialogInput: DialogActivity.DialogInput)

        fun onPositiveClicked(input: String)

        fun onNegativeClicked(input: String)
    }

    interface Screen {

        fun setTitle(text: String)

        fun setMessage(text: String)

        fun setPositive(text: String)

        fun setNegative(text: String)

        fun setInput(text: String)

        fun showInput()

        fun hideInput()

        fun showSoftInput()

        fun quit()
    }
}
