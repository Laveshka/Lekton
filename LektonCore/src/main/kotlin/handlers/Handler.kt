package handlers

import commands.Request

interface Handler<T, R : Request<T>> {
    operator fun invoke(request: R): T
}