package com.jakebarnby.simpleml.helpers

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import okhttp3.*
import okio.buffer
import okio.sink
import java.io.File
import java.io.IOException

class FileDownloader : CoroutineBase {

    override val job = Job()

    companion object {
        private val client by lazy { OkHttpClient() }
    }

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
                val body = response.body ?: throw IllegalStateException("No response body")
                val contentLength = body.contentLength()
                val source = body.source()

                val downloadedFile = File(destination, filename)
                if (downloadedFile.exists()) {
                    return
                }

                val sink = downloadedFile.sink().buffer()
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