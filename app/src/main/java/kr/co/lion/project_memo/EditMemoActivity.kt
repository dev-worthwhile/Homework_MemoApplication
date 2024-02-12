package kr.co.lion.project_memo

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.lion.project_memo.databinding.ActivityEditMemoBinding

/** 메모의 내용을 수정하는 Activity */
class EditMemoActivity : AppCompatActivity() {
    var positionIndex = 0

    lateinit var editMemoBinding : ActivityEditMemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editMemoBinding = ActivityEditMemoBinding.inflate(layoutInflater)
        setContentView(editMemoBinding.root)

        initToolbar()
        initView()
    }

    fun initToolbar(){
        editMemoBinding.apply {
            toolbarEditMemo.apply {
                title = "메모 수정"
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationIconTint(Color.WHITE)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }

                inflateMenu(R.menu.menu_edit_memo)
                setOnMenuItemClickListener{
                    when(it.itemId){
                        R.id.menu_edit_submit -> {
                            updateMemo() // 메모를 저장한다.
                            setResult(RESULT_OK)
                        }
                    }
                    true
                }

            }
        }
    }

    fun initView(){
        editMemoBinding.apply {
            // 선택한 메모의 index 값
            positionIndex = intent.getIntExtra("editPosition", 0)

            inputEditTitle.setText(Util.memoList[positionIndex].title)
            inputEditContent.setText(Util.memoList[positionIndex].content)
        }
    }

    fun updateMemo(){
        editMemoBinding.apply {
            val title = inputEditTitle.text.toString()
            val content = inputEditContent.text.toString()

            // 제목이 작성되지 않은 경우
            if(title.trim().isEmpty()){
                Util.showDialog(this@EditMemoActivity,"제목 입력오류","제목이 입력되지 않았습니다. 제목을 입력해주세요", inputEditTitle)
                return
            }
            // 내용이 작성되지 않은 경우
            if(content.trim().isEmpty()){
                Util.showDialog(this@EditMemoActivity,"내용 입력오류", "내용이 입력되지 않았습니다. 내용을 입력해주세요", inputEditContent)
                return
            }

            // 모든 정보가 입력되었다면, 내용을 저장해준다.
            Util.memoList[positionIndex].title = title
            Util.memoList[positionIndex].content = content

            val updateIntent = Intent()
            updateIntent.putExtra("positionIndex", positionIndex)

            // 저장이 완료되면 이전 화면으로 돌아간다.
            setResult(RESULT_OK, updateIntent)
            finish()
        }
    }
}