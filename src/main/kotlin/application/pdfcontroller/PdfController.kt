package application.pdfcontroller

import application.FileResourceUtilK
import application.RequestMessage
import application.ResponseMessage
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.io.FileOutputStream
import java.util.*
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.stereotype.Controller



@RestController
@RequestMapping("pdf")
class PdfController {

    @RequestMapping(path = ["save/base64"], method = [RequestMethod.POST])
    fun countWord(@RequestBody requestBody: RequestMessage): ResponseMessage {
        val responseMessage = ResponseMessage()

        val file = File("./test.pdf")
        val resource: Resource = ClassPathResource("static/pdfs/file.txt")

        val input = resource.inputStream

        val fileBase64:File = resource.file

        try {
            FileOutputStream(file).use { fos ->
                // To be short I use a corrupted PDF string, so make sure to use a valid one if you want to preview the PDF file
                val utilK = FileResourceUtilK()
                val b64 = utilK.getAsStringFromFile(fileBase64)

                val decoder = Base64.getDecoder().decode(b64)
                fos.write(decoder)
                println("PDF File Saved")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        responseMessage.resMessage = "success"
        return  responseMessage
    }

    @RequestMapping(path = ["generate/demo"], method = [RequestMethod.POST])
    fun generatePdfDemo(@RequestBody requestBody: RequestMessage): ResponseMessage {
        val responseMessage = ResponseMessage()
        val util = FileResourceUtilK()
        HtmlConverter.convertToPdf(util.getFileFromResource("templates/pdf-demo.html"), File("./demo-html.pdf"))

        responseMessage.resMessage = "success"
        return  responseMessage
    }


    @RequestMapping(path = ["generate/data"], method = [RequestMethod.POST])
    fun generatePdfFromData(@RequestBody requestBody: RequestMessage): ResponseMessage {
        val responseMessage = ResponseMessage()



        responseMessage.resMessage = "success"
        return  responseMessage
    }

}