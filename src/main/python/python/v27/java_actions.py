if (item=="{--itemId--}"):
    if (action=="enable"):
        self.{--itemId--}.set_sensitive(True)
        return 0
    if (action=="disable"):
        self.{--itemId--}.set_sensitive(False)
        return 0
    if (action=="show"):
        self.{--itemId--}.show()
        return 0
    if (action=="hide"):
        self.{--itemId--}.hide()
        return 0
    if (action=="set_label"):
        self.{--itemId--}.set_label(param)
        return 0
    {--javaCheckActions--}

