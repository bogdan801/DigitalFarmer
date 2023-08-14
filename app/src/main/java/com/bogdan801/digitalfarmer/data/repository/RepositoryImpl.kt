package com.bogdan801.digitalfarmer.data.repository

import com.bogdan801.digitalfarmer.data.login.AuthUIClient
import com.bogdan801.digitalfarmer.data.remote_db.ActionResult
import com.bogdan801.digitalfarmer.data.remote_db.FieldDTO
import com.bogdan801.digitalfarmer.data.remote_db.toFieldDTO
import com.bogdan801.digitalfarmer.domain.model.Field
import com.bogdan801.digitalfarmer.domain.repository.Repository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

class RepositoryImpl(
    private val databaseReference: DatabaseReference,
    private val authUIClient: AuthUIClient
) : Repository {
    override fun addField(field: Field): ActionResult<Field> {
        val user = authUIClient.getSignedInUser()
        return if(user!=null){
            try {
                val childReference = databaseReference.child(user.userID).child("fields").push()
                childReference.setValue(field.copy(id = childReference.key!!).toFieldDTO())

                ActionResult.Success(null)
            } catch (e: Exception){
                e.printStackTrace()
                ActionResult.Error(e.message.toString())
            }
        } else{
            ActionResult.Error("User is not logged in!!!")
        }
    }

    override suspend fun editField(newField: Field): ActionResult<Field> {
        val user = authUIClient.getSignedInUser()
        return if(user!=null){
            try {
                databaseReference
                    .child(user.userID)
                        .child("fields")
                            .child(newField.id).setValue(newField.toFieldDTO())

                ActionResult.Success(null)
            } catch (e: Exception){
                e.printStackTrace()
                ActionResult.Error(e.message.toString())
            }
        } else{
            ActionResult.Error("User is not logged in!!!")
        }
    }

    override suspend fun deleteField(index: Int): ActionResult<Field> {
        val user = authUIClient.getSignedInUser()
        return if(user!=null){
            try {
                val result = databaseReference.child(user.userID).child("fields").get().await()
                result.children.toList()[index].ref.removeValue()
                ActionResult.Success(null)
            } catch (e: Exception){
                e.printStackTrace()
                ActionResult.Error(e.message.toString())
            }
        } else{
            ActionResult.Error("User is not logged in!!!")
        }
    }

    override fun deleteField(field: Field): ActionResult<Field> {
        val user = authUIClient.getSignedInUser()
        return if(user!=null){
            try {
                databaseReference
                    .child(user.userID)
                        .child("fields")
                            .child(field.id).removeValue()

                ActionResult.Success(null)
            } catch (e: Exception){
                e.printStackTrace()
                ActionResult.Error(e.message.toString())
            }
        } else{
            ActionResult.Error("User is not logged in!!!")
        }
    }

    override fun addFieldsListener(listener: (ActionResult<List<Field>>) -> Unit) {
        val user = authUIClient.getSignedInUser()
        if(user!=null){
            val valuesListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val listOfFields = mutableListOf<Field>()
                    dataSnapshot.children.forEach{ field ->
                        field.getValue(FieldDTO::class.java)?.let { listOfFields.add(it.toField()) }
                    }
                    listener(ActionResult.Success(data = listOfFields))
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    listener(ActionResult.Error(databaseError.toException().message.toString()))
                    databaseError.toException().printStackTrace()
                }
            }
            databaseReference.child(user.userID).child("fields").addValueEventListener(valuesListener)
        }
        else {
            listener(ActionResult.Error("User is not logged in!!!"))
        }
    }
}