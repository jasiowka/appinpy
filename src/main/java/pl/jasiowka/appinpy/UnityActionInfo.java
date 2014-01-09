package pl.jasiowka.appinpy;

class UnityActionInfo implements ActionInfo {

    private Object source; 

    @Override
    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

}
