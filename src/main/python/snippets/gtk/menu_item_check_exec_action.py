def {--itemId--}Action(self, widget):
    self.rpcClient.Signals.itemSelection("{--itemId--}", widget.active)
    return 0

