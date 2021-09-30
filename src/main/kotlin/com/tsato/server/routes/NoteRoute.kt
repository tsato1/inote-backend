package com.tsato.server.routes

import com.tsato.server.data.*
import com.tsato.server.data.collections.Note
import com.tsato.server.data.requests.AddOwnerRequest
import com.tsato.server.data.requests.DeleteNoteRequeset
import com.tsato.server.data.responses.SimpleResponse
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.ContentTransformationException
import io.ktor.html.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.*

fun Route.noteRoute() {
    route("/getNotes") {
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name // null check is done in authenticate block
                val notes = getNotesForUser(email)

                call.respond(OK, notes)
            }
        }
    }

    route("/addNote") {
        authenticate {
            post {
                val note = try {
                    call.receive<Note>()
                }
                catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }

                if (saveNote(note)) {
                    call.respond(OK)
                }
                else {
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/deleteNote") {
        authenticate {
            post {
                // principal is the object the user authenticated with
                val email = call.principal<UserIdPrincipal>()!!.name
                val request = try {
                    call.receive<DeleteNoteRequeset>()
                }
                catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }

                if (deleteNote(email, request.noteId)) {
                    call.respond(OK)
                }
                else {
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/addOwnerToNote") {
        authenticate {
            post {
                val request = try {
                    call.receive<AddOwnerRequest>()
                }
                catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }

                if (!userExists(request.owner)) {
                    call.respond(OK, SimpleResponse(false, "No user with this email exists"))
                    return@post
                }

                if (isOwnerOfNote(request.noteId, request.owner)) {
                    call.respond(OK, SimpleResponse(false, "This user is already an owner of this note"))
                    return@post
                }

                if (addOwnerToNote(request.noteId, request.owner)) {
                    call.respond(OK, SimpleResponse(true, "${request.owner} can now see this note"))
                }
                else {
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/notes") { // returns all notes from the server
        authenticate {
            get {
                val allNotes = getAllNotes()
                call.respondHtml {
                    head {
                        styleLink("/static/css/styles.css")
                    }
                    body {
                        h1 {
                            +"All notes"
                        }
                        for (note in allNotes) {
                            h3 {
                                +"${note.title} (belongs to ${note.owners}):"
                            }
                            p {
                                +note.content
                            }
                            br
                        }
                    }
                }
            }
        }
    }
}