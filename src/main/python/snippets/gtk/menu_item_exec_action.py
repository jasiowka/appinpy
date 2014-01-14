def {--itemId--}Action(self, widget):
    self.rpcClient.Signals.itemSelection("{--itemId--}", False)
    return 0

