package org.liamjd.bascule.lib

import java.io.File
import java.io.InputStream

interface FileHandler {

    val pathSeparator: String

    fun createDirectories(path: String): Boolean

    fun createDirectories(path: File): Boolean

    fun createDirectory(parentPath: String, folderName: String) : File

    fun createDirectories(parentPath: String, folderName: String): File

    fun getFileStream(folder: File, fileName: String): InputStream

    fun writeFile(destination: File, finalFileName: String, data: String)

    fun copyFileFromResources(fileName: String, destination: File, destFileName: String? = null, sourceDir: String = "")

    fun readFileFromResources(sourceDir: String, fileName: String): String

    fun emptyFolder(folder: File, fileType: String)

    fun emptyFolder(folder: File)
}
