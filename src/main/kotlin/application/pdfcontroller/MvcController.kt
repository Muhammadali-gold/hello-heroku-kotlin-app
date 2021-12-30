package application.pdfcontroller

import application.pdfcontroller.OrderHelper.order
import application.qrcode.generateQRcode
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.itextpdf.html2pdf.ConverterProperties
import com.itextpdf.html2pdf.HtmlConverter
import org.apache.commons.codec.binary.Base64
import org.json.CDL
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.context.WebContext
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.*
import javax.servlet.ServletContext
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.text.Charsets.UTF_8


@Controller
@RequestMapping("order")
 class MvcController(private val templateEngine: TemplateEngine?) {

    @Autowired
    var servletContext: ServletContext? = null


    @RequestMapping(path = ["/"],method = [RequestMethod.GET])
    @Throws(IOException::class)
    fun getOrderPage(model: Model): String {
        val order = order
        model.addAttribute("orderEntry", order)
        return "order"
    }

    @RequestMapping(path = ["/dsi"],method = [RequestMethod.POST])
    @Throws(IOException::class)
    fun getTechDSIPage(model: Model,@RequestBody tech:String): String {
//        val json = JSONObject(tech)
        val objectMapper = ObjectMapper()
//        val tech = prepareTech(json)
//        model.addAttribute("year", json.optInt("year"))
//        model.addAttribute("day", json.optInt("day"))
//        model.addAttribute("month", json.optInt("moth"))
//        model.addAttribute("date", json.optInt("date"))

//        model.addAttribute("rownum", json.optInt("rownum"))
//        model.addAttribute("tin", json.optInt("tin"))
//        model.addAttribute("name", json.optInt("name"))
//        model.addAttribute("registration_name", json.optInt("registration_name"))
//        model.addAttribute("type", json.optInt("type"))
//        model.addAttribute("type", json.optInt("type"))
//        model.addAttribute("type", json.optInt("type"))
//        model.addAttribute("type", json.optInt("type"))
//        model.addAttribute("type", json.optInt("type"))
//        model.addAttribute("type", json.optInt("type"))
//        model.addAttribute("type", json.optInt("type"))

//        val objList = mutableListOf(String)
//        for (obj in json.optJSONArray("techs")){
//            val newTech = Tech()
//            newTech.rownum =
//            model.addAttribute("rownum", json.optInt("rownum"))
//        }
        val userData: Map<String, Any> = objectMapper.readValue(
            tech, object : TypeReference<Map<String, Any>>() {})
        model.addAttribute("tech", userData)
        return "tech"
    }


    @RequestMapping(path = ["/dsi/withqrcode"],method = [RequestMethod.POST])
    @Throws(IOException::class)
    fun getTechDSIPageWithQrCode(model: Model,@RequestBody tech:String): String {
        val objectMapper = ObjectMapper()
        val json = JSONObject(tech)
        val qrcodebase64=generateQRcode(CDL.toString(json.optJSONArray("data")),"utf-8",200,200)
        val userData: Map<String, Any> = objectMapper.readValue(
            tech, object : TypeReference<Map<String, Any>>() {})
        model.addAttribute("tech", userData)
        model.addAttribute("qrcode", qrcodebase64)
        return "tech"
    }

    @RequestMapping(path = ["/dsi/withqrcode/base64"],method = [RequestMethod.POST])
    @Throws(IOException::class)
    fun getTechDSIPageWithQrCodeBase64(request: HttpServletRequest?, response: HttpServletResponse?): ResponseEntity<String> {
        val objectMapper = ObjectMapper()
        val tech = is2Str(request?.inputStream)
        val json = JSONObject(tech)
        val qrcodebase64=generateQRcode(CDL.toString(json.optJSONArray("data")),"utf-8",200,200)
        val userData: Map<String, Any> = objectMapper.readValue(
            tech, object : TypeReference<Map<String, Any>>() {})


        /* Do Business Logic*/
        val order = order

        /* Create HTML using Thymeleaf template Engine */
        val context = WebContext(request, response, servletContext)
        context.setVariable("tech", userData)
        context.setVariable("qrcode", qrcodebase64)
        val orderHtml: String = templateEngine?.process("tech", context) ?: ""

        /* Setup Source and target I/O streams */
        val target = ByteArrayOutputStream()
        val converterProperties = ConverterProperties()
        converterProperties.baseUri = "http://localhost:8080"
        HtmlConverter.convertToPdf(orderHtml, target, converterProperties)

//        val templateResolver = ClassLoaderTemplateResolver()
//        templateResolver.suffix = ".html"
//        templateResolver.templateMode = TemplateMode.HTML
//        val templateEngine = TemplateEngine()
//        templateEngine.setTemplateResolver(templateResolver)
//        val context = Context()
//        context.setVariable("tech", userData)
//        context.setVariable("qrcode", qrcodebase64)
//        val html = templateEngine.process("tech", context)


//        val outputFolder = System.getProperty("user.home") + File.separator.toString() + "thymeleaf.pdf"
//        val outputStream: OutputStream = FileOutputStream("thymeleaf.pdf")
//        val renderer = ITextRenderer()
//        renderer.setDocumentFromString(orderHtml)
//        renderer.layout()
//        renderer.createPDF(outputStream)
//        outputStream.close()
//        val inputStream: InputStream = FileInputStream("thymeleaf.pdf")

        val bytes = target.toByteArray()
        val arrstr = String(Base64.encodeBase64(bytes))
//        inputStream.close()
        return ResponseEntity.ok(arrstr)
    }

    fun generatePdfFromHtml(html: String?) {
        val outputFolder = System.getProperty("user.home") + File.separator.toString() + "thymeleaf.pdf"
        val outputStream: OutputStream = FileOutputStream(outputFolder)
        val renderer = ITextRenderer()
        renderer.setDocumentFromString(html)
        renderer.layout()
        renderer.createPDF(outputStream)
        outputStream.close()
    }

    fun parseThymeleafTemplate(): String? {
        val templateResolver = ClassLoaderTemplateResolver()
        templateResolver.suffix = ".html"
        templateResolver.templateMode = TemplateMode.HTML
        val templateEngine = TemplateEngine()
        templateEngine.setTemplateResolver(templateResolver)
        val context = Context()
        context.setVariable("to", "Baeldung")
        return templateEngine.process("thymeleaf_template", context)
    }

    @RequestMapping(path = ["/dsi/qrcode"],method = [RequestMethod.POST])
    @Throws(IOException::class)
    fun getTechDSIQRCode(model: Model,@RequestBody tech:String): ResponseEntity<String> {
        val json = JSONObject(tech)
//        println("data:"+CDL.toString(json.optJSONArray("data")))
        val qrcodebase64=generateQRcode(CDL.toString(json.optJSONArray("data")),"utf-8",800,800)
//        val objectMapper = ObjectMapper()
//        val tech = prepareTech(json)
//        model.addAttribute("year", json.optInt("year"))
//        model.addAttribute("day", json.optInt("day"))
//        model.addAttribute("month", json.optInt("moth"))
//        model.addAttribute("date", json.optInt("date"))

//        model.addAttribute("rownum", json.optInt("rownum"))
//        model.addAttribute("tin", json.optInt("tin"))
//        model.addAttribute("name", json.optInt("name"))
//        model.addAttribute("registration_name", json.optInt("registration_name"))
//        model.addAttribute("type", json.optInt("type"))
//        model.addAttribute("type", json.optInt("type"))
//        model.addAttribute("type", json.optInt("type"))
//        model.addAttribute("type", json.optInt("type"))
//        model.addAttribute("type", json.optInt("type"))
//        model.addAttribute("type", json.optInt("type"))
//        model.addAttribute("type", json.optInt("type"))

//        val objList = mutableListOf(String)
//        for (obj in json.optJSONArray("techs")){
//            val newTech = Tech()
//            newTech.rownum =
//            model.addAttribute("rownum", json.optInt("rownum"))
//        }
//        val userData: Map<String, Any> = objectMapper.readValue(
//            tech, object : TypeReference<Map<String, Any>>() {})
//        model.addAttribute("tech", userData)
        return ResponseEntity.ok(qrcodebase64);
    }

    fun is2Str(`is`: InputStream?): String? {
        val sb = StringBuilder()
        try {
            val isr = InputStreamReader(`is`, UTF_8)
            var numCharsRead=0
            val charArray = CharArray(1024)
            while (isr.read(charArray).also { numCharsRead = it } > 0) {
                sb.append(charArray, 0, numCharsRead)
            }
        } catch (e: Exception) {
            println(e.message)
        }
        return sb.toString()
    }

    @RequestMapping(path = ["/pdf"])
    @Throws(IOException::class)
    fun getPDF(request: HttpServletRequest?, response: HttpServletResponse?): ResponseEntity<*>? {

        /* Do Business Logic*/
        val order = order

        /* Create HTML using Thymeleaf template Engine */
        val context = WebContext(request, response, servletContext)
        context.setVariable("orderEntry", order)
        val orderHtml: String = templateEngine?.process("order", context) ?: ""

        /* Setup Source and target I/O streams */
        val target = ByteArrayOutputStream()
        val converterProperties = ConverterProperties()
        converterProperties.baseUri = "http://localhost:8080"
        /* Call convert method */HtmlConverter.convertToPdf(orderHtml, target, converterProperties)

        /* extract output as bytes */
        val bytes = target.toByteArray()


        /* Send the response as downloadable PDF */return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(bytes)
    }


    @RequestMapping(path = ["/pdf/base64"])
    @Throws(IOException::class)
    fun getPDFBase64(request: HttpServletRequest?, response: HttpServletResponse?): ResponseEntity<String> {

        /* Do Business Logic*/
        val order = order

        /* Create HTML using Thymeleaf template Engine */
        val context = WebContext(request, response, servletContext)
        context.setVariable("orderEntry", order)
        val orderHtml: String = templateEngine?.process("order", context) ?: ""

        /* Setup Source and target I/O streams */
        val target = ByteArrayOutputStream()
        val converterProperties = ConverterProperties()
        converterProperties.baseUri = "http://localhost:8080"
        /* Call convert method */HtmlConverter.convertToPdf(orderHtml, target, converterProperties)

        /* extract output as bytes */
        val bytes = target.toByteArray()
//        val encoder = Base64()

//        val encodedBytes = Base64.encodeBase64("Test".toByteArray())
//        println("encodedBytes " + String(encodedBytes))
//        val decodedBytes = Base64.decodeBase64(encodedBytes)
//        println("decodedBytes " + String(decodedBytes))
        //Returns Base64 encoded byte array string
        //Returns Base64 encoded byte array string
        val arrstr = String(Base64.encodeBase64(bytes))

        return ResponseEntity.ok(arrstr)
    }
}