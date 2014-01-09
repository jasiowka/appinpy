package pl.jasiowka.appinpy;

class ActionInfoUnity implements ActionInfo {

    private MenuItem source; 

    @Override
    public MenuItem getSource() {
        return source;
    }

    public void setSource(MenuItem source) {
        this.source = source;
    }

}
