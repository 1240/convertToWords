import java.io.InputStream
import java.util.*

/**
 * @author Alexander Popov on 30/01/2017.
 */
object ConvertToWords {

    private val ERROR = "значение < 0 или > 9223372036854775807"
    private val UNITS = arrayOf(arrayOf("одна", "две", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"), arrayOf("один", "два"))
    private val DECADES = arrayOf("десять", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать", "пятнадцать", "шестнадцать", "семнадцать", "восемнадцать", "девятнадцать")
    private val DECADES2 = arrayOf("двадцать", "тридцать", "сорок", "пятьдесят", "шестьдесят", "семьдесят", "восемьдесят", "девяносто")
    private val HUNDREDS = arrayOf("сто", "двести", "триста", "четыреста", "пятьсот", "шестьсот", "семьсот", "восемьсот", "девятьсот")
    private val OTHERS = arrayOf(
            arrayOf("", "", "", "0"),
            arrayOf("", "", "", "1"),
            arrayOf("тысяча", "тысячи", "тысяч", "0"),
            arrayOf("миллион", "миллиона", "миллионов", "1"),
            arrayOf("миллиард", "миллиарда", "миллиардов", "1"),
            arrayOf("миллиард", "миллиарда", "миллиардов", "1"),
            arrayOf("триллиона", "триллиона", "триллионов", "1"),
            arrayOf("квадриллион", "квадриллиона", "квадриллионов", "1")) // впринципе можно продолжать до Дециллиона 10 ** 33 и дальше т.к. цикл рекурсивный


    private fun num2words(num: Long, level: Int): String {
        val words = StringBuilder(50)
        if (num == 0L) words.append("ноль ")
        val sex = OTHERS[level][3].indexOf("1") + 1
        val h = (num % 1000).toInt()
        var d = h / 100
        if (d > 0) words.append(HUNDREDS[d - 1]).append(" ")
        var n = h % 100
        d = n / 10
        n %= 10
        when (d) {
            0 -> {
            }
            1 -> words.append(DECADES[n]).append(" ")
            else -> words.append(DECADES2[d - 2]).append(" ")
        }
        if (d == 1) n = 0
        when (n) {
            0 -> {
            }
            1, 2 -> words.append(UNITS[sex][n - 1]).append(" ")
            else -> words.append(UNITS[0][n - 1]).append(" ")
        }
        when (n) {
            1 -> words.append(OTHERS[level][0])
            2, 3, 4 -> words.append(OTHERS[level][1])
            else -> if (h != 0 || h == 0 && level == 1)
                words.append(OTHERS[level][2])
        }
        val nextNum = num / 1000
        if (nextNum > 0) {
            return (num2words(nextNum, level + 1) + " " + words.toString()).trim { it <= ' ' }
        } else {
            return words.toString().trim { it <= ' ' }
        }
    }

    fun convertTowords(lines: List<String>): List<String> {
        val result = ArrayList<String>()
        for (line in lines) {
            try {
                val longValue = line.toLong()
                if (longValue > 0) {
                    result.add(num2words(longValue, 1))
                } else {
                    result.add(ERROR)
                }
            } catch (e: NumberFormatException) {
                result.add(ERROR)
            }
        }
        return result

    }

    fun convertTowords(line: String): String? {
        try {
            val longValue = line.toLong()
            if (longValue > 0) {
                return num2words(longValue, 1)
            }
        } catch (e: NumberFormatException) {
//            NumberFormatException(ERROR)
        }
        return ERROR
    }

    fun convertTowords(inputStream: InputStream): List<String> {
        var content: String? = null
        inputStream.bufferedReader(Charsets.UTF_8).use { content = it.readText() }
        val lines = content!!.replace("\"", "").split("\r\n").toList()
        val result = ArrayList<String>()
        for (line in lines) {
            try {
                val longValue = line.toLong()
                if (longValue > 0) {
                    result.add(num2words(longValue, 1))
                } else {
                    result.add(ERROR)
                }
            } catch (e: NumberFormatException) {
                result.add(ERROR)
            }
        }
        return result
    }


    //    @JvmStatic fun main(args: Array<String>) {
//        val lines = File("../input.txt").readLines()
//        val convertTowords = convertTowords(lines)
//        for (inword in convertTowords) {
//            println(inword)
//        }
//    }
//    @JvmStatic fun main(args: Array<String>) {
//        val convertTowords = convertTowords("9223372036854775807")
//        println(convertTowords)
//    }

}