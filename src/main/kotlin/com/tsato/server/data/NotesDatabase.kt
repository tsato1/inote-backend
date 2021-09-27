package com.tsato.server.data

import com.tsato.server.data.collections.Note
import com.tsato.server.data.collections.User
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine // how we want to access our db for operations (coroutine chosen)

private val database = client.getDatabase("NotesDatabase")

private val users = database.getCollection<User>()
private val notes = database.getCollection<Note>()

// by having this function in a file (not a class), we can access this function from everywhere within this project
suspend fun registerUser(user: User): Boolean {
    return users.insertOne(user).wasAcknowledged()
}