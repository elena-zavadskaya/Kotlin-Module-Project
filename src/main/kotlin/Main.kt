import java.util.Scanner

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
                        println("Введите наазвание архива")
                        if (scanner.hasNextLine()) {
                            val name = scanner.nextLine()
                            archiveList.add(Archive(name, mutableListOf()))
                        }
                        menuState = MenuState.ARCHIVE
                        continue
                    }
                    selectedArchiveIndex >= 0 -> {
                        println("Введите заголовок заметки ")
                        val name = scanner.nextLine()
                        println("Введите текст заметки")
                        val body = scanner.nextLine()
                        archiveList[selectedArchiveIndex].notes.add(Note(name, body))
                        menuState = MenuState.NOTES
                        continue
                    }
                }
            }
        }

        while (true) {
            if (scanner.hasNextInt()) {
                val selectedIndex = scanner.nextLine().toInt()
                menu.select(
                        selectedIndex,
                        { menuState = MenuState.CREATE },
                        { selectedIndex ->
                            when (menuState) {
                                MenuState.ARCHIVE -> {
                                    selectedArchiveIndex = selectedIndex - 1
                                    menuState = MenuState.NOTES
                                }
                                MenuState.NOTES -> {
                                    selectedNoteIndex = selectedIndex - 1
                                    menuState = MenuState.VIEW
                                }
                                else -> {}
                            }
                        },
                        { when (menuState) {
                            MenuState.NOTES -> menuState = MenuState.ARCHIVE
                            else -> return@select
                        }
                        }
                )
                break
            } else {
                println("Вы ввели что-то не то. Попробуйте снова")
                scanner.hasNextInt()
                continue
            }
        }

    }
}