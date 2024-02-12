package kr.co.lion.project_memo

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kr.co.lion.project_memo.databinding.ActivityViewMemoBinding

/** 메모 보기 */
class ViewMemoActivity : AppCompatActivity() {
    lateinit var viewMemoBinding : ActivityViewMemoBinding

    // 메모 수정 Launcher
    lateinit var editMemoLauncher : ActivityResultLauncher<Intent>

    var positionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewMemoBinding = ActivityViewMemoBinding.inflate(layoutInflater)
        setContentView(viewMemoBinding.root)

        setLauncher()
        initView()
        initToolbar()

    }

    fun setLauncher(){
        val editContract = ActivityResultContracts.StartActivityForResult()
        editMemoLauncher = registerForActivityResult(editContract){
            if(it.resultCode == RESULT_OK){ // 수정되었으면, 수정된 내용을 보여준다.
                initView()
            }
        }
    }

    fun initToolbar(){
        viewMemoBinding.apply {
            toolbarViewMemo.apply {
                // 제목
                title = "메모 보기"

                // 뒤로가기 버튼
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationIconTint(Color.WHITE)
                setNavigationOnClickListener {
                    finish()
                }

                // 메뉴 아이콘
                inflateMenu(R.menu.menu_view_memo)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        // 수정 버튼
                        R.id.menu_view_edit -> { // EditMemoActivity로 이동
                            val editIntent = Intent(this@ViewMemoActivity, EditMemoActivity::class.java)
                            editIntent.putExtra("editPosition", positionIndex)
                            editMemoLauncher.launch(editIntent)
                        }
                        // 삭제 버튼
                        R.id.menu_view_delete -> {
                            Util.memoList.removeAt(positionIndex) // 메모 목록에서 제거
                            finish()
                        }
                    }
                    true
                }
            }
        }
    }

    fun initView(){
        // 선택한 메모의 index 값
        positionIndex = intent.getIntExtra("positionIndex", 0)

        viewMemoBinding.apply {
            inputViewTitle.setText(Util.memoList[positionIndex].title)
            inputViewDate.setText(Util.memoList[positionIndex].timeStamp)
            inputViewContent.setText(Util.memoList[positionIndex].content)
        }
    }
}