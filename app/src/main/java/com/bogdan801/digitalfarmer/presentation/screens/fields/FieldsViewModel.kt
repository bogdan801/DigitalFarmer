package com.bogdan801.digitalfarmer.presentation.screens.fields

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.digitalfarmer.data.login.AuthUIClient
import com.bogdan801.digitalfarmer.data.remote_db.ActionResult
import com.bogdan801.digitalfarmer.domain.model.Field
import com.bogdan801.digitalfarmer.domain.model.Shape
import com.bogdan801.digitalfarmer.domain.repository.Repository
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FieldsViewModel
@Inject
constructor(
    private val repository: Repository,
    val authUIClient: AuthUIClient,
    val databaseReference: DatabaseReference,
    application: Application
): AndroidViewModel(application) {
    //context
    private val context
        get() = getApplication<Application>()

    private val _screenState = MutableStateFlow(FieldsScreenState())
    val screenState = _screenState.asStateFlow()

    fun updateShape(newShape: Shape){
        _screenState.update {
            it.copy(shape = newShape)
        }
    }

    fun addField(field: Field){
        when(val result = repository.addField(field)){
            is ActionResult.Success -> Toast.makeText(context, "Все ок", Toast.LENGTH_LONG).show()
            is ActionResult.Error -> Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
        }
    }

    fun updateField(newField: Field, id: Int){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.editField(newField, id)
            }
            when(result){
                is ActionResult.Success -> Toast.makeText(context, "Все ок", Toast.LENGTH_LONG).show()
                is ActionResult.Error -> Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun deleteField(id: Int){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.deleteField(id)
            }
            when(result){
                is ActionResult.Success -> Toast.makeText(context, "Все ок", Toast.LENGTH_LONG).show()
                is ActionResult.Error -> Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
            }
        }
    }


    init {
        /*val user = authUIClient.getSignedInUser()
        if(user != null){
            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val newName = dataSnapshot
                        .child("nickname").value.toString()
                    updateDisplayName(newName)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(context, "${databaseError.toException().message}", Toast.LENGTH_LONG).show()
                    databaseError.toException().printStackTrace()
                }
            }
            databaseReference.child(user.userID).addValueEventListener(listener)
        }*/
        repository.addFieldsListener { result ->
            when(result){
                is ActionResult.Success -> {
                    val list = result.data
                    val shape = list!![0].shape
                    updateShape(shape)
                }
                is ActionResult.Error -> {
                    Toast.makeText(context, "${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}