package com.plugin.tinysinternalfs

import android.app.Activity
import android.util.Log
import app.tauri.annotation.Command
import app.tauri.annotation.InvokeArg
import app.tauri.annotation.TauriPlugin
import app.tauri.plugin.JSObject
import app.tauri.plugin.Plugin
import app.tauri.plugin.Invoke

@InvokeArg
internal class ReadFileArgs {
    lateinit var path: String
}
@InvokeArg
internal class WriteFileArgs {
    lateinit var path: String
    lateinit var content: String
}
@InvokeArg
internal class OpenFileArgs {
    lateinit var path: String
    lateinit var mode: String
}
@InvokeArg
internal class CloseFileArgs {
    lateinit var path: String
}
@InvokeArg
internal class ExistsArgs {
    lateinit var path: String
}
@InvokeArg
internal class DeleteFileArgs {
    lateinit var path: String
}
@InvokeArg
internal class ReadFileAllArgs {
    lateinit var path: String
}
@TauriPlugin
class ISMPlugin(private val activity: Activity) : Plugin(activity) {
    private val ism = InternalStorageManager(activity)

    @Command
    fun readFileImmediately(invoke: Invoke) {
        val args = invoke.parseArgs(ReadFileArgs::class.java)
        val ret = JSObject()
        val result = ism.readFileImmediately(args.path)
        ret.put("success", result.success)
        ret.put("content", result.content)
        invoke.resolve(ret)
    }

    @Command
    fun writeFileImmediately(invoke: Invoke) {
        val args = invoke.parseArgs(WriteFileArgs::class.java)
        val ret = JSObject()
        val result=ism.writeFileImmediately(args.path, args.content)

        ret.put("success", result.success)
        ret.put("content", result.content)
        invoke.resolve(ret)
    }

    @Command
    fun openFile(invoke: Invoke) {
        val args = invoke.parseArgs(OpenFileArgs::class.java)
        val ret = JSObject()
        val result=ism.openFile(args.path, args.mode)
        ret.put("success", result.success)
        ret.put("content", result.content)
        invoke.resolve(ret)
    }

    @Command
    fun closeFile(invoke: Invoke) {
        val args = invoke.parseArgs(CloseFileArgs::class.java)
        val ret = JSObject()
        val result=ism.closeFile(args.path)
        ret.put("success", result.success)
        ret.put("content", result.content)
        invoke.resolve(ret)
    }

    @Command
    fun writeFile(invoke: Invoke) {
        val args = invoke.parseArgs(WriteFileArgs::class.java)
        val ret = JSObject()
        val result = ism.writeFile(args.path, args.content)
        ret.put("success", result.success)
        ret.put("content", result.content)
        invoke.resolve(ret)
    }

    @Command
    fun checkFileExists(invoke: Invoke) {
        val args = invoke.parseArgs(ExistsArgs::class.java)
        val ret = JSObject()
        val result = ism.exists(args.path)
        ret.put("success", result.success)
        ret.put("content", result.content)
        invoke.resolve(ret)
    }

    @Command
    fun deleteFile(invoke: Invoke) {
        val args = invoke.parseArgs(DeleteFileArgs::class.java)
        val ret = JSObject()
        val result = ism.deleteFile(args.path)
        ret.put("success", result.success)
        ret.put("content", result.content)
        invoke.resolve(ret)
    }

    @Command
    fun readFileAll(invoke: Invoke) {
        val args = invoke.parseArgs(ReadFileAllArgs::class.java)
        val ret = JSObject()
        val result = ism.readFileAll(args.path)
        ret.put("success", result.success)
        ret.put("content", result.content)
        invoke.resolve(ret)
    }
    @Command
    fun closeFileAll(invoke: Invoke) {
        ism.closeAll()
        invoke.resolve()
    }
}