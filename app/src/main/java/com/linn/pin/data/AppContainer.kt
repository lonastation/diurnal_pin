/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.linn.pin.data

import android.content.Context
import com.linn.pin.data.length.LengthRepository
import com.linn.pin.data.length.OfflineLengthRepository
import com.linn.pin.data.work.OfflineWorksRepository
import com.linn.pin.data.work.WorksRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val worksRepository: WorksRepository
    val lengthRepository: LengthRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineWorksRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {

    override val worksRepository: WorksRepository by lazy {
        OfflineWorksRepository(PinDatabase.getDatabase(context).workDao())
    }

    override val lengthRepository: LengthRepository by lazy {
        OfflineLengthRepository(PinDatabase.getDatabase(context).lengthDao())
    }
}
