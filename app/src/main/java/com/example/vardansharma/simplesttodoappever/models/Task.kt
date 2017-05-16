package com.example.vardansharma.simplesttodoappever.models

/**
 * Created by vardansharma on 14/05/17.
 */
data class Task(val id: String = System.currentTimeMillis().toString(), val createdAt: String = System.currentTimeMillis().toString(),
                val description: String="", var taskDone: Boolean = false)