package kr.co.lion.project_memo

import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kr.co.lion.project_memo.databinding.ActivityAddMemoBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import kotlin.concurrent.thread

/** 메모 작성 Activity */
class AddMemoActivity : AppCompatActivity() {
    lateinit var addMemoBinding : ActivityAddMemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addMemoBinding = ActivityAddMemoBinding.inflate(layoutInflater)
        setContentView(addMemoBinding.root)

        initToolbar()
    }

    fun initToolbar(){
        addMemoBinding.apply {
            toolbarAddMemo.apply {
                title = "메모 작성"
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationIconTint(Color.WHITE)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish() // 이전화면으로 돌아간다
                }

                inflateMenu(R.menu.menu_add_memo)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        // 툴바에 있는 완료(V) 버튼을 누르는 경우
                        R.id.menu_add_submit -> {
                            saveMemo()
                        }
                    }
                    true
                }
            }
        }
    }

    // 제목, 내용 입력 여부 확인 -> 내용 저장
    fun saveMemo(){
        addMemoBinding.apply {
            val title = inputAddTitle.text.toString()
            val content = inputAddContent.text.toString()

            // 제목이 작성되지 않은 경우
            if(title.trim().isEmpty()){
                Util.showDialog(this@AddMemoActivity, "제목 입력오류","제목이 입력되지 않았습니다. 제목을 입력해주세요", inputAddTitle)
                return
            }
            // 내용이 작성되지 않은 경우
            if(content.trim().isEmpty()){
                Util.showDialog(this@AddMemoActivity, "내용 입력오류", "내용이 입력되지 않았습니다. 내용을 입력해주세요", inputAddContent)
                return
            }

            val currentDateTime = System.currentTimeMillis()
            val dateTimeFormat = SimpleDateFormat("yyyy.MM.dd HH:mm")
            val timeStamp = dateTimeFormat.format(currentDateTime)

            // 모든 정보가 입력되었다면, 내용을 저장해준다.
            val newMemo = Memo(title, content, timeStamp)
            Util.memoList.add(newMemo)
            // 저장이 완료되면 이전 화면으로 돌아간다.
            setResult(RESULT_OK)
            finish()
        }
    }
}