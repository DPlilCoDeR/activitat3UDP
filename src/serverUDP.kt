//Fer un programa per gestionar una llibreta de contactes.
// El programa tindrà una part servidor que s'encarregarà d'atendre les peticions
// del client que es connectaran.
// Tan el client com el servidor faran servir el protocol UDP per comunicar-se.
// El servidor escoltarà al port 27855.
//
// Les peticions
//Les peticions que poden fer els clients poden ser:
//
//LLISTA_CONTACTES   --> El servidor respondrà amb una llista de contactes separats
// per comes: nom_contacte1,nom_contacte2,...,nom_contacte3
//DADES nom_contacte --> El servidor respondrà amb les dades del contacte:
//nom_contacte:email:telèfon (suposarem que no hi han dos contactes amb el mateix nom)
// o NOT_FOUND si no s'ha trobat res.
//CREA nom_contacte:email:telèfon --> El servidor respondrà OK si ha pogut crear el
// contacte i ERROR si no l'ha pogut crear
//
//Les dades
//El servidor guardarà totes les dades en un fitxer.
// Com estem parlant d'un volum de dades petit, el servidor, quan arrenqui,
// carregarà totes les dades a memòria (una llista, un mapa...).
// Cada cop que es crei un contacte nou, s'haurà d'afegir al fitxer.

import java.io.File
import java.net.*

val arxiu = File("./contactes.txt")

fun main() {
    if (!arxiu.exists()) {
        arxiu.createNewFile()
    }
    val port = 8080
    val buffer = ByteArray(1472)
    val contactes = mutableMapOf<String, Contacte>()

    fun updateContactes() {

    if (arxiu.exists()) {
        arxiu.forEachLine { line ->
            val contact = line.split(":")
            if (contact.size == 3) {
                contactes[contact[0]] = Contacte(contact[0], contact[1], contact[2])
            }
            }
        }
    }

    DatagramSocket(port).use { socket ->
        while (true){
            val packet = DatagramPacket(buffer,0,buffer.size)
            val clientIp = packet.address
            val clientPort = packet.port
            socket.receive(packet)

            val resposta = when (val command = String(packet.data, 0, packet.length)) {
               "LLISTA_CONTACTES" -> llistaContactes(contactes)
               command.startsWith("DADES").toString() -> dadesContacte(command, contactes)
               command.startsWith("CREA").toString() -> creaContacte(command, contactes)
               else  -> println("Unknown command")
           }
            val respostaByte = resposta.toString().toByteArray()
            val respostaPacket = DatagramPacket(respostaByte,respostaByte.size,clientIp,clientPort)
            socket.send(respostaPacket)
        }
    }
}

fun creaContacte(command: String, contactes: MutableMap<String, Contacte>) {
    val (nom, email, telefon) = command.removePrefix("CREA").trim().split(":")
    if(contactes.containsKey(nom)){
        "Error creating a contacte"
    } else {
        try {
            arxiu.appendText("${nom}:${email}:${telefon}\n")
            println("OK")
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}

fun dadesContacte(command: String, contactes: MutableMap<String, Contacte>) {
    val nom = command.removePrefix("DADES").trim()
    val contacte = contactes[nom]
    if (contacte != null) "${contacte.nom}:${contacte.email}:${contacte.telefon}" else ""
}

fun llistaContactes(contactes: MutableMap<String, Contacte>) {
    TODO("Not yet implemented")
}
