self.{--subMenuId--} = gtk.MenuItem("{--menuText--}")
self.{--subMenuId--}.show()
self.{--subMenuId--}.set_submenu(self.{--menuId--})
self.{--parentMenuId--}.append(self.{--subMenuId--})

