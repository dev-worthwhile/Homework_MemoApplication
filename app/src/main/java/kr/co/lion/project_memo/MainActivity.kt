package kr.co.lion.project_memo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.project_memo.databinding.ActivityMainBinding
import kr.co.lion.project_memo.databinding.HolderMemoBinding

/** 메모 관리 Activity (메인화면) */
class MainActivity : AppCompatActivity() {
    lateinit var mainBinding : ActivityMainBinding

    // AddMemoActivity Launcher
    lateinit var addMemoLauncher : ActivityResultLauncher<Intent>
    // ViewMemoActivity Launcher
    lateinit var viewMemoLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        setLauncher()
        initToolbar()
        initView()
    }

    fun setLauncher(){
        // 메모 추가
        val addMemoContract = ActivityResultContracts.StartActivityForResult()
        addMemoLauncher = registerForActivityResult(addMemoContract){
            if(it.resultCode == RESULT_OK){ // 새롭게 추가된 경우
                mainBinding.recyclerViewMemo.adapter?.notifyItemInserted(Util.memoList.size-1)
            }
        }
        // 메모 보기
        val viewMemoContract = ActivityResultContracts.StartActivityForResult()
        viewMemoLauncher = registerForActivityResult(viewMemoContract){
            // 리사이클러뷰 갱신
            mainBinding.recyclerViewMemo.adapter?.notifyDataSetChanged()
        }
    }

    fun initToolbar(){
        mainBinding.apply {
            toolbarMain.apply {
                title = "메모 관리"
                inflateMenu(R.menu.menu_main)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_main_add -> {
                            // AddMemoActivity 화면으로 이동
                            val addMemoIntent = Intent(this@MainActivity, AddMemoActivity::class.java)
                            addMemoLauncher.launch(addMemoIntent)
                        }
                    }
                    true
                }
            }
        }
    }

    fun initView(){
        mainBinding.apply {
            recyclerViewMemo.adapter = MemoAdapter()
            val layoutManager =LinearLayoutManager(this@MainActivity)
           /* layoutManager.apply { // 최신순으로 정렬
                reverseLayout = true
                stackFromEnd = true
            }*/
            recyclerViewMemo.layoutManager = layoutManager
        }

    }

    // 메모 목록 RecyclerView Adapter
    inner class MemoAdapter : RecyclerView.Adapter<MemoAdapter.MemoViewHolder>(){
        inner class MemoViewHolder(memoBinding : HolderMemoBinding):RecyclerView.ViewHolder(memoBinding.root){
            val memoBinding : HolderMemoBinding
            init {
                this.memoBinding = memoBinding

                // 메모 카드뷰의 너비가 화면에 꽉 찰 수 있도록 + 클릭 되도록 길이 설정
                this.memoBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
            val memoBinding = HolderMemoBinding.inflate(layoutInflater)
            val memoViewHolder = MemoViewHolder(memoBinding)

            return memoViewHolder
        }

        override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
            holder.memoBinding.textMemoTitle.text = Util.memoList[position].title
            holder.memoBinding.textMemoDate.text = Util.memoList[position].timeStamp

            holder.memoBinding.root.setOnClickListener {
                val viewIntent = Intent(this@MainActivity, ViewMemoActivity::class.java)
                viewIntent.putExtra("positionIndex", position)
                viewMemoLauncher.launch(viewIntent)
            }
        }

        override fun getItemCount(): Int {
            return Util.memoList.size
        }
    }
}