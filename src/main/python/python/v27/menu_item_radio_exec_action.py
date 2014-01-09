def {--itemId--}Action(self, widget):
    if (widget.get_active()):
        self.rpcClient.Signals.itemSelection("{--itemId--}", True)
    return 0

