package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.TaskRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val mPriorityRepository = PriorityRepository(application)
    private val mTaskRepository =TaskRepository(application)

    private  val mPriorityList = MutableLiveData<List<PriorityModel>>()
    val priorities: LiveData<List<PriorityModel>> = mPriorityList

    private  val mValidation = MutableLiveData<ValidationListener>()
    val validation: LiveData<ValidationListener> = mValidation

    private  val mTask = MutableLiveData<TaskModel>()
    val task: LiveData<TaskModel> = mTask

    fun listPriorities(){
        mPriorityList.value = mPriorityRepository.list()
    }

    fun save(task: TaskModel) {

        if(task.id == 0 ) {
            mTaskRepository.create(task, object : APIListener<Boolean> {
                override fun onSucess(model: Boolean) {
                    mValidation.value = ValidationListener()
                }

                override fun onFailure(err: String) {
                    mValidation.value = ValidationListener(err)
                }

            })
        } else {
            mTaskRepository.update(task, object : APIListener<Boolean> {
                override fun onSucess(model: Boolean) {
                    mValidation.value = ValidationListener()
                }

                override fun onFailure(err: String) {
                    mValidation.value = ValidationListener(err)
                }

            })
        }

    }

    fun load(taskId: Int) {
        mTaskRepository.load(taskId, object : APIListener<TaskModel>{
            override fun onSucess(model: TaskModel) {
                mTask.value = model
            }

            override fun onFailure(err: String) {
                TODO("Not yet implemented")
            }

        })

    }

}