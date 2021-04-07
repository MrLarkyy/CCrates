package cz.larkyy.ccrates.utils;

import cz.larkyy.ccrates.CCrates;

public class StorageUtils {

    private final CCrates main;
    private final Utils utils;

    public StorageUtils(CCrates main) {
        this.main = main;
        this.utils = main.getUtils();
    }
}
