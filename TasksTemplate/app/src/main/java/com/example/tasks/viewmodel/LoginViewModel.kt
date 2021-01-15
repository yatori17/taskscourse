package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.local.SecurityPreferences
import com.example.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val mPersonRepository = PersonRepository(application)
    private val mSharedPreferences = SecurityPreferences(application)
    private val mPriorityRepository = PriorityRepository(application)
    /**
     * Faz login usando API
     */
    private  val mLogin = MutableLiveData<ValidationListener>()
    val login: LiveData<ValidationListener> = mLogin

    private  val mLoggedUser = MutableLiveData<Boolean>()
    val loggedUser: LiveData<Boolean> = mLoggedUser

    fun doLogin(email: String, password: String) {
        mPersonRepository.login(email, password, object : APIListener<HeaderModel>{
            override fun onSucess(model: HeaderModel) {

                mSharedPreferences.store(TaskConstants.SHARED.TOKEN_KEY, model.token)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_KEY, model.personKey)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_NAME, model.name)

                RetrofitClient.addHeader(model.token, model.personKey)

                mLogin.value = ValidationListener()
            }

            override fun onFailure(err: String) {
                mLogin.value= ValidationListener(err)
            }

        })
    }

    /**
     * Verifica se usuário está log
     * ado
     */
    fun verifyLoggedUser() {

        val token = mSharedPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val personKey = mSharedPreferences.get(TaskConstants.SHARED.PERSON_KEY)
        RetrofitClient.addHeader(token, personKey)

        val logged = (token!= "" && personKey != "")
        if(!logged){
            //mPriorityRepository.all()
        }
        mLoggedUser.value = logged


    }

}