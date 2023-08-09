package com.bogdan801.digitalfarmer.data.repository

import android.widget.Toast
import com.bogdan801.digitalfarmer.data.login.AuthUIClient
import com.bogdan801.digitalfarmer.data.remote_db.ActionResult
import com.bogdan801.digitalfarmer.data.remote_db.FieldDTO
import com.bogdan801.digitalfarmer.domain.model.Field
import com.bogdan801.digitalfarmer.domain.repository.Repository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class RepositoryImpl(
    private val databaseReference: DatabaseReference,
    private val authUIClient: AuthUIClient
) : Repository {
    override fun addField(field: Field): ActionResult<Field> {
        TODO("Not yet implemented")
    }

    override fun editField(field: Field, id: Int): ActionResult<Field> {
        TODO("Not yet implemented")
    }

    override fun deleteField(field: Field, id: Int): ActionResult<Field> {
        TODO("Not yet implemented")
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