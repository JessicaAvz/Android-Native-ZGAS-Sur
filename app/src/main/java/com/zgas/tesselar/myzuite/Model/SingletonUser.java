package com.zgas.tesselar.myzuite.Model;

/**
 * Created by jarvizu on 19/01/2018.
 */

public class SingletonUser {
    private static String DEBUG_TAG = "SingletonUser";

    private static volatile SingletonUser singletonUser;

    protected SingletonUser() {
        //Existe solo para no permitir la instanciación
    }

    public static SingletonUser getSingletonUser() {
        if (singletonUser == null) {
            singletonUser = new SingletonUser();
        }
        return singletonUser;
    }

    private boolean isUserInSession() {
        //return isInSession;
        return true; //???????????????????????????????
    }
}


/*//Some code
if (!SessionUser.getInstance().isUserInSession())
{
    //Go to login
}
//More code
*/