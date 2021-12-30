package application.qrcode

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.client.j2se.MatrixToImageWriter
import org.apache.commons.codec.binary.Base64
import java.awt.image.DataBufferByte
import java.awt.image.WritableRaster
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO





@Throws(WriterException::class, IOException::class)
fun generateQRcode(data: String, charset: String?, h: Int, w: Int): String {
    val matrix =
        MultiFormatWriter().encode(data,
            BarcodeFormat.QR_CODE,
            w,
            h)
    val bufferedImage = MatrixToImageWriter.toBufferedImage(matrix)
    val outputfile = File("image.jpg")
    ImageIO.write(bufferedImage, "jpg", outputfile)
//    val bytes = ByteArrayOutputStream(bufferedImage.outputStream) .toByteArray()

//    val raster: WritableRaster = bufferedImage.getRaster()
//    val buffer = raster.dataBuffer as DataBufferByte
    val bytes = outputfile.readBytes()
    outputfile.delete()
    return String(Base64.encodeBase64(bytes))
}