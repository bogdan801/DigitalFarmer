package com.bogdan801.digitalfarmer.presentation.screens.main.fields

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.digitalfarmer.data.login.AuthUIClient
import com.bogdan801.digitalfarmer.data.remote_db.ActionResult
import com.bogdan801.digitalfarmer.domain.model.Field
import com.bogdan801.digitalfarmer.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FieldsViewModel
@Inject
constructor(
    private val repository: Repository,
    val authUIClient: AuthUIClient,
    application: Application
): AndroidViewModel(application) {
    //context
    private val context
        get() = getApplication<Application>()

    private val _screenState = MutableStateFlow(FieldsScreenState())
    val screenState = _screenState.asStateFlow()

    private val selectedCardsIDs get() = _screenState.value.cardSelectionState.entries.toList().filter { it.value }.map { it.key }

    val isAnyCardSelected get() = _screenState.value.cardSelectionState.values.contains(true)

    fun flipCardSelectionState(id: String) {
        val newValue = !(_screenState.value.cardSelectionState[id] ?: false)
        _screenState.update {
            it.copy(cardSelectionState = _screenState.value.cardSelectionState.toMutableMap().apply { set(id, newValue) })
        }
    }

    fun unselectAllCards(){
        if(isAnyCardSelected){
            _screenState.update {
                it.copy(cardSelectionState = mapOf())
            }
        }
    }

    fun selectSortMethod(method: SortMethod){
        _screenState.update {
            it.copy(currentSortMethod = method)
        }
    }

    fun flipShowSortingOptions(shouldShow: Boolean? = null){
        _screenState.update {
            it.copy(shouldShowSortingOptions = shouldShow ?: !_screenState.value.shouldShowSortingOptions)
        }
    }

    private fun updateListOfFields(newList: List<Field>){
        _screenState.update {
            it.copy(listOfFields = newList)
        }
    }

    private fun collapseAllLoadedCards(){
        _screenState.value.cardExpansionState.forEach { (identifier, _) ->
            val isCardLoading = _screenState.value.loadingCards[identifier] ?: false
            if(!isCardLoading) updateCardExpansionState(identifier, false)
        }
    }

    fun updateCardExpansionState(id: String, isExpanded: Boolean){
        _screenState.update {
            it.copy(
                cardExpansionState = _screenState.value.cardExpansionState.toMutableMap().apply { set(id, isExpanded) }
            )
        }
    }

    fun flipCardExpandState(id: String){
        if(_screenState.value.cardExpansionState[id] == true){
            val isCardLoading = _screenState.value.loadingCards[id] ?: false
            if(!isCardLoading) updateCardExpansionState(id, false)
        }
        else {
            collapseAllLoadedCards()
            updateCardExpansionState(id, true)
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
                collapseAllLoadedCards()
                updateCardExpansionState(result.data!!.id, true)
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

    fun deleteField(id: String){
        context.deleteFile(id)
        when(val result = repository.deleteField(id)){
            is ActionResult.Success -> {}
            is ActionResult.Error -> Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
        }
    }

    fun deleteSelectedFields() {
        val selectedIDs = selectedCardsIDs
        selectedIDs.forEach { id ->
            deleteField(id)
        }
        unselectAllCards()
    }


    fun setBackPressTimer() {
        viewModelScope.launch {
            _screenState.update {
                it.copy(
                    backExitFlag = true
                )
            }
            delay(2000)
            _screenState.update {
                it.copy(
                    backExitFlag = false
                )
            }
        }
    }


    init {
        repository.addFieldsListener { result ->
            when(result){
                is ActionResult.Success -> {
                    val list = result.data!!
                    list.forEach { field ->
                        if(!File(context.filesDir, field.id).exists()) updateCardExpansionState(field.id, true)
                    }
                    updateListOfFields(list)
                }
                is ActionResult.Error -> {
                    Toast.makeText(context, "${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}