package application

import java.io.*
import java.net.URISyntaxException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.*
import java.util.function.Consumer

class FileResourceUtilK {
    //    public static void main(String[] args) throws IOException, URISyntaxException {
    //
    //        FileResourcesUtils app = new FileResourcesUtils();
    //
    //        //String fileName = "database.properties";
    //        String fileName = "json/file1.json";
    //
    //        System.out.println("getResourceAsStream : " + fileName);
    //        InputStream is = app.getFileFromResourceAsStream(fileName);
    //        printInputStream(is);
    //
    //        System.out.println("\ngetResource : " + fileName);
    //        File file = app.getFileFromResource(fileName);
    //        printFile(file);
    //
    //    }
    @Throws(URISyntaxException::class)
    fun getAsStringFromFileName(fileName: String): String? {
        val file = getFileFromResource(fileName)
        val result = StringBuilder()
        val lines: List<String>
        try {
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8)
            for (line in lines) {
                result.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result.toString()
    }


    @Throws(URISyntaxException::class)
    fun getAsStringFromFile(file: File): String? {
        val result = StringBuilder()
        val lines: List<String>
        try {
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8)
            for (line in lines) {
                result.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result.toString()
    }


    // get a file from the resources folder
    // works everywhere, IDEA, unit test and JAR file.
    private fun getFileFromResourceAsStream(fileName: String): InputStream? {

        // The class loader that loaded the class
        val classLoader = javaClass.classLoader
        val inputStream = classLoader.getResourceAsStream(fileName)

        // the stream holding the file content
        return inputStream ?: throw IllegalArgumentException("file not found! $fileName")
    }

    /*
        The resource URL is not working in the JAR
        If we try to access a file that is inside a JAR,
        It throws NoSuchFileException (linux), InvalidPathException (Windows)

        Resource URL Sample: file:java-io.jar!/json/file1.json
     */
    @Throws(URISyntaxException::class)
    fun getFileFromResource(fileName: String): File {
        val classLoader = javaClass.classLoader
        val resource = classLoader.getResource(fileName)
        return if (resource == null) {
            throw IllegalArgumentException("file not found! $fileName")
        } else {

            // failed if files have whitespaces or special characters
            //return new File(resource.getFile());
            File(resource.toURI())
        }
    }

    fun getFileFromResourceW(fileName: String): File {
        val classLoader = javaClass.classLoader
        val resource = classLoader.getResource(fileName)
        return File(resource.toURI())

    }
    fun getFileFromResourceWW(filePath: String?): File? {
        val classloader = Thread.currentThread().contextClassLoader
        return File(Objects.requireNonNull(classloader.getResource(filePath)).file)
    }

    // print input stream
    private fun printInputStream(`is`: InputStream) {
        try {
            InputStreamReader(`is`, StandardCharsets.UTF_8).use { streamReader ->
                BufferedReader(streamReader).use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        println(line)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // print a file
    private fun printFile(file: File) {
        val lines: List<String>
        try {
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8)
            lines.forEach(Consumer { x: String? -> println(x) })
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}