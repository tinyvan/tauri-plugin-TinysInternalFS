package com.plugin.tinysinternalfs

import android.content.Context
import android.util.Log
import java.io.File

data class Result(val success: Boolean, val content: String)
class InternalStorageManager (private val context: Context){
    private var fileHandlers:MutableMap<String,FileHandle> = mutableMapOf()
    fun readFileImmediately(filePath: String):Result {
        try{
            val file = File(context.filesDir, filePath)
            if (!file.exists()) {
                return Result(false,"File not found")
            }
            return Result(true,file.readText(charset = Charsets.UTF_8))
        }catch (e: Exception){
            return Result(false,e.stackTraceToString())
        }
    }
    fun writeFileImmediately(filePath: String, content: String):Result {
        try{
            val file = File(context.filesDir, filePath)
            file.writeText(content,charset = Charsets.UTF_8)
            return Result(true,"")
        }catch (e: Exception){
            return Result(false,e.stackTraceToString())
        }
    }
    fun openFile(filePath: String, mode: String):Result {
        try {

            val file = File(context.filesDir, filePath)
            val absolutePath=file.absolutePath
            if (fileHandlers.containsKey(absolutePath)) {
                return Result(false,"File already open in mode ${fileHandlers[absolutePath]!!.mode()}")
            }
            val fileHandle = FileHandle(file, mode)

            fileHandlers[absolutePath] = fileHandle
            return Result(true, absolutePath)
        }catch (e: Exception){
            return Result(false,e.stackTraceToString())
        }
    }
    fun closeFile(filePath: String):Result {
        try {
            val file = fileHandlers[filePath]
            file?.close()
            fileHandlers.remove(filePath)
            return Result(true, "")
        }catch (e: Exception){
            e.printStackTrace()
            return Result(false,e.stackTraceToString())
        }
    }
    fun writeFile(filePath: String, content: String):Result {
        try {
            val file = fileHandlers[filePath]
            file!!.write(content)
            return Result(true, "")
        }catch (e: Exception){
            return Result(false,e.stackTraceToString())
        }
    }
    fun exists(filePath: String):Result {
        return try {
            val file = File(context.filesDir, filePath)
            if (!file.exists()) {
                Result(false,"")
            }else{
                Result(true,"")
            }
        }catch (e: Exception){
            e.printStackTrace()
            Result(false,e.message.toString())
        }
    }
//    fun getFolderFiles(filePath: String):Result {
//        try {
//            val file = File(context.filesDir, filePath)
//            val absolutePath=file.absolutePath
//            if (fileHandlers.containsKey(absolutePath)) {
//                return Result(false,"File already open in mode ${fileHandlers[absolutePath]!!.mode()}")
//            }
//            val files = file.listFiles()
//
//        }
//    }
    fun deleteFile(filePath: String):Result {
        try {
            val file = File(context.filesDir, filePath)
            val absolutePath=file.absolutePath
            if (fileHandlers.containsKey(absolutePath)) {
                return Result(false,"File already open in mode ${fileHandlers[absolutePath]!!.mode()}")
            }
            file.delete()
            return Result(true, "")
        }catch (e: Exception){
            return Result(false,e.stackTraceToString())
        }
    }
    fun readFileAll(filePath: String):Result {
        try {
            val fileHandle=fileHandlers[filePath]
            return Result(true,fileHandle!!.readAll())
        }catch (e: Exception){
            e.printStackTrace()
            return Result(false,e.message.toString())
        }
    }
    fun closeAll()
    {
        fileHandlers.forEach {
            it.value.close()
        }
        fileHandlers.clear()
    }
    fun getFilesDir():String{
        return context.filesDir.absolutePath
    }
}