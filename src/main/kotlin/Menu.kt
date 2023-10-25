class Menu {
    private val menuFormatter = "%d. %s" // формат вывода
    private val menuItems = mutableListOf<MenuItem>()

    fun render(list: List<File>, fileType: String) {
        menuItems.clear()
        menuItems.add(MenuItem("Создать $fileType"))
        menuItems.addAll(list.map { MenuItem(it.name) })
        menuItems.add(MenuItem("Выход"))

        menuItems.forEachIndexed { index, archiveMenuItem ->
            println("$index. ${archiveMenuItem. title}")
        }
    }

    fun select(index: Int,
               onCreate: () -> Unit,
               onItem: (Int) -> Unit,
               onClose: () -> Unit) {
        when {
            index == 0 -> onCreate()
            index < menuItems.lastIndex -> {
                onItem(index)
            }
            index == menuItems.lastIndex -> {
                onClose()
            }
            else -> println("Вы ввели что-то не то. Попробуйте снова")
        }
    }
}