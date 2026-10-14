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

fun main(){
    val arxiu = File("./contactes.txt")
    if(!arxiu.exists()){
        arxiu.createNewFile()
    }
    val port = 8080

    DatagramSocket(port).use { socket ->
        val buffer = ByteArray(1472)

        while (true){
            val packet = DatagramPacket(buffer,0,buffer.size)
            val clientIp = packet.address
            val clientPort = packet.port
            socket.receive(packet)

            val resposta = when (val command = String(packet.data, 0, packet.length)) {
               "LLISTA_CONTACTES" -> llistaContactes()
               command.startsWith("DADES").toString() -> dadesContacte()
               command.startsWith("CREA").toString() -> creaContacte()
               else  -> println("Unknown command")
           }
            val respostaByte = resposta.toString().toByteArray()
            val respostaPacket = DatagramPacket(respostaByte,respostaByte.size,clientIp,clientPort)
            socket.send(respostaPacket)
        }
    }
}

fun creaContacte() {
    TODO("Not yet implemented")
}

fun dadesContacte() {
    TODO("Not yet implemented")
}

fun llistaContactes() {
    TODO("Not yet implemented")
}
