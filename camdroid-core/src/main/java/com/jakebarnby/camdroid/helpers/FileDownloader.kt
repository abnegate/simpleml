package com.jakebarnby.camdroid.helpers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import okhttp3.*
import okio.Okio
import java.io.File
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class FileDownloader : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val job: Job = Job()
    private val client: OkHttpClient by lazy { OkHttpClient() }

    @Throws(IOException::class)
    fun fetchAsync(
        url: String,
        destination: String,
        filename: String,
        onProgress: (Int) -> Unit
    ) = async {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                throw e
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body() ?: throw IllegalStateException("No response body")
                val contentLength = body.contentLength()
                val source = body.source()

                val downloadedFile = File(destination, filename)
                if (downloadedFile.exists()) {
                    return
                }

                val sink = Okio.buffer(Okio.sink(downloadedFile))
                val sinkBuffer = sink.buffer()

                var totalBytesRead: Long = 0
                val bufferSize: Long = 8 * 1024
                var bytesRead: Long = source.read(sinkBuffer, bufferSize)

                while (bytesRead > 0) {
                    sink.emit()
                    totalBytesRead += bytesRead
                    val progress = (totalBytesRead * 100 / contentLength).toInt()
                    onProgress(progress)

                    bytesRead = source.read(sinkBuffer, bufferSize)
                }
                sink.flush()
                sink.close()
                source.close()
            }
        })
    }
}