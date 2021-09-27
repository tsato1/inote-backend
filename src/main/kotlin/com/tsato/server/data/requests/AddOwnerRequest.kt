package com.tsato.server.data.requests

data class AddOwnerRequest(
    val noteId: String,
    val owner: String
)
