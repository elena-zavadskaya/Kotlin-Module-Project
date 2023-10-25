import java.lang.NumberFormatException
import java.util.Scanner
import kotlin.system.exitProcess

fun main() {
    val archiveList = mutableListOf<Archive>()
    val scanner = Scanner(System.`in`)
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
                        while (true) {
                            println("Введите наазвание архива")
                            val name = scanner.nextLine()
                            if (name.isNotEmpty()) {
                                archiveList.add(Archive(name, mutableListOf()))
                                menuState = MenuState.ARCHIVE
                                break
                            } else {
                                println("Вы ввели пустую строку. Попробуйте снова")
                            }
                        }
                        continue
                    }
                    selectedArchiveIndex >= 0 -> {
                        var name : String
                        while (true) {
                            println("Введите заголовок заметки ")
                            name = scanner.nextLine()
                            if (name.isNotEmpty()) {
                                break
                            } else {
                                println("Вы ввели пустую строку. Попробуйте снова")
                            }
                        }

                        while (true) {
                            println("Введите текст заметки")
                            val body = scanner.nextLine()
                            if (body.isNotEmpty()) {
                                archiveList[selectedArchiveIndex].notes.add(Note(name, body))
                                menuState = MenuState.NOTES
                                break
                            } else {
                                println("Вы ввели пустую строку. Попробуйте снова")
                            }
                        }
                        continue
                    }
                }
            }
        }

        while (true) {
            try {
                val selectedIndex = scanner.nextLine().toInt()
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
            } catch (e: NumberFormatException) {
                println("Вы ввели что-то не то. Попробуйте снова")
            }
        }
    }
}