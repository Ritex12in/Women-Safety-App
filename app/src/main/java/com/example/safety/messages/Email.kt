package com.example.safety.messages

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object Email {
    fun sendEmail(recipientEmails: List<String>,senderEmail:String,password:String,sub:String,msg:String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {

                val host = "smtp.gmail.com"

                val properties: Properties = System.getProperties()

                properties.setProperty("mail.transport.protocol", "smtp")
                properties.setProperty("mail.host", host)
                properties["mail.smtp.host"] = host
                properties["mail.smtp.port"] = "465"
                properties["mail.smtp.socketFactory.fallback"] = "false"
                properties.setProperty("mail.smtp.quitwait", "false")
                properties["mail.smtp.socketFactory.port"] = "465"
                properties["mail.smtp.starttls.enable"] = "true"
                properties["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
                properties["mail.smtp.ssl.enable"] = "true"
                properties["mail.smtp.auth"] = "true"

                val session: Session = Session.getInstance(properties, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(senderEmail, password)
                    }
                })

                val mimeMessage = MimeMessage(session)

                // Set From
                mimeMessage.setFrom(InternetAddress(senderEmail))

                // Set To
                val recipientAddresses = InternetAddress.parse(recipientEmails.joinToString(","))
                mimeMessage.setRecipients(Message.RecipientType.TO, recipientAddresses)

                mimeMessage.subject = sub
                mimeMessage.setText(msg)

                // Use Transport.send() directly, it handles threading internally
                try {
                    Transport.send(mimeMessage)
                } catch (e: MessagingException) {
                    e.printStackTrace()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}