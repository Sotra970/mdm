package tkamul.ae.mdmcontrollers.PrinterModule.core


/**
 * Created by sotra@altakamul.tr on 3/1/2021.
 */
object LineUtils {
    private const val SPACE =  " "
    internal const val ASTERISK: String = "*"
    internal const val DASH: String = "-"
    internal const val EMPTY_LINE: String = "\n"

    fun getLineOfChar(lineCount: Int, char: String): String {
        var line = ""
        for (i in 0 until lineCount) line += char
        return line 
    }

    fun  convertTextToLine(text: String, maxCharCountInLine: Int): MutableList<String> {
        val lineList = mutableListOf<String>()
        if (text.length > maxCharCountInLine) {
            var startIndex = 0
            while (startIndex < text.length) {
                var endIndex = startIndex + maxCharCountInLine
                endIndex = if (endIndex > text.length) text.length else endIndex
                val temp_line = text.substring(startIndex, endIndex)
                lineList.add(temp_line)
                startIndex = endIndex
            }
        } else {
           lineList.add(text)
        }
        return lineList
    }

    fun getCenterdLine(text: String , maxCharCountInLine: Int): String {
        // if text more than line or equal then return same text
       val trimedText = text.trim()
        if (trimedText.length>=maxCharCountInLine)
            return trimedText
      // get available space in line
        val avSpace = maxCharCountInLine - trimedText.length
        // half available space + text + half available space
        return getSpace(avSpace/2 ) + trimedText + getSpace(avSpace/2)
    }


    private fun  getSpace(count :Int): String {
        var line = ""
        for (i in 0 until count - 1) line += SPACE
        return line
    }
}