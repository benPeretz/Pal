package com.example.pal.ui.signin

import androidx.lifecycle.*
import com.example.pal.data.models.User
import com.example.pal.data.repository.AuthRepository
import il.co.syntax.myapplication.util.Resource
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository): ViewModel() {

    // the user status
    private val _userSignInStatus = MutableLiveData<Resource<User>>()

    // expose access to the user status
    val userSignInStatus: LiveData<Resource<User>> = _userSignInStatus

    // the current user if someone is logged in
    private val _currentUser = MutableLiveData<Resource<User>>()

    // expose access to the user
    val currentUser: LiveData<Resource<User>> = _currentUser


    init {
        viewModelScope.launch {
            _currentUser.postValue(Resource.Loading()) // started to load the user
            _currentUser.postValue(repository.currentUser())
        }
    }

    fun signInUser(userEmail: String, userPass: String){
        if(userEmail.isEmpty() || userPass.isEmpty())
            _userSignInStatus.postValue(Resource.Error("Empty email or password\n please try again"))

        else{
            _userSignInStatus.postValue(Resource.Loading()) // started to load the user
            viewModelScope.launch {
                val loginResult = repository.login(userEmail, userPass)
                _userSignInStatus.postValue(loginResult)
            }
        }
    }

    fun signOut() {
        repository.logout()
    }

    // factory method
    @Suppress("UNCHECKED_CAST")
    class LoginViewModelFactory(private val repo:AuthRepository) : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(repo) as T
        }
    }


}