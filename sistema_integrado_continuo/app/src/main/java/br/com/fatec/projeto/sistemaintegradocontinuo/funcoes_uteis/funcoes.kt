package br.com.fatec.projeto.sistemaintegradocontinuo.funcoes_uteis
import java.net.URL

// Pegar o dados do ViaCep
data class ViaCepResponse(
    val cep: String,
    val logradouro: String,
    val complemento: String,
    val bairro: String,
    val localidade: String,
    val uf: String
)

fun requestViaCep(cep: String): ViaCepResponse? {
    val urlString = "https://viacep.com.br/ws/$cep/json/"
    val url = URL(urlString)
    val connection = url.openConnection()
    val response = connection.getInputStream().bufferedReader().use { it.readText() }

    return try {
        // O response da API ViaCEP é um JSON, você pode utilizar alguma biblioteca de JSON parsing
        // para converter o response em um objeto Kotlin, como o Gson ou o Moshi.
        // Aqui, estou usando uma abordagem mais simples para exemplificar.
        val responseArray = response.replace("[", "").replace("]", "").split(",")
        ViaCepResponse(
            responseArray[0].split(":")[1].trim().removeSurrounding("\""),
            responseArray[1].split(":")[1].trim().removeSurrounding("\""),
            responseArray[2].split(":")[1].trim().removeSurrounding("\""),
            responseArray[3].split(":")[1].trim().removeSurrounding("\""),
            responseArray[4].split(":")[1].trim().removeSurrounding("\""),
            responseArray[5].split(":")[1].trim().removeSurrounding("\"")
        )
    } catch (e: Exception) {
        null
    }
}
