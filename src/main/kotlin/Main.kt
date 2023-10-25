import java.util.Scanner
import kotlin.system.exitProcess

val scanner = Scanner(System.`in`)
const val commentError = "Вы ввели пустую строку. Попробуйте снова"

fun main() {
    val archiveList = mutableListOf<Archive>()
    val menu = Menu()
    var menuState = MenuState.ARCHIVE
    var selectedArchiveIndex = -1
    var selectedNoteIndex = -1

    while (true) {
        when (menuState) {
            MenuState.ARCHIVE -> menu.render(archiveList, "архив")
            MenuState.NOTES -> menu.render(archiveList[selectedArchiveIndex].notes, "заметку")
            MenuState.VIEW -> {
                if (selectedArchiveIndex >= 0 && selectedNoteIndex >= 0) {
                    println(archiveList[selectedArchiveIndex].notes[selectedNoteIndex])
                    if (scanner.hasNextLine()) {
                        scanner.nextLine()
                    }
                    menuState = MenuState.NOTES
                    continue
                }
            }
            MenuState.CREATE -> {
                when {
                    selectedArchiveIndex < 0 -> {
                        archiveList.add(Archive(isEmptyString("Введите название архива"), mutableListOf()))
                        menuState = MenuState.ARCHIVE
                        continue
                    }
                    selectedArchiveIndex >= 0 -> {
                        archiveList[selectedArchiveIndex].notes.add(
                            Note(isEmptyString("Введите заголовок заметки"),
                                isEmptyString("Введите текст заметки")))
                        menuState = MenuState.NOTES
                        continue
                    }
                }
            }
        }

        while (true) {
            try {
                var selectedIndex : Int
                while (true) {
                    selectedIndex = scanner.nextLine().toInt()
                    if(selectedIndex >= 0) {
                        break
                    } else {
                        println("Вы ввели что-то не то. Попробуйте снова")
                    }
                }

                menu.select(
                    selectedIndex,
                    { menuState = MenuState.CREATE },
                    { index ->
                        when (menuState) {
                            MenuState.ARCHIVE -> {
                                selectedArchiveIndex = selectedIndex - 1
                                menuState = MenuState.NOTES
                            }
                            MenuState.NOTES -> {
                                selectedNoteIndex = selectedIndex - 1
                                menuState = MenuState.VIEW
                            }
                            else -> Unit
                        }
                    },
                    { when (menuState) {
                        MenuState.NOTES -> menuState = MenuState.ARCHIVE
                        else -> {
                            scanner.close()
                            exitProcess(0)
                        }
                    }
                    }
                )
                break
            } catch (e: Exception) {
                println("Вы ввели что-то не то. Попробуйте снова")
            }
        }
    }
}

fun isEmptyString(str: String): String {
    var value: String
    while (true) {
        println(str)
        value = scanner.nextLine()
        if (value.isNotEmpty()) {
            break
        } else {
            println(commentError)
        }
    }
    return value
}