package du.joandlab.firebaselab;

import java.util.Random;

/**
 * Created by gson73 on 2016-10-28.
 */

public class AvatarPicker {

    private static Random randomAvatarGenerator = new Random();
    private static int NUMBER_OF_AVATAR = 14;


    /*Generate an avatar randomly*/
    public static int generateRandomAvatarForUser() {
        return randomAvatarGenerator.nextInt(NUMBER_OF_AVATAR);
    }

    /*Get avatar id*/

    public static int getDrawableAvatarId(int givenRandomAvatarId) {

        switch (givenRandomAvatarId) {

            case 0:
                return R.drawable.abra;
            case 1:
                return R.drawable.bellsprout;
            case 2:
                return R.drawable.caterpie;
            case 3:
                return R.drawable.charmander;
            case 4:
                return R.drawable.eevee;
            case 5:
                return R.drawable.mankey;
            case 6:
                return R.drawable.meowth;
            case 7:
                return R.drawable.mew;
            case 8:
                return R.drawable.pidgey;
            case 9:
                return R.drawable.rattata;
            case 10:
                return R.drawable.squirtle;
            case 11:
                return R.drawable.venonat;
            case 12:
                return R.drawable.weedle;
            case 13:
                return R.drawable.zubat;
            default:
                return R.drawable.abra;
        }
    }
}
