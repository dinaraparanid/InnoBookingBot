package com.paranid5.innobookingbot.data.firebase

import com.google.cloud.firestore.QueryDocumentSnapshot

internal inline val List<QueryDocumentSnapshot>.emailsToTgIds
    get() = associate { fsDoc ->
        (fsDoc["id"] as String) to (fsDoc["tg_id"] as String)
    }