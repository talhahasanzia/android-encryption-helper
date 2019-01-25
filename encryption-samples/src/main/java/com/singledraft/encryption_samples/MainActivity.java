package com.singledraft.encryption_samples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;


import com.thz.keystorehelper.EncryptionDecryptionListener;
import com.thz.keystorehelper.KeyStoreManager;

import net.sqlcipher.database.SQLiteDatabase;

public class MainActivity extends AppCompatActivity implements EncryptionDecryptionListener {

    private DbUtil mDbUtil; // sample utility for database operations


    String alias = "test_alias13";

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);

        // initialize this in app object, SQLite cipher library
        SQLiteDatabase.loadLibs(this);

        SaveDataUtil saveDataUtil = new SaveDataUtil(this);

        KeyStoreManager.init(getApplicationContext());


        testSimple(saveDataUtil); // test simple calls

        testAsync(); // test async calls


        //testInvalidBlockSize();


    }


    private void testSimple(SaveDataUtil saveDataUtil) {

        String key;

        if (saveDataUtil.getSecurityPhrase() == null) {

            key = KeyStoreManager.getNewRandomPhrase(63);


            addTextResult("testSimple: generated: " + key);
            String encrypted = KeyStoreManager.encryptData(getPackageName(), "password123", getPackageName(), "password123", key);


            addTextResult("testSimple: encrypted: " + encrypted);

            saveDataUtil.setSecurityPhrase(encrypted);
        }
        key = saveDataUtil.getSecurityPhrase();


        addTextResult("testSimple: from prefs: " + key);

        String decrypted = KeyStoreManager.decryptData(getPackageName(), "password123", getPackageName(), "password123", key);

        addTextResult("testSimple: after decrypt: " + decrypted);

        mDbUtil = new DbUtil(this, decrypted);
    }


    private void testInvalidBlockSize() {
        String data = "{\"data\":[{\"id\":1,\"name\":\"United Arab Emirates\"},{\"id\":2,\"name\":\"Aland Islands\"},{\"id\":3,\"name\":\"Albania\"},{\"id\":4,\"name\":\"Algeria\"},{\"id\":5,\"name\":\"American Samoa\"},{\"id\":6,\"name\":\"Andorra\"},{\"id\":7,\"name\":\"Angola\"},{\"id\":8,\"name\":\"Anguilla\"},{\"id\":9,\"name\":\"Antarctica\"},{\"id\":10,\"name\":\"Antigua and Barbuda\"},{\"id\":11,\"name\":\"Argentina\"},{\"id\":12,\"name\":\"Armenia\"},{\"id\":13,\"name\":\"Aruba\"},{\"id\":14,\"name\":\"Australia\"},{\"id\":15,\"name\":\"Austria\"},{\"id\":16,\"name\":\"Azerbaijan\"},{\"id\":17,\"name\":\"Bahamas\"},{\"id\":18,\"name\":\"Bahrain\"},{\"id\":19,\"name\":\"Bangladesh\"},{\"id\":20,\"name\":\"Barbados\"},{\"id\":21,\"name\":\"Belarus\"},{\"id\":22,\"name\":\"Belgium\"},{\"id\":23,\"name\":\"Belize\"},{\"id\":24,\"name\":\"Benin\"},{\"id\":25,\"name\":\"Bermuda\"},{\"id\":26,\"name\":\"Bhutan\"},{\"id\":27,\"name\":\"Bolivia\"},{\"id\":28,\"name\":\"Bosnia and Herzegovina\"},{\"id\":29,\"name\":\"Botswana\"},{\"id\":30,\"name\":\"Bouvet Island\"},{\"id\":31,\"name\":\"Brazil\"},{\"id\":32,\"name\":\"British Indian Ocean Territory\"},{\"id\":33,\"name\":\"British Virgin Islands\"},{\"id\":34,\"name\":\"Brunei\"},{\"id\":35,\"name\":\"Bulgaria\"},{\"id\":36,\"name\":\"Burkina Faso\"},{\"id\":37,\"name\":\"Burundi\"},{\"id\":38,\"name\":\"Cambodia\"},{\"id\":39,\"name\":\"Cameroon\"},{\"id\":40,\"name\":\"Canada\"},{\"id\":41,\"name\":\"Cape Verde\"},{\"id\":42,\"name\":\"Cayman Islands\"},{\"id\":43,\"name\":\"Central African Republic\"},{\"id\":44,\"name\":\"Chad\"},{\"id\":45,\"name\":\"Chile\"},{\"id\":46,\"name\":\"China\"},{\"id\":47,\"name\":\"Christmas Island\"},{\"id\":48,\"name\":\"Cocos (Keeling) Islands\"},{\"id\":49,\"name\":\"Colombia\"},{\"id\":50,\"name\":\"Comoros\"},{\"id\":51,\"name\":\"Congo (Brazzaville)\"},{\"id\":52,\"name\":\"Congo (Kinshasa)\"},{\"id\":53,\"name\":\"Cook Islands\"},{\"id\":54,\"name\":\"Costa Rica\"},{\"id\":55,\"name\":\"Croatia\"},{\"id\":56,\"name\":\"Cuba\"},{\"id\":57,\"name\":\"Curaçao\"},{\"id\":58,\"name\":\"Cyprus\"},{\"id\":59,\"name\":\"Czech Republic\"},{\"id\":60,\"name\":\"Denmark\"},{\"id\":61,\"name\":\"Djibouti\"},{\"id\":62,\"name\":\"Dominica\"},{\"id\":63,\"name\":\"Dominican Republic\"},{\"id\":64,\"name\":\"Ecuador\"},{\"id\":65,\"name\":\"Egypt\"},{\"id\":66,\"name\":\"El Salvador\"},{\"id\":67,\"name\":\"Equatorial Guinea\"},{\"id\":68,\"name\":\"Eritrea\"},{\"id\":69,\"name\":\"Estonia\"},{\"id\":70,\"name\":\"Ethiopia\"},{\"id\":71,\"name\":\"Falkland Islands\"},{\"id\":72,\"name\":\"Faroe Islands\"},{\"id\":73,\"name\":\"Fiji\"},{\"id\":74,\"name\":\"Finland\"},{\"id\":75,\"name\":\"France\"},{\"id\":76,\"name\":\"French Guiana\"},{\"id\":77,\"name\":\"French Polynesia\"},{\"id\":78,\"name\":\"French Southern Territories\"},{\"id\":79,\"name\":\"Gabon\"},{\"id\":80,\"name\":\"Gambia\"},{\"id\":81,\"name\":\"Georgia\"},{\"id\":82,\"name\":\"Germany\"},{\"id\":83,\"name\":\"Ghana\"},{\"id\":84,\"name\":\"Gibraltar\"},{\"id\":85,\"name\":\"Greece\"},{\"id\":86,\"name\":\"Greenland\"},{\"id\":87,\"name\":\"Grenada\"},{\"id\":88,\"name\":\"Guadeloupe\"},{\"id\":89,\"name\":\"Guam\"},{\"id\":90,\"name\":\"Guatemala\"},{\"id\":91,\"name\":\"Guernsey\"},{\"id\":92,\"name\":\"Guinea\"},{\"id\":93,\"name\":\"Guinea-Bissau\"},{\"id\":94,\"name\":\"Guyana\"},{\"id\":95,\"name\":\"Haiti\"},{\"id\":96,\"name\":\"Honduras\"},{\"id\":97,\"name\":\"Hong Kong S.A.R., China\"},{\"id\":98,\"name\":\"Hungary\"},{\"id\":99,\"name\":\"Iceland\"},{\"id\":100,\"name\":\"India\"},{\"id\":101,\"name\":\"Indonesia\"},{\"id\":102,\"name\":\"Iran\"},{\"id\":103,\"name\":\"Iraq\"},{\"id\":104,\"name\":\"Ireland\"},{\"id\":105,\"name\":\"Isle of Man\"},{\"id\":106,\"name\":\"Israel\"},{\"id\":107,\"name\":\"Italy\"},{\"id\":108,\"name\":\"Ivory Coast\"},{\"id\":109,\"name\":\"Jamaica\"},{\"id\":110,\"name\":\"Japan\"},{\"id\":111,\"name\":\"Jersey\"},{\"id\":112,\"name\":\"Jordan\"},{\"id\":113,\"name\":\"Kazakhstan\"},{\"id\":114,\"name\":\"Kenya\"},{\"id\":115,\"name\":\"Kiribati\"},{\"id\":116,\"name\":\"Kuwait\"},{\"id\":117,\"name\":\"Kyrgyzstan\"},{\"id\":118,\"name\":\"Laos\"},{\"id\":119,\"name\":\"Latvia\"},{\"id\":120,\"name\":\"Lebanon\"},{\"id\":121,\"name\":\"Lesotho\"},{\"id\":122,\"name\":\"Liberia\"},{\"id\":123,\"name\":\"Libya\"},{\"id\":124,\"name\":\"Liechtenstein\"},{\"id\":125,\"name\":\"Lithuania\"},{\"id\":126,\"name\":\"Luxembourg\"},{\"id\":127,\"name\":\"Macao S.A.R., China\"},{\"id\":128,\"name\":\"Macedonia\"},{\"id\":129,\"name\":\"Madagascar\"},{\"id\":130,\"name\":\"Malawi\"},{\"id\":131,\"name\":\"Malaysia\"},{\"id\":132,\"name\":\"Maldives\"},{\"id\":133,\"name\":\"Mali\"},{\"id\":134,\"name\":\"Malta\"},{\"id\":135,\"name\":\"Marshall Islands\"},{\"id\":136,\"name\":\"Martinique\"},{\"id\":137,\"name\":\"Mauritania\"},{\"id\":138,\"name\":\"Mauritius\"},{\"id\":139,\"name\":\"Mayotte\"},{\"id\":140,\"name\":\"Mexico\"},{\"id\":141,\"name\":\"Micronesia\"},{\"id\":142,\"name\":\"Moldova\"},{\"id\":143,\"name\":\"Monaco\"},{\"id\":144,\"name\":\"Mongolia\"},{\"id\":145,\"name\":\"Montenegro\"},{\"id\":146,\"name\":\"Montserrat\"},{\"id\":147,\"name\":\"Morocco\"},{\"id\":148,\"name\":\"Mozambique\"},{\"id\":149,\"name\":\"Myanmar\"},{\"id\":150,\"name\":\"Namibia\"},{\"id\":151,\"name\":\"Nauru\"},{\"id\":152,\"name\":\"Nepal\"},{\"id\":153,\"name\":\"Netherlands\"},{\"id\":154,\"name\":\"Netherlands Antilles\"},{\"id\":155,\"name\":\"New Caledonia\"},{\"id\":156,\"name\":\"New Zealand\"},{\"id\":157,\"name\":\"Nicaragua\"},{\"id\":158,\"name\":\"Niger\"},{\"id\":159,\"name\":\"Nigeria\"},{\"id\":160,\"name\":\"Niue\"},{\"id\":161,\"name\":\"Norfolk Island\"},{\"id\":162,\"name\":\"Northern Mariana Islands\"},{\"id\":163,\"name\":\"North Korea\"},{\"id\":164,\"name\":\"Norway\"},{\"id\":165,\"name\":\"Oman\"},{\"cities\":[{\"id\":1,\"name\":\"Karachi\"}],\"id\":166,\"name\":\"Pakistan\"},{\"id\":167,\"name\":\"Palau\"},{\"id\":168,\"name\":\"Palestinian Territory\"},{\"id\":169,\"name\":\"Panama\"},{\"id\":170,\"name\":\"Papua New Guinea\"},{\"id\":171,\"name\":\"Paraguay\"},{\"id\":172,\"name\":\"Peru\"},{\"id\":173,\"name\":\"Philippines\"},{\"id\":174,\"name\":\"Pitcairn\"},{\"id\":175,\"name\":\"Poland\"},{\"id\":176,\"name\":\"Portugal\"},{\"id\":177,\"name\":\"Puerto Rico\"},{\"id\":178,\"name\":\"Qatar\"},{\"id\":179,\"name\":\"Reunion\"},{\"id\":180,\"name\":\"Romania\"},{\"id\":181,\"name\":\"Russia\"},{\"id\":182,\"name\":\"Rwanda\"},{\"id\":183,\"name\":\"Saint Barthélemy\"},{\"id\":184,\"name\":\"Saint Helena\"},{\"id\":185,\"name\":\"Saint Kitts and Nevis\"},{\"id\":186,\"name\":\"Saint Lucia\"},{\"id\":187,\"name\":\"Saint Martin (French part)\"},{\"id\":188,\"name\":\"Saint Pierre and Miquelon\"},{\"id\":189,\"name\":\"Samoa\"},{\"id\":190,\"name\":\"San Marino\"},{\"id\":191,\"name\":\"Sao Tome and Principe\"},{\"id\":192,\"name\":\"Saudi Arabia\"},{\"id\":193,\"name\":\"Senegal\"},{\"id\":194,\"name\":\"Serbia\"},{\"id\":195,\"name\":\"Seychelles\"},{\"id\":196,\"name\":\"Sierra Leone\"},{\"id\":197,\"name\":\"Singapore\"},{\"id\":198,\"name\":\"Slovakia\"},{\"id\":199,\"name\":\"Slovenia\"},{\"id\":200,\"name\":\"Solomon Islands\"},{\"id\":201,\"name\":\"Somalia\"},{\"id\":202,\"name\":\"South Africa\"},{\"id\":203,\"name\":\"South Korea\"},{\"id\":204,\"name\":\"Spain\"},{\"id\":205,\"name\":\"Sri Lanka\"},{\"id\":206,\"name\":\"Sudan\"},{\"id\":207,\"name\":\"Suriname\"},{\"id\":208,\"name\":\"Svalbard and Jan Mayen\"},{\"id\":209,\"name\":\"Swaziland\"},{\"id\":210,\"name\":\"Sweden\"},{\"id\":211,\"name\":\"Switzerland\"},{\"id\":212,\"name\":\"Syria\"},{\"id\":213,\"name\":\"Taiwan\"},{\"id\":214,\"name\":\"Tajikistan\"},{\"id\":215,\"name\":\"Tanzania\"},{\"id\":216,\"name\":\"Thailand\"},{\"id\":217,\"name\":\"Timor-Leste\"},{\"id\":218,\"name\":\"Togo\"},{\"id\":219,\"name\":\"Tokelau\"},{\"id\":220,\"name\":\"Tonga\"},{\"id\":221,\"name\":\"Trinidad and Tobago\"},{\"id\":222,\"name\":\"Tunisia\"},{\"id\":223,\"name\":\"Turkey\"},{\"id\":224,\"name\":\"Turkmenistan\"},{\"id\":225,\"name\":\"Turks and Caicos Islands\"},{\"id\":226,\"name\":\"Tuvalu\"},{\"id\":227,\"name\":\"U.S. Virgin Islands\"},{\"id\":228,\"name\":\"Uganda\"},{\"id\":229,\"name\":\"Ukraine\"},{\"id\":230,\"name\":\"United Kingdom\"},{\"id\":231,\"name\":\"United States\"},{\"id\":232,\"name\":\"Uruguay\"},{\"id\":233,\"name\":\"Uzbekistan\"},{\"id\":234,\"name\":\"Vanuatu\"},{\"id\":235,\"name\":\"Vatican\"},{\"id\":236,\"name\":\"Venezuela\"},{\"id\":237,\"name\":\"Vietnam\"},{\"id\":238,\"name\":\"Wallis and Futuna\"},{\"id\":239,\"name\":\"Western Sahara\"},{\"id\":240,\"name\":\"Yemen\"},{\"id\":241,\"name\":\"Zambia\"},{\"id\":242,\"name\":\"Zimbabwe\"},{\"id\":243,\"name\":\"Afghanistan\"}],\"errors\":[],\"success\":true}";

       /* data = "sample";
        String encrypted = KeyStoreManager.encryptData(data, alias);
        String decrypted = KeyStoreManager.decryptData(data, alias);


        String result = data + "\n\n" + encrypted + "\n\n" + decrypted;

        textView.setText(result);*/
    }

    private void testAsync() {

      /*  String key = KeyStoreManager.getNewRandomPhrase(25);


        addTextResult("testAsync: generated: " + key);


        // pass data (key) , alias (for keystore) and a listener reference so
        // we get results (in onSuccess) or error messages (in onFailure) after our task is completed in the background
        KeyStoreManager.encryptDataAsync(key, alias, this);
*/

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void insertDummyData() {
        Log.d("MainActivity", "onResume: Inserted :: " + mDbUtil.insertIntoTable("person alan"));
        Log.d("MainActivity", "onResume: Inserted :: " + mDbUtil.insertIntoTable("person bret"));
        Log.d("MainActivity", "onResume: Inserted :: " + mDbUtil.insertIntoTable("person charlie"));
        Log.d("MainActivity", "onResume: Inserted :: " + mDbUtil.insertIntoTable("person dave"));
        Log.d("MainActivity", "onResume: Inserted :: " + mDbUtil.insertIntoTable("person eggsy"));


        // check if key encryption operations were performed successfully
        // if its successful then it should show data
        // remember to call inserDummyData() at least once to populate some data
        for (String name : mDbUtil.getDataFromTable()) {
            Log.d("MainActivity", "result from db: " + name);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // close db for to avoid IO leaks
        mDbUtil.close();

    }

    // called when encryption/decryption operation is successful
    @Override
    public void onSuccess(String encrypted) {
        /*addTextResult("testAsync: encrypted: " + encrypted);
        KeyStoreManager.decryptDataAsync(encrypted, alias, decryptionListener);
*/

    }

    // called when there is a problem during encryption decryption
    @Override
    public void onFailure(String errorMessage) {
        addTextResult(errorMessage);
    }


    // METHOD 2: Example EncryptionDecryptionListener usage
    // this listener is specifically defined for decryption as separate instance

    EncryptionDecryptionListener decryptionListener = new EncryptionDecryptionListener() {
        @Override
        public void onSuccess(String data) {
            addTextResult("testAsync: after decrypt: " + data);
        }

        @Override
        public void onFailure(String errorMessage) {
            addTextResult(errorMessage);
        }
    };


    private void addTextResult(String text) {
        String resultText = textView.getText().toString() + "\n" + text + "\n";
        textView.setText(resultText);
    }
}
