def {--itemId--}Action(self, widget):
    self.rpcClient.Signals.itemSelection("{--itemId--}")
    return 0

