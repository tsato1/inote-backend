package com.tsato.server.data

import com.tsato.server.data.collections.Note
import com.tsato.server.data.collections.User
import com.tsato.server.security.checkHashForPassword
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.setValue

private val client = KMongo.createClient().coroutine // how we want to access our db for operations (coroutine chosen)

private val database = client.getDatabase("NotesDatabase")

private val users = database.getCollection<User>()
private val notes = database.getCollection<Note>()

// by having this function in a file (not a class), we can access this function from everywhere within this project
suspend fun registerUser(user: User): Boolean {
    return users.insertOne(user).wasAcknowledged()
}

suspend fun userExists(email: String): Boolean {
//    return users.findOne("{email: $email}") != null // expression not suitable
    return users.findOne(User::email eq email) != null
}

suspend fun isPasswordValid(email: String, passwordToCheck: String): Boolean {
    val actualPassword = users.findOne(User::email eq email)?.password ?: return false // saved in database
    return checkHashForPassword(passwordToCheck, actualPassword)
}

suspend fun getNotesForUser(email: String): List<Note> {
    return notes.find(Note::owners contains email).toList()
}

suspend fun saveNote(note: Note): Boolean {
    val noteExists = notes.findOneById(note.id) != null

    return if (noteExists) {
        notes.updateOneById(note.id, note).wasAcknowledged()
    }
    else {
        notes.insertOne(note).wasAcknowledged()
    }
}

suspend fun deleteNote(email: String, id: String): Boolean {
    val note = notes.findOne(Note::id eq id, Note::owners contains email)

    note?.let {
        if (it.owners.size > 1) {
            // the note has multiple owners, so we just delete this email from the owners list
            val newOwners = it.owners - email
            val updateResult = notes.updateOne(Note::id eq it.id, setValue(Note::owners, newOwners))

            return updateResult.wasAcknowledged()
        }

        return notes.deleteOneById(it.id).wasAcknowledged()
    } ?: return false
}

/*
 add the email(owner) to the note which has this id
 */
suspend fun addOwnerToNote(id: String, owner: String): Boolean {
    val owners = notes.findOneById(id)?.owners ?: return false

    return notes.updateOneById(id, setValue(Note::owners, owners + owner)).wasAcknowledged()
}

suspend fun isOwnerOfNote(id: String, owner: String): Boolean {
    val note = notes.findOneById(id) ?: return false

    return owner in note.owners
}