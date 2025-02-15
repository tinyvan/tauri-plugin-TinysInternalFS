package com.plugin.tinysinternalfs

import android.util.Log
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.nio.channels.FileChannel
import java.nio.channels.FileLock
import java.nio.file.StandardOpenOption
import kotlin.text.charset

class FileHandle(private val file: File, private val mode: String, val encoding: String = "utf-8") : AutoCloseable {

    private var fileChannel: FileChannel? = null
    private var fileLock: FileLock? = null
    private var writer: BufferedWriter? = null
    private var reader: BufferedReader? = null
    private var isOpen = false

    init {
        require(mode in listOf("r", "w", "rw", "a")) {
            "Invalid file mode: $mode. Mode must be one of 'r', 'w', 'rw', or 'a'."
        }

        try {
            fileChannel = FileChannel.open(file.toPath(), getOpenOptions(mode))
            if(mode=="w") {
                fileLock = fileChannel?.lock()
            }
            isOpen = true

        } catch (e: IOException) {
            fileLock?.release() 
            fileChannel?.close() 
            throw IOException("Failed to open or lock file: ${file.path} in mode '$mode'", e)
        }
    }
    fun mode():String{
        return mode
    }
    private fun getOpenOptions(mode: String): Set<StandardOpenOption> {
        return when (mode) {
            "r" -> setOf(StandardOpenOption.READ)
            "w" -> setOf(StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
            "rw" -> setOf(StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE)
            "a" -> setOf(StandardOpenOption.APPEND, StandardOpenOption.CREATE)
            else -> throw IllegalArgumentException("Invalid mode: $mode") 
        }
    }

    fun readAll(): String {
        checkMode("r", "rw", action = "read")
        checkFileOpen()
        try {
            if (reader == null) {
                reader = file.bufferedReader(charset(encoding))
            }
            return reader!!.readText() 
        } catch (e: IOException) {
            throw IOException("Failed to read file: ${file.path}", e)
        }
    }

    fun write(content: String) {
        checkMode("w", "rw", action = "write")
        checkFileOpen()
        try {
            if (writer == null) {
                writer = file.bufferedWriter(charset(encoding))
            }
            writer!!.write(content) 
            writer!!.flush() 
        } catch (e: IOException) {
            throw IOException("Failed to write to file: ${file.path}", e)
        }
    }

    fun append(content: String) {
        checkMode("a", "rw", action = "append")
        checkFileOpen()
        try {
            if (writer == null) {
                writer = BufferedWriter(OutputStreamWriter(FileOutputStream(file, true), charset(encoding)))
            }
            writer!!.append(content) 
            writer!!.flush() 
        } catch (e: IOException) {
            throw IOException("Failed to append to file: ${file.path}", e)
        }
    }

    override fun close() {
        Log.d("tinywang","close()")
        if (isOpen) {
            try {
                Log.d("tinywang","close() writer?.close()")
                writer?.close() 
                reader?.close() 
            } finally {
                try {
                    Log.d("tinywang","close() fileLock?.release()")
                    fileLock?.release() 
                    Log.d("tinywang",(fileLock==null).toString())
                } finally {
                    Log.d("tinywang","close() finally")
                    fileChannel?.close() 
                    isOpen = false
                }
            }
        }
    }

    private fun checkMode(vararg allowedModes: String, action: String) {
        if (mode !in allowedModes) {
            throw IllegalStateException("File opened in mode '$mode', which is not valid for $action operation. Allowed modes for $action are ${allowedModes.joinToString(", ")}.")
        }
    }

    private fun checkFileOpen() {
        if (!isOpen) {
            throw IllegalStateException("File handle is closed.")
        }
    }
}
