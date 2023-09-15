package com.bogdan801.digitalfarmer.presentation.screens.main.fields

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.digitalfarmer.data.login.AuthUIClient
import com.bogdan801.digitalfarmer.domain.util.ActionResult
import com.bogdan801.digitalfarmer.domain.model.Field
import com.bogdan801.digitalfarmer.domain.model.SortMethod
import com.bogdan801.digitalfarmer.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

    //screen state
    private val _screenState = MutableStateFlow(FieldsScreenState())
    val screenState = _screenState.asStateFlow()

    //cards selection
    private val selectedCardsIDs get() =
        _screenState.value.cardSelectionState.entries.toList().filter { it.value }.map { it.key }

    val isAnyCardSelected get() = _screenState.value.cardSelectionState.values.contains(true)

    fun flipCardSelectionState(id: String) {
        val newValue = !(_screenState.value.cardSelectionState[id] ?: false)
        collapseAllLoadedCards()
        _screenState.update {
            it.copy(
                cardSelectionState = _screenState.value.cardSelectionState
                                        .toMutableMap()
                                        .apply { set(id, newValue) })
        }
    }

    fun unselectAllCards(){
        if(isAnyCardSelected){
            _screenState.update {
                it.copy(cardSelectionState = mapOf())
            }
        }
    }

    //cards sorting
    fun selectSortMethod(method: SortMethod, doNotSort: Boolean = false){
        viewModelScope.launch {
            repository.setSortingMethodSetting(method)
        }
        if(doNotSort){
            _screenState.update {
                it.copy(
                    currentSortMethod = method
                )
            }
        }
        else{
            _screenState.update {
                it.copy(
                    currentSortMethod = method,
                    listOfFields = sortFieldsList(_screenState.value.listOfFields, method)
                )
            }
        }
    }

    private fun sortFieldsList(list: List<Field>, sortMethod: SortMethod): List<Field> {
        val listToSort = list.toMutableList()
        when(sortMethod){
            SortMethod.Name -> {
                listToSort.sortBy { it.name }
            }
            SortMethod.Area -> {
                listToSort.sortByDescending { it.shape.area }
            }
            SortMethod.Crop -> {
                listToSort.sortBy {
                    if(it.plantedCrop != null)
                        context.getString(it.plantedCrop.localizedNameID)
                    else null
                }
            }
            SortMethod.PlantingDate -> {
                listToSort.sortBy { it.plantDate }
            }
            SortMethod.HarvestDate -> {
                listToSort.sortBy { it.harvestDate }
            }
        }
        return listToSort
    }

    fun flipShowSortingOptions(shouldShow: Boolean? = null){
        _screenState.update {
            it.copy(showSortingOptions = shouldShow ?: !_screenState.value.showSortingOptions)
        }
    }

    //floating action button
    fun setFABState(isVisible: Boolean){
        _screenState.update {
            it.copy(showFAB = isVisible)
        }
    }

    //dialog box logic
    fun setDeleteAllDialogBoxState(isVisible: Boolean){
        _screenState.update {
            it.copy(showDeleteDialog = isVisible)
        }
    }

    //cards expansion and loading state logic
    private fun collapseAllLoadedCards(){
        _screenState.value.cardExpansionState.forEach { (identifier, _) ->
            val isCardLoading = _screenState.value.loadingCards[identifier] ?: false
            if(!isCardLoading) updateCardExpansionState(identifier, false)
        }
    }

    fun updateCardExpansionState(id: String, isExpanded: Boolean){
        _screenState.update {
            it.copy(
                cardExpansionState = _screenState.value.cardExpansionState
                                         .toMutableMap()
                                         .apply { set(id, isExpanded) }
            )
        }
    }

    fun flipCardExpandState(id: String){
        if(_screenState.value.cardExpansionState[id] == true){
            val isCardLoading = _screenState.value.loadingCards[id] ?: false
            if(!isCardLoading) {
                setFABState(true)
                updateCardExpansionState(id, false)
            }
        }
        else {
            collapseAllLoadedCards()
            updateCardExpansionState(id, true)
        }
    }

    fun setCardLoadingStatus(id: String, status: Boolean){
        _screenState.update {
            it.copy(
                loadingCards = _screenState.value.loadingCards
                                   .toMutableMap()
                                   .apply { set(id, status) }
            )
        }
    }

    //field control
    private fun updateListOfFields(newList: List<Field>){
        _screenState.update {
            it.copy(listOfFields = newList)
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

    private fun deleteField(id: String){
        context.deleteFile(id)
        when(val result = repository.deleteField(id)){
            is ActionResult.Success -> {}
            is ActionResult.Error -> Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
        }
    }

    fun deleteAllFields(){
        _screenState.value.listOfFields.forEach { field ->
            deleteField(field.id)
        }
        unselectAllCards()
        setFABState(true)
    }

    fun deleteSelectedFields() {
        val selectedIDs = selectedCardsIDs
        selectedIDs.forEach { id ->
            deleteField(id)
        }
        unselectAllCards()
    }

    //back press logic
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
        runBlocking {
            selectSortMethod(
                repository.getSortingMethodSetting() ?: SortMethod.Name,
                doNotSort = true
            )
        }
        repository.addFieldsListener { result ->
            when(result){
                is ActionResult.Success -> {
                    val list = sortFieldsList(result.data!!, _screenState.value.currentSortMethod)

                    list.forEach { field ->
                        if(!File(context.filesDir, field.id).exists())
                            updateCardExpansionState(field.id, true)
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