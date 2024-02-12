package kr.co.lion.project_memo

import android.content.Context
import android.content.DialogInterface
import android.os.SystemClock
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlin.concurrent.thread

class Util {
    companion object{
        val memoList = mutableListOf<Memo>()

        // 다이얼로그를 보여주는 메서드
        fun showDialog(context: Context, title: String, message:String, focusView: TextInputEditText){
            // 다이얼로그를 보여준다.
            val builder = MaterialAlertDialogBuilder(context).apply {
                setTitle(title)
                setMessage(message)
                setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    focusView.setText("")
                    focusView.requestFocus() //비워져 있는 곳에 focus를 준다.
                    showSoftInput(context, focusView) //키보드를 보여준다.
                }
            }
            builder.show()
        }

        // 포커스를 주고 키보드를 올려주는 메서드
        fun showSoftInput(context: Context, focusView: TextInputEditText){
            thread {
                SystemClock.sleep(1000)

                val inputMethodManager = context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(focusView, 0)
            }
        }
    }
}

class Memo(
    var title : String,
    var content : String,
    var timeStamp : String
)

