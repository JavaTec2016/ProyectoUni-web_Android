package model;

public class ModelLoader {
    public static void loadAll(){

        Models.getModels().addPrototype(new Clase());
        Models.getModels().addPrototype(new Corporacion());
    }
}
