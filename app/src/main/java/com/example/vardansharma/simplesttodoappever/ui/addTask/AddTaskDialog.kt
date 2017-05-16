package com.example.vardansharma.simplesttodoappever.ui.addTask

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import com.example.vardansharma.simplesttodoappever.R
import com.example.vardansharma.simplesttodoappever.models.Task
import com.example.vardansharma.simplesttodoappever.ui.taskList.OnTaskAddedListener
import org.jetbrains.anko.toast


/**
 * Created by vardansharma on 14/05/17.
 */

class AddTaskDialog : android.support.v4.app.DialogFragment() {
    companion object {
        private var listener: OnTaskAddedListener? = null

        fun getInstance(listener: OnTaskAddedListener): AddTaskDialog {
            Companion.listener = listener
            return AddTaskDialog()
        }
    }

    private var edtTask: EditText? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.popup_height)
        dialog.window?.setLayout(width, height)
        return dialog
    }

    override fun onResume() {
        super.onResume()
        val params = dialog.window!!.attributes
        params.width = LinearLayout.LayoutParams.MATCH_PARENT
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = params as WindowManager.LayoutParams

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.dialog_add_task, container, false)
        edtTask = view!!.findViewById(R.id.edt_task_description) as EditText
        val btn = view.findViewById(R.id.btn_add)

        btn.setOnClickListener { onTaskAdded() }

        return view
    }


    override fun onStart() {
        super.onStart()
        val lp = WindowManager.LayoutParams()
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT

    }

    private fun onTaskAdded() {
        val taskDesc = edtTask!!.text.toString()
        if (taskDesc.length !in 3..100) {
            context.toast("Invalid task description")
            return
        }
        listener?.taskAdded(Task(description = taskDesc))
        this.dismiss()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
