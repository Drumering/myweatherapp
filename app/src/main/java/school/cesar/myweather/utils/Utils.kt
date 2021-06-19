package school.cesar.myweather.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class Utils {
    companion object {
        fun hideKeyboard(context: Context, editText: EditText) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText.windowToken, 0)
        }
    }
}