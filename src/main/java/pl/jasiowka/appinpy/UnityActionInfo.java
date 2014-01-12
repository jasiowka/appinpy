package pl.jasiowka.appinpy;

class UnityActionInfo implements ActionInfo {

    private Item source; 

    @Override
    public Item getSource() {
        return source;
    }

    public void setSource(Item source) {
        this.source = source;
    }

}
