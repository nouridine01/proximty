package com.uqac.proximty;

/**
 * Copyright (c) 2021, NIKISS. All Rights Reserved.
 * <p>
 * Save to the extent permitted by law, you may not use, copy, modify, distribute or
 * create derivative works of this material or any part of it without the prior
 * written consent of  OUEDRAOGO ISSOUF NIKISS.email:ouedraogo.nikiss@gmail.com
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the software.
 * Created on 10,novembre,2021
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.uqac.proximty.entities.User;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // Shared preferences file name
    private static final String PREF_NAME_DEFAULT = "default_pref";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String USER_PSEUDO = "UserPseudo";
    private static final String USER_CONNECTED = "UserConnected";

    // shared pref mode
    int PRIVATE_MODE = 0;


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME_DEFAULT, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.putString(USER_PSEUDO,"UserPseudo");
        editor.commit();
    }

    /**
     * Permet de tester si l'application à deja été lancer auparavant,
     * Retourne True si OUI et False sinon
     * @return
     */
    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public String getUserPseudo() {
        return pref.getString(USER_PSEUDO,"test");
    }

    public void setUserPseudo(String pseudo) {
        editor.putString(USER_PSEUDO,pseudo);
        editor.commit();
    }

    public boolean getUserConnected() {
        return pref.getBoolean(USER_CONNECTED, false);
    }

    public void setUserConnected(boolean newVal) {
        editor.putBoolean(USER_CONNECTED, newVal);
        editor.commit();
    }
}