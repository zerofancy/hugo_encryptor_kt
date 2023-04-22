import org.apache.commons.codec.Resources
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.DigestUtils
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.jsoup.Jsoup
import java.io.File
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.security.Security
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.path.Path
import kotlin.io.path.name
import kotlin.io.path.writeText

fun main(args: Array<String>) {
    if (args.firstOrNull()?.trim() == "install") {
        File("layouts/shortcodes").mkdirs()
        Files.copy(Resources.getInputStream("hugo-encryptor.html"), Path("layouts/shortcodes/hugo-encryptor.html"), StandardCopyOption.REPLACE_EXISTING)
        println("""
            You can use like the following in md files:
            
            
            {{% hugo-encryptor "PASSWORD" %}}
            Type your content here.
            {{% /hugo-encryptor %}}

        """.trimIndent())
        return
    }

    Security.addProvider(BouncyCastleProvider())

    if (!File("public").exists()) {
        return
    }

    Files.walkFileTree(Path("public"), object : SimpleFileVisitor<Path>() {
        override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
            if (file?.name?.endsWith(".html") == true) {
                handleHtmlFile(file)
            }
            return FileVisitResult.CONTINUE
        }
    })
    Files.copy(Resources.getInputStream("decrypt.js"), Path("public/decrypt.js"), StandardCopyOption.REPLACE_EXISTING)
}

val cipher by lazy(LazyThreadSafetyMode.NONE) { Cipher.getInstance("AES/CBC/PKCS7Padding") }

fun handleHtmlFile(path: Path) {
    val document = Jsoup.parse(path.toFile())
    val blocks = document.select("div.hugo-encryptor-cipher-text")
    if (blocks.isEmpty()) {
        return
    }
    println(path)
    blocks.forEach {
        val htmlContent = it.html()
        val password = it.attr("data-password")
        if (password.trim().isBlank()) {
            return@forEach
        }
        val passwordDigest = DigestUtils.md5Hex(password)
        val keySpec = SecretKeySpec(passwordDigest.encodeToByteArray(), "AES/CBC/PKCS7Padding")
        val ivSpec = IvParameterSpec(passwordDigest.substring(16).toByteArray())
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)

        val encrypted = cipher.doFinal(htmlContent.encodeToByteArray())
        val encryptResult = Base64.encodeBase64String(encrypted)

        it.html(encryptResult)
        it.removeAttr("data-password")
    }
    if (document.select("script.decrpytjs").isEmpty()) {
        document.body().append(
            """
        <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/3.1.9-1/crypto-js.js" />
        <script class="decrpytjs" src="/decrypt.js" />
    """.trimIndent()
        )
    }
    path.writeText(document.outerHtml())
}