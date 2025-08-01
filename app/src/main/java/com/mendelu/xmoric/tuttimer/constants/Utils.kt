package com.mendelu.xmoric.tuttimer.constants

import android.content.Context
import android.util.Log
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText


class Utils {
    companion object {
        fun print(e: Exception) = Log.e("TAG", e.stackTraceToString())

        fun showMessage(
            context: Context,
            message: String?
        ) = makeText(context, message, LENGTH_LONG).show()
    }
}