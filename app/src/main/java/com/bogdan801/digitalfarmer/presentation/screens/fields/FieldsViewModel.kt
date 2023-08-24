package com.bogdan801.digitalfarmer.presentation.screens.fields

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.digitalfarmer.data.login.AuthUIClient
import com.bogdan801.digitalfarmer.data.remote_db.ActionResult
import com.bogdan801.digitalfarmer.domain.model.Field
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

    private fun updateListOfFields(newList: List<Field>){
        _screenState.update {
            it.copy(listOfFields = newList)
        }
    }

    fun updateCardState(id: String, isExpanded: Boolean){
        _screenState.update {
            it.copy(
                cardState = _screenState.value.cardState.toMutableMap().apply { set(id, isExpanded) }
            )
        }
    }

    private fun collapseAllCards(){
        _screenState.value.cardState.forEach { (identifier, _) ->
            val isCardLoading = _screenState.value.loadingCards[identifier] ?: false
            if(!isCardLoading) updateCardState(identifier, false)
        }
    }

    fun flipCardState(id: String){
        if(_screenState.value.cardState[id] == true){
            val isCardLoading = _screenState.value.loadingCards[id] ?: false
            if(!isCardLoading) updateCardState(id, false)
        }
        else {
            collapseAllCards()
            updateCardState(id, true)
        }
    }

    fun setCardLoadingStatus(id: String, status: Boolean){
        _screenState.update {
            it.copy(
                loadingCards = _screenState.value.loadingCards.toMutableMap().apply { set(id, status) }
            )
        }
    }

    fun addField(field: Field){
        when(val result = repository.addField(field)){
            is ActionResult.Success -> {
                collapseAllCards()
                updateCardState(result.data!!.id, true)
            }
            is ActionResult.Error -> Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
        }
    }

    fun updateField(newField: Field){
        context.deleteFile(newField.id)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.editField(newField)
            }
            when(result){
                is ActionResult.Success -> Toast.makeText(context, "Все ок", Toast.LENGTH_LONG).show()
                is ActionResult.Error -> Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun deleteField(id: Int){
        context.deleteFile(_screenState.value.listOfFields[id].id)
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

    fun deleteField(field: Field){
        context.deleteFile(field.id)
        when(val result = repository.deleteField(field)){
            is ActionResult.Success -> Toast.makeText(context, "Все ок", Toast.LENGTH_LONG).show()
            is ActionResult.Error -> Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
        }
    }


    init {
        repository.addFieldsListener { result ->
            when(result){
                is ActionResult.Success -> {
                    val list = result.data
                    updateListOfFields(list!!)
                }
                is ActionResult.Error -> {
                    Toast.makeText(context, "${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}