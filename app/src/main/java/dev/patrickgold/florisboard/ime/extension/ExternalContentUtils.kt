/*
 * Copyright (C) 2021 Patrick Goldinger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:OptIn(ExperimentalContracts::class)

package dev.patrickgold.florisboard.ime.extension

import android.content.Context
import android.net.Uri
import java.io.BufferedReader
import java.io.BufferedWriter
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class ExternalContentUtils private constructor() {
    companion object {
        inline fun <R> readFromUri(context: Context, uri: Uri, maxSize: Int, block: (it: BufferedReader) -> R): Result<R> {
            contract {
                callsInPlace(block, InvocationKind.AT_MOST_ONCE)
            }
            return runCatching {
                val contentResolver = context.contentResolver
                    ?: throw NullPointerException("System content resolver not available")
                val inputStream = contentResolver.openInputStream(uri)
                    ?: throw NullPointerException("Cannot open input stream for given uri '$uri'")
                val assetFileDescriptor = contentResolver.openAssetFileDescriptor(uri, "r")
                    ?: throw NullPointerException("Cannot open asset file descriptor for given uri '$uri'")
                if (assetFileDescriptor.length > maxSize) {
                    throw Exception("Contents of given uri '$uri' exceeds maximum size of $maxSize bytes!")
                }
                inputStream.bufferedReader(Charsets.UTF_8).use { block(it) }
            }
        }

        fun readTextFromUri(context: Context, uri: Uri, maxSize: Int): Result<String> {
            return readFromUri(context, uri, maxSize) { it.readText() }
        }

        inline fun writeToUri(context: Context, uri: Uri, block: (it: BufferedWriter) -> Unit): Result<Unit> {
            contract {
                callsInPlace(block, InvocationKind.AT_MOST_ONCE)
            }
            return runCatching {
                val contentResolver = context.contentResolver
                    ?: throw NullPointerException("System content resolver not available")
                // Must use "rwt" mode to ensure destination file length is truncated after writing.
                val outputStream = contentResolver.openOutputStream(uri, "rwt")
                    ?: throw NullPointerException("Cannot open output stream for given uri '$uri'")
                outputStream.bufferedWriter(Charsets.UTF_8).use { block(it) }
            }
        }

        fun writeTextToUri(context: Context, uri: Uri, text: String): Result<Unit> {
            return writeToUri(context, uri) { it.write(text) }
        }
    }
}
