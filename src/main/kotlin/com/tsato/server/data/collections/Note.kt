package com.tsato.server.data.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Note(
    val title: String,
    val content: String,
    val date: Long,
    val owners: List<String>, // list of emails
    val color: String, // hex color
    @BsonId // binary representation of json
    val id: String = ObjectId().toString() // mongo db commonly has string id
)
