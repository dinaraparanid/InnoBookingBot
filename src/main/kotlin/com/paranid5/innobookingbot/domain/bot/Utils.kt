package com.paranid5.innobookingbot.domain.bot

import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId

inline val CommandHandlerEnvironment.chatId
    get() = ChatId.fromId(message.chat.id)

inline val TextHandlerEnvironment.chatId
    get() = ChatId.fromId(message.chat.id)

inline val CommandHandlerEnvironment.telegramId
    get() = message.from!!.id

inline val TextHandlerEnvironment.telegramId
    get() = message.from!!.id