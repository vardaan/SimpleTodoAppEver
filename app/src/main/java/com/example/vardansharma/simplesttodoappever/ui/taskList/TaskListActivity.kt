package com.example.vardansharma.simplesttodoappever.ui.taskList

import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log.e
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.example.vardansharma.simplesttodoappever.R
import com.example.vardansharma.simplesttodoappever.models.Task
import com.example.vardansharma.simplesttodoappever.ui.addTask.AddTaskDialog
import com.example.vardansharma.simplesttodoappever.utils.inflate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TaskListActivity : AppCompatActivity(), OnTaskAddedListener, OnTaskUpdateListener {
    var taskRecyclerView: RecyclerView? = null

    var taskList: MutableList<Task> = mutableListOf()

    val  rootRef = FirebaseDatabase.getInstance()

    lateinit var tasksRef: DatabaseReference

    private lateinit var taskAdapter: TaskAdapter

    private  var user: FirebaseUser? =null

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        taskList = mutableListOf()
        taskRecyclerView = findViewById(R.id.task_list) as RecyclerView
        taskRecyclerView!!.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(taskList!!, this)
        taskRecyclerView!!.adapter = taskAdapter

        user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        tasksRef = rootRef.getReference("users/$uid")


        val btnAdd = findViewById(R.id.addTask) as FloatingActionButton
        btnAdd.setOnClickListener { showAddTaskDialog() }

    }

    override fun onResume() {
        super.onResume()
        tasksRef.addChildEventListener(childListener)
    }

    override fun onPause() {
        super.onPause()
        tasksRef.removeEventListener(childListener)
    }

    private fun handleOnTaskDelted(dataSnapshot: DataSnapshot) {
        val deletedTask = dataSnapshot.getValue(Task::class.java)
        var removeIndex = -1
        taskList!!.forEachIndexed { index, (id) ->
            when (id) {
                deletedTask?.id -> {
                    removeIndex = index
                }
            }
        }
        when {
            removeIndex != -1 -> {
                taskList.removeAt(removeIndex)
                taskAdapter.notifyItemRemoved(removeIndex)
            }
        }
    }

    private fun handleOnTaskAdded(dataSnapshot: DataSnapshot) {
        val addedTask = dataSnapshot.getValue(Task::class.java)
        if (!taskList.contains(addedTask)) {
            taskList.add(addedTask)
            taskAdapter.notifyItemInserted(taskAdapter.itemCount.minus(1))
        }
    }

    private fun handleOnTaskUpdate(changedTask: Task?) {
        taskList.forEachIndexed { index, (id) ->
            if (id == changedTask?.id) {
                taskList[index] = changedTask
                taskAdapter.notifyItemChanged(index)
            }
        }
    }

    override fun taskUpdate(task: Task) {
        tasksRef.child(task.id).setValue(task)
    }


    private fun showAddTaskDialog() {
        val fm = supportFragmentManager
        val fragment = AddTaskDialog.Companion.getInstance(this)
        fragment.show(fm, "dialog")
    }

    override fun taskAdded(task: Task) {
        rootRef.getReference("users/${user?.uid}/${task.id}")
                .setValue(task)
    }

    val childListener: com.google.firebase.database.ChildEventListener = object : com.google.firebase.database.ChildEventListener {
        override fun onCancelled(dataSnapshot: DatabaseError?) {
            e("onCancelled", dataSnapshot.toString())
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot?, p1: String?) {
            e("onChildMoved", dataSnapshot.toString())
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot?, p1: String?) {
            val changedTask = dataSnapshot?.getValue(Task::class.java)
            handleOnTaskUpdate(changedTask)
        }

        override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
            if (dataSnapshot != null) {
                handleOnTaskAdded(dataSnapshot)
            }
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
            if (dataSnapshot != null) {
                handleOnTaskDelted(dataSnapshot)
            }
        }
    }
}

class TaskAdapter(val taskList: List<Task>, val listener: OnTaskUpdateListener) : RecyclerView.Adapter<TaskAdapter.TaskVH>() {

    override fun onBindViewHolder(holder: TaskAdapter.TaskVH?, position: Int) {
        val task = taskList[position]
        holder!!.checkbox.isChecked = task.taskDone
        holder.checkbox.text = task.description
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            task.taskDone = isChecked
            listener.taskUpdate(task)
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TaskAdapter.TaskVH {
        val view = parent!!.inflate(R.layout.item_task)
        return TaskAdapter.TaskVH(view)
    }

    class TaskVH(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val checkbox = itemView!!.findViewById(R.id.task) as CheckBox
    }

}
