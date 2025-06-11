@file:Suppress("SpellCheckingInspection")

import data.EXPECTED_NU_I_DNIPRO
import kotlinx.serialization.json.Json
import logic.filterAlert
import model.Entity
import model.TelegramChannelHistory
import kotlin.test.Test
import kotlin.test.assertEquals

class AlertsTest {

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    private val dniproAlertsData = readTextResource("ChatExport_2024-09-22_dnipro_alerts/result.json")
    private val dniproAlertsParsed = json.decodeFromString(TelegramChannelHistory.serializer(), dniproAlertsData)
    private val dniproAlertsMessages = dniproAlertsParsed.messages
    private val nuIDniproData = readTextResource("ChatExport_2024-09-22_nu_i_dnipro/result.json")
    private val nuIDniproParsed = json.decodeFromString(TelegramChannelHistory.serializer(), nuIDniproData)
    private val nuIDniproMessages = nuIDniproParsed.messages
    private val vanekNikolaevData = readTextResource("ChatExport_2024-09-22_vanek_nikolaev/result.json")
    private val vanekNikolaevParsed = json.decodeFromString(TelegramChannelHistory.serializer(), vanekNikolaevData)
    private val vanekNikolaevMessages = vanekNikolaevParsed.messages

    @Test
    fun testSerilization() {
        val testMessage = dniproAlertsMessages.first {
            it.id == 16593
        }
//        println(dniproAlertsParsed)
//        println(dniproAlertsData.indexOf("16593").let {
//            dniproAlertsData.substring(it, min(it + 2000, dniproAlertsData.length))
//        })
        assertEquals(
            "Впав сокиркою. \\nМать сира земля.\\nГарного тихого дня.\\n\\n\uD83E\uDD19 Дніпро | ✌\uFE0F Київ  |  \uD83E\uDD1D Підтримати",
            testMessage.toString()
        )
        println(testMessage)
    }

    @Test
    fun testDniproAlerts() {
        val filtered = dniproAlertsMessages.filter { filterAlert(it.toString(), Entity.Channel(dniproAlertsParsed.id)) }
//        filtered.forEach {
//            println(it)
//        }
        assertEquals(
            listOf(
                "Ситуація по шахедам:\\nПолтавська область, Херсонська, Миколаївська.\\nВ межах Дніпропетровської не фіксуються.\\n\\n🤙 Дніпро | ✌️ Київ  |  🤝 Підтримати",
                "Дніпро в укриття\\n\\n🤙 Дніпро | ✌️ Київ  |  🤝 Підтримати",
                "Око Саурона в трикутнику Дніпро/Новомосковськ/Синельникове.\\n\\n🤙 Дніпро | ✌️ Київ  |  🤝 Підтримати",
            ),
            filtered.map { it.toString() }
        )
    }

    @Test
    fun testNuIDnipro() {
        val filtered = nuIDniproMessages.filter { filterAlert(it.toString(), Entity.Channel(nuIDniproParsed.id)) }
//        filtered.forEach {
//            println(it)
//        }
        assertEquals(EXPECTED_NU_I_DNIPRO, filtered.map { it.toString() })
    }

    @Test
    fun testVanekNikolaev() {
        val filtered =
            vanekNikolaevMessages.filter { filterAlert(it.toString(), Entity.Channel(vanekNikolaevParsed.id)) }
//        filtered.forEach {
//            println(it)
//        }
        val expected =
            ("❗\uFE0FДніпропетровщина. російські війська здійснили атаку по Павлограду. Попередньо, 1 людина загинула, ще 30 — постраждали\\n\\nПошкоджений п’ятиповерховий житловий будинок. Вогнеборці ліквідували пожежу та врятували 5 людей.\\n\\nНа місці удару працюють усі необхідні служби.\\n\\n\uD83C\uDDFA\uD83C\uDDE6Підписатися | Міністр | МВС | Гвардія наступу\n" +
                    "ещё одна баллистика на Павлоград, в этот раз с Таганрога").split("\n")
        assertEquals(
            expected,
            filtered.map { it.toString() }
        )
    }

}